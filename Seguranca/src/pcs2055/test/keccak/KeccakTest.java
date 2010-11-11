package pcs2055.test.keccak;

import static org.junit.Assert.assertArrayEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pcs2055.hash.Keccak;
import pcs2055.hash.KeccakF;
import pcs2055.math.ByteUtil;
import pcs2055.sponge.Duplex;

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
    Duplex keccak;

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

        // 2
        in = tests.get(0).getRounds1().get(20).beforeTeta();
        out = tests.get(0).getRounds1().get(20).beforeRho();
        assertArrayEquals(ByteUtil.byteArrayToLongArray(out), f.theta(ByteUtil.byteArrayToLongArray(in)));

        // 3
        in = tests.get(0).getRounds2().get(12).beforeTeta();
        out = tests.get(0).getRounds2().get(12).beforeRho();
        assertArrayEquals(ByteUtil.byteArrayToLongArray(out), f.theta(ByteUtil.byteArrayToLongArray(in)));

        // 4
        in = tests.get(1).getRounds1().get(10).beforeTeta();
        out = tests.get(1).getRounds1().get(10).beforeRho();
        assertArrayEquals(ByteUtil.byteArrayToLongArray(out), f.theta(ByteUtil.byteArrayToLongArray(in)));

        // 5
        in = tests.get(1).getRounds2().get(3).beforeTeta();
        out = tests.get(1).getRounds2().get(3).beforeRho();
        assertArrayEquals(ByteUtil.byteArrayToLongArray(out), f.theta(ByteUtil.byteArrayToLongArray(in)));

    }

    @Test
    public void testRho() {
        
        // 1
        byte[] in = tests.get(0).getRounds1().get(0).beforeRho();
        byte[] out = tests.get(0).getRounds1().get(0).beforePi();
        assertArrayEquals(ByteUtil.byteArrayToLongArray(out), f.rho(ByteUtil.byteArrayToLongArray(in)));

        // 2
        in = tests.get(0).getRounds1().get(2).beforeRho();
        out = tests.get(0).getRounds1().get(2).beforePi();
        assertArrayEquals(ByteUtil.byteArrayToLongArray(out), f.rho(ByteUtil.byteArrayToLongArray(in)));

        // 3
        in = tests.get(0).getRounds2().get(17).beforeRho();
        out = tests.get(0).getRounds2().get(17).beforePi();
        assertArrayEquals(ByteUtil.byteArrayToLongArray(out), f.rho(ByteUtil.byteArrayToLongArray(in)));

        // 4
        in = tests.get(1).getRounds1().get(8).beforeRho();
        out = tests.get(1).getRounds1().get(8).beforePi();
        assertArrayEquals(ByteUtil.byteArrayToLongArray(out), f.rho(ByteUtil.byteArrayToLongArray(in)));

        // 5
        in = tests.get(1).getRounds2().get(5).beforeRho();
        out = tests.get(1).getRounds2().get(5).beforePi();
        assertArrayEquals(ByteUtil.byteArrayToLongArray(out), f.rho(ByteUtil.byteArrayToLongArray(in)));
    }

    @Test
    public void testPi() {
        
        // 1
        byte[] in = tests.get(0).getRounds1().get(0).beforePi();
        byte[] out = tests.get(0).getRounds1().get(0).beforeChi();
        assertArrayEquals(ByteUtil.byteArrayToLongArray(out), f.pi(ByteUtil.byteArrayToLongArray(in)));

        // 2
        in = tests.get(0).getRounds1().get(5).beforePi();
        out = tests.get(0).getRounds1().get(5).beforeChi();
        assertArrayEquals(ByteUtil.byteArrayToLongArray(out), f.pi(ByteUtil.byteArrayToLongArray(in)));

        // 3
        in = tests.get(0).getRounds2().get(7).beforePi();
        out = tests.get(0).getRounds2().get(7).beforeChi();
        assertArrayEquals(ByteUtil.byteArrayToLongArray(out), f.pi(ByteUtil.byteArrayToLongArray(in)));

        // 4
        in = tests.get(1).getRounds1().get(2).beforePi();
        out = tests.get(1).getRounds1().get(2).beforeChi();
        assertArrayEquals(ByteUtil.byteArrayToLongArray(out), f.pi(ByteUtil.byteArrayToLongArray(in)));

        // 5
        in = tests.get(1).getRounds2().get(15).beforePi();
        out = tests.get(1).getRounds2().get(15).beforeChi();
        assertArrayEquals(ByteUtil.byteArrayToLongArray(out), f.pi(ByteUtil.byteArrayToLongArray(in)));
    }

    @Test
    public void testChi() {
        
        // 1
        byte[] in = tests.get(0).getRounds1().get(0).beforeChi();
        byte[] out = tests.get(0).getRounds1().get(0).beforeIota();
        assertArrayEquals(ByteUtil.byteArrayToLongArray(out), f.chi(ByteUtil.byteArrayToLongArray(in)));

        // 2
        in = tests.get(0).getRounds1().get(2).beforeChi();
        out = tests.get(0).getRounds1().get(2).beforeIota();
        assertArrayEquals(ByteUtil.byteArrayToLongArray(out), f.chi(ByteUtil.byteArrayToLongArray(in)));

        // 3
        in = tests.get(0).getRounds2().get(12).beforeChi();
        out = tests.get(0).getRounds2().get(12).beforeIota();
        assertArrayEquals(ByteUtil.byteArrayToLongArray(out), f.chi(ByteUtil.byteArrayToLongArray(in)));

        // 4
        in = tests.get(1).getRounds1().get(7).beforeChi();
        out = tests.get(1).getRounds1().get(7).beforeIota();
        assertArrayEquals(ByteUtil.byteArrayToLongArray(out), f.chi(ByteUtil.byteArrayToLongArray(in)));

        // 5
        in = tests.get(1).getRounds2().get(21).beforeChi();
        out = tests.get(1).getRounds2().get(21).beforeIota();
        assertArrayEquals(ByteUtil.byteArrayToLongArray(out), f.chi(ByteUtil.byteArrayToLongArray(in)));

    }
    
    @Test
    public void testIota() {
        
        // 1
        byte[] in = tests.get(0).getRounds1().get(0).beforeIota();
        byte[] out = tests.get(0).getRounds1().get(1).beforeTeta();
        assertArrayEquals(ByteUtil.byteArrayToLongArray(out), f.iota(ByteUtil.byteArrayToLongArray(in)));

        // 2
        in = tests.get(0).getRounds1().get(1).beforeIota();
        out = tests.get(0).getRounds1().get(2).beforeTeta();
        assertArrayEquals(ByteUtil.byteArrayToLongArray(out), f.iota(ByteUtil.byteArrayToLongArray(in), 1));

        // 3
        in = tests.get(0).getRounds2().get(0).beforeIota();
        out = tests.get(0).getRounds2().get(1).beforeTeta();
        assertArrayEquals(ByteUtil.byteArrayToLongArray(out), f.iota(ByteUtil.byteArrayToLongArray(in)));

        // 4
        in = tests.get(0).getRounds2().get(1).beforeIota();
        out = tests.get(0).getRounds2().get(2).beforeTeta();
        assertArrayEquals(ByteUtil.byteArrayToLongArray(out), f.iota(ByteUtil.byteArrayToLongArray(in), 1));

        // 5
        in = tests.get(1).getRounds2().get(1).beforeIota();
        out = tests.get(1).getRounds2().get(2).beforeTeta();
        assertArrayEquals(ByteUtil.byteArrayToLongArray(out), f.iota(ByteUtil.byteArrayToLongArray(in), 1));
    }
    
    @Test
    public void testZ() {
        
        keccak = new Keccak();

        keccak.init(0);
        byte[] in = tests.get(0).getSigma0();
        byte[] out = tests.get(0).getZ0();
        byte[] z = keccak.duplexing(in, in.length, null, 10);
        assertArrayEquals(out, z);        

        in = tests.get(0).getSigma1();
        out = tests.get(0).getZ1();
        z = keccak.duplexing(in, in.length, null, 10);
        assertArrayEquals(out, z);            

        keccak.init(0);
        in = tests.get(1).getSigma0();
        out = tests.get(1).getZ0();
        z = keccak.duplexing(in, in.length, null, 10);
        assertArrayEquals(out, z);  
        
        in = tests.get(1).getSigma1();
        out = tests.get(1).getZ1();
        z = keccak.duplexing(in, in.length, null, 10);
        assertArrayEquals(out, z);            
    }
    
}
