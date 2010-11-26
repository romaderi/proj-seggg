package pcs2055.asymmetric;

import java.math.BigInteger;

import pcs2055.hash.HashFunction;
import pcs2055.sponge.SpongeRandom;

public class CramerShoup implements KeyEncapsulation {
    
    // TODO nomes das variáveis estão de acordo com wikipedia
    // deixar de acordo com os nomes nas interfaces    
    
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

        // chave pública fornecida
        BigInteger c = pk[0];
        BigInteger d = pk[1];
        BigInteger h = pk[2];
        
        // chooses a random k from {0, ..., q-1}
        byte[] zz = random.fetch(null, 128); // TODO: 128???
        BigInteger k = new BigInteger(zz).mod(q);
         
        // encripta a chave simétrica m com a chave pública pk
        BigInteger u1 = g1.modPow(k, q); 
        BigInteger u2 = g2.modPow(k, q); 
        BigInteger mm = new BigInteger(m);
        BigInteger e = h.modPow(k, q).multiply(mm).mod(q); 
        hash.init(0); // TODO 0 ???
        hash.update(u1.toByteArray(), u1.toByteArray().length);
        hash.update(u2.toByteArray(), u2.toByteArray().length);
        hash.update(e.toByteArray(), e.toByteArray().length);
        BigInteger alfa = new BigInteger(hash.getHash(null));
        BigInteger v = c.modPow(k, q);
        BigInteger kalfa = k.multiply(alfa).mod(q);
        v = v.multiply(d.modPow(kalfa, q)).mod(q);
        // TODO: verificar os mod q
        
        return new BigInteger[]{u1, u2, e, v};
    }

    @Override
    public byte[] decrypt(String passwd, BigInteger[] cs) {

        // chave simétrica criptografada em {u1,u2,e,v}
        BigInteger u1 = cs[0];
        BigInteger u2 = cs[1];
        BigInteger e  = cs[2];
        BigInteger v  = cs[3];
        
        // TODO checar mod p, mod q
        
        // faz teste pra checar se é possível descriptografar
        hash.init(0); // TODO 0 ???
        hash.update(u1.toByteArray(), u1.toByteArray().length);
        hash.update(u2.toByteArray(), u2.toByteArray().length);
        hash.update(e.toByteArray(), e.toByteArray().length);
        BigInteger alfa = new BigInteger(hash.getHash(null));
        BigInteger check = u1.modPow(x1, q);
        check = check.multiply(u2.modPow(x2, q)).mod(q);
        BigInteger factor = u1.modPow(y1, q);
        factor = factor.multiply(u2.modPow(y2, q));
        factor = factor.modPow(alfa, q);
        check = check.multiply(factor).mod(q);
        if (!check.equals(v))
            return null;
        
        // descriptografa e retorna a chave simétrica
        
        // m = e * (u1^z)^(-1) mod p
        BigInteger fac = u1.modPow(z, p).modInverse(p);
        BigInteger m = e.multiply(fac).mod(p);
        
        // convert to byte array, without the left-padding of “0” bits
        byte[] mbytes = m.toByteArray();
        // TODO without the left-padding of “0” bits
        
        return mbytes;
    }
}
