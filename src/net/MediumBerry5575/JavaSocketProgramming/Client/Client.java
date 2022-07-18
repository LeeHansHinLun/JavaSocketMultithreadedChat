package net.MediumBerry5575.JavaSocketProgramming.Client;

import java.net.*;
import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client {
	
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private MessageListener messageListener;
    private static String address;
    private static int port;
    
    private static String exitCommand = ".exit";
	
	public Client(Socket socket, String username) {
		try {
			this.socket = socket;
			this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.messageListener = new MessageListener(socket, bufferedReader,bufferedWriter);
		} catch (IOException e) {
			System.err.println("IOException: An error has occurred.");
			closeEverything(socket, bufferedReader, bufferedWriter);
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public void sendMessage(String username) {
		Scanner _scanner;
		String _msgToSend;
		try {
			bufferedWriter.write(username);
			bufferedWriter.newLine();
			bufferedWriter.flush();
			_scanner = new Scanner(System.in);
			System.out.println("[INFO] Client Ready");
			
			while (socket.isConnected()) {
				//System.out.println("Enter your message:");
				_msgToSend = _scanner.nextLine();
				//System.out.println("Your message is: "+_msgToSend);
				
				if (_msgToSend != exitCommand) {
					bufferedWriter.write("[CHAT] " + username + " > " + _msgToSend);
					bufferedWriter.newLine();
					bufferedWriter.flush();
				}
				
			}
			_scanner.close();
		} catch (IOException e) {
			System.err.println("IOException: An error has occurred.");
			closeEverything(socket, bufferedReader, bufferedWriter);
			e.printStackTrace();
			System.exit(-1);
		} catch (NoSuchElementException ignore) {
			
		}
	}
	
	public void listenForMessage() {
		if (this.messageListener == null) {
			this.messageListener = new MessageListener(this.socket, this.bufferedReader, this.bufferedWriter);
		}
		Thread listener = new Thread(this.messageListener);
		listener.start();
	}	
	
	private void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
		
		try {
			if (bufferedReader != null) {
				bufferedReader.close();
			}
			if (bufferedWriter != null) {
				bufferedWriter.close();
			}
			if (socket != null) {
				socket.close();
			}
		} catch (IOException e) {
			System.err.println("IOException: An error has occurred.");
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public static void main(String[] args) {
		try {
			System.out.println("[INFO] Socket Client");
			System.out.println("[INFO] Author: LeeHansHinLun");
			
			Scanner scanner = new Scanner(System.in);
			address = getHost(scanner);
			port = getPort(scanner);
			
			System.out.println("[SYSTEM] Chat Username[Username]");
			String username = scanner.nextLine();
			Socket socket = new Socket(address, port);
			Client client = new Client(socket, username);
			System.out.println("[INFO] Client Started");
			client.listenForMessage();
			client.sendMessage(username);
		} catch (IOException e) {
			System.err.println("IOException: An error has occurred.");
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	private static int getPort(Scanner scanner) {
		System.out.println("Server Port[Port]");
		String _port = scanner.nextLine();
		if (!isValidPort(_port)) {
			getPort(scanner);
		}
		
		return Integer.parseInt(_port);
	}

	private static boolean isValidPort(String theNumber) {
		boolean result = false;
		int theNumberInt = 0;
		
		try {
			theNumberInt = Integer.parseInt(theNumber);
			result = true;
		} catch (NumberFormatException e) {
			result = false;
		}
		
		if (theNumberInt <= 0) {
			result = false;
		}
		
		return result;
	}
	
	private static String getHost(Scanner scanner) {
		System.out.println("Server Address[IP/Domain] ");
		String host = scanner.nextLine();
		return host;
	}
	
}
