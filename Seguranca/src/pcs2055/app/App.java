package pcs2055.app;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Random;

import pcs2055.curupira.Curupira;
import pcs2055.interfaces.AED;
import pcs2055.interfaces.BlockCipher;
import pcs2055.interfaces.MAC;
import pcs2055.ls.LetterSoup;
import pcs2055.marvin.Marvin;
import pcs2055.math.ByteUtil;

public class App {

	static private BufferedReader inFromUser;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int sizeKey = 96;
		int sizeMac = 96;
		byte[] key = new byte[12]; 
		
		inFromUser = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("PCS2054 - Seguranca da Informacao");
		System.out.println("Projeto - Fase 1\n");
		
		String command = new String();
		while (!command.equals(Integer.toString(0))) {

			System.out.println("Menu principal. Digite uma das opcoes (numero + enter) ou H/h para ajuda:");
			System.out.print("$"); // prompt
			try {command = inFromUser.readLine(); 
			} catch(Exception e){}
			
			if(command.length() > 0) {
				
				if (command.equals(Integer.toString(1))) {
					sizeKey = opcao1();
					key = new byte[sizeKey/8];
				} else if (command.equals(Integer.toString(2))) {
					sizeMac = opcao2();
				} else if (command.equals(Integer.toString(3))) {
					key = new byte[sizeKey/8];
					key = opcao3(sizeKey);
				} else if (command.equals(Integer.toString(4))) {
					opcao4(key, sizeKey, sizeMac);
				} else if (command.equals(Integer.toString(5))) {
					opcao5(key, sizeKey, sizeMac);
				} else if (command.equals(Integer.toString(6))) {
					opcao6(key, sizeKey, sizeMac);
				} else if (command.equals(Integer.toString(7))) {
					opcao7(key, sizeKey, sizeMac);
				} else if (command.equals(Integer.toString(8))) {
					opcao8(key, sizeKey, sizeMac);
				} else if (command.equals(Integer.toString(9))) {
					opcao9(key, sizeKey, sizeMac);
				} else if (command.equals("H") || command.equals("h")) {
					opcaoHelp();
				}
			} else {
				
				//sendMessage(command);				
			}

		}
		
