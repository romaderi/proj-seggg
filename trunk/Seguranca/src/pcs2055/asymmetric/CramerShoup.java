package pcs2055.asymmetric;

import java.math.BigInteger;

import pcs2055.hash.HashFunction;
import pcs2055.sponge.SpongeRandom;

public class CramerShoup implements KeyEncapsulation {
    
    // TODO checar pads
    
    // 256 bits para fornecer um nível de segurança de 128 bits
    private static int PRIVATE_BITS_KEY_SIZE = 256;
    
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
        random.init(PRIVATE_BITS_KEY_SIZE);
        random.feed(passwd.getBytes(), passwd.getBytes().length);
        
        // calcula chaves privadas (quíntupla de inteiros módulo q)
        // (x1,x2,y1,y2,z) random values in [0, q-1]
        int zLength = PRIVATE_BITS_KEY_SIZE/8; 
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
        
        // calcula chaves públicas (tripla de inteiros módulo p)
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

        // chave pública fornecida
        BigInteger c = pk[0];
        BigInteger d = pk[1];
        BigInteger h = pk[2];
        
        // chooses a random r from {0, ..., q-1}
        byte[] zz = random.fetch(null, PRIVATE_BITS_KEY_SIZE); 
        BigInteger r = new BigInteger(zz).mod(q); 
         
        // encripta a chave simétrica m com a chave pública pk
        // u1 = g1^r , u2 = g2^r , e = h^r * m, α = H(u1 , u2 , e), v = c^r * d^(rα) 
        BigInteger u1 = g1.modPow(r, p); 
        BigInteger u2 = g2.modPow(r, p); 
        BigInteger mm = new BigInteger(m);
        BigInteger e = h.modPow(r, p).multiply(mm).mod(p); 
        hash.init(PRIVATE_BITS_KEY_SIZE); 
        hash.update(u1.toByteArray(), u1.toByteArray().length);
        hash.update(u2.toByteArray(), u2.toByteArray().length);
        hash.update(e.toByteArray(), e.toByteArray().length);
        BigInteger alfa = new BigInteger(hash.getHash(null));
        BigInteger v = c.modPow(r, p);
        BigInteger kalfa = r.multiply(alfa).mod(p);
        v = v.multiply(d.modPow(kalfa, p)).mod(p);
        
        return new BigInteger[]{u1, u2, e, v};
    }

    @Override
    public byte[] decrypt(String passwd, BigInteger[] cs) {

        // chave simétrica criptografada em {u1,u2,e,v}
        BigInteger u1 = cs[0];
        BigInteger u2 = cs[1];
        BigInteger e  = cs[2];
        BigInteger v  = cs[3];
        
        // faz teste pra checar se é possível descriptografar
        // α = H(u1 , u2 , e)
        // teste: u1^(x1 + y1*α) * u2^(x2 + y2*α) = v
        hash.init(PRIVATE_BITS_KEY_SIZE); 
        hash.update(u1.toByteArray(), u1.toByteArray().length);
        hash.update(u2.toByteArray(), u2.toByteArray().length);
        hash.update(e.toByteArray(), e.toByteArray().length);
        BigInteger alfa = new BigInteger(hash.getHash(null));
        BigInteger check = u1.modPow(x1, p);
        check = check.multiply(u2.modPow(x2, p)).mod(p);
        BigInteger factor = u1.modPow(y1, p);
        factor = factor.multiply(u2.modPow(y2, p));
        factor = factor.modPow(alfa, p);
        check = check.multiply(factor).mod(p);
        if (!check.equals(v))
            return null;
        
        // descriptografa e retorna a chave simétrica
        // m = e / (u1^z) mod p
        BigInteger fac = u1.modPow(z, p).modInverse(p);
        BigInteger m = e.multiply(fac).mod(p);
        
        // convert to byte array, without the left-padding of “0” bits
        byte[] mbytes = m.toByteArray();
        
        return mbytes;
    }
}
