package pcs2055.test.keccak;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Testes da Kekkac
 *
 */
public class KeccakTest {
    
    private final String TEST_FILE1 = "test/Test.Vectors.256bit.Keccak.txt";
    
    private List<CaseTest> tests;

    @Before
    public void setUp() throws Exception {
        
        TestReader reader = new TestReader(TEST_FILE1);
        this.tests = reader.parseTests();
        for (CaseTest t: tests)
            t.print();
    }

    @Test
    public void testGetHash() {
        fail("Not yet implemented");
    }

}
