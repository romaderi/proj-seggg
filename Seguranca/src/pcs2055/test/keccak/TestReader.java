package pcs2055.test.keccak;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pcs2055.math.ByteUtil;

import com.sun.xml.internal.ws.org.objectweb.asm.ByteVector;

public class TestReader {
    

    private enum SearchState {SIGMA0, XOR1, ROUNDS1, PERMUTATION1, Z0, 
        SIGMA1, XOR2, ROUNDS2, PERMUTATION2, Z1};
    private enum SearchSubState {THETHA, RHO, PI, CHI, IOTA};

    private List<CaseTest> tests;
    private BufferedReader br;

    public TestReader(String fileName) {
        
        // abre arquivo de testes
        File file = new File(fileName);
        Reader reader = null;
        try {
            reader = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.br = new BufferedReader(reader);   
    }
    
    /**
     * Analisa o arquivo de testes do keccak gerando o array de testes
     * @return o array de testes
     * @throws IOException 
     */
    public List<CaseTest> parseTests() throws IOException {

        // inicializando variáveis
        SearchState state = SearchState.SIGMA0;
        CaseTest test = new CaseTest();
        boolean out = false; // flag auxiliar (mais para os testes)

        String line = br.readLine();
        while (line != null && !out) {
            
            
            switch(state) { // máquina de estados
                
                case SIGMA0:
                    Pattern pt = Pattern.compile("sigma0 = 0x(\\p{XDigit}*)");
                    Matcher matcher = pt.matcher(line);
                    if (matcher.matches()) {
                        String s = matcher.group(1);
                        System.out.print(s);
                        test = new CaseTest();
                        test.setSigma0(ByteUtil.convertHexString(s));
                        state = SearchState.XOR1;
                    }
                    break;
                
                case XOR1:
                    
                    // primeiro * é quantificador de espaço em branco
                    pt = Pattern.compile("state after xor *= (0x)?(\\p{XDigit}*)");
                    matcher = pt.matcher(line);
                    if (matcher.matches()) {
                        String s = matcher.group(2);
                        System.out.print(s);
                        test.setXor1(ByteUtil.convertHexString(s));
                        state = SearchState.ROUNDS1;
                        out = true;
                    }
                    break;
            }
            
            line = br.readLine();
        }
        
        ByteUtil.printArray(test.getSigma0(), "sigma0= ");
        ByteUtil.printArray(test.afterXor1(), "after xor= ");
        
        return this.tests;
    }
    
    /**
     * 
     * @return o array de testes gerado pelo método parseTests
     */
    public List<CaseTest> getParsedTests() {
        
        return this.tests;
    }

}
