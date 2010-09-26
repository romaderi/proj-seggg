package pcs2055.ls;

import pcs2055.interfaces.AED;
import pcs2055.interfaces.BlockCipher;
import pcs2055.interfaces.MAC;

public class LetterSoup implements AED {

    @Override
    public byte[] decrypt(byte[] cData, int cLength, byte[] mData) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public byte[] encrypt(byte[] mData, int mLength, byte[] cData) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public byte[] getTag(byte[] tag, int tagBits) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setCipher(BlockCipher cipher) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setIV(byte[] iv, int ivLength) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setKey(byte[] cipherKey, int keyBits) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setMAC(MAC mac) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void update(byte[] aData, int aLength) {
        // TODO Auto-generated method stub
        
    }

}
