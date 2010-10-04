package pcs2055.curupira;

import java.util.Arrays;

import pcs2055.math.ByteUtil;

public class KeyScheduler {
    
    public enum Mode {ENCRYPTING, DECRYPTING};
    
    private byte[] key;
    private byte[] currentSubKey;
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
        this.currentSubKey = key;
        this.t = t / 48;
        this.mode = mode;
        
        if (mode == Mode.ENCRYPTING) {
            this.round = -1;
        }
        else {
            this.round = roundMax;
        }
    }
    
    public byte[] getSubKey (int round) {
    	
    	if (round == 0)
    		return fi(sigma(this.key, 0));
    	
    	byte[] block = new byte[48*this.t];
    	block = Arrays.copyOf(this.key, this.key.length);

        int tmp = this.round;
        this.round = 1;
        
    	block = sigma(this.key, this.round);
        block = csi(block);
        block = mi(block);
    	
        for (int i = 2; i <= round; i++){
        	block = sigma(block, i);
            block = csi(block);
            block = mi(block);
    	}
        
        this.currentSubKey = block;
        
    	this.round = tmp;
    	return fi(block);
    }
    
    public byte[] nextSubKey(int roundMax) {

    	int round;
        if (this.mode == Mode.ENCRYPTING) {
            this.round++;
            round = this.round;
        }
        else {
            this.round--;
            round = roundMax - this.round;
        }
               
        if (round == 0) {
        	this.currentSubKey = sigma(this.key, 0);
        	return fi(this.currentSubKey);
        }

        byte[] block = new byte[this.currentSubKey.length];
        block = sigma(this.currentSubKey, round);
        block = csi(block);
        this.currentSubKey = mi(block);
        return fi(this.currentSubKey);
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
    	
        if (s < 1)
        	return q;
        
        for (int j = 0; j < 2*this.t; j++)
        	q[0 + 3*j] = SBox.sbox16b((byte)(2*t*(s-1)+j));
        
        return q;
    }
    
    
    private byte[] sigma(byte[] b, int round) {
    	
    	byte[] q = scheduleConstant(round, this.t);   	
     	return ByteUtil.xor(b, q, b.length);
    }
    
    private byte[] csi(byte[] b) {
        
        byte[] result = Arrays.copyOf(b, b.length); 
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
		
		// volta pra forma de vetor
		for (i = 0; i < 3; i++)
			for (j = 0; j < 2*this.t; j++)
				result[i + 3*j] = key[i][j];
				
        return result;
    }
    
    // Algoritmo 3, explicado no artigo do Curupira
    private byte[] alg3 (byte[] a) {
    	
    	byte v = (byte)(a[0] ^ a[1] ^ a[2]);
    	v = ByteUtil.ctimes(v);
    	
    	byte[] b = new byte[3];
    	b[0] = (byte)(a[0] ^ v);
    	b[1] = (byte)(a[1] ^ v);
    	b[2] = (byte)(a[2] ^ v);

    	return b;
    }

    private byte[] mi(byte[] b) {
    	
    	byte[] b0 = new byte[3];
    	byte[] result = new byte[b.length];
    	
    	for (int j = 0; j < b.length/3; j++){
    		b0[0] = b[3*j]; 
    		b0[1] = b[3*j + 1];
    		b0[2] = b[3*j + 2];
    		b0 = alg3(b0);
    		result[3*j] = b0[0];
    		result[3*j + 1] = b0[1];
    		result[3*j + 2] = b0[2];
    	}
    	    	
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
			key[3*j] = SBox.sbox16b(K[3*j]);
    	
    	for (i = 1; i < 3; i++)
    		for (j = 0; j < 4; j++)
    			key[i + 3*j] = K[i + 3*j]; 

        return key;
    }
  
    // Algoritmo 2, explicado no artigo do Curupira
    private byte[] alg2 (byte[] a){
    	
    	byte v = ByteUtil.xtimes((byte)(a[0] ^ a[1] ^ a[2]));
    	byte w = ByteUtil.xtimes(v);
    	byte[] b = new byte[3];
    	b[0] = (byte)(a[0] ^ v);
    	b[1] = (byte)(a[1] ^ w);
    	b[2] = (byte)(a[2] ^ v ^ w);
    	return b;
    }
    
    public byte[] teta(byte[] a) {

    	byte[] b0 = new byte[3];
    	byte[] b = new byte[12];

    	for (int j = 0; j < 4; j++) {
    		b0[0] = a[3*j];
    		b0[1] = a[3*j + 1];
    		b0[2] = a[3*j + 2];
    		b0 = alg2(b0);
    		b[3*j] = b0[0];
    		b[3*j + 1] = b0[1];
    		b[3*j + 2] = b0[2];
    	}
    	
    	return b;
    }
    

}
