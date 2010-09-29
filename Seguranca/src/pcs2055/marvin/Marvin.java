package pcs2055.marvin;

import java.math.BigInteger;

import pcs2055.interfaces.BlockCipher;
import pcs2055.interfaces.MAC;
import pcs2055.math.ByteUtil;
import pcs2055.math.MathUtil;

public class Marvin implements MAC {
    
    private static final byte c = 0x2A;
    
    private BlockCipher cipher;
    private byte[] key;
    private int keyBits;
    private byte[] R;
    private int i = 1;
    private byte[] A; // valor acumulado
    private int mLength = 0;
    
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
    public void init() {

        this.cipher.makeKey(this.key, this.keyBits);

        // linha 2 do algoritmo 1
        byte[] cBlock = null;
        byte[] carray = {c};
        byte[] mBlock = ByteUtil.lpad(carray, this.cipher.blockBits());
        this.cipher.encrypt(mBlock, cBlock);
        BigInteger r = new BigInteger(cBlock).xor(new BigInteger(mBlock));
        this.R = r.toByteArray(); 
    }
    
    @Override
    public void init(byte[] R) {

        this.R = R;
    }

    @Override
    public void update(byte[] aData, int aLength) {

        // linha 4 do algoritmo 1
        
        // Oi <- R . (x^w)^i
        int w = this.cipher.blockBits();
        byte[] Oi = new byte[w]; 
        for (int k=0; k<this.i; k++)    
            Oi = MathUtil.xtimes(this.R, w);
        
        // Ai <- sct (rpad(Mi) xor Oi)
        byte[] Ai = null; 
        BigInteger m = new BigInteger(ByteUtil.rpad(aData, w)).xor(new BigInteger(Oi));
        byte[] mBlock = m.toByteArray();
        this.cipher.sct(Ai, mBlock); // confirmar posição dos parâmetros
        BigInteger a = new BigInteger(this.A).xor(new BigInteger(Ai));
        
        // corresponde a um passo da somatória da linha 7
        this.A = a.toByteArray();
        this.i++;
        this.mLength += aLength;
    }
    
    @Override
    public byte[] getTag(byte[] tag, int tagBits) {

        // tag ???

        // linhas 6, 7 e 8 do Algoritmo 1
        
        byte[] A0 = calculateA0(tagBits);
        
        // A = Somatória(Ai), i = 0, 1, ..., t
        // aqui na verdade só fazemos o passo de fazer o xor com A0 (i=0)
        BigInteger a = new BigInteger(this.A).xor(new BigInteger(A0));
        this.A = a.toByteArray();
        
        // T <- Ek(A)[tau]
        byte[] cBlock = null;
        this.cipher.encrypt(this.A, cBlock);
        byte[] T = new byte[tagBits];
        for (int k=0; k<tagBits; k++)
            T[k] = cBlock[k];
        return T;
    }
    
    private byte[] calculateA0(int tagBits) {

        BigInteger um = BigInteger.valueOf(1);
        
        // linha 6 do Algoritmo 1
        int n = this.cipher.blockBits();
        BigInteger r = new BigInteger(this.R); // R
        byte[] b = BigInteger.valueOf(n - tagBits).or(um).toByteArray();
        BigInteger x = new BigInteger(ByteUtil.rpad(b, n)); // rpad(bin(n − tau) || 1)
        byte[] modM = BigInteger.valueOf(this.mLength).toByteArray(); 
        BigInteger y = new BigInteger(ByteUtil.lpad(modM, n)); // lpad(bin(|M|))

        return r.xor(x).xor(y).toByteArray();
    }


}
