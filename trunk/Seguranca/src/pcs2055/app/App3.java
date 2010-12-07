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
import java.math.BigInteger;
import java.util.Random;

import pcs2055.aed.AED;
import pcs2055.aed.LetterSoup;
import pcs2055.asymmetric.CramerShoup;
import pcs2055.asymmetric.KeyEncapsulation;
import pcs2055.blockCipher.BlockCipher;
import pcs2055.blockCipher.Curupira;
import pcs2055.hash.HashFunction;
import pcs2055.hash.Keccak;
import pcs2055.mac.MAC;
import pcs2055.mac.Marvin;
import pcs2055.math.ByteUtil;
import pcs2055.signature.DigitalSignature;
import pcs2055.signature.Schnorr;
import pcs2055.sponge.SpongePRNG;
import pcs2055.sponge.SpongeRandom;

public class App3 {

    static private BufferedReader inFromUser;

	
    public static void main(String[] args) {

        inFromUser = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("PCS2054 - Seguranca da Informacao");
        System.out.println("Projeto - Fase 3\n");

        
        /*
         *  ITEM 1:
        */
        
        
        System.out.println("Digite uma senha (ASCII): ");
        System.out.print("$ "); 
        String password = new String();
        try {
            password = inFromUser.readLine();
        } catch (Exception e) {
        }
        
        // Chave Cramer-shoup
        HashFunction hash = new Keccak();
        SpongeRandom random = new SpongePRNG();
        KeyEncapsulation ke = new CramerShoup();
        ke.setup(Defaults.p, Defaults.q, Defaults.g1, Defaults.g2, hash, random);
        BigInteger[] pkCramerShoup = ke.makeKeyPair(password + "Cramer-Shoup");        
        writeByteFile("publickey.csc", pkCramerShoup[0].toByteArray());
        writeByteFile("publickey.csd", pkCramerShoup[1].toByteArray());
        writeByteFile("publickey.csh", pkCramerShoup[2].toByteArray());
        
        // Chave Schnorr
        hash = new Keccak();
        random = new SpongePRNG();
        DigitalSignature ds = new Schnorr();
        ds.setup(Defaults.p, Defaults.q, Defaults.g, hash, random);
        BigInteger pkSchnorr = ds.makeKeyPair(password + "Schnorr");
        writeByteFile("publickey.sy", pkSchnorr.toByteArray());


        
        
        /*
         *  ITEM 2
        */
        
        System.out.println("Digite o nome principal da chave publica do destinatario: ");
        System.out.print("$ "); 
        String publicKeyDest = new String();
        try {
        	publicKeyDest = inFromUser.readLine();
        } catch (Exception e) {
        }

        BigInteger[] pkDestCramerShoup = new BigInteger[3];
//        pkDestCramerShoup[0] = BigInteger.valueOf(readByteFile(publicKeyDest + ".csc"));
//        pkDestCramerShoup[1] = BigInteger.valueOf(readByteFile(publicKeyDest + ".csd"));
//        pkDestCramerShoup[2] = BigInteger.valueOf(readByteFile(publicKeyDest + ".csh"));
        
        // gera chave simetrica aleatoria
        random = new SpongePRNG();
        random.init(96);
        random.feed(new byte[128], 128);
        byte[] symetricKey = random.fetch(null, 0);
        
        ByteUtil.printArray(symetricKey);
        
        // cifra o arquivo
        System.out.println("Digite o nome de um arquivo para ser cifrado: ");
        System.out.print("$ ");
        String fileName = "";
        try {
        	fileName = inFromUser.readLine();
        } catch (Exception e) {
        }
        byte[] fileData = readByteFile(fileName);
        
		byte[] nounce = new byte[96/8];
		BlockCipher curupira = new Curupira();
        MAC marvin = new Marvin();
        AED ls = new LetterSoup();
     
        ls.setKey(symetricKey, 96);
        ls.setCipher(curupira);
        ls.setIV(nounce, nounce.length);
        ls.setMAC(marvin);
        
        byte[] tmp = new byte[12];
        byte[] cipherData = ls.encrypt(fileData, fileData.length, null);
        byte[] tag = ls.getTag(tmp, 96);
        
        // cifra a chave simetrica com a chave publica do destinatario
        // usando cramer shoup
        hash = new Keccak();
        random = new SpongePRNG();
        ke = new CramerShoup();
        ke.setup(Defaults.p, Defaults.q, Defaults.g1, Defaults.g2, hash, random);
        BigInteger[] quadruple = ke.encrypt(pkDestCramerShoup, symetricKey);
        writeByteFile("criptograma.csu1", quadruple[0].toByteArray());
        writeByteFile("criptograma.csu2", quadruple[1].toByteArray());
        writeByteFile("criptograma.cse", quadruple[2].toByteArray());
        writeByteFile("criptograma.csv", quadruple[3].toByteArray());

        
        /*
         *  ITEM 3
        */

        System.out.println("Digite uma senha do destinatario (ASCII): ");
        System.out.print("$ "); 
        password = new String();
        try {
            password = inFromUser.readLine();
        } catch (Exception e) {
        }
        
        // Chave Cramer-shoup
        hash = new Keccak();
        random = new SpongePRNG();
        ke = new CramerShoup();
        ke.setup(Defaults.p, Defaults.q, Defaults.g1, Defaults.g2, hash, random);
        ke.makeKeyPair(password + "Cramer-Shoup");        
        
        // decifrar o arquivo
        System.out.println("Digite o nome do arquivo a ser decifrado: ");
        System.out.print("$ "); 
        fileName = new String();
        try {
            fileName = inFromUser.readLine();
        } catch (Exception e) {
        }
        fileData = readByteFile(fileName);
//        ke.decrypt(password + "Cramer-Shoup", fileData);
        
        /*
         *  ITEM 4
        */
        
        hash = new Keccak();
        random = new SpongePRNG();
        ds = new Schnorr();
        ds.setup(Defaults.p, Defaults.q, Defaults.g, hash, random);
        ds.makeKeyPair(password + "Schnorr");
        ds.update(fileData, fileData.length);
        BigInteger[] schnorrSignature = ds.sign(password + "Schnorr");
        writeByteFile("SchnorrSignature.se",schnorrSignature[0].toByteArray());
        writeByteFile("SchnorrSignature.ss",schnorrSignature[1].toByteArray());
        
        
        /*
         *  ITEM 5
        */
 
        System.out.println("Digite o nome principal da chave publica do destinatario: ");
        System.out.print("$ "); 
        publicKeyDest = new String();
        try {
        	publicKeyDest = inFromUser.readLine();
        } catch (Exception e) {
        }
//        BigInteger pkDestSchnorr = BigInteger.valueOf(readByteFile(publicKeyDest + ".sy"));
        
        System.out.println("Digite o nome do arquivo acompanhado de assinatura: ");
        System.out.print("$ "); 
        fileName = new String();
        try {
        	fileName = inFromUser.readLine();
        } catch (Exception e) {
        }
        
        cipherData = readByteFile(fileName);
        ds.update(cipherData, cipherData.length);
        
        fileName = fileName.replaceAll("\\.\\w{3}$", ".se");
        BigInteger[] signatureFile = new BigInteger[2];
//        signatureFile[0] = readByteFile(fileName.replaceAll("\\.\\w{3}$", ".se"));
//        signatureFile[1] = readByteFile(fileName.replaceAll("\\.\\w{3}$", ".se"));

//        if ( ds.verify(pkDestSchnorr, signatureFile) )
//        	System.out.println("Item 5: arquivo " + fileName + " verificado com sucesso");
//        else 
//        	System.out.println("Item 5: erro na verificacao do arquivo" + fileName);
        
    }
    
    
    public static byte[] readByteFile(String fileName) {

        byte[] dataIn = new byte[0];

        try {
            File file = new File(fileName);
            InputStream in = new FileInputStream(file);
            in = new BufferedInputStream(in);

            byte[] bufferIn = new byte[102400]; // leitura de 100kb em 100kb
            int inLength = in.read(bufferIn);
            while (inLength > 0) {
                dataIn = ByteUtil.append(dataIn, bufferIn, dataIn.length, inLength);
                inLength = in.read(bufferIn);
            }

            in.close();
            return dataIn;

        } catch (Exception e) {
        	System.out.println("Error in reading file " + fileName);
            return null;
        }
    }

    public static int writeByteFile(String fileName, byte[] data) {

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
