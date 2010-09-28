package pcs2055.math;

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
        
        byte R[] = new byte[V.length];
        byte r1[] = new byte[V.length];
        byte r2[] = new byte[V.length];
        byte r3[] = new byte[V.length];

        for (byte b=0; b<R.length; b++) {
            R[b] = V[b];
            r1[b] = V[b];
            r2[b] = V[b];
            r3[b] = V[b];
        }

        // R <- V & 0xFF
        R[0] = (byte) (R[0] & 0xFF);
        
        // r1 <- R << k1
        for (int i=0; i< k1; i++)
            r1 = r1 * 2;
        // r2 <- R << k2
        for (int i=0; i< k2; i++)
            r2 = r2 * 2;
        // r3 <- R << k3
        for (int i=0; i< k3; i++)
            r3 = r3 * 2;

        // V <- V xor (R << k3) xor (R << k2) xor (R << k1)
        V = ByteUtil.xor(V, r1, V.length);
        V = ByteUtil.xor(V, r2, V.length);
        V = ByteUtil.xor(V, r3, V.length);
        
        return V;
    }

}
