package pcs2055.ls;

import java.util.Arrays;

import pcs2055.curupira.Curupira;
import pcs2055.interfaces.AED;
import pcs2055.interfaces.BlockCipher;
import pcs2055.interfaces.MAC;
import pcs2055.math.ByteUtil;
import pcs2055.math.MathUtil;

/**
 * Ordem esperado de uso:
 *   setKey
 *   setCipher
 *   setIV
 *   setMAC
 *   encrypt / decrypt: passa a mensagem inteira com uma só chamada (não adianta chamar várias vezes)
 *   update (passa o header inteiro de uma vez, mas pode chamar várias vezes)
 *   getTag
 *
 */
public class LetterSoup implements AED {
    
    private BlockCipher cipher;
    private MAC mac; 
    private byte[] key;
    private int keyBits;
    private byte[] iv;
    
    private byte[] R; // calculado na linha 2, usado na linha 3
    private byte[] H; // texto associado
    private byte[] autData; // texto cifrado ou decifrado a ser decifrado
    
    @Override
    public void setCipher(BlockCipher cipher) {

        this.cipher = cipher;
    }

    @Override
    public void setKey(byte[] cipherKey, int keyBits) {

        this.key = cipherKey;
        this.keyBits = keyBits;
    }

    @Override
    public void setMAC(MAC mac) {

        this.mac = mac;
        this.mac.setCipher(this.cipher);
        this.mac.setKey(this.key, this.keyBits);
    }
    
    @Override
    public void setIV(byte[] iv, int ivLength) {

        this.iv = iv;
    }

    @Override
    public byte[] encrypt(byte[] mData, int mLength, byte[] cData) {

        // criptografa a mensagem

        this.cipher.makeKey(this.key, this.keyBits);
        
        // R <- Ek(lpad(bin(N))) xor lpad(bin(N))
        int n = this.cipher.blockBits(); 
        byte[] lpad = ByteUtil.lpad(this.iv, n);
        byte[] ek = new byte[n/8];
        this.cipher.encrypt(lpad, ek);
        this.R = ByteUtil.xor(ek, lpad, n/8);
        
        // C <- LFSRC(R, M, K)
        cData = this.lfsrc(this.R, mData); // cData: ciphertext buffer for encrypted chunk
        
        // salva buffer em this.aData
        this.autData = new byte[0];
        this.autData = ByteUtil.append(this.autData, cData, this.autData.length, cData.length);
        
        return this.autData;
    }
    
    @Override
    public byte[] decrypt(byte[] cData, int cLength, byte[] mData) {

        // descriptografa 
        
        this.cipher.makeKey(this.key, this.keyBits);
        
        // R <- Ek(lpad(bin(N))) xor lpad(bin(N))
        int n = this.cipher.blockBits(); 
        byte[] lpad = ByteUtil.lpad(this.iv, n);
        byte[] ek = new byte[n/8];
        this.cipher.encrypt(lpad, ek);
        this.R = ByteUtil.xor(ek, lpad, n/8);
        
        // M <- LFSRC(R, C, K)
        mData = this.lfsrc(this.R, cData); 
        
        // salva buffer em this.aData
        this.autData = new byte[0];
        this.autData = ByteUtil.append(this.autData, cData, this.autData.length, cData.length);
        
        return mData;
    }

    @Override
    public void update(byte[] aData, int aLength) {

        // acumula mensgaem associada

        // inicialização
        if (this.H == null) {
            this.H = new byte[0];
        }
        
        this.H = ByteUtil.append(this.H, aData, this.H.length, aLength);
    }

    @Override
    public byte[] getTag(byte[] tag, int tagBits) {
        
        // autentica texto cifrado
        byte[] A = cipherTag(tagBits);

        if (this.H != null && this.H.length>0) {

            // autentica dado associado
            byte[] D = associatedTag(tagBits);
        
            // calcula a resultante das autenticações
            
            // A <- (A xor sct(D))
            int n = this.cipher.blockBits()/8;
            byte[] sctD = new byte[n];
            this.cipher.sct(sctD, D); 
            A = ByteUtil.xor(A, sctD, n);
        }

        // T <- Ek(A)[tau]
        this.cipher.encrypt(A, tag); // tag: the MAC tag buffer
        return Arrays.copyOfRange(tag, 0, tagBits/8);
    }
    
    // calcula A (quadradinho asterisco, linha 3)
    private byte[] cipherTag(int tagBits) {
        
        int n = this.cipher.blockBits()/8; 

        byte[] A = new byte[n];
        this.mac.init(this.R);
        for (int i=0; i < this.autData.length; i+=n) {
            
            int b = i + n;
            if (i+n > this.autData.length)
                b = this.autData.length;

            byte[] aData = Arrays.copyOfRange(this.autData, i, b);
            this.mac.update(aData, b-i);
            this.mac.getTag(A, tagBits); // MAC Tag Buffer (parece lanche do McDonnads!)
            
        }
        
        return A;
    }

    // calcula D (quadradinho asterisco, linha 5)
    private byte[] associatedTag(int tagBits) {
        
        // L <- Ek(bin(0^n)) 
        int n = this.cipher.blockBits()/8;
        byte[] L = new byte[n];
        this.cipher.encrypt(ByteUtil.lpad(new byte[] {0}, 8*n), L); 
        
        // D <- ∗ (L, H, tau)  
        byte[] D = new byte[n];
        this.mac.init(L);
        for (int i=0; i < this.H.length; i+=n) {
            
            int b = i + n;
            if (i+n > this.H.length)
                b = this.H.length;
            
            byte[] aData = Arrays.copyOfRange(this.H, i, b);
            this.mac.update(aData, b-i);
            this.mac.getTag(D, tagBits); 
        }

        return D;
    }

    /**
     * Linear feedback shift register counter
     * Algoritmo 8
     * @param N nounce
     * @param M message
     * @param mode ENCRYPT ou DECRYPT
     * @return ciphertext of M under this.key
     */
    private byte[] lfsrc(byte[] N, byte[] M) {
        
        int n = this.cipher.blockBits();
        int t = (int) Math.ceil((double) 8*M.length / n);
        
        byte[] Oi = Arrays.copyOf(N, n/8);
        byte[] C = new byte[0];
        for (int i=0; i<t; i++) {

            Oi = MathUtil.mult_xw_gf8(Oi); // Oi <- N.(x^w)^i, com w=8
            byte[] ekoi = new byte[n/8];
            this.cipher.encrypt(Oi, ekoi);
            
            int a = i*n/8;
            int b;
            if (i != t-1)
                b = i*n/8 + n/8;
            else
                b = M.length; 
            byte[] Mi = Arrays.copyOfRange(M, a, b);
            ekoi = Arrays.copyOf(ekoi, Mi.length); 
            byte[] Ci = ByteUtil.xor(Mi, ekoi, Mi.length); // Ci <- M xor El(Oi)[|mi|]
        
            C = ByteUtil.append(C, Ci, C.length, Ci.length); // C <- C1||...||C2
        }
        
        return C;
    }
}
