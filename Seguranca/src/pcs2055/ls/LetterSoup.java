package pcs2055.ls;

import pcs2055.interfaces.AED;
import pcs2055.interfaces.BlockCipher;
import pcs2055.interfaces.MAC;

public class LetterSoup implements AED {
    
    private BlockCipher cipher;
    private MAC mac;
    private byte[] key;
    private int keyBits;
    private byte[] iv;
    private int ivLength;

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
    }

    @Override
    public byte[] decrypt(byte[] cData, int cLength, byte[] mData) {

        this.cipher.makeKey(this.key, this.keyBits);
        this.cipher.decrypt(cData, mData);
        return mData;
    }

    @Override
    public byte[] encrypt(byte[] mData, int mLength, byte[] cData) {

        this.cipher.makeKey(this.key, this.keyBits);
        this.cipher.decrypt(cData, mData);
        return cData;
    }

    @Override
    public byte[] getTag(byte[] tag, int tagBits) {

        this.mac.getTag(tag, tagBits);
        return tag;
    }

    @Override
    public void setIV(byte[] iv, int ivLength) {

        this.iv = iv;
        this.ivLength = ivLength;
    }

    @Override
    public void update(byte[] aData, int aLength) {

        this.mac.update(aData, aLength);
    }

}
