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
 
        byte[] tempBlock = new byte[BLOCK_SIZE];
        byte[][] key = new byte[this.roundMax][this.key.length];
        byte[] nextKey = new byte[this.key.length];
        
        for (int i = 0; i < this.roundMax; i++){
        	key[i] = keyScheduler.nextSubKey(this.roundMax);
        }
        
        nextKey = key[this.roundMax-1];
        tempBlock = Round.initialKeyAddition(cBlock, nextKey);
        
        for (int r = this.roundMax-1; r > 0; r--) {
        	nextKey = keyScheduler.decryptKey(key[r-1], this.roundMax-r);
        	//System.out.println("---->>>>> Round " + (this.roundMax-r));
           	//System.out.print(" KEY -> ");
        	//ByteUtil.printArray(keyScheduler.decryptKey(key[r-1], this.roundMax-r));
        	//System.out.print(" mBLOCK -> ");
        	//ByteUtil.printArray(Round.roundFunction(mBlock, keyScheduler.decryptKey(key[r-1], this.roundMax-r)));
        	tempBlock = Round.roundFunction(tempBlock, nextKey);
        }

        //System.out.println("---->>>>> Round 0");
       	//System.out.print(" KEY -> ");
    	//ByteUtil.printArray(keyScheduler.getSubKey(0));
        nextKey = keyScheduler.getSubKey(0);
        tempBlock = Round.lastRoundFunction(tempBlock, nextKey);
        
    	for (int i=0; i<this.blockBits()/8; i++)
    	    mBlock[i] = tempBlock[i];
    }

    @Override
    public void encrypt(byte[] mBlock, byte[] cBlock) {

        // algoritmo de acordo com fórmula da pg 9 (item 3.9)
        
        KeyScheduler keyScheduler = new KeyScheduler(this.key, this.keyBits, 
                KeyScheduler.Mode.ENCRYPTING, this.roundMax);
        
        byte[] tempBlock = new byte[BLOCK_SIZE];
        tempBlock = Round.initialKeyAddition(mBlock, keyScheduler.nextSubKey(this.roundMax));
        
        byte[] nextKey;
        
        for (int r=1; r <= this.roundMax-1; r++) {
        	System.out.println("---->>>>> Round " + r);
        	nextKey = keyScheduler.nextSubKey(this.roundMax);
           	System.out.print(" KEY -> ");
        	ByteUtil.printArray(nextKey);
        	tempBlock = Round.roundFunction(tempBlock, nextKey);
            //cBlock = Round.roundFunction(cBlock, keyScheduler.nextSubKey());
        }
        System.out.println("---->>>>> Round " + this.roundMax);
        nextKey = keyScheduler.nextSubKey(this.roundMax);
       	System.out.print(" KEY -> ");
    	ByteUtil.printArray(nextKey);
        tempBlock = Round.lastRoundFunction(tempBlock, nextKey);
        System.out.print("CHYPERTEXT -> ");
    	ByteUtil.printArray(tempBlock);
    	
    	// nosso return bizarro
    	for (int i=0; i<this.blockBits()/8; i++)
    	    cBlock[i] = tempBlock[i];
    	
    }

    @Override
    public int keyBits() {

        return this.keyBits;
    }

    @Override
    public void makeKey(byte[] cipherKey, int keyBits) {

        this.key = cipherKey;
        this.keyBits = keyBits;
        
        int t = keyBits / 48;
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

       // SCT = 4 rodadas do Curupira, *sem* adição de chave.
       // by @pbarreto
       
       cBlock = Round.sct(mBlock);
       for (int i=0; i<3; i++) {
           cBlock = Round.sct(cBlock);
       }
    }

}
