package pcs2055.math;

import java.math.BigInteger;
import java.util.Arrays;

public class ByteUtil {

    /**
     * 
     * @param b
     * @param n bits
     * @return
     */
    public static byte[] lpad(byte[] b, int n) {
        
        
        byte[] pad = new byte[n/8];
        int z = n/8 - b.length;
        for (int i=0; i<b.length; i++) {
            pad[z + i] = b[i];        
        }
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
        for (int i=0; i<b.length && i<n/8; i++) {
            pad[i] = b[i];
        }
        return pad;
    }

    public static String bin(int n) {
        
        // converte decimal pra binário
        StringBuilder s = new StringBuilder();
        while(n/2 > 0) {
            s.append(n%2);
            n = (int) n/2;
        }
        s.append(n%2);

        //inverte a ordem
        StringBuilder r = new StringBuilder();
        for (int i=s.length()-1; i>=0; i--)
            r.append(s.charAt(i));
        
        return r.toString();
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
    }

    public static void printArray(byte[] v, String label) {
        System.out.print("\n"+label);
        printArray(v);
    }
    
    public static void printArray(byte[] v) {
        
        for (int i=0; i<v.length; i++)
            System.out.print(Integer.toHexString((short)(0x000000FF & v[i])) + " ");
    }

    public static void printArray(int[] v, String label) {
        System.out.print("\n"+label);
        printArray(v);
    }
    
    public static void printArray(int[] v) {
        
        for (int i=0; i<v.length; i++)
            System.out.print(Integer.toHexString(v[i]) + " ");
    }
    
    public static void printArray(long[] v) {
        
        for (int i=0; i<v.length; i++)
            System.out.print(Long.toHexString(v[i]) + " ");
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
        for (int i=0; i<length; i++) {
            byte x = a[i];
            byte y = b[i];
            c[i] = (byte) (x ^ y);
        }
        
        return c;
    }
    
