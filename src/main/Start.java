package main;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

import algorithms.AlgorithmOFB;

public class Start {

	public static void main(String[] args) throws Exception {

		System.out.println("Szyfrowanie DES + OFB");

		Properties file = new Properties();
		InputStream in = null;
		Scanner keyScanner = null;
		Scanner initScanner = null;
		Scanner inputScanner = null;
		FileWriter out = null;

		try {
			// Load properties
			in = new FileInputStream("config.properties");
			file.load(in);

//			String mode = file.getProperty("mode");

			String init_type = file.getProperty("init_type");
			String key_type = file.getProperty("key_type");
			String input_type = file.getProperty("input_type");
			String output_type = file.getProperty("output_type");

			String init_path = file.getProperty("init_path");
			String key_path = file.getProperty("key_path");
			String input_path = file.getProperty("input_path");
			String output_path = file.getProperty("output_path");

			// Load key
			keyScanner = new Scanner(new File(key_path));
			String keyString = keyScanner.useDelimiter("\\Z").next();
			byte[] key;
			if ("txt".equals(key_type)) {
				key = keyString.getBytes();
			} else if ("hex".equals(key_type)) {
				key = hexStringToByteArray(keyString);
			} else {
				throw new Exception(key_type + " is not a valid parameter.");
			}
			System.out.println("Key:");
			System.out.println(byteToHEX(key));
			System.out.println();
			
			// Load initial shift register
			initScanner = new Scanner(new File(init_path));
			String initString = initScanner.useDelimiter("\\Z").next();
			byte[] init;
			if ("txt".equals(init_type)) {
				init = initString.getBytes();
			} else if ("hex".equals(init_type)) {
				init = hexStringToByteArray(initString);
			} else {
				throw new Exception(init_type + " is not a valid parameter.");
			}
			System.out.println("Initial shift register:");
			System.out.println(byteToHEX(init));
			System.out.println();
			
			// Load message
			inputScanner = new Scanner(new File(input_path));
			String inputString = inputScanner.useDelimiter("\\Z").next();
			byte[] message;
			if ("txt".equals(input_type)) {
				message = inputString.getBytes();
			} else if ("hex".equals(input_type)) {
				message = hexStringToByteArray(inputString);
			} else {
				throw new Exception(input_type + " is not a valid parameter.");
			}
			System.out.println("Message:");
			System.out.println(byteToHEX(message));
			System.out.println();

			// Perform algorithm
			AlgorithmOFB cipher = new AlgorithmOFB(copyOfRange(init, 0, 8));
			byte[] output = cipher.encrypt(copyOfRange(key, 0, 8), message);

			System.out.println("Output:");
			System.out.println(byteToHEX(output));
			System.out.println();
			
			// Save output
			File outputFile = new File(output_path);
			if (outputFile.exists()) {
				outputFile.delete();
			}
			outputFile.createNewFile();
			out = new FileWriter(outputFile);
			String outputString;
			if ("txt".equals(output_type)) {
				outputString = new String(output);
			} else if ("hex".equals(output_type)) {
				outputString = byteToHEX(output);
			} else {
				throw new Exception(output_type + " is not a valid parameter.");
			}
			out.write(outputString);

		} catch (FileNotFoundException e) {
			System.err.println("Nie znaleziono pliku wejœciowego");
		} catch (IOException e) {
			System.err.println("B³¹d wejœcia / wyjœcia");
		} catch (Exception e) {
			System.err.println(e.getMessage());
		} finally {
			// Close stream
			close(initScanner);
			close(keyScanner);
			close(inputScanner);
			close(in);
			close(out);
		}

	}

	private static String byteToHEX(byte[] in) {
		StringBuilder sb = new StringBuilder();
		for (byte b : in) {
			sb.append(byteToHEX(b));
		}
		return sb.toString();
	}

	private static String byteToHEX(byte in) {
		return String.format("%02X ", in);
	}

	private static void close(Closeable stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static byte[] hexStringToByteArray(String in) {
		String hexString = in.replaceAll("\\s+", "");
		int len = hexString.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character
					.digit(hexString.charAt(i + 1), 16));
		}
		return data;
	}
	
	private static byte[] copyOfRange(byte[] in, int pos, int len) {
		byte[] out = new byte[len];
		for (int i = 0; i < len; i++) {
			out[i] = in[i+pos];
		}
		return out;
	}

}
