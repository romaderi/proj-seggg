package pcs2055.curupira;

import pcs2055.interfaces.BlockCipher;

public class Curupira implements BlockCipher {

    public static final int KEY_SIZE_96  =  96;
    public static final int KEY_SIZE_144 = 144;
    public static final int KEY_SIZE_192 = 196;
    
    
    @Override
    public int blockBits() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void decrypt(byte[] cBlock, byte[] mBlock) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void encrypt(byte[] mBlock, byte[] cBlock) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int keyBits() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void makeKey(byte[] cipherKey, int keyBits) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void sct(byte[] cBlock, byte[] mBlock) {
        // TODO Auto-generated method stub
        
    }

}
