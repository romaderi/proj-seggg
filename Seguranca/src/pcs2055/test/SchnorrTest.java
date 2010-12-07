package pcs2055.test;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

import pcs2055.app.Defaults;
import pcs2055.hash.HashFunction;
import pcs2055.hash.Keccak;
import pcs2055.signature.DigitalSignature;
import pcs2055.signature.Schnorr;
import pcs2055.sponge.SpongePRNG;
import pcs2055.sponge.SpongeRandom;

public class SchnorrTest {

    private DigitalSignature ds;
    
    @Before
    public void setUp() throws Exception {
        
        HashFunction hash = new Keccak();
        SpongeRandom random = new SpongePRNG();

        ds = new Schnorr();
        ds.setup(Defaults.p, Defaults.q, Defaults.g, hash, random);
    }

    /**
     * Teste de consistência interna
     * Não garante inter-operabilidade
     */    
    @Test
    public void selfTest() {
        
        String senha = "aligeiraraposaatacaocaopreguicoso";
        String text = "estaehastringqrepresentaotextoqseraassinadodigitalmentepoisassimsuaveracidadeestaragarantida";

        BigInteger pk = ds.makeKeyPair(senha);
        ds.init();
        ds.update(text.getBytes(), text.getBytes().length);
        BigInteger[] sign = ds.sign(senha);
        boolean ok = ds.verify(pk, sign);
        
        assertEquals(true, ok);
    }
    
    //@Test 
    public void selfMiniTest() {

        String senha = "aligeiraraposaatacaocaopreguicoso";
        byte[] M = new byte[]{(byte) 0xC4, 0x54};
        
        BigInteger pk = ds.makeKeyPair(senha);
        ds.init();
        ds.update(M, M.length);
        BigInteger[] sign = ds.sign(senha);
        boolean ok = ds.verify(pk, sign);
        
        assertEquals(true, ok);
    }

}
