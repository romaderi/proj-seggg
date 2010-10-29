package pcs2055.hash;

import java.util.Arrays;

import pcs2055.math.ByteUtil;

public class KeccakF {
    
    // colocar aqui constantes (segundo enunciado) que determinam número de rounds
	
	private static int b = 1600;
	private static int l = 6;
	private static int nt = 24;  // 12+2*l
	private static int indexRound;
    
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
	
	
    public static long[] f(long[] aData) {
    	
    	long[] data = new long[25];
        //for ( indexRound = 0; indexRound < nt; indexRound++){
        //	data = round(data);
        //}
    	data = round(aData);
        return data;
    }
    
    private static long[] round(long[] data) {

        long[] aData = theta(data);
        /*aData = rho(aData);
        aData = pi(aData);
        aData = chi(aData);
        aData = iota(aData);*/
        return aData;
    }
    
    private static long[] theta(long[] data) {

    	int x, y;
    	long[] C = new long[5];
    	long[] D = new long[5];
    	long[] ret = Arrays.copyOf(data, data.length);
    	
    	for ( x = 0; x < 5; x++ ) {
    		C[x] = data[index(x,0)];
    		for ( y = 1; y < 5; y++ )
    			C[x] = C[x] ^ data[index(x,y)];
    	}
    	  	
    	D[0] = C[4] ^ ROL64(C[1],1);
    	for ( x = 1; x < 5 ; x++ )
    		D[x] = C[(x-1)%5] ^ ROL64(C[(x+1)%5],1);
    	
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
    
    private static long[] iota(long[] data) {

    	long[] ret = Arrays.copyOf(data, data.length);
    	
   		ret[index(0,0)] = data[index(0,0)] ^ RC[indexRound];
        return ret;
    }
    
    private static long[] chi(long[] data) {
        int x, y;
        long[] C = new long[5];
        long[] ret = Arrays.copyOf(data, data.length);

        for( y = 0; y < 5; y++ ) { 
            for( x = 0; x < 5; x++ )
                C[x] = data[index(x,y)] ^ ((~data[index(x+1,y)])
                		& data[index(x+2,y)]);
            for( x = 0; x < 5; x++ )
                data[index(x, y)] = C[x];
        }
        return data;
    }
    
    private static long[] pi(long[] data) {

        long[] ret = new long[25];
        
        for( int x = 0; x < 5; x++ )
        	for( int y = 0; y < 5; y++ )
        		ret[index(0*x+1*y, 2*x+3*y)] = data[index(x, y)];
        
        return ret;
    }
    
    private static long[] rho(long[] data) {

    	long[] ret = Arrays.copyOf(data, data.length);
    	
        //for( int x = 0; x < 5; x++ )
        //	for( int y = 0; y < 5; y++ )
        //		ret[index(x,y)] = ROL64(ret[index(x,y)], KeccakRhoOffsets[index(x,y)]);t;
    	return ret;
    }
    

    
    private static int index(int x, int y){
    	return (x%5)+5*(y%5);
    }
    
    private static long ROL64(long A, int offset){
    	if (offset != 0)
    		return ((A << offset) | (A >> (64-offset)));
    	return A;
    }

}
