package pcs2055.app;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;

import pcs2055.hash.HashFunction;
import pcs2055.hash.Keccak;
import pcs2055.math.ByteUtil;

public class App2 {

	static private BufferedReader inFromUser;
	
    public static void main(String[] args) {
    	
    	final int b = 1600;
    	int r = 0, c = 0, d = 0, hashlen = 0;
    	inFromUser = new BufferedReader(new InputStreamReader(System.in));
    	
    	System.out.println("PCS2054 - Seguranca da Informacao");
		System.out.println("Projeto - Fase 2\n");
		
		System.out.println("[r,c,d]: 0 para valores default ou 1 para configura-los: ");
		System.out.print("$ "); // prompt
		
		String command = new String();
		try {command = inFromUser.readLine(); 
		} catch(Exception e){}
		
		if (command.equals(Integer.toString(0))){
			System.out.println("Valores default: r = 1024, c = 576, d = 0.");
			r = 1024;
			c = 576;
			d = 0;
			hashlen = c/2;
		} else if (command.equals(Integer.toString(1))){
			String value;
			System.out.print("Entre com o valor de r: ");
			try { 
				value = inFromUser.readLine();
				r = Integer.parseInt(value);
			} catch(Exception e){}
			c = b - r;
			System.out.println("O valor de c': " + c);
			System.out.print("Entre com o valor de d: ");
			try { 
				value = inFromUser.readLine();
				d = Integer.parseInt(value);
			} catch(Exception e){}
			hashlen = c/2;
		}
		
		System.out.print("Digite 0 para calculo o resumo Keccak[r,c,d] ou 1 para " +
				"calcular um numero de bytes pseudo-aleatorios: ");
		String option = new String();
		try {option = inFromUser.readLine(); 
		} catch(Exception e){}
			
		System.out.print("Digite o nome do arquivo para ser autenticado: ");
		String fileName = new String("");
		try {fileName = inFromUser.readLine(); 
		} catch(Exception e){}
		byte[] fileData = readByteFile (fileName);
		
		if ( option.equals(Integer.toString(0)) ){
			System.out.println("Inicio do calculo do Keccak[r,c,d].");
			HashFunction k = new Keccak();
			k.init(hashlen);
			
			byte[] tmp = new byte[r/8];
			for (int i = 0; i < fileData.length; i += r/8) {
				if ( i + r/8 < fileData.length ) {
				    tmp = Arrays.copyOfRange(fileData, i, i+r/8-1);
				    k.update(tmp, r/8);
				} else {
					tmp = Arrays.copyOfRange(fileData, i, fileData.length);
				    k.update(tmp, tmp.length);
				}
			}
			
			byte[] val = k.getHash(null);
				
		} else if ( option.equals(Integer.toString(1)) ){
			System.out.println("Inicio do calculo dos bytes pseudo-aleatorios.");
		}
		
    }

	public static byte[] readByteFile(String fileName) {

		byte[] dataIn = new byte[0];
		
		try {
			File file = new File(fileName);
			InputStream in = new FileInputStream(file);
			in = new BufferedInputStream(in);
			
			byte[] bufferIn = new byte[102400]; // leitura de 100kb em 100kb
			int inLength = in.read(bufferIn);
			while ( inLength > 0 ) {
				dataIn = ByteUtil.append(dataIn, bufferIn, dataIn.length, inLength);
				inLength = in.read(bufferIn);
			}
			
			in.close();
			return dataIn;
			
		} catch (Exception e) {
			return null;
		}
	}
	
	public static int writeByteFile (String fileName, byte[] data){
		
		try {
			File file = new File(fileName);
			OutputStream out = new FileOutputStream(file);
			out = new BufferedOutputStream(out);
		
			out.write(data);
			out.close();
			
			System.out.println("Arquivo '" + fileName + "' criado.");
		
			return 0;
		} catch (Exception e) {
			return 1;
		}
	}
    
}
