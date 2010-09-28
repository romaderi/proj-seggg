package pcs2055.math;

import java.math.BigInteger;

import sun.security.util.BigInt;

public class MathUtil {
    
    // GF(8)
    
    public static byte multGf8(byte a, byte b) {
        
        return 0;
    }
    
    // curupira
    
    // marvin
    
    // letter soup
    

    public static byte[] bin(int n) {
        
        return null;
    }
    
    public static byte[] lfsrc(byte[] b) {
        
        return null;
    }

    /**
     * Wordwise multiplication by x^w in GF(2^n)
     * Algoritmo 6
     * @return
     */
    public static byte[] xtimes(byte[] V, int w) {
        
        // p(x) = x^n + x^k3 + x^k2 + x^k1 + 1, primitive pentanomial
        int k3, k2, k1;
        
        V = rotl(V, w); // ???

        BigInteger dois = BigInteger.valueOf(2);
        BigInteger ff = BigInteger.valueOf(0xFF);
        BigInteger v = new BigInteger(V);
        BigInteger R = new BigInteger(V);
        BigInteger r1 = new BigInteger(V);
        BigInteger r2 = new BigInteger(V);
        BigInteger r3 = new BigInteger(V);

        // R <- V & 0xFF
        R = R.and(ff);
        
        // r1 <- R << k1
        for (int i=0; i< k1; i++)
            r1 = r1.multiply(dois);
        // r2 <- R << k2
        for (int i=0; i< k2; i++)
            r2 = r2.multiply(dois);
        // r3 <- R << k3
        for (int i=0; i< k3; i++)
            r3 = r3.multiply(dois);

        // V <- V xor (R << k3) xor (R << k2) xor (R << k1)
        v = v.xor(r1);
        v = v.xor(r2);
        v = v.xor(r3);
        
        return v.toByteArray();
    }

}
