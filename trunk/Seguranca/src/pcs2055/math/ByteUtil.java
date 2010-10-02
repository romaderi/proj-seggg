package pcs2055.math;

import java.math.BigInteger;

public class ByteUtil {

    /**
     * 
     * @param b
     * @param n bits
     * @return
     */
    public static byte[] lpad(byte[] b, int n) {
        
        byte[] pad = new byte[n/8];
        int z = n - b.length;
        for (int i=0; i<b.length; i++)
            pad[z + i] = b[i];        
        return pad;
    }

    /**
     * 
     * @param b
     * @param n bits
     * @return
     */
    public static byte[] rpad(byte[] b, int n) {

        byte[] pad = new byte[n/8];
        for (int i=0; i<b.length; i++)
            pad[i] = b[i];
        return pad;
    }

    public static byte[] bin(byte[] b, int n) {
        
        return null;
    }

    public static byte[] add(byte[] a, byte[] b, int length) {
        
        byte[] c = new byte[length];
        for (int i=0; i<length; i++)
            c[i] = (byte) (a[i] + b[i]);
        
        return c;
    }
    
    public static byte[] append(byte[] a, byte[] b, int lenA, int lenB) {
        
        byte[] c = new byte[lenA + lenB];
        for (int i=0; i<lenA; i++)
            c[i] = a[i];
        for (int i=0; i<lenB; i++)
            c[i+lenA] = b[i];        
        return c;
    }

    /**
     * multiplicação de matrizes 3xn
     * @param a matriz 3xn
     * @param b matriz nx3
     * @param n 
     * @return matriz 3x3
     */
    public static byte[] mult3xn(byte[] a, byte[] b, int n) {
        
    	int i, j;
    	BigInteger[][] ma = new BigInteger[3][3];
    	BigInteger[][] mb = new BigInteger[3][n];
    	
    	// matriz ma
    	for (i = 0; i < 3; i++)
    		for (j = 0; j < 3; j++)
    			ma[i][j] = BigInteger.valueOf(a[i + 3*j]);
    			
    	// matriz de b
    	for (i = 0; i < 3; i++)
    		for (j = 0; j < n; j++)
    			mb[i][j] = BigInteger.valueOf(b[i + 3*j]);

    	// multiplica
        BigInteger[][] mc = new BigInteger[3][n];
        for (i = 0; i < 3; i++)
            for (j = 0; j < n; j++)
            	mc[i][j] = BigInteger.valueOf(0);
        
        for (i = 0; i < 3; i++) 
            for (j = 0; j < n; j++) 
                for (int k = 0; k < 3; k++)
                    mc[i][j] = mc[i][j].add(ma[i][k].multiply(mb[k][j]));
        
        // volta pra forma de vetor
        byte[] result = new byte[3*n];
    	for (i = 0; i < 3; i++)
    		for (j = 0; j < 2*2; j++)
    			result[i + 3*j] = mc[i][j].byteValue();  
    	
        return result;
    	
    	
    	
    	/*
        // cria matrizes
        byte[][] ma = new byte[3][3];
        byte[][] mb = new byte[3][n];
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++)
                ma[i][j] = (byte) a[i + 3*j];
        }
        for (int i=0; i<3; i++) {
            for (int j=0; j<n; j++)
                mb[i][j] = (byte) b[i + 3*j];
        }

        // multiplica
        byte[][] mc = new byte[3][n];
        for (int i=0; i<3; i++) {
            for (int j=0; j<n; j++) {
                for (int k=0; k<3; k++)
                    mc[i][j] += (byte) (ma[i][k] * mb[k][j]);
            }
        }

        // volta pra forma de vetor
        byte[] c = new byte[3*n];
        for (int i = 0; i < 3; i++)
        	for (int j = 0; j < n; j++)
        		c[i + 3*j] = mc[i][j];

        return c;*/
    }

    public static void printArray(byte[] v) {
        
        for (int i=0; i<v.length; i++)
            System.out.print(Integer.toHexString((short)(0x000000FF & v[i])) + " ");
        System.out.println("");
    }

    public static void printMatriz(byte[][] m) {
        
        for (int i=0; i<m.length; i++) {
            for (int j=0; j<m[i].length; j++)
                System.out.print(m[i][j] + " ");
            System.out.println("");
        }
    }

    public static byte[] xor(byte[] a, byte[] b, int length) {
        
        byte[] c = new byte[length];
        for (int i=0; i<length; i++)
            c[i] = (byte) (a[i] ^ b[i]);
        
        return c;
    }
    
    public static byte xtimes (byte b){
    	int i = (int)(b) & 0xFF;
    	if ( b < 0)
    		return (byte)((b << 1) ^ 0x4D); 
    	return (byte)(b << 1);
    }

    public static byte ctimes (byte b){
    	return xtimes(xtimes((byte)(xtimes((byte)((xtimes(b))^b))^b)));
    }
    
}
