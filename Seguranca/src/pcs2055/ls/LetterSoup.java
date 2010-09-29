package pcs2055.ls;

import java.math.BigInteger;

import pcs2055.interfaces.AED;
import pcs2055.interfaces.BlockCipher;
import pcs2055.interfaces.MAC;
import pcs2055.math.ByteUtil;

public class LetterSoup implements AED {
    
    private BlockCipher cipher;
    private MAC mac;
    private byte[] key;
    private int keyBits;
    private byte[] iv;
    private int ivLength;
    private byte[] R;
    private byte[] A; // autenticação acumulada
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
        
        // C <- LFSRC(R, M, K)
        cData = lfsrc(cData, mData, this.key);
        this.cData = cData;
        
        // A <- ∗ (R, C, tau)
        this.mac.update(mData, mLength);
        
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

        this.mac.getTag(tag, tagBits);
        return tag;
    }

    private static byte[] lfsrc(byte[] R, byte[]M, byte[]K) {
        
        return null;
    }
}
