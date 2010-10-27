package pcs2055.test.keccak;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
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

    private enum SearchState {SIGMA0, XOR1, ROUNDS1, PERMUTATION1, Z0, 
                                SIGMA1, XOR2, ROUNDS2, PERMUTATION2, Z1};
    private enum SearchSubState {THETHA, RHO, PI, CHI, IOTA};
    
    @Before
    public void setUp() throws Exception {
        
        // abre arquivo de testes
        File file = new File(TEST_FILE1);
        Reader reader = new FileReader(file);
        BufferedReader br = new BufferedReader(reader);
        
        
    }

    @Test
    public void testGetHash() {
        fail("Not yet implemented");
    }

}
