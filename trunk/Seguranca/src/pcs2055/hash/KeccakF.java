package pcs2055.hash;

import java.util.Arrays;

import pcs2055.math.ByteUtil;

public class KeccakF {
    
    // colocar aqui constantes (segundo enunciado) que determinam número de rounds
	
	//private static int b = 1600;
	//private static int l = 6;
	private static int nt = 24;  // 12+2*l, com l = 6
	private static int indexRound;
	private static final int[] keccakRhoOffsets = {
		 0,  1, 62, 28, 14,
		36, 44,  6, 55, 20,
		 3, 10, 43, 25, 39,
		41, 45, 15, 21,  8,
		18,  2, 61, 56, 14
	};
	
	private static final long[] RC = {
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
	

	private static void KeccakInitializeRhoOffsets()
	{
	    /*//int x, y, t, newX, newY;

	    keccakRhoOffsets = new int[25];
	    keccakRhoOffsets[index(0,0)] = 0;
	    keccakRhoOffsets[index(0,1)] = 36;
	    keccakRhoOffsets[index(0,2)] = 3;
	    keccakRhoOffsets[index(0,3)] = 41;
	    keccakRhoOffsets[index(0,4)] = 18;
	    keccakRhoOffsets[index(1,0)] = 1;
	    keccakRhoOffsets[index(1,1)] = 44;
	    keccakRhoOffsets[index(1,2)] = 10;
	    keccakRhoOffsets[index(1,3)] = 45;
	    keccakRhoOffsets[index(1,4)] = 2;
	    keccakRhoOffsets[index(2,0)] = 62;
	    keccakRhoOffsets[index(2,1)] = 6;
	    keccakRhoOffsets[index(2,2)] = 43;
	    keccakRhoOffsets[index(2,3)] = 15;
	    keccakRhoOffsets[index(2,4)] = 61;
	    keccakRhoOffsets[index(3,0)] = 28;
	    keccakRhoOffsets[index(3,1)] = 55;
	    keccakRhoOffsets[index(3,2)] = 25;
	    keccakRhoOffsets[index(3,3)] = 21;
	    keccakRhoOffsets[index(3,4)] = 56;
	    keccakRhoOffsets[index(4,0)] = 27;
	    keccakRhoOffsets[index(4,1)] = 20;
	    keccakRhoOffsets[index(4,2)] = 39;
	    keccakRhoOffsets[index(4,3)] = 8;
	    keccakRhoOffsets[index(4,4)] = 14;
	   
	    /*x = 1;
	    y = 0;
	    for( t = 0 ; t < 24 ; t++ ) {
	        keccakRhoOffsets[index(x,y)] = ((t+1)*(t+2)/2) % 64;
	        newX = (0*x+1*y) % 5;
	        newY = (2*x+3*y) % 5;
	        x = newX;
	        y = newY;
	    }*/
	}
	
    public static long[] f(long[] aData) {
    	
    	//KeccakInitializeRhoOffsets();
    	long[] data = aData;
        for ( indexRound = 0; indexRound < nt; indexRound++) {
        	data = round(data);
        }
        return data;
    }
    
    private static long[] round(long[] data) {

    	System.out.print("Before theta : ");
    	ByteUtil.printArray(data);
    	System.out.println();
    	
    	long[] aData = theta(data);
        
    	System.out.print("Before rho : ");
        ByteUtil.printArray(aData);
    	System.out.println();
    	
        aData = rho(aData);
        
        System.out.print("Before pi : ");
        ByteUtil.printArray(aData);
    	System.out.println();

        aData = pi(aData);
        
        System.out.print("Before chi : ");
        ByteUtil.printArray(aData);
    	System.out.println();
    	
        aData = chi(aData);
        
        System.out.print("Before iota : ");
        ByteUtil.printArray(aData);
    	System.out.println();
    	
        aData = iota(aData);

    	return aData;
    }
    
    private static long[] theta(long[] data) {

    	int x, y;
    	long[] C = new long[5];
    	long[] D = new long[5];
    	long[] ret = Arrays.copyOf(data, data.length);
    	
    	for ( x = 0; x < 5; x++ )
    		C[x] = data[index(x,0)] ^ data[index(x,1)] ^ data[index(x,2)] ^
    		       data[index(x,3)] ^ data[index(x,4)];
    	  	
    	D[0] = C[4] ^ Long.rotateLeft(C[1],1);
    	for ( x = 1; x < 5 ; x++ )
    		D[x] = C[(x-1)%5] ^ Long.rotateLeft(C[(x+1)%5],1);
    	
    	for ( x = 0; x < 5; x++ )
    		for ( y = 0; y < 5; y++ )
    			ret[index(x,y)] = data[index(x,y)] ^ D[x];
    	
/*
    	ByteUtil.printArray(ret);
    	ret = new long[25];
    	C = new long[5];
    	D = new long[5];

        for( x = 0; x < 5; x++ ) {
            C[x] = 0;
            for( y = 0; y < 5; y++ ) 
                C[x] = C[x] ^ ret[index(x, y)];
            D[x] = ROL64(C[x], 1);
        }
        for( x = 0; x < 5; x++ )
            for( y = 0; y < 5; y++ ) {
            	ret[index(x,y)] = ret[index(x,y)] ^ D[(x+1)%5] ^ C[(x+4)%5];
            }
        
        ByteUtil.printArray(ret);*/
        
        return ret;
    }
    
    private static long[] rho(long[] data) {

    	long[] ret = Arrays.copyOf(data, data.length);
    	
    	for( int x = 0; x < 5; x++ )
        	for( int y = 0; y < 5; y++ )
        		ret[index(x,y)] = Long.rotateLeft(ret[index(x,y)], keccakRhoOffsets[index(x,y)]);
    	return ret;
    }
    
    private static long[] pi(long[] data) {

        long[] ret = new long[25];
        
        for( int x = 0; x < 5; x++ )
        	for( int y = 0; y < 5; y++ )
        		ret[index(y,2*x+3*y)] = data[index(x, y)];
        
        return ret;
    }
    
    private static long[] chi(long[] data) {
        int x, y;
        long[] ret = Arrays.copyOf(data, data.length);

        for( y = 0; y < 5; y++ )
            for( x = 0; x < 5; x++ )
            	ret[index(x,y)] = ret[index(x,y)] ^ ((~ret[index(x+1,y)])
                		& ret[index(x+2,y)]);

        return ret;
    }
    
    private static long[] iota(long[] data) {

    	long[] ret = Arrays.copyOf(data, data.length);
    	
   		ret[0] = data[0] ^ RC[indexRound]; // index(0,0) = 0
        return ret;
    }

    private static int index(int x, int y){
    	return (x%5)+5*(y%5);
    }
    
    //private static long ROL64(long A, int offset){
    	  	
    //	return Long.rotateLeft(A, offset);
    //}

}
