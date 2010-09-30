package pcs2055.app;

import java.io.BufferedReader;
import java.io.InputStreamReader;

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
				
				if (command.equals("1")) {
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
					opcao7();
				} else if (command.equals(Integer.toString(8))) {
					opcao8(key, sizeKey);
				} else if (command.equals(Integer.toString(9))) {
					opcao9();
				} else if (command.equals("H") || command.equals("h")) {
					opcaoHelp();
				} else { // comando Ã© mensagem
				//	sendMessage(command);
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
		String command = new String();
		
		while (admissivel == 0) {
			
			System.out.print("Digite o nome do arquivo para ser autenticado: ");
			try {command = inFromUser.readLine(); 
			} catch(Exception e){}
			
			if ( true ) { // se conseguiu ler o arquivo
				System.out.print("Nao foi possivel abrir o arquivo. " + command);
			} else {
				admissivel = 1;
			}
		
		}
	}
	
	private static void opcao5() {

		int admissivel = 0;
		String command = new String();
		
		while (admissivel == 0) {
			
			System.out.print("Digite o nome do arquivo com seu MAC para ser" +
					"validado: ");
			try {command = inFromUser.readLine(); 
			} catch(Exception e){}
			
			if ( true ) { // se conseguiu ler o arquivo
				System.out.print("Nao foi possivel abrir o arquivo. " + command);
			} else {
				admissivel = 1;
			}
		
		}
		
	}
	
	private static void opcao6(byte[] key, int sizeKey) {
		
		int admissivel = 0;
		byte[] mBlock = new byte[12];
		byte[] cBlock = new byte[12];
		String command = new String();
		
		while (admissivel == 0) {
			
			System.out.print("Digite o nome do arquivo para ser cifrado e " +
					"autenticado: ");
			try {command = inFromUser.readLine(); 
			} catch(Exception e){}
			
			if ( true ) { // se conseguiu ler o arquivo
				System.out.print("Nao foi possivel abrir o arquivo. " + command);
			} else {
				
				Curupira cur = new Curupira();
				cur.makeKey(key, sizeKey);
				
				//for () {
				//	mBlock = ;
				//	cur.encrypt(mBlock, cBlock);
				//}
				
				admissivel = 1;
			}
		
		}
	}
	
	private static void opcao7() {
		int admissivel = 0;
		String command = new String();
		
		while (admissivel == 0) {
			
			System.out.print("Digite o nome do arquivo cifrado (com IV E MAC) para" +
					"ser validado e decifrado: ");
			try {command = inFromUser.readLine(); 
			} catch(Exception e){}
			
			if ( true ) { // se conseguiu ler o arquivo
				System.out.print("Nao foi possivel abrir o arquivo. " + command);
			} else {
				admissivel = 1;
			}
		
		}
	}
	
	private static void opcao8(byte[] key, int sizeKey) {
		
		int admissivel = 0;
		byte[] mBlock = new byte[12];
		byte[] cBlock = new byte[12];
		String command = new String();
		
		while (admissivel == 0) {
			
			System.out.print("Digite o nome do arquivo para ser cifrado e" +
					"autenticado: ");
			try {command = inFromUser.readLine(); 
			} catch(Exception e){}
			
			if ( true ) { // se conseguiu ler o arquivo
				System.out.print("Nao foi possivel abrir o arquivo. " + command);
			} else {
				
				Curupira cur = new Curupira();
				cur.makeKey(key, sizeKey);
				
				//for () {
				//	mBlock = ;
				//	cur.encrypt(mBlock, cBlock);
				//}
				
				admissivel = 1;
			}
		
		}
		
		admissivel = 0;
		while (admissivel == 0) {
			
			System.out.print("Digite o nome do arquivo associado de dados para ser" +
					"autenticado: ");
			try {command = inFromUser.readLine(); 
			} catch(Exception e){}
			
			if ( true ) { // se conseguiu ler o arquivo
				System.out.print("Nao foi possivel abrir o arquivo. " + command);
			} else {
				admissivel = 1;
			}
		
		}
	}
	
	private static void opcao9() {
		
		int admissivel = 0;
		String command = new String();
		
		while (admissivel == 0) {
			
			System.out.print("Digite o nome do arquivo cifrado (com IV E MAC) para" +
					"ser validado e decifrado: ");
			try {command = inFromUser.readLine(); 
			} catch(Exception e){}
			
			if ( true ) { // se conseguiu ler o arquivo
				System.out.print("Nao foi possivel abrir o arquivo. " + command);
			} else {
				admissivel = 1;
			}
		
		}
		
		admissivel = 0;
		while (admissivel == 0) {
			
			System.out.print("Digite o nome do arquivo associado de dados para ser" +
					"autenticado: ");
			try {command = inFromUser.readLine(); 
			} catch(Exception e){}
			
			if ( true ) { // se conseguiu ler o arquivo
				System.out.print("Nao foi possivel abrir o arquivo. " + command);
			} else {
				admissivel = 1;
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
}
