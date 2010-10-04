package pcs2055.app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import pcs2055.curupira.Curupira;
import pcs2055.math.ByteUtil;

public class App {

	static private BufferedReader inFromUser;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int sizeKey = 96;
		int sizeMac= 96;
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
				} else if (command.equals(Integer.toString(2))) {
					sizeMac = opcao2();
				} else if (command.equals(Integer.toString(3))) {
					key = new byte[sizeKey];
					key = opcao3(sizeKey);
				} else if (command.equals(Integer.toString(4))) {
					opcao4();
				} else if (command.equals(Integer.toString(5))) {
					opcao5();
				} else if (command.equals(Integer.toString(6))) {
					opcao6(key, sizeKey);
				} else if (command.equals(Integer.toString(7))) {
					opcao7(key, sizeKey);
				} else if (command.equals(Integer.toString(8))) {
					opcao8(key, sizeKey);
				} else if (command.equals(Integer.toString(9))) {
					opcao9();
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


	private static void opcao4() {
		
		int admissivel = 0;
		String filename = new String();
		
		while (admissivel == 0) {
			
			System.out.print("Digite o nome do arquivo para ser autenticado: ");
			try {filename = inFromUser.readLine(); 
			} catch(Exception e){}
			
			try {

				File file = new File(filename);
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line = br.readLine();
				String data = new String("");
				while (line != null) {
					data = data.concat(line);
					line = br.readLine();
				}
				br.close();
				byte[] filedata = data.getBytes();
				
				admissivel = 1;
			} catch (Exception e) {}
			
			if ( admissivel == 0 ) { // se conseguiu ler o arquivo
				System.out.println("Nao foi possivel abrir o arquivo : '" + filename + "'");
			}		
		}
	}
	
	private static void opcao5() {

		int admissivel = 0;
		String filename = new String();
		
		while (admissivel == 0) {
			
			System.out.print("Digite o nome do arquivo com seu MAC para ser" +
					"validado: ");
			try {filename = inFromUser.readLine(); 
			} catch(Exception e){}
			
			try {

				File file = new File(filename);
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line = br.readLine();
				String data = new String("");
				while (line != null) {
					data = data.concat(line);
					line = br.readLine();
				}
				br.close();
				byte[] filedata = data.getBytes();
				
				admissivel = 1;
			} catch (Exception e) {}
			
			if ( admissivel == 0 ) { // se conseguiu ler o arquivo
				System.out.println("Nao foi possivel abrir o arquivo : '" + filename + "'");
			}	
		
		}
		
	}
	
	private static void opcao6(byte[] key, int sizeKey) {
		
		int admissivel = 0;
		byte[] mBlock = new byte[12];
		byte[] cBlock = new byte[12];
		String filename = new String();
		
		while (admissivel == 0) {
			
			System.out.print("Digite o nome do arquivo para ser cifrado e " +
					"autenticado: ");
			try {filename = inFromUser.readLine(); 
			} catch(Exception e){}
			
			try {

				File file = new File(filename);
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line = br.readLine();
				String data = new String("");
				while (line != null) {
					data = data.concat(line);
					line = br.readLine();
				}
				br.close();
				byte[] filedata = data.getBytes();
				
				admissivel = 1;
				
				Curupira cur = new Curupira();
				cur.makeKey(key, sizeKey);
				
				System.out.println(filedata.length);
				
				String cdata = new String("");
				
				for (int i = 0; i < filedata.length/12; i++) {
					mBlock = Arrays.copyOfRange(filedata, 12*i, 12*i+11);
					ByteUtil.printArray(mBlock);
					cur.encrypt(mBlock, cBlock);
					cdata = cdata.concat(mBlock.toString());
					System.out.println("->>> " + cdata);
				}
				
			} catch (Exception e) {}
			
			if ( admissivel == 0 ) { // se conseguiu ler o arquivo
				System.out.println("Nao foi possivel abrir o arquivo : '" + filename + "'");
			}	
			
	
		}
	}
	
	private static void opcao7(byte[] key, int sizeKey) {
		
		int admissivel = 0;
		String filename = new String();
		byte[] cBlock = new byte[12];
		byte[] mBlock = new byte[12];
		
		while (admissivel == 0) {
			
			System.out.print("Digite o nome do arquivo cifrado (com IV E MAC) para" +
					"ser validado e decifrado: ");
			try {filename = inFromUser.readLine(); 
			} catch(Exception e){}
			
			try {

				File file = new File(filename);
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line = br.readLine();
				String data = new String("");
				while (line != null) {
					data = data.concat(line);
					line = br.readLine();
				}
				br.close();
				byte[] filedata = data.getBytes();
				
				Curupira cur = new Curupira();
				cur.makeKey(key, sizeKey);
				
				System.out.println(filedata.length);
				
				String mData = new String("");
				
				for (int i = 0; i < filedata.length/12; i++) {
					cBlock = Arrays.copyOfRange(filedata, 12*i, 12*i+11);
					ByteUtil.printArray(cBlock);
					cur.decrypt(cBlock, mBlock);
					mData = mData.concat(mBlock.toString());
					System.out.println("->>> " + mData);
				}
				
				
				admissivel = 1;
							
			} catch (Exception e) {}
			
			if ( admissivel == 0 ) { // se conseguiu ler o arquivo
				System.out.println("Nao foi possivel abrir o arquivo : '" + filename + "'");
			}	
		
		}
	}
	
	private static void opcao8(byte[] key, int sizeKey) {
		
		int admissivel = 0;
		byte[] mBlock = new byte[12];
		byte[] cBlock = new byte[12];
		String filename = new String();
		
		while (admissivel == 0) {
			
			System.out.print("Digite o nome do arquivo para ser cifrado e" +
					"autenticado: ");
			try {filename = inFromUser.readLine(); 
			} catch(Exception e){}
			
			try {
				
				File file = new File(filename);
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line = br.readLine();
				String data = new String("");
				while (line != null) {
					data = data.concat(line);
					line = br.readLine();
				}
				br.close();
				byte[] filedata = data.getBytes();

				Curupira cur = new Curupira();
				cur.makeKey(key, sizeKey);
				
				System.out.println(filedata.length);
				
				String cData = new String("");
				
				for (int i = 0; i < filedata.length/12; i++) {
					mBlock = Arrays.copyOfRange(filedata, 12*i, 12*i+11);
					ByteUtil.printArray(mBlock);
					cur.encrypt(mBlock, cBlock);
					cData = cData.concat(cBlock.toString());
					System.out.println("->>> " + cData);
				}
				
				admissivel = 1;
							
			} catch (Exception e) {}
			
			if ( admissivel == 0 ) { // se conseguiu ler o arquivo
				System.out.println("Nao foi possivel abrir o arquivo : '" + filename + "'");
			}	
		
		}
		
		admissivel = 0;
		while (admissivel == 0) {
			
			System.out.print("Digite o nome do arquivo associado de dados para ser" +
					"autenticado: ");
			try {filename = inFromUser.readLine(); 
			} catch(Exception e){}
			
			try {

				File file = new File(filename);
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line = br.readLine();
				String data = new String("");
				while (line != null) {
					data = data.concat(line);
					line = br.readLine();
				}
				br.close();
				byte[] filedata = data.getBytes();
				
				admissivel = 1;
							
			} catch (Exception e) {}
		
		}
	}
	
	private static void opcao9() {
		
		int admissivel = 0;
		String filename = new String();
		
		while (admissivel == 0) {
			
			System.out.print("Digite o nome do arquivo cifrado (com IV E MAC) para" +
					"ser validado e decifrado: ");
			try {filename = inFromUser.readLine(); 
			} catch(Exception e){}
			
			try {
				File file = new File(filename);
				BufferedReader br = new BufferedReader(new FileReader(file));
				StringBuilder sb = new StringBuilder();
				String line = br.readLine();
				while (line != null) {
					sb.append(line);
					line = br.readLine();
				}
				br.close();
				byte[] filedata = sb.toString().getBytes();
				
				System.out.println(filedata.length/12);
				ByteUtil.printArray(filedata);
				admissivel = 1;
							
			} catch (Exception e) {}
		
		}
		
		admissivel = 0;
		while (admissivel == 0) {
			
			System.out.print("Digite o nome do arquivo associado de dados para ser" +
					"autenticado: ");
			try {filename = inFromUser.readLine(); 
			} catch(Exception e){}
			
			try {
				File file = new File(filename);
				BufferedReader br = new BufferedReader(new FileReader(file));
				StringBuilder sb = new StringBuilder();
				String line = br.readLine();
				while (line != null) {
					sb.append(line);
					line = br.readLine();
				}
				br.close();
				byte[] filedata = sb.toString().getBytes();
				
				System.out.println(filedata.length/12);
				ByteUtil.printArray(filedata);
				admissivel = 1;
							
			} catch (Exception e) {}
		
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
	
	public static String readTextFile(File filename) {
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line + "\n");
				line = br.readLine();
			}
			br.close();
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(filename);
			System.err.println(e);
			return "";
		}
	}
	
}
