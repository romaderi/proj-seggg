package pcs2055.app;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;

import pcs2055.hash.HashFunction;
import pcs2055.hash.Keccak;
import pcs2055.math.ByteUtil;
import pcs2055.sponge.SpongePRNG;
import pcs2055.sponge.SpongeRandom;

public class App2 {

    static private BufferedReader inFromUser;

    public static void main(String[] args) {

        final int b = 1600;
        int r = 0, c = 0, d = 0, hashlen = 0;
        inFromUser = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("PCS2054 - Seguranca da Informacao");
        System.out.println("Projeto - Fase 2\n");

        System.out.println("r,c,d: 0 para valores default ou 1 para configura-los: ");
        System.out.print("$ "); // prompt

        String command = new String();
        try {
            command = inFromUser.readLine();
        } catch (Exception e) {
        }

        if (command.equals(Integer.toString(0))) {
            System.out.println("Valores default: r = 1024, c = 576, d = 0.");
            r = 1024;
            c = 576;
            d = 0;
            hashlen = c / 2;
        } else if (command.equals(Integer.toString(1))) {
            String value;
            System.out.print("Entre com o valor de r: ");
            try {
                value = inFromUser.readLine();
                r = Integer.parseInt(value);
            } catch (Exception e) {
            }
            c = b - r;
            System.out.println("O valor de c': " + c);
            System.out.print("Entre com o valor de d: ");
            try {
                value = inFromUser.readLine();
                d = Integer.parseInt(value);
            } catch (Exception e) {
            }
            hashlen = c / 2;
        }
        
        r = r/8;
        c = c/8;
        d = d/8;

        System.out.print("Digite 0 para calculo o resumo Keccak[r,c,d] ou 1 para "
                + "calcular um numero de bytes pseudo-aleatorios: ");
        String option = new String();
        try {
            option = inFromUser.readLine();
        } catch (Exception e) {
        }

        System.out.print("Digite o nome do arquivo de entrada: ");
        String fileName = new String("");
        try {
            fileName = inFromUser.readLine();
        } catch (Exception e) {
        }
        
        byte[] fileData = readByteFile(fileName);
        
        byte[] result = new byte[hashlen];
        String outName = null;
        if (option.equals(Integer.toString(0))) {
            
            outName = fileName.replaceAll("\\.\\w{3}$", ".hash");
            System.out.println("Inicio do calculo do Keccak[r,c,d].");

            HashFunction k = new Keccak();
            k.init(hashlen);            
            k.update(fileData, fileData.length);
            result = k.getHash(null);
            ByteUtil.printArray(result, "\nhash: ");
            System.out.println();
        
        } else if (option.equals(Integer.toString(1))) {

            outName = fileName.replaceAll("\\.\\w{3}$", ".rand");
            System.out.println("Inicio do calculo dos bytes pseudo-aleatorios.");
            System.out.println("Número de bytes a ser gerado: ");
            int n = 0;
            try {
                n = Integer.parseInt(inFromUser.readLine());
            } catch (NumberFormatException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            SpongeRandom random = new SpongePRNG();
            random.init(0);
            for (int i = 0; (i + 100) < fileData.length; i += 100) {
                byte[] seed = Arrays.copyOfRange(fileData, i, i + 99);
                random.feed(seed, seed.length);
            }
            byte[] z = new byte[r];
            result = new byte[0];
            for (int i = 0; i < n / r; i++) {
                byte[] by = random.fetch(z, r);
                result = ByteUtil.append(result, by, result.length, r);
            }
            byte[] by = random.fetch(z, n % r);
            result = ByteUtil.append(result, by, result.length, n % r);
            
            System.out.print("Número gerado ("+result.length+")= ");
            ByteUtil.printArray(result);
        }

        // gera arquivo com o resultado
        try {
            File outFile = new File(outName);
            OutputStream out = new FileOutputStream(outFile);
            out = new BufferedOutputStream(out);
            out.write(result);
            out.close();
            System.out.println("Arquivo " + outName + " gerado.");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
