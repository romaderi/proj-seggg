package pcs2055.test;


import static org.junit.Assert.assertArrayEquals;

import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

import pcs2055.app.Defaults;
import pcs2055.asymmetric.CramerShoup;
import pcs2055.asymmetric.KeyEncapsulation;
import pcs2055.hash.HashFunction;
import pcs2055.hash.Keccak;
import pcs2055.sponge.SpongePRNG;
import pcs2055.sponge.SpongeRandom;

public class CramerShoupTest {
    
    private KeyEncapsulation ke;
    private String senha;
    private byte[] m;// chave simétrica

    @Before
    public void setUp() throws Exception {
        
        senha = "otrabalhodobarretoehmtlegalpenaqdahmttrabalho";
        m = senha.getBytes(); // na verdade m é independente da senha
        HashFunction hash = new Keccak();
        SpongeRandom random = new SpongePRNG();
        
        ke = new CramerShoup();
        ke.setup(Defaults.p, Defaults.q, Defaults.g1, Defaults.g2, hash, random);
    }
    
    /**
     * Teste de consistência interna
     * Não garante inter-operabilidade
     */
    @Test
    public void selfTest() {
        
        BigInteger[] pk = ke.makeKeyPair(senha);
        
        BigInteger[] cs = ke.encrypt(pk, m); // crammer-shoup criptograma
        
        byte[] decryptedKey = ke.decrypt(senha, cs);
        
        assertArrayEquals(m, decryptedKey);
    }

}