		System.out.println("Fim do programa");
	}
	
	private static int opcao1() {
		
		int admissivel = 0;
		int t = 0;
		String command = new String();
		
		while (admissivel == 0) {
			
			System.out.print("Selecione o tamanho da chave (96, 144, 192): ");	
			try {command = inFromUser.readLine(); 
			} catch(Exception e){}
			
			if (!command.equals(Integer.toString(96)) && 
					!command.equals(Integer.toString(144)) &&
					!command.equals(Integer.toString(192))) {
				System.out.print("Tamanho de chave nao admissivel. ");
			} else {
				t = Integer.parseInt(command);
				System.out.println("Tamanho da chave selecionado: " + t + "\n");
				admissivel = 1;
			}
		}
		return t;
	}

	private static int opcao2() {
		
		int admissivel = 0;
		int t = 0;
		String command = new String();
		
		while (admissivel == 0) {
		
			System.out.print("Selecione o tamanho de IV e MAC (entre 64 e 96): ");
			try {command = inFromUser.readLine(); 
			} catch(Exception e){}
			
			if ( (Integer.parseInt(command) < 64) || 
				 (Integer.parseInt(command) > 96) ) {
				System.out.print("Tamanho nao admissivel. " );
			} else {
				t = Integer.parseInt(command);
				System.out.println("Tamanho de IV e MAC selecionado: " + t + "\n");
				admissivel = 1;
			}

		}
		return t;
	}


	private static byte[] opcao3(int sizeKey) {
		
		int admissivel = 0;
		byte[] password = new byte[sizeKey];
		
		String command = new String();
		
		System.out.print("Digite sua senha de ate " + sizeKey/8 + " caracteres: ");
		while (admissivel == 0) {
			
			try {command = inFromUser.readLine(); 
			} catch(Exception e){}
			
			if ( command.length() > sizeKey/8 ) {
				System.out.print("Tamanho de senha invalida. " +
						"Digite sua senha de ate " + sizeKey/8 + " caracteres: ");
			} else if ( command.length() < sizeKey/8 ) {
				password = ByteUtil.rpad(command.getBytes(), sizeKey);
				admissivel = 1;
			} else {
				password = command.getBytes();
				admissivel = 1;
			}
		
		}
		System.out.println("");
		return password;
	}


	private static void opcao4(byte[] key, int sizeKey, int sizeMac) {
		
		int admissivel = 0;
		String fileName = new String();
		
		while (admissivel == 0) {
			
			System.out.print("Digite o nome do arquivo para ser autenticado: ");
			try {fileName = inFromUser.readLine(); 
			} catch(Exception e){}
			
			byte[] fileData = readByteFile (fileName);
			if ( fileData != null ) {
				
				BlockCipher curupira = new Curupira();
		        MAC marvin = new Marvin();
		        marvin.setKey(key, sizeKey);
		        marvin.setCipher(curupira);
		        marvin.init();
		        
		        byte[] chunk;
		        for (int i = 0; i < fileData.length; i = i + 12) {
		        	if (i + 12 < fileData.length) {
		        		chunk = new byte[12];
		        		chunk = Arrays.copyOfRange(fileData, i, i+12);
		        	} else {
		        		chunk = new byte[fileData.length-i];
		        		chunk = Arrays.copyOfRange(fileData, i, fileData.length-1);
		        	}
		        	marvin.update(chunk, chunk.length);
		        }
		        
		        byte[] tagFake = new byte[12];
		        byte[] tag = marvin.getTag(tagFake, 96);
		        
				writeByteFile(fileName.concat(".mac"), tag);
		        
				admissivel = 1;
			} else { // se conseguiu ler o arquivo
				System.out.println("Nao foi possivel abrir o arquivo : '" + fileName + "'");
			}		
		}
	}
	
	private static void opcao5(byte[] key, int sizeKey, int sizeMac) {

		int admissivel = 0;
		String fileName = new String();
		
		while (admissivel == 0) {
			
			System.out.print("Digite o nome do arquivo (MAC é obtido automaticament) " +
					"para ser validado: ");
			try {fileName = inFromUser.readLine(); 
			} catch(Exception e){}
			
			byte[] fileData = readByteFile (fileName);
			if ( fileData != null ) {
				
				byte[] mac = readByteFile (fileName.concat(".mac"));
							
				BlockCipher curupira = new Curupira();
		        MAC marvin = new Marvin();
		        marvin.setKey(key, sizeKey);
		        marvin.setCipher(curupira);
		        marvin.init();
		        
		        byte[] chunk;
		        for (int i = 0; i < fileData.length; i = i + 12) {
		        	if (i + 12 < fileData.length) {
		        		chunk = new byte[12];
		        		chunk = Arrays.copyOfRange(fileData, i, i+12);
		        	} else {
		        		chunk = new byte[fileData.length-i];
		        		chunk = Arrays.copyOfRange(fileData, i, fileData.length-1);
		        	}
		        	marvin.update(chunk, chunk.length);
		        }
		        
		        byte[] tagFake = new byte[12];
		        byte[] tag = marvin.getTag(tagFake, 96);

		        if ( ByteUtil.compareArray(tag, mac) == 1) {
		        	System.out.println("Arquivo '" + fileName + "' validado.");
		        } else {
		        	System.out.println("ERRO : autenticacao do arquivo '" + fileName +
		        			"' invalida.");
		        }
		        
				admissivel = 1;
			} else { // se conseguiu ler o arquivo
				System.out.println("Nao foi possivel abrir o arquivo : '" + fileName + "'");
			}	
		
		}
		
	}
	
	private static void opcao6(byte[] key, int sizeKey, int sizeMac) {
		
		int admissivel = 0;
		String fileName = new String();
		
		while (admissivel == 0) {
			
			System.out.print("Digite o nome do arquivo para ser cifrado e " +
					"autenticado: ");
			try {fileName = inFromUser.readLine(); 
			} catch(Exception e){}
			
			byte[] fileData = readByteFile (fileName);
			
			if ( fileData != null ) {
				
				Random gen = new Random();
				byte[] nounce = new byte[12];
				for (int i = 0; i < 12; i++)
					nounce[i] = (byte) gen.nextInt(i ^ sizeKey);
				
				BlockCipher curupira = new Curupira();
		        MAC marvin = new Marvin();
		        AED ls = new LetterSoup();
		        
		        ls.setKey(key, sizeKey);
		        ls.setCipher(curupira);
		        ls.setIV(nounce, sizeMac);
		        ls.setMAC(marvin);
		        
		        byte[] tmp = new byte[12];
		        byte[] cData = ls.encrypt(fileData, fileData.length, null);
		        byte[] tag = ls.getTag(tmp, sizeMac);
		        
				writeByteFile(fileName.concat(".cypher"), cData);
				writeByteFile(fileName.concat(".mac"), tag);
				writeByteFile(fileName.concat(".iv"), nounce);

		        System.out.print("TAG : ");
		        ByteUtil.printArray(tag);
				
				admissivel = 1;
			} else {
				System.out.println("Nao foi possivel abrir o arquivo : '" + fileName + "'");
			}	
		}
	}
	
	private static void opcao7(byte[] key, int sizeKey, int sizeMac) {
		
		int admissivel = 0;
		String fileName = new String();
		
		while (admissivel == 0) {
			
			System.out.print("Digite o nome do arquivo cifrado (sem a extensao '.cypher') para " +
					"ser validado e decifrado: ");
			try {fileName = inFromUser.readLine(); 
			} catch(Exception e){}
			
			byte[] fileData = readByteFile (fileName.concat(".cypher"));
			
			if ( fileData != null ) {
			
				byte[] mac = readByteFile (fileName.concat(".mac"));
				byte[] nounce = readByteFile (fileName.concat(".iv"));

				BlockCipher curupira = new Curupira();
		        MAC marvin = new Marvin();
		        AED ls = new LetterSoup();
		        
		        ls.setKey(key, sizeKey);
		        ls.setCipher(curupira);
		        ls.setIV(nounce, 12);
		        ls.setMAC(marvin);
		        
		        byte[] tmp = new byte[12];
		        byte[] cData = ls.encrypt(fileData, fileData.length, tmp);
		        byte[] tag = ls.getTag(tmp, 12*8);
		        
		        System.out.print("TAG : ");
		        ByteUtil.printArray(tag);

		        if ( ByteUtil.compareArray(tag, mac) == 1) {
		        	System.out.println("Arquivo '" + fileName + "' validado.");
		        	byte[] mData = ls.decrypt(fileData, fileData.length, tmp);	
		            writeByteFile(fileName, mData);
		        	
		        } else {
		        	System.out.println("ERRO : autenticacao do arquivo '" + fileName +
		        			"' invalida.");
		        }
				        			
				admissivel = 1;
			} else { // se conseguiu ler o arquivo
				System.out.println("Nao foi possivel abrir o arquivo : '" + fileName + "'");
			}	
		
		}
	}
	
	private static void opcao8(byte[] key, int sizeKey, int sizeMac) {
		
		int admissivel = 0;
		String fileName = new String();
		
		while (admissivel == 0) {
			
			System.out.print("Digite o nome do arquivo para ser cifrado e " +
					"autenticado: ");
			try {fileName = inFromUser.readLine(); 
			} catch(Exception e){}
			
			byte[] fileData = readByteFile (fileName);

			if ( fileData != null ) {
				
				Random gen = new Random();
				byte[] nounce = new byte[12];
				for (int i = 0; i < 12; i++)
					nounce[i] = (byte) gen.nextInt(i ^ sizeKey);
				
				BlockCipher curupira = new Curupira();
		        MAC marvin = new Marvin();
		        AED ls = new LetterSoup();
		        
		        ls.setKey(key, sizeKey);
		        ls.setCipher(curupira);
		        ls.setIV(nounce, sizeMac);
		        ls.setMAC(marvin);
		        
		        byte[] tmp = new byte[12];
		        byte[] cData = ls.decrypt(fileData, fileData.length, tmp);
		        byte[] tag = ls.getTag(tmp, sizeMac);
		        
				writeByteFile(fileName.concat(".cypher"), cData);
				writeByteFile(fileName.concat(".mac"), tag);
				writeByteFile(fileName.concat(".iv"), nounce);
				
				admissivel = 1;
			} else { // se conseguiu ler o arquivo
				System.out.println("Nao foi possivel abrir o arquivo : '" + fileName + "'");
			}	
		
		}
		
		admissivel = 0;
		while (admissivel == 0) {
			
			System.out.print("Digite o nome do arquivo associado de dados para ser " +
					"autenticado: ");
			try {fileName = inFromUser.readLine(); 
			} catch(Exception e){}
			
			byte[] fileData = readByteFile (fileName);

			if ( fileData != null ) {
				
				// inicio da autenticacao (arquivo associado)
				System.out.println("Inicio da autenticacao (arquivo associado).");
				
				admissivel = 1;
			} else { // se conseguiu ler o arquivo
				System.out.println("Nao foi possivel abrir o arquivo : '" + fileName + "'");
			}
		
		}
	}
	
	private static void opcao9(byte[] key, int sizeKey, int sizeMac) {
		
		int admissivel = 0;
		String fileName = new String();
		
		while (admissivel == 0) {
			
			System.out.print("Digite o nome do arquivo cifrado (sem a extensao '.cypher') para " +
					"ser validado e decifrado: ");
			try {fileName = inFromUser.readLine(); 
			} catch(Exception e){}
			
			byte[] fileData = readByteFile (fileName);
			
			if ( fileData != null ) {
				
				byte[] mac = readByteFile (fileName.concat(".mac"));
				byte[] nounce = readByteFile (fileName.concat(".iv"));

				BlockCipher curupira = new Curupira();
				MAC marvin = new Marvin();
				AED ls = new LetterSoup();
				
				ls.setKey(key, sizeKey);
				ls.setCipher(curupira);
				ls.setIV(nounce, nounce.length);
				ls.setMAC(marvin);
				
		        byte[] tmp = new byte[12];
		        byte[] tag = ls.getTag(mac, sizeMac);
				
		        if ( ByteUtil.compareArray(tag, mac) == 1) {
		        	System.out.println("Arquivo '" + fileName + "' validado.");
			        byte[] mData = ls.decrypt(fileData, fileData.length, tmp);
			        writeByteFile(fileName, mData);
		        } else {
		        	System.out.println("ERRO : autenticacao do arquivo '" + fileName +
		        			"' invalida.");
		        }
				
				admissivel = 1;
			} else {
				System.out.println("Nao foi possivel abrir o arquivo : '" + fileName + "'");
			}
		}
		
		admissivel = 0;
		while (admissivel == 0) {
			
			System.out.print("Digite o nome do arquivo associado de dados para ser" +
					" autenticado: ");
			try {fileName = inFromUser.readLine(); 
			} catch(Exception e){}
			
			byte[] fileData = readByteFile (fileName); 
			if ( fileData != null ) {
				
				// inicio da autenticacao (arquivo associado)
				System.out.println("Inicio da autenticacao (arquivo associado).");
				
				admissivel = 1;
			} else {
				System.out.println("Nao foi possivel abrir o arquivo : '" + fileName + "'");
			}
		
		}
		
	}
	
	private static void opcaoHelp(){
		System.out.println("1. Selecionar um tamanho de chave dentre os valores " +
		"admissíveis (96, 144, 192)");
		System.out.println("2. Selecionar um tamanho de IV e de MAC entre o mínimo " +
				"de 64 bits e o tamanho completo do bloco");
		System.out.println("3. Escolher uma senha alfanumérica (ASCII) de ate 12, " +
				"18 ou 24 caracteres, conforme o tamanho da chave");
		System.out.println("4. Selecionar um arquivo para ser apenas autenticado");
		System.out.println("5. Selecionar um arquivo com seu respectivo MAC para " +
				"ser validado");
		System.out.println("6. Selecionar um arquivo para ser cifrado e autenticado");
		System.out.println("7. Selecionar um arquivo cifrado com seus respectivos " +
				"IV e MAC para ser validado e decifrado");
		System.out.println("8. Selecionar um arquivo para ser cifrado e autenticado, e " +
				"um arquivo correspondente de dados associados para ser autenticado.");
		System.out.println("9. Selecionar um arquivo cifrado com seus respectivos IV " +
				"e MAC para ser validado e decifrado, um arquivo correspondente de " +
				"dados associados para ser autenticado");
		System.out.println("0. Sair");
	}
	
	public static byte[] readByteFile(String fileName) {

		byte[] tmp = new byte[0];
		byte[] dataIn = new byte[0];
		
		try {
			File file = new File(fileName);
			InputStream in = new FileInputStream(file);
			in = new BufferedInputStream(in);
			
			int byteIn = in.read();
			while (byteIn != -1) {
				tmp = Arrays.copyOf(dataIn, dataIn.length);
				dataIn = new byte[tmp.length + 1];
				int j;
				for (j = 0; j < tmp.length; j++)
					dataIn[j] = tmp[j];
				dataIn[j] = ByteUtil.convertIntToHexa(byteIn);
				byteIn = in.read();
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
