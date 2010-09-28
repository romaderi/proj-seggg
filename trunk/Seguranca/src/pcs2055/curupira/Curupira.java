package pcs2055.curupira;

import pcs2055.interfaces.BlockCipher;
import pcs2055.math.ByteUtil;

public class Curupira implements BlockCipher {

    public static final int BLOCK_SIZE  =  96;
    public static final int KEY_SIZE_96  =  96;
    public static final int KEY_SIZE_144 = 144;
    public static final int KEY_SIZE_192 = 196;
    
    private byte[] key;
    private int roundMax;
    private int keyBits;
    
    @Override
    public int blockBits() {

        return BLOCK_SIZE;
    }

    @Override
    public void decrypt(byte[] cBlock, byte[] mBlock) {

        // algoritmo de acordo com fórmula da pg 9 (item 3.9)

        KeyScheduler keyScheduler = new KeyScheduler(this.key, this.keyBits, 
                KeyScheduler.Mode.DECRYPTING, this.roundMax);
        
        mBlock = Round.initialKeyAddition(cBlock, keyScheduler.nextSubKey());
        for (int r=1; r <= this.roundMax-1; r++) {
            
            mBlock = Round.roundFunction(mBlock, keyScheduler.nextSubKey());
        }
        mBlock = Round.lastRoundFunction(mBlock, keyScheduler.nextSubKey());        
    }

    @Override
    public void encrypt(byte[] mBlock, byte[] cBlock) {

        // algoritmo de acordo com fórmula da pg 9 (item 3.9)
        
        KeyScheduler keyScheduler = new KeyScheduler(this.key, this.keyBits, 
                KeyScheduler.Mode.ENCRYPTING, this.roundMax);
        
        ByteUtil.printArray(mBlock);
        
        cBlock = Round.initialKeyAddition(mBlock, keyScheduler.nextSubKey());
        
        System.out.print("round KEY ADDITION cBlock => ");
        ByteUtil.printArray(cBlock); // to be removed
        
        for (int r=1; r <= this.roundMax-1; r++) {
            
            cBlock = Round.roundFunction(cBlock, keyScheduler.nextSubKey());
            System.out.println("---->>>>> Round " + r);
        }
        cBlock = Round.lastRoundFunction(cBlock, keyScheduler.nextSubKey());
    }

    @Override
    public int keyBits() {

        return this.keyBits;
    }

    @Override
    public void makeKey(byte[] cipherKey, int keyBits) {

        this.key = cipherKey;
        this.keyBits = keyBits;
        
        int t = keyBits / 4;
        switch(t) { // número de rounds é escolhido pelo mínimo necessário
            case (2):
                this.roundMax = 10;
            break;
            case (3):
                this.roundMax = 14;
            break;               
            case (4):
                this.roundMax = 18;
            break;
        }

    }

    @Override
    public void sct(byte[] cBlock, byte[] mBlock) {
    	// 
        // ????
    }

}
