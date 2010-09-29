package pcs2055.curupira;

import java.math.BigInteger;

import pcs2055.math.ByteUtil;

public class KeyScheduler {
    
    public enum Mode {ENCRYPTING, DECRYPTING};
    
    private byte[] key;
    private byte[] currentKey;
    private int t; // 2, 3 ou 4 de acordo com tamanho da chave
    private int round;
    private Mode mode;

    /**
     * 
     * @param key
     * @param t tamanho da chave
     * @param mode criptografia ou descriptografia
     * @param roundMax quantidade de rounds
     */
    public KeyScheduler(byte[] key, int t, Mode mode, int roundMax) {
        
        this.key = key;
        this.currentKey = key;
        this.t = t / 48;
        this.mode = mode;
        
        if (mode == Mode.ENCRYPTING) {
            this.round = -1;
        }
        else {
            this.round = roundMax;
        }
    }
    
    public byte[] nextSubKey() {
        
        if (this.mode == Mode.ENCRYPTING)
            this.round++;
        else
            this.round--;
        /*
        byte[] o;
        if (round == 0)
        	o = this.key.clone();
        else {
        	o = omega(this.key);
        	for (int i=2; i<=round; i++)
        		o = omega(o); 
        }
        
        System.out.println("Round : " + this.round);
        System.out.print("KEY : ");
        ByteUtil.printArray(this.key);
        System.out.print("OMEGA : ");
        ByteUtil.printArray(o);
        
        byte[] accSchdCst = accumulatedScheduleConstant(this.round);
        
        System.out.print("ACC SCHED CST : ");
        ByteUtil.printArray(accSchdCst);
        
        
        byte[] keyStage = ByteUtil.add(o, accSchdCst, ((48/8)*this.t));
        
        System.out.print("KEYSTAGE ");
        ByteUtil.printArray(keyStage);
        return fi(keyStage);
        */
        
        byte[] block = new byte[48*this.t];
        block = sigma(block);
    	System.out.print(" KEY_SIGMA -> ");
    	ByteUtil.printArray(block);
        block = csi(block);
    	System.out.print(" KEY_CSI -> ");
    	ByteUtil.printArray(block);
        block = mi(block);
    	System.out.print(" KEY_MI -> ");
    	ByteUtil.printArray(block);
        this.currentKey = fi(block);
    	System.out.print(" KEY_FI -> ");
    	ByteUtil.printArray(this.currentKey);
        return fi(this.currentKey);
    }
    
    private byte[] omega(byte[] b) {
        return mi(csi(b));
    }
    
    /**
     * 
     * @param s round
     * @param t tamanho da chave (2,3 ou 4)
     * @return
     */
    private byte[] scheduleConstant(int s, int t) {
    	
    	byte[] q = new byte[3*(2*t)];
    	for (int i = 0; i < 3*(2*t); i++)
    		q[i] = 0;
    	
        if (this.round < 1)
        	return q;
        
        for (int j = 0; j < 2*this.t; j++)
        	q[0 + 3*j] = SBox.sbox16b((byte)(2*t*(s-1)+j));
        
        System.out.print("Schedule constant q : ");
        ByteUtil.printArray(q);
        
        return q;
    }
    
    /**
     * 
     * @param s round
     * @return
     */
    private byte[] accumulatedScheduleConstant(int s) {
        
        //byte[] result = new byte[];
        byte[] q;
        byte[] o = new byte[48/8*this.t];
        
        for (int i=0; i<=s; i++)    
            for (int j=1; j<=s-i+1; j++) {// s-i+1 ou s-i ???
            	q = scheduleConstant(j, this.t);
                o = ByteUtil.add(o, omega(q), 48/8*this.t); // soma de matrizes
            }
        
        return o;
    }
    
    private byte[] sigma(byte[] b) {
    	
    	byte[] result = b.clone();
    	result = scheduleConstant(this.round, this.t);
    	return result;
    }
    