    public static long[] xor(long[] a, long[] b, int length) {
        
        long[] c = new long[length];
        for (int i=0; i<length; i++) {
            c[i] = (long)(a[i] ^ b[i]);
        }
        
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
    
    public static void print3xn(byte[] b, int length, String label) {
        
        System.out.println(label);
        print3xn(b, length);
    }    
    
    /**
     * imprime vetor em forma de matriz 3xn, 
     * jogando os valores em coluna
     * @param b
     * @param length plz, múltiplo de 3!
     */
    public static void print3xn(byte[] b, int length) {
        
        int n = length / 3;
        for (int i=0; i<3; i++) {
            
            for (int j=0; j<n; j++) {
                System.out.printf("%x ", b[j*3 + i]);
            }
            System.out.println("");
        }
        System.out.println("");
    }
    
    /**
     * faz bin(n)||1
     * @param n
     * @return
     */
    public static byte[] binConcat1(int n) {

        StringBuilder s = new StringBuilder(bin(n));
        if (s.toString().equals("0")) // if descoberto pragmaticamente por eng. reversa õÓ
            s = new StringBuilder("");
        s.append("1");
        // completa com zeros
        int m = 8-s.length()%8;
        for (int i=0; i<m; i++)
            s.append("0");
        
        // reconverte para inteiro
        BigInteger dois = BigInteger.valueOf(2);
        BigInteger bi = BigInteger.valueOf(0);
        int exp = 0;
        for (int i=s.length()-1; i>=0; i--){
            int x = Integer.parseInt(s.substring(i, i+1));
            BigInteger f = dois.pow(exp);
            BigInteger p = BigInteger.valueOf(x).multiply(f);
            bi = bi.add(p);
            exp++;
        }
        
        // gambiarra pra jogar fora zero a esquerda
        byte[] r = bi.toByteArray();
        if (r[0] == 0)
            r = Arrays.copyOfRange(r, 1, r.length);
        
        return r;
    }
    
    public static byte convertIntToHexa(int i){

    	byte[] table = {0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7,
    			0x8, 0x9, 0xA, 0xB, 0xC, 0xD, 0xE, 0xF};
    	
    	int uh = i/16;
    	int ul = i%16;
    	return (byte)((byte)((table[uh]<<4)&0xF0)|table[ul]);
    	
    }
    
    public static int compareArray(byte[] x, byte[] y) {
    	if (x.length != y.length)
    		return 0;
    	for (int i = 0; i < x.length; i++)
    		if ( x[i] != y[i])
    			return 0;
    	return 1;
    }
    
    /**
     * Converte string que possui caracteres hexadecimais
     * @param str
     * @return vetor de bytes correspondente à string
     */
    public static byte[] convertHexString(String str) {
        
        int n = str.length();
        if (n%2 == 1) { // ímpar
            str = "0" + str;
            n++;
        }
        
        byte[] bytes = new byte[n/2];
        for (int i=0; i<n/2; i++) {
            String hex = str.substring(2*i, 2*i+2);
            // unsignedInt = signedByte & 0xFF
            bytes[i] = (byte) (0xFF & Integer.parseInt(hex, 16));
        }
        return bytes;
    }
    
    public static byte[] longToByteArray (long val) {

    	byte[] buf = new byte[8];
    	buf[0] = (byte)(val & 0xFF);
		buf[1] = (byte)((val >> 8) & 0xFF);
		buf[2] = (byte)((val >> 16) & 0xFF);
		buf[3] = (byte)((val >> 24) & 0xFF);
		buf[4] = (byte)((val >> 32) & 0xFF);
		buf[5] = (byte)((val >> 40) & 0xFF);
		buf[6] = (byte)((val >> 48) & 0xFF);
		buf[7] = (byte)((val >> 56) & 0xFF);
		return buf;
	}
    
    public static long byteArrayToLong (byte[] val) {
		
    	long buf = 0x0L;
    	buf = (((long) val[0]) & 0xFF) |
    		  (((long) val[1]) & 0xFF) << 8 |
    		  (((long) val[2]) & 0xFF) << 16 |
    		  (((long) val[3]) & 0xFF) << 24 |
    		  (((long) val[4]) & 0xFF) << 32 |
    		  (((long) val[5]) & 0xFF) << 40 |
    		  (((long) val[6]) & 0xFF) << 48 |
    		  (((long) val[7]) & 0xFF) << 56;
		return buf;
	}
    
    public static byte invertByte (byte x){
    	
    	BigInteger y = BigInteger.valueOf(x);
    	BigInteger temp = BigInteger.ZERO;
    	
    	for (int i = 0; i < 8; i++)
    		if ( y.testBit(i) )
    			temp = temp.setBit(8-i-1);

    	return temp.byteValue();
    }
    
    public static byte[] invertByteArray(byte[] x){
    	
    	byte[] tmp = new byte[x.length];
    	
    	for (int i = 0; i < x.length; i++){
    		tmp[i] = invertByte(x[i]);
    	}
    	
    	return tmp;
    }
    
    public static long[] invertLongArray (long[] x){
    	
    	byte[] tmp = new byte[8];
    	long[] ret = Arrays.copyOf(x, x.length);
    	
    	for (int i = 0; i < x.length; i++){
    		tmp = longToByteArray(x[i]);
    		tmp = invertByteArray(tmp);
    		ret[i] = byteArrayToLong(tmp);
    	}
    	return ret;
    }

    public static long[] byteArrayToLongArray (byte[] x){
    	
    	long[] ret = new long[x.length/8];
    	byte[] tmp = new byte[8];
    	
    	for (int i = 0; i < x.length/8; i++) {
    	    for (int j = 0; j < 8; j++)
    		    tmp[j] = x[8*i + j];
    	    ret[i] = ByteUtil.byteArrayToLong(tmp);
    	}
    	
    	return ret;
    }
    
    public static byte[] longArrayToByteArray (long[] x){
    	
    	byte[] tmp;
    	byte[] y = new byte[0];
    	byte[] tmp2 = new byte[8];
    	
    	for (int i = 0; i < x.length; i++){
    		tmp = Arrays.copyOf(y, y.length);
    		tmp2 = ByteUtil.longToByteArray(x[i]);
    		y = new byte[y.length + 8];
    		y = ByteUtil.append(tmp, tmp2, tmp.length, tmp2.length)	;	
    	}
    	
    	return y;
    }

}
