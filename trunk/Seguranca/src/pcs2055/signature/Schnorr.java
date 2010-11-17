package pcs2055.signature;

import java.math.BigInteger;

import pcs2055.hash.HashFunction;
import pcs2055.sponge.SpongeRandom;

public class Schnorr implements DigitalSignature {
    
    // chave privada
    private BigInteger x;
    
    // chave pública
    private BigInteger y;
    
    @Override
    public BigInteger akeKeyPair(String passwd) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setup(BigInteger p, BigInteger q, BigInteger g, HashFunction H, SpongeRandom sr) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public BigInteger[] sign(String passwd) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void update(byte[] aData, int aLength) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean verify(BigInteger y, BigInteger[] sig) {
        // TODO Auto-generated method stub
        return false;
    }

}
