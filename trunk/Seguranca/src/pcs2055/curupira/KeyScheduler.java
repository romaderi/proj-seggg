package pcs2055.curupira;

import pcs2055.math.ByteUtil;

public class KeyScheduler {
    
    public enum Mode {ENCRYPTING, DECRYPTING};
    
    private byte[] key;
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
        
        byte[] o = omega(this.key);
        for (int i=2; i<=round; i++)
            o = omega(o); 
        
        byte[] keyStage = ByteUtil.add(o, accumulatedScheduleConstant(this.round), (48*this.t));
        
        return fi(keyStage);
    }
    
    
    private byte[] omega(byte[] b) {
        
        return mi(csi(b));
    }
    
    /**
     * 
     * @param s round
     * @param t tamanho da chave
     * @return
     */
    private byte[] scheduleConstant(int s, int t) {
    	
    	byte[] q = new byte[t];
    	for (int i = 0; i < t; i++)
    		q[i] = 0;
    	
        if (this.round < 1)
        	return q;
        
        for (int j = 0; j < 2*this.t; j++)
        	q[3*j] = SBox.sbox16b((byte)(2*t*(s-1)+j));
        
        return null;
    }
    
    /**
     * 
     * @param s round
     * @return
     */
    private byte[] accumulatedScheduleConstant(int s) {
        
        //byte[] result = new byte[];
        byte[] q = scheduleConstant(s, this.t);
        byte[] o = new byte[48*this.t];
        
        for (int i=0; i<=s; i++)    
            for (int j=1; j<=s-i+1; j++) // s-i+1 ou s-i ???
                o = ByteUtil.add(o, omega(q), 48*this.t); // soma de matrizes
        
        return o;
    }
    
    private byte[] csi(byte[] b) {
        
    	byte[] result = b.clone();
    	byte[][] key = new byte[3][2*this.t];
    	
    	// transforma em matriz
    	int i, j;
    	for (i = 0; i < 3; i++)
    		for (j = 0; j < 2*this.t; j++)
    			key[i][j] = b[i + 3*j];
    	
    	// mantem a primeira linha
    	// desloca segunda linha pra esquerda
    	// desloca terceira linha pra direita
    	byte tmp = 0;
		for (j = 0; j < 2*this.t; j++) {
			if (j == 0)
				tmp = key[1][0];
			else if (j == 2*this.t - 1)
				key[1][2*this.t-1] = tmp;
			else 
				key[1][j] = key[1][j+1];
		}
		for (j = 2*this.t-1; j >= 0; j--) {
			if (j == 2*this.t-1)
				tmp = key[2][2*this.t-1];
			else if (j == 0)
				key[2][0] = tmp;
			else 
				key[2][j] = key[2][j-1];
		}    			
    	
		// volta pra forma de vetor
		for (i = 0; i < 3; i++)
			for (j = 0; j < 2*this.t; j++)
				result[i + 3*j] = key[i][j];
				
        return result;
    }

    private byte[] mi(byte[] b) {
        
    	int i, j;
    	byte c = 0;
    	byte[][] mb = new byte[3][2*this.t];
    	byte[][] e = new byte[3][3];
    	
    	// matriz E = I + C
    	for (i = 0; i < 3; i++)
    		for (j = 0; j < 3; j++) {
    			e[i][j] = c;
    			if (i == j)
    				e[i][j] += (byte)1;
    		}
    	
    	// matriz de b
    	for (i = 0; i < 3; i++)
    		for (j = 0; j < 2*this.t; j++)
    			mb[i][j] = b[i + 3*j];  
    	
    	// multiplica
        byte[][] mc = new byte[3][2*this.t];
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 3; j++) {
                for (int k = 0; k < 2*this.t; k++)
                    mc[i][j] += (byte) (e[i][k] * mb[k][j]);
            }
        }

        // volta pra forma de vetor
        byte[] result = new byte[3*2*this.t];
        for (i = 0; i < 3*2*this.t; i++)
            result[i] = mc[i/3][i%3];
    	
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
