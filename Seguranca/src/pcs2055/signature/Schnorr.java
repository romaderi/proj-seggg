package pcs2055.signature;

import java.math.BigInteger;

import pcs2055.hash.HashFunction;
import pcs2055.sponge.SpongeRandom;

public class Schnorr implements DigitalSignature {
    
    // chave privada
    private BigInteger x;
    
    // chave pública
    private BigInteger y;
    
    // atributos do setup
    private BigInteger p, q, g;
    private HashFunction hash;
    private SpongeRandom random;    
    
    @Override
    public void setup(BigInteger p, BigInteger q, BigInteger g, HashFunction H, SpongeRandom sr) {

        this.p = p;
        this.q = q;
        this.g = g;
        this.hash = H;
        this.random = sr;        
    }

    @Override
    public BigInteger makeKeyPair(String passwd) {

        // alimenta o gerador aleatório
        random.feed(passwd.getBytes(), passwd.getBytes().length);

        // choose a private key x such that 0 < x < q
        int zLength = 128; // TODO: zlength???
        byte[] zbuf = new byte[zLength]; 
        zbuf = random.fetch(zbuf, zLength);        
        x = new BigInteger(zbuf).mod(q);
        
        // calcula y = g^x mod p
        y = g.modPow(x, p);
        
        return y;
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void update(byte[] aData, int aLength) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public BigInteger[] sign(String passwd) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public boolean verify(BigInteger y, BigInteger[] sig) {
        // TODO Auto-generated method stub
        return false;
    }

}
