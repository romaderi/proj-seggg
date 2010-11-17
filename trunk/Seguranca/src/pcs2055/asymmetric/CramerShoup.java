package pcs2055.asymmetric;

import java.math.BigInteger;

import pcs2055.hash.HashFunction;
import pcs2055.sponge.SpongeRandom;

public class CramerShoup implements KeyEncapsulation {
    
    // chave privada
    private BigInteger x1, x2, y1, y2, k;
    
    // chave pública
    private BigInteger c, d, h;

    @Override
    public byte[] decrypt(String passwd, BigInteger[] cs) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BigInteger[] encrypt(BigInteger[] pk, byte[] m) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BigInteger[] makeKeyPair(String passwd) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setup(BigInteger p, BigInteger q, BigInteger g1, BigInteger g2, HashFunction H, SpongeRandom sr) {
        // TODO Auto-generated method stub
        
    }

}
