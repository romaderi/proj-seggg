package pcs2055.test.keccak;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pcs2055.math.ByteUtil;

public class TestReader {
    

    private enum SearchState {SIGMA0, XOR1, ROUNDS1, Z0, SIGMA1, XOR2, ROUNDS2, Z1};

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
        tests = new ArrayList<CaseTest>();
        CaseTest test = null;

        String line = br.readLine();
        while (line != null) {
            
            
            switch(state) { // máquina de estados
                
                case SIGMA0:
                    line = br.readLine();
                    if (line == null)
                        break; // acabou! =)
                    Pattern pt = Pattern.compile("sigma0 = 0x(\\p{XDigit}*)");
                    Matcher matcher = pt.matcher(line);
                    if (matcher.matches()) {
                        String s = matcher.group(1);
                        test = new CaseTest();
                        test.setSigma0(ByteUtil.convertHexString(s));
                        state = SearchState.XOR1;
                    }
                    break;
                                    
                case XOR1:
                    
                    // primeiro * é quantificador de espaço em branco
                    line = br.readLine();
                    pt = Pattern.compile("state after xor *= (0x)?(\\p{XDigit}*)");
                    matcher = pt.matcher(line);
                    if (matcher.matches()) {
                        String s = matcher.group(2);
                        test.setXor1(ByteUtil.convertHexString(s));
                        state = SearchState.ROUNDS1;
                        br.readLine(); // come linha em branco
                        br.readLine(); // come comentário /* Number of round:0 */
                    }
                    break;
                
                case ROUNDS1:
                    
                    Round round = this.parseRound();
                    test.getRounds1().add(round);
                    
                    // e agora, será que tem mais rounds?
                    pt = Pattern.compile("state after permutation = (\\p{XDigit}*)");
                    // line pode ser comentário "number of rounds" ou "state after permutation"
                    matcher = pt.matcher(br.readLine());
                    if (matcher.matches()) {
                        String s = matcher.group(1);
                        test.setPermutation1(ByteUtil.convertHexString(s));
                        state = SearchState.Z0;
                    }
                    break;
                    
                case Z0:
                    
                    line = br.readLine();
                    pt = Pattern.compile("z0(\\(10\\))? *= (\\p{XDigit}*)");
                    matcher = pt.matcher(line);
                    if (matcher.matches()) {
                        String s = matcher.group(2);
                        test.setZ0(ByteUtil.convertHexString(s));
                        state = SearchState.SIGMA1;
                    }                    
                    break;
                    
                case SIGMA1:
                    
                    line = br.readLine();
                    pt = Pattern.compile("sigma1 *= 0x(\\p{XDigit}*)");
                    matcher = pt.matcher(line);
                    if (matcher.matches()) {
                        String s = matcher.group(1);
                        test.setSigma1(ByteUtil.convertHexString(s));
                        state = SearchState.XOR2;
                    }                    
                    break;
                    
                case XOR2:
                    
                    line = br.readLine();
                    pt = Pattern.compile("state after xor *= (0x)?(\\p{XDigit}*)");
                    matcher = pt.matcher(line);
                    if (matcher.matches()) {
                        String s = matcher.group(2);
                        test.setXor2(ByteUtil.convertHexString(s));
                        state = SearchState.ROUNDS2;
                        br.readLine(); // come linha em branco
                        br.readLine(); // come comentário /* Number of round:0 */
                    }
                    break;
                    
                case ROUNDS2:
                    
                    round = this.parseRound();
                    test.getRounds2().add(round);
                    
                    // e agora, será que tem mais rounds?
                    pt = Pattern.compile("state after permutation = (\\p{XDigit}*)");
                    // line pode ser comentário "number of rounds" ou "state after permutation"
                    line = br.readLine();
                    matcher = pt.matcher(line);
                    if (matcher.matches()) {
                        String s = matcher.group(1);
                        test.setPermutation2(ByteUtil.convertHexString(s));
                        state = SearchState.Z1;
                    }
                    break;    
                    
                case Z1:
                    
                    line = br.readLine();
                    pt = Pattern.compile("z1(\\(10\\))? *= (\\p{XDigit}*)");
                    matcher = pt.matcher(line);
                    if (matcher.matches()) {
                        String s = matcher.group(2);
                        test.setZ1(ByteUtil.convertHexString(s));
                        state = SearchState.SIGMA0;
                        this.tests.add(test);
                    }                    
                    break;                    
            }            
        }

        return this.tests;
    }
        
    private Round parseRound() throws IOException {
        
        Round round = new Round();
        
        round.setTeta(this.extractRoundValue());
        round.setRho(this.extractRoundValue());
        round.setPi(this.extractRoundValue());
        round.setChi(this.extractRoundValue());
        round.setIota(this.extractRoundValue());
        
        return round;
    }
    
    private byte[] extractRoundValue() throws IOException {

        Pattern pt = Pattern.compile("state before \\w*:? *= (\\p{XDigit}*)");
        String line = br.readLine();
        Matcher matcher = pt.matcher(line);
        if (matcher.matches()) {
            String s = matcher.group(1);
            return ByteUtil.nistReverseStringToByteArray(s);
        }
        else return null;
    }
    
    /**
     * 
     * @return o array de testes gerado pelo método parseTests
     */
    public List<CaseTest> getParsedTests() {
        
        return this.tests;
    }

}
