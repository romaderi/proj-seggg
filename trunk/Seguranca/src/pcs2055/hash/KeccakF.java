package pcs2055.hash;

import java.util.Arrays;

import pcs2055.math.ByteUtil;

public class KeccakF {
    
    // colocar aqui constantes (segundo enunciado) que determinam número de rounds
	
	//private static int b = 1600;
	//private static int l = 6;
	private int nt = 24;  // 12+2*l, com l = 6
	private int indexRound = 0;
	private static final int[] keccakRhoOffsets = {
		 0,  1, 62, 28, 27,
		36, 44,  6, 55, 20,
		 3, 10, 43, 25, 39,
		41, 45, 15, 21,  8,
		18,  2, 61, 56, 14
	};
	
	private static final long[] RC ={
        0x0000000000000001L, 0x0000000000008082L,
        0x800000000000808AL, 0x8000000080008000L,
        0x000000000000808BL, 0x0000000080000001L,
        0x8000000080008081L, 0x8000000000008009L,
        0x000000000000008AL, 0x0000000000000088L,
        0x0000000080008009L, 0x000000008000000AL,
        0x000000008000808BL, 0x800000000000008BL,
        0x8000000000008089L, 0x8000000000008003L,
        0x8000000000008002L, 0x8000000000000080L,
        0x000000000000800AL, 0x800000008000000AL,
        0x8000000080008081L, 0x8000000000008080L,
        0x0000000080000001L, 0x8000000080008008L
    };

	
    public byte[] f(byte[] inData) {
    	
    	long[] data = ByteUtil.byteArrayToLongArray(inData);
    	
    	for (int i = 0; i < 25; i++)
    		data[i] = Long.reverseBytes(data[i]);
    	
    	for ( indexRound = 0; indexRound < nt; indexRound++)
        	data = round(data);
    	
    	for (int i = 0; i < 25; i++)
    		data[i] = Long.reverseBytes(data[i]);
    	
        return ByteUtil.longArrayToByteArray(data);
    }
    
    private long[] round(long[] data) {

    	long[] aData = theta(data);
    	aData = rho(aData);
        aData = pi(aData);
        aData = chi(aData);
        aData = iota(aData);
        
    	return aData;
    }
    
    public long[] theta(long[] data) {

    	int x, y;
    	long[] C = new long[5];
    	long[] D = new long[5];
    	long[] ret = Arrays.copyOf(data, data.length);
    	
    	for ( x = 0; x < 5; x++ )
    		C[x] = data[index(x,0)] ^ data[index(x,1)] ^ data[index(x,2)] ^
    		       data[index(x,3)] ^ data[index(x,4)];

    	for ( x = 0; x < 5 ; x++ )
    		D[x] = C[(5+x-1)%5] ^ Long.rotateLeft(C[(x+1)%5],1);
    	
    	for ( x = 0; x < 5; x++ )
    		for ( y = 0; y < 5; y++ )
    			ret[index(x,y)] = data[index(x,y)] ^ D[x];
    	
        return ret;
    }
      
    public long[] rho(long[] data) {
        
        long[] ret = new long[data.length];
        
        ret[index(0,0)] = data[index(0,0)];
        int x = 1;
        int y = 0;
        for (int t=0; t<24; t++) {
            ret[index(x,y)] = Long.rotateLeft(data[index(x,y)], (t+1)*(t+2)/2);
            int temp = x;
            x = y;
            y = (2*temp+3*y)%5;
        }
        
        return ret;
    }
    
    public long[] pi(long[] data) {

        long[] ret = new long[25];
        
        for( int x = 0; x < 5; x++ )
        	for( int y = 0; y < 5; y++ )
        		ret[index(y,2*x+3*y)] = data[index(x,y)];
        
        return ret;
    }
    
    public long[] chi(long[] data) {
        int x, y;
        long[] ret = Arrays.copyOf(data, data.length);

        for( y = 0; y < 5; y++ )
            for( x = 0; x < 5; x++ )
            	ret[index(x,y)] = data[index(x,y)] ^ ((~data[index(x+1,y)])
                		& data[index(x+2,y)]);

        return ret;
    }
    
    public long[] iota(long[] data) {

    	long[] ret = Arrays.copyOf(data, data.length);
    	
   		ret[0] = data[0] ^ RC[indexRound]; // index(0,0) = 0
        return ret;
    }

    public long[] iota(long[] data, int indexRound) {

        long[] ret = Arrays.copyOf(data, data.length);
        
                ret[0] = data[0] ^ RC[indexRound]; // index(0,0) = 0
        return ret;
    }

    private int index(int x, int y){
    	return (x%5)+5*(y%5);
    }    
}