package pcs2055.marvin;

import pcs2055.interfaces.BlockCipher;
import pcs2055.interfaces.MAC;

public class Marvin implements MAC {
    
    private static final int c = 0x2A;

    @Override
    public byte[] getTag(byte[] tag, int tagBits) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setCipher(BlockCipher cipher) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setKey(byte[] cipherKey, int keyBits) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void update(byte[] aData, int aLength) {
        // TODO Auto-generated method stub
        
    }
    
    

}
