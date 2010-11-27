package pcs2055.app;

import java.math.BigInteger;

/**
 * Valores default para o sistema criptográfico 
 * de acordo com o enunciado (parte 3)
 *
 */
public class Defaults {

    // 256 bits para fornecer um nível de segurança de 128 bits
    public final static int PRIVATE_BITS_KEY_SIZE = 256;
    
    public final static BigInteger q;
    public final static BigInteger w;
    public final static BigInteger p;
    public final static BigInteger g;
    public final static BigInteger g1;
    public final static BigInteger g2;
    
    // inicializador estático
    static {
        
        // q = 2^256 – 2^168 + 1
        BigInteger dois = BigInteger.valueOf(2);
        BigInteger um = BigInteger.valueOf(1);
        q = dois.pow(256).subtract(dois.pow(168)).add(um);
        
        // w = 2^2815 + 231
        w = dois.pow(2815).add(BigInteger.valueOf(231));
        
        // p = 2^wq + 1
        int wq = w.multiply(q).intValue();
        p = dois.pow(wq).add(um);
        
        // g = 2^2w mod p
        BigInteger dw = dois.multiply(w).mod(p);
        g = dois.modPow(dw, p);
        
        // g1 = 2055^2w mod p
        g1 = BigInteger.valueOf(2055).modPow(dw, p);

        // g2 = 2582^2w mod p
        g2 = BigInteger.valueOf(2582).modPow(dw, p);
    }
}
