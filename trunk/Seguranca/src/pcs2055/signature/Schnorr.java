package pcs2055.signature;

import java.math.BigInteger;

import pcs2055.hash.HashFunction;
import pcs2055.math.ByteUtil;
import pcs2055.sponge.SpongeRandom;

public class Schnorr implements DigitalSignature {
    
    // 256 bits para fornecer um nível de segurança de 128 bits
    private static int PRIVATE_BITS_KEY_SIZE = 256;

    // chave privada (módulo q)
    private BigInteger x;
    
    // chave pública (módulo p)
    private BigInteger y;
    
    private byte[] M; // mensagem
    
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
        String id = "Schnorr";
        random.init(PRIVATE_BITS_KEY_SIZE);
        random.feed(passwd.getBytes(), passwd.getBytes().length);
        random.feed(id.getBytes(), id.getBytes().length);

        // choose a private key x such that 0 < x < q
        int zLength = PRIVATE_BITS_KEY_SIZE; 
        byte[] zbuf = null; 
        zbuf = random.fetch(zbuf, zLength);        
        x = new BigInteger(zbuf).mod(q);

        // calcula y = g^x mod p
        y = g.modPow(x, p);
        
        return y;
    }

    @Override
    public void init() {

        M = new byte[0];
    }

    @Override
    public void update(byte[] aData, int aLength) {

        M = ByteUtil.append(M, aData, M.length, aLength);
    }

    @Override
    public BigInteger[] sign(String passwd) {

        // Choose a random k such that 0 < k < q
        int zzLength = PRIVATE_BITS_KEY_SIZE/8; 
        byte[] zz = random.fetch(null, zzLength); 
        BigInteger r = new BigInteger(zz).mod(q);
        byte[] u = g.modPow(r, p).toByteArray();
        
        byte[] hu = ByteUtil.append(M, u, M.length, u.length);
        hash.init(PRIVATE_BITS_KEY_SIZE); 
        hash.update(hu, hu.length);
        byte[] zero = new byte[1];
        byte[] resume = hash.getHash(null);
        BigInteger e = new BigInteger(ByteUtil.append(zero, resume, 1, resume.length)).mod(q); // mod q é o segredo!
        BigInteger s = r.subtract(x.multiply(e)).mod(q);
        
        return new BigInteger[]{e, s};
    }
    
    @Override
    public boolean verify(BigInteger y, BigInteger[] sig) {

        BigInteger e = sig[0];
        BigInteger s = sig[1];
        
        BigInteger u = g.modPow(s, p);
        BigInteger factor = y.modPow(e, p);
        u = u.multiply(factor).mod(p);

        byte[] Mu = ByteUtil.append(M, u.toByteArray(), M.length, u.toByteArray().length);
        hash.init(PRIVATE_BITS_KEY_SIZE); 
        hash.update(Mu, Mu.length);
        byte[] zero = new byte[1];
        byte[] resume = hash.getHash(null);
        BigInteger ev = new BigInteger(ByteUtil.append(zero, resume, 1, resume.length)).mod(q); // mod q é o segredo!

        if (ev.equals(e))
            return true;
        else
            return false;
    }
    
}
