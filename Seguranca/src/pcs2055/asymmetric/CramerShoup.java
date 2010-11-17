package pcs2055.asymmetric;

import java.math.BigInteger;

import pcs2055.hash.HashFunction;
import pcs2055.sponge.SpongeRandom;

public class CramerShoup implements KeyEncapsulation {
    
    // chave privada
    private BigInteger x1, x2, y1, y2, z;
    
    // chave pública
    private BigInteger c, d, h;
    
    // atributos do setup
    private BigInteger p, q, g1, g2;
    private HashFunction hash;
    private SpongeRandom random;

    @Override
    public void setup(BigInteger p, BigInteger q, BigInteger g1, BigInteger g2, HashFunction H, SpongeRandom sr) {

        this.p = p;
        this.q = q;
        this.g1 = g1;
        this.g2 = g2;
        this.hash = H;
        this.random = sr;
    }

    @Override
    public BigInteger[] makeKeyPair(String passwd) {

        // alimenta o gerador aleatório
        random.feed(passwd.getBytes(), passwd.getBytes().length);
        
        // calcula chaves privadas
        // (x1,x2,y1,y2,z) random values in [0, q-1]
        int zLength = 128; // TODO: zlength???
        byte[] zbuf = new byte[zLength]; 
        zbuf = random.fetch(zbuf, zLength);
        x1 = new BigInteger(zbuf).mod(q);
        zbuf = random.fetch(zbuf, zLength);
        x2 = new BigInteger(zbuf).mod(q);
        zbuf = random.fetch(zbuf, zLength);
        y1 = new BigInteger(zbuf).mod(q);
        zbuf = random.fetch(zbuf, zLength);
        y2 = new BigInteger(zbuf).mod(q);
        zbuf = random.fetch(zbuf, zLength);
        z = new BigInteger(zbuf).mod(q);
        
        // calcula chaves públicas
        // c = g1^x1 g2^x2 mod p
        c = g2.modPow(x2, p);
        c = g1.modPow(x1, p).multiply(c).mod(p);
        // d = g1^y1 g2^y2 mod p
        d = g2.modPow(y2, p);
        d = g1.modPow(y1, p).multiply(d).mod(p);
        // h = g1^z mod p
        h = g1.modPow(z, p);
        
        return new BigInteger[]{c, d, h};
    }

    @Override
    public BigInteger[] encrypt(BigInteger[] pk, byte[] m) {

        BigInteger c = pk[0];
        BigInteger d = pk[1];
        BigInteger h = pk[2];
        
        // random value r ???
        
        // encripta a chave simétrica m com a chave pública pk
        BigInteger u1 = null, u2 = null, e = null, v = null;
        
        return new BigInteger[]{u1, u2, e, v};
    }

    @Override
    public byte[] decrypt(String passwd, BigInteger[] cs) {

        BigInteger u1 = cs[0];
        BigInteger u2 = cs[1];
        BigInteger e  = cs[2];
        BigInteger v  = cs[3];
        
        // descriptografa e retorna a chave simétrica
        
        // m = e * (u1^z)^(-1)
        BigInteger m = null;

        // convert to byte array, without the left-padding of “0” bits
        byte[] mbytes = m.toByteArray();
        
        return mbytes;
    }
}
