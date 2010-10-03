package pcs2055.marvin;

import java.math.BigInteger;
import java.util.Arrays;

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
    private byte[] Oi; // valor acumulado 
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
        byte[] mBlock = ByteUtil.lpad(carray, this.cipher.blockBits()); // lpad(c)
        this.cipher.encrypt(mBlock, cBlock);
        BigInteger r = new BigInteger(cBlock).xor(new BigInteger(mBlock));
        this.R = r.toByteArray(); 
        
        // inicializa Oi (linha 4)
        this.Oi = Arrays.copyOf(this.R, this.R.length);
        this.A = new byte[this.cipher.blockBits()/8];
    }
    
    @Override
    public void init(byte[] R) {

        this.R = R;
        this.Oi = Arrays.copyOf(this.R, this.R.length);
        this.A = new byte[this.cipher.blockBits()/8];
    }

    @Override
    public void update(byte[] aData, int aLength) {

        // linha 4 do algoritmo 1
        
        // Oi <- R . (x^w)^i
        this.Oi = MathUtil.mult_xw_gf8(this.Oi);
        
        // Ai <- sct (rpad(Mi) xor Oi)
        int n = this.cipher.blockBits();
        byte[] Ai = null; 
        byte[] rpad = ByteUtil.rpad(aData, n);
        byte[] mBlock = ByteUtil.xor(rpad, Oi, n);
        this.cipher.sct(Ai, mBlock); 
        
        // corresponde a um passo da somatória da linha 7
        this.A = ByteUtil.xor(this.A, Ai, n);
        this.mLength += aLength;
    }
    
    @Override
    public byte[] getTag(byte[] tag, int tagBits) {

        // linhas 6, 7 e 8 do Algoritmo 1
        
        byte[] A0 = calculateA0(tagBits);
        
        // A = Somatória(Ai), i = 0, 1, ..., t
        // aqui na verdade só fazemos o passo de fazer o xor com A0 (i=0)
        int n = this.cipher.blockBits();
        this.A = ByteUtil.xor(this.A, A0, n);
        
        tag = this.A; // MAC tag buffer
        
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
        BigInteger bin = BigInteger.valueOf(n - tagBits);
        bin = bin.shiftLeft(1).add(um); // bin(n-tau)||1
        byte[] rpad = ByteUtil.rpad(bin.toByteArray(), n); // rpad(bin(n-tau)||1)
        byte[] m = BigInteger.valueOf(8*this.mLength).toByteArray();
        byte[] lpad = ByteUtil.lpad(m, n); // lpad(bin(|M|))
        byte[] pad = ByteUtil.xor(rpad, lpad, n);
        return ByteUtil.xor(this.R, pad, n);
    }


}
