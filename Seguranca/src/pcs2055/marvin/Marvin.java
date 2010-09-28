package pcs2055.marvin;

import pcs2055.interfaces.BlockCipher;
import pcs2055.interfaces.MAC;
import pcs2055.math.ByteUtil;
import pcs2055.math.MathUtil;

public class Marvin implements MAC {
    
    private static final byte c = 0x2A;
    
    private BlockCipher cipher;
    private byte[] key;
    private int keyBits;
    private byte R;
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
        
        byte[] cBlock = null;
        byte[] carray = {c};
        byte[] mBlock = ByteUtil.lpad(carray, this.cipher.blockBits());
        this.cipher.encrypt(mBlock, cBlock);
        this.R = ByteUtil.xor(cBlock, mBlock); 
    }


    @Override
    public void update(byte[] aData, int aLength) {

        byte[] r = {this.R};
        byte[] Oi = MathUtil.xtimes(r, this.cipher.blockBits());
        byte[] Ai; 
        byte[] mBlock = ByteUtil.xor(ByteUtil.rpad(aData), Oi);
        this.cipher.sct(Ai, mBlock); // confirmar posição dos parâmetros
        this.A = ByteUtil.xor(this.A, Ai);
        this.i++;
        this.mLength += aLength;
    }
    
    @Override
    public byte[] getTag(byte[] tag, int tagBits) {

        // tag ???
        
        byte[] A0 = calculateA0(tagBits);
        this.A = ByteUtil.xor(this.A, A0);
        byte[] cBlock = null;
        this.cipher.encrypt(this.A, cBlock);
        
        return cBlock;
    }
    
    private byte[] calculateA0(int tagBits) {
        
        int x = ByteUtil.lpad(ByteUtil.bin(this.mLength));
        int y = 0;
        int z = x ^ y;
            
        return this.R ^ z;
    }
}
