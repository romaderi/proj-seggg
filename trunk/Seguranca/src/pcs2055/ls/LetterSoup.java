package pcs2055.ls;

import java.util.Arrays;

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
 *   encrypt / decrypt (várias vezes)
 *   update (várias vezes)
 *   getTag
 *
 */
public class LetterSoup implements AED {
    
    private BlockCipher cipher;
    private MAC mac; 
    private byte[] key;
    private int keyBits;
    private byte[] iv;
    private int ivLength;
    
    private byte[] R; // calculado na linha 2, usado na linha 3
    private byte[] H; // texto associado
    private byte[] cData; // texto cifrado
    
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
        this.ivLength = ivLength;
    }

    @Override
    public byte[] encrypt(byte[] mData, int mLength, byte[] cData) {

        // criptografa a mensagem

        this.cipher.makeKey(this.key, this.keyBits);
        
        // R <- Ek(lpad(bin(N))) xor lpad(bin(N))
        int n = this.cipher.blockBits(); // TODO: n?
        byte[] lpad = ByteUtil.lpad(this.iv, n);
        byte[] ek = null;
        this.cipher.encrypt(lpad, ek);
        this.R = ByteUtil.xor(ek, lpad, n);
        
        // C <- LFSRC(R, M, K)
        cData = this.lfsrc(this.R, mData); // cData: ciphertext buffer for encrypted chunk
        
        // acumula buffer em this.cData
        if (this.cData == null)
            this.cData = new byte[0];
        this.cData = ByteUtil.append(this.cData, cData, this.cData.length, mLength);
        
        return this.cData;
    }
    
    @Override
    public byte[] decrypt(byte[] cData, int cLength, byte[] mData) {

        // descriptografa e autentica
        
        this.cipher.makeKey(this.key, this.keyBits);
        this.cipher.decrypt(cData, mData);
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
        
        // autentica dado associado
        byte[] D = associatedTag(tagBits);
        
        // calcula a resultante das autenticações
        
        // A <- (A xor sct(D))
        int n = this.cipher.blockBits()/8;
        byte[] sctD = null;
        this.cipher.sct(sctD, D); // confirmar posição dos parâmetros
        A = ByteUtil.xor(A, sctD, n); 

        // T <- Ek(A)[tau]
        this.cipher.encrypt(A, tag); // tag: the MAC tag buffer
        return Arrays.copyOfRange(tag, 0, tagBits/8);
    }
    
    // calcula A (linha 3)
    private byte[] cipherTag(int tagBits) {
        
        int n = this.cipher.blockBits(); 

        byte[] A = null;
        this.mac.init(this.R);
        for (int i=0; i < this.cData.length; i+=n) {
            
            byte[] aData = Arrays.copyOfRange(this.cData, i, i+n);
            this.mac.update(aData, n);
            this.mac.getTag(A, tagBits); // MAC Tag Buffer (parece lanche do McDonnads!)
        }
        
        return A;
    }

    // calcula D (linha 5)
    private byte[] associatedTag(int tagBits) {
        
        // L <- Ek(bin(0^n)) 
        int n = this.cipher.blockBits();
        byte[] L = null;
        this.cipher.encrypt(ByteUtil.lpad(new byte[] {0}, n), L); // TODO: n???
        
        // D <- ∗ (L, H, tau)  
        byte[] D = null;
        this.mac.init(L);
        for (int i=0; i < this.H.length; i+=n) {
            
            byte[] aData = Arrays.copyOfRange(this.H, i, i+n);
            this.mac.update(aData, n);
            this.mac.getTag(D, tagBits); 
        }

        return D;
    }

    /**
     * Linear feedback shift register counter
     * Algoritmo 8
     * @param N nounce
     * @param M message
     * @return ciphertext of M under this.key
     */
    private byte[] lfsrc(byte[] N, byte[] M) {
        
        int n = this.cipher.blockBits();
        int t = (int) Math.ceil(8*M.length / n);
        
        byte[] Oi = Arrays.copyOf(N, n/8);
        byte[] C = new byte[0];
        for (int i=0; i<t; i++) {
            Oi = MathUtil.mult_xw_gf8(Oi); // Oi <- N.(x^w)^i, com w=8
            byte[] ekoi = null;
            this.cipher.encrypt(Oi, ekoi);
            ekoi = Arrays.copyOf(ekoi, n); 
            int a = i*n/8;
            int b;
            if (i != t-1)
                b = i*n/8 + n/8;
            else
                b = M.length; 
            byte[] Mi = Arrays.copyOfRange(M, a, b);
            byte[] Ci = ByteUtil.xor(Mi, ekoi, n); // Ci <- M xor El(Oi)[|mi|]
        
            // quadradinho asterisco...
            // A <- ∗ (R, C, tau)
            this.mac.update(Ci, b-a);
            
            C = ByteUtil.append(C, Ci, C.length, n); // C <- C1||...||C2
        }
        
        return C;
    }
}