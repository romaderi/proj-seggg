package pcs2055.signature;

import java.math.BigInteger;

import pcs2055.hash.HashFunction;
import pcs2055.math.ByteUtil;
import pcs2055.sponge.SpongeRandom;

public class Schnorr implements DigitalSignature {
    
    // TODO nomes das variáveis estão de acordo com wikipedia
    // deixar de acordo com os nomes nas interfaces
    
    // chave privada
    private BigInteger x;
    
    // chave pública
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

        M = new byte[0];
        
        // TODO verificar mod p q
        
        // Choose a random k such that 0 < k < q
        byte[] zz = random.fetch(null, 128); // TODO: 128???
        k = new BigInteger(zz).mod(q);
        r = g.modPow(k, q).toByteArray();
    }

    @Override
    public void update(byte[] aData, int aLength) {

        ByteUtil.append(M, aData, M.length, aLength);
    }

    @Override
    public BigInteger[] sign(String passwd) {

        byte[] hr = ByteUtil.append(M, r, M.length, r.length);
        hash.init(0); // TODO 0 ???
        hash.update(hr, hr.length);
        BigInteger e = new BigInteger(hash.getHash(null));
        BigInteger xe = x.multiply(e).mod(q);
        BigInteger s = k.subtract(xe).mod(q);
        
        return new BigInteger[]{e, s};
    }
    
    @Override
    public boolean verify(BigInteger y, BigInteger[] sig) {

        BigInteger e = sig[0];
        BigInteger s = sig[1];
        
        BigInteger rv = g.modPow(s, q);
        BigInteger factor = y.modPow(e, q);
        rv = rv.multiply(factor).mod(q);

        byte[] Mrv = ByteUtil.append(M, rv.toByteArray(), M.length, rv.toByteArray().length);
        hash.init(0); // TODO 0 ???
        hash.update(Mrv, Mrv.length);
        BigInteger ev = new BigInteger(hash.getHash(null));

        if (ev.equals(e))
            return true;
        else
            return false;
    }

}
