package pcs2055.math;

import java.math.BigInteger;

public class MathUtil {
    
    // GF(8)
    
    public static byte multGf8(byte a, byte b) {
        
        return 0;
    }
    
    /**
     * Wordwise multiplication by x^w in GF(2^n)
     * Algoritmo 6
     * @return
     */
//    public static byte[] xtimes(byte[] V, int w) {
//        
//        // p(x) = x^n + x^k3 + x^k2 + x^k1 + 1, primitive pentanomial
//        int k3, k2, k1;
//        
//        V = rotl(V, w); // ???
//
//        BigInteger dois = BigInteger.valueOf(2);
//        BigInteger ff = BigInteger.valueOf(0xFF);
//        BigInteger v = new BigInteger(V);
//        BigInteger R = new BigInteger(V);
//        BigInteger r1 = new BigInteger(V);
//        BigInteger r2 = new BigInteger(V);
//        BigInteger r3 = new BigInteger(V);
//
//        // R <- V & 0xFF
//        R = R.and(ff);
//        
//        // r1 <- R << k1
//        for (int i=0; i< k1; i++)
//            r1 = r1.multiply(dois);
//        // r2 <- R << k2
//        for (int i=0; i< k2; i++)
//            r2 = r2.multiply(dois);
//        // r3 <- R << k3
//        for (int i=0; i< k3; i++)
//            r3 = r3.multiply(dois);
//
//        // V <- V xor (R << k3) xor (R << k2) xor (R << k1)
//        v = v.xor(r1);
//        v = v.xor(r2);
//        v = v.xor(r3);
//        
//        return v.toByteArray();
//    }

    /**
     * Wordwise multiplication by x^w in GF(2^n)
     * Algoritmo 6 (w=8)
     * @return
     */
    public static byte[] mult_xw_gf8(byte[] V) {

        // para V.length = 12 bytes
        // TODO: generalizar (???)
        
        // w = 8;
        byte[] res = new byte[12];
        
        for (int i=0; i<12; i++) {
            if (i < 9)
                res[i] = V[i+1];
            if (i == 9)
                res[9] = (byte) (V[10] ^ T1(V[0]));
            if (i == 10)
                res[10] = (byte) (V[11] ^ T0(V[0]));
            if (i == 11)
                res[11] = V[0];
        }
        
        return res;
    }
    
    public static byte T1(byte U) {

        byte[] b = {0, U};
        BigInteger u = new BigInteger(b);
        BigInteger b1 = new BigInteger(b);
        BigInteger b2 = new BigInteger(b);

        b1 = b1.shiftRight(3);
        b2 = b2.shiftRight(5);
        
        byte[] res = u.xor(b1).xor(b2).toByteArray();
        return res[res.length-1];
    }

    public static byte T0(byte U) {

        byte[] b = {0, U};
        BigInteger b1 = new BigInteger(b);
        BigInteger b2 = new BigInteger(b);

        b1 = b1.shiftLeft(5);
        b2 = b2.shiftLeft(3);
        
        byte[] res = b1.xor(b2).toByteArray();
        return res[res.length-1];
    }

}
