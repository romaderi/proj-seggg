package pcs2055.ls;

import java.math.BigInteger;
import java.util.Arrays;

import pcs2055.interfaces.AED;
import pcs2055.interfaces.BlockCipher;
import pcs2055.interfaces.MAC;
import pcs2055.math.ByteUtil;
import pcs2055.math.MathUtil;

public class LetterSoup implements AED {
    
    private BlockCipher cipher;
    private MAC mac;
    private byte[] key;
    private int keyBits;
    private byte[] iv;
    private int ivLength;
    private byte[] R;
    //private byte[] A; // autenticação acumulada
    private byte[] cData;
    
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

        int n = 0; // ???
        
        this.mac = mac;
        
        // R <- E K (lpad(bin(N))) xor lpad(bin(N))
        this.R = null;
        BigInteger a = new BigInteger(ByteUtil.lpad(this.iv, n));
        BigInteger b = new BigInteger(a.toByteArray()); 
        byte[] mBlock = a.xor(b).toByteArray(); 
        this.cipher.encrypt(mBlock, this.R);
        this.mac.init(this.iv);
    }
    
    @Override
    public void setIV(byte[] iv, int ivLength) {

        this.iv = iv;
        this.ivLength = ivLength;
    }

    @Override
    public byte[] encrypt(byte[] mData, int mLength, byte[] cData) {

        // criptografa e autentica mensagem
        this.cipher.makeKey(this.key, this.keyBits);
        this.mac.setCipher(this.cipher);
        this.mac.setKey(this.key, this.keyBits);
        this.mac.init();

        // R <- Ek(lpad(bin(N))) xor lpad(bin(N))
        int n = this.cipher.blockBits();
        byte[] lpad = ByteUtil.lpad(this.iv, n);
        byte[] ek = null;
        this.cipher.encrypt(lpad, ek);
        this.R = ByteUtil.xor(ek, lpad, n);
        
        // C <- LFSRC(R, M, K)
        cData = this.lfsrc(this.R, mData);
        this.cData = cData;
                
        return cData;
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

        // autentica informação associada
        
        // L <- Ek(bin(0^n)) 
        int n = 0;
        byte[] L = null;
        this.cipher.encrypt(ByteUtil.lpad(new byte[] {0}, n), L); // n???
        
        // D <- ∗ (L, H, tau)  
        byte[] D = null;
        this.mac.update(aData, aLength);
        this.mac.getTag(D, this.tagBits); // tagBits ???
        
        // A <- (A xor sct(D))
        byte[] d = null;
        this.cipher.sct(d, D); // confirmar posição dos parâmetros
        this.A = new BigInteger(this.A).xor(new BigInteger(d)).toByteArray();
    }

    @Override
    public byte[] getTag(byte[] tag, int tagBits) {

        // T <- Ek(A)[tau]
        
        byte[] A = null;
        A = this.mac.getTag(A, tagBits);
        
        return tag;
    }

    /**
     * Linear feedback shift register counter
     * Algoritmo 8
     * @param N nounce
     * @param M message
     * @param K chave
     * @return ciphertext od M under K
     */
    private byte[] lfsrc(byte[] N, byte[] M) {
        
        int n = this.cipher.blockBits();
        int t = (int) Math.ceil(8*M.length / n);
        
        byte[] Oi = Arrays.copyOf(N, n/8);
        byte[] C = new byte[0];
        for (int i=0; i<t; i++) {
            Oi = MathUtil.mult_xw_gf8(Oi); // Oi <- N.(x^w)^i
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