    private byte[] csi(byte[] b) {
        
    	byte[] result = b.clone();
    	byte[][] key = new byte[3][2*this.t];
    	
    	// transforma em matriz
    	int i, j;
    	for (i = 0; i < 3; i++)
    		for (j = 0; j < 2*this.t; j++)
    			key[i][j] = b[i + 3*j];

    	//ByteUtil.printMatriz(key);
    	
    	// mantem a primeira linha
    	// desloca segunda linha pra esquerda
    	// desloca terceira linha pra direita
    	byte tmp = 0;
		for (j = 0; j < 2*this.t; j++) {
			if (j == 0) {
				tmp = key[1][0];
				key[1][j] = key[1][j+1];
			}
			else if (j == 2*this.t - 1)
				key[1][2*this.t-1] = tmp;
			else 
				key[1][j] = key[1][j+1];
		}
		for (j = 2*this.t-1; j >= 0; j--) {
			if (j == 2*this.t-1) {
				tmp = key[2][j];
				key[2][j] = key[2][j-1];
			}
			else if (j == 0)
				key[2][0] = tmp;
			else 
				key[2][j] = key[2][j-1];
		}    			
		
		//ByteUtil.printMatriz(key);
		
		// volta pra forma de vetor
		for (i = 0; i < 3; i++)
			for (j = 0; j < 2*this.t; j++)
				result[i + 3*j] = key[i][j];
				
        return result;
    }

    private static byte[] mi(byte[] b) {
        
    	int i, j;
    	byte c = 0x1C; // c(x) = x^4 + x^3 + x^2
    	BigInteger[][] mb = new BigInteger[3][2*2];
    	BigInteger[][] e = new BigInteger[3][3];
    	
    	// matriz E = I + C
    	for (i = 0; i < 3; i++)
    		for (j = 0; j < 3; j++) {
    			e[i][j] = BigInteger.valueOf(c);
    			if (i == j)
    				e[i][j] = e[i][j].add(BigInteger.valueOf(1));
    		}
    	
        for (i=0; i<3; i++) {
            for (j=0; j<3; j++)
                System.out.print(Integer.toHexString((short)(0x000000FF & e[i][j].byteValue())) + " ");
            System.out.println("");
        }
    	
    	// matriz de b
    	for (i = 0; i < 3; i++)
    		for (j = 0; j < 2*2; j++)
    			mb[i][j] = BigInteger.valueOf(b[i + 3*j]);  

        for (i=0; i<3; i++) {
            for (j=0; j<4; j++)
                System.out.print(Integer.toHexString((short)(0x000000FF & mb[i][j].byteValue())) + " ");
            System.out.println("");
        }
    	
    	// multiplica
        BigInteger[][] mc = new BigInteger[3][2*2];
        for (i = 0; i < 3; i++)
            for (j = 0; j < 2*2; j++)
            	mc[i][j] = BigInteger.valueOf(0);
        
        for (i = 0; i < 3; i++) 
            for (j = 0; j < 2*2; j++) 
                for (int k = 0; k < 3; k++)
                    mc[i][j] = mc[i][j].add(e[i][k].multiply(mb[k][j]));
        
        // volta pra forma de vetor
        byte[] result = new byte[3*2*2];
    	for (i = 0; i < 3; i++)
    		for (j = 0; j < 2*2; j++)
    			result[i + 3*j] = mc[i][j].byteValue();  
    	
        return result;
    }
    
    /**
     * key selection
     * @param K key stage
     * @return
     */
    private byte[] fi(byte[] K) {
        
        // cópia de K trocando a primeira linha com o resultado da primeira linha de sbox(K)
        // limita aqui a sub-chave a 96 bits
    	
    	byte[] key = new byte[12];
    	int i, j;
    	
    	for (j = 0; j < 4; j++)
			key[3*j] = SBox.sbox16b(key[3*j]);
    	
    	for (i = 1; i < 3; i++)
    		for (j = 0; j < 4; j++)
    			key[i + 3*j] = K[i + 3*j]; 

        return key;
    }
    

}
