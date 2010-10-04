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
        int n = this.cipher.blockBits();
        
        // linha 2 do algoritmo 1
        byte[] cBlock = new byte[n/8];
        byte[] carray = {c};
        byte[] mBlock = ByteUtil.lpad(carray, n); // lpad(c)
        this.cipher.encrypt(mBlock, cBlock);
        this.R = ByteUtil.xor(cBlock, mBlock, n/8); 

        System.out.println("R=");
        ByteUtil.print3xn(this.R, 12);
        
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

        // aLength é em bytes
        
        // linha 4 do algoritmo 1
        
        // Oi <- R . (x^w)^i
        this.Oi = MathUtil.mult_xw_gf8(this.Oi);
        
        System.out.println("Oi=");
        ByteUtil.print3xn(this.Oi, 12);
        
        // Ai <- sct (rpad(Mi) xor Oi)
        int n = this.cipher.blockBits();
        byte[] Ai = new byte[n/8]; 
        byte[] rpad = ByteUtil.rpad(aData, n);
        byte[] mBlock = ByteUtil.xor(rpad, Oi, n/8);
        this.cipher.sct(Ai, mBlock); 

        // corresponde a um passo da somatória da linha 7
        this.A = ByteUtil.xor(this.A, Ai, n/8);
        this.mLength += aLength;

        System.out.println("R xor A=");
        ByteUtil.print3xn(ByteUtil.xor(this.R, this.A, 12), 12);
    }
    
    @Override
    public byte[] getTag(byte[] tag, int tagBits) {

        // linhas 6, 7 e 8 do Algoritmo 1
        
        byte[] A0 = calculateA0(tagBits);
        
        // A = Somatória(Ai), i = 0, 1, ..., t
        // aqui na verdade só fazemos o passo de fazer o xor com A0 (i=0)
        int n = this.cipher.blockBits();
        tag = ByteUtil.xor(this.A, A0, n/8); // MAC tag buffer
        
        System.out.println("Acc=");
//        tag[0] = (byte) 0xc8;
//        tag[1] = (byte) 0x14;
        ByteUtil.print3xn(tag, 12);
        
        // T <- Ek(A)[tau]
        byte[] cBlock = new byte[n/8];
        this.cipher.encrypt(tag, cBlock);
        System.out.println("**");
        ByteUtil.printArray(cBlock);
        byte[] T = new byte[tagBits/8];
        for (int k=0; k<tagBits/8; k++)
            T[k] = cBlock[k];
        return T;
    }
    
    private byte[] calculateA0(int tagBits) {

        BigInteger um = BigInteger.valueOf(1);
        
        // linha 6 do Algoritmo 1
        int n = this.cipher.blockBits();

//        byte[] bin0 = BigInteger.valueOf(n - tagBits).toByteArray();
//        byte[] bin = new byte[bin0.length+1];
//        for (int i=0; i<bin0.length; i++)
//            bin[i] = bin[0];
//        bin[bin0.length] = 1; // bin(n-tau)||1
        byte[] bin = ByteUtil.binConcat1(n - tagBits);
        byte[] rpad = ByteUtil.rpad(bin, n); // rpad(bin(n-tau)||1)
        byte[] m = BigInteger.valueOf(8*this.mLength).toByteArray();
        byte[] lpad = ByteUtil.lpad(m, n); // lpad(bin(|M|))

        System.out.println("");
        System.out.println("Calculando A0...");
        System.out.println("mLength= " + this.mLength);
        System.out.println("lpad " + lpad.length);
        ByteUtil.print3xn(lpad, 12);
        System.out.println("rpad " + rpad.length);
//        rpad[0] = (byte) 0x80;
//        rpad[1] = (byte) 0x00;
        ByteUtil.print3xn(rpad, 12);
        
        byte[] pad = ByteUtil.xor(rpad, lpad, n/8);
        return ByteUtil.xor(this.R, pad, n/8);
    }


}
