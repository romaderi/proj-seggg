package pcs2055.test.keccak;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pcs2055.hash.KeccakF;
import pcs2055.math.ByteUtil;

/**
 * Testes do Kekkac baseados nos vetores de teste
 *
 */
public class KeccakTest {

    private final String TEST_FILE1 = "test/Test.Vectors.256bit.Keccak.txt";
    private final String TEST_FILE2 = "test/Test.Vectors.512bit.Keccak.txt";
    private final String TEST_FILE3 = "test/Test.Vectors.Default.Keccak.txt";
    
    private List<CaseTest> tests;
    private KeccakF f = new KeccakF();

    @Before
    public void setUp() throws Exception {
        
        TestReader reader = new TestReader(TEST_FILE3);
        this.tests = reader.parseTests(); // obtêm valores dos vetores de teste
        
    }

    @Test
    public void testTheta() {
        
        // 1
        byte[] in = tests.get(0).getRounds1().get(0).beforeTeta();
        byte[] out = tests.get(0).getRounds1().get(0).beforeRho();
        assertArrayEquals(ByteUtil.byteArrayToLongArray(out), f.theta(ByteUtil.byteArrayToLongArray(in)));
    }

    @Test
    public void testRho() {
        
        // 1
        byte[] in = tests.get(0).getRounds1().get(0).beforeRho();
        byte[] out = tests.get(0).getRounds1().get(0).beforePi();
        assertArrayEquals(ByteUtil.byteArrayToLongArray(out), f.rho(ByteUtil.byteArrayToLongArray(in)));
    }

    @Test
    public void testPi() {
        
        // 1
        byte[] in = tests.get(0).getRounds1().get(0).beforePi();
        byte[] out = tests.get(0).getRounds1().get(0).beforeChi();
        assertArrayEquals(ByteUtil.byteArrayToLongArray(out), f.pi(ByteUtil.byteArrayToLongArray(in)));
    }

    @Test
    public void testChi() {
        
        // 1
        byte[] in = tests.get(0).getRounds1().get(0).beforeChi();
        byte[] out = tests.get(0).getRounds1().get(0).beforeIota();
        assertArrayEquals(ByteUtil.byteArrayToLongArray(out), f.chi(ByteUtil.byteArrayToLongArray(in)));
    }
    
    @Test
    public void testIota() {
        
        // 1
        byte[] in = tests.get(0).getRounds1().get(0).beforeIota();
        byte[] out = tests.get(0).getRounds1().get(1).beforeTeta();
        assertArrayEquals(ByteUtil.byteArrayToLongArray(out), f.iota(ByteUtil.byteArrayToLongArray(in)));
    }


    
}
