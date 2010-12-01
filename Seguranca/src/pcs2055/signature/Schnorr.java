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
    
    private BigInteger k; // número aleatório
    private byte[] r; // usado pro pad
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
        random.init(PRIVATE_BITS_KEY_SIZE);
        random.feed(passwd.getBytes(), passwd.getBytes().length);

        // choose a private key x such that 0 < x < q
        int zLength = PRIVATE_BITS_KEY_SIZE; 
        byte[] zbuf = new byte[zLength]; 
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
        k = new BigInteger(zz).mod(q);
        r = g.modPow(k, p).toByteArray();

        ByteUtil.printArray(r);
        System.out.println();
        System.out.println(this.x);
        System.out.println(this.q);
        
        byte[] hr = ByteUtil.append(M, r, M.length, r.length);
        hash.init(PRIVATE_BITS_KEY_SIZE); 
        hash.update(hr, hr.length);
        byte[] zero = new byte[1];
        byte[] resume = hash.getHash(null);
        BigInteger e = new BigInteger(ByteUtil.append(zero, resume, 1, resume.length));
        BigInteger xe = x.multiply(e).mod(p);
        BigInteger s = k.subtract(xe).mod(q);
        
        return new BigInteger[]{e, s};
    }
    
    @Override
    public boolean verify(BigInteger y, BigInteger[] sig) {

        BigInteger e = sig[0];
        BigInteger s = sig[1];
        
        BigInteger rv = g.modPow(s, p);
        BigInteger factor = y.modPow(e, p);
        rv = rv.multiply(factor).mod(p);

        byte[] Mrv = ByteUtil.append(M, rv.toByteArray(), M.length, rv.toByteArray().length);
        hash.init(PRIVATE_BITS_KEY_SIZE); 
        hash.update(Mrv, Mrv.length);
        byte[] zero = new byte[1];
        byte[] resume = hash.getHash(null);
        BigInteger ev = new BigInteger(ByteUtil.append(zero, resume, 1, resume.length));

        if (ev.equals(e))
            return true;
        else
            return false;
    }

}
