package net.MediumBerry5575.JavaSocketProgramming.Server;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Server {
	
	private ServerSocket serverSocket;
	
	private static int port = 0;
	
	public Server(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}
	
	public void startServer() {
		System.out.println("[SERVER] Server Started");
		try {
			System.out.println("[SERVER] Server Ready");
			while (!serverSocket.isClosed()) {
				Socket socket = serverSocket.accept();
				System.out.println("[SERVER] A new client has connected!");
				ClientHandler clientHandler = new ClientHandler(socket);
				
				Thread thread = new Thread(clientHandler);
				thread.start();
			}
		} catch (IOException e) {
			System.err.println("IOException: An error has occurred.");
			closeServerSocket();
			e.printStackTrace();
			System.exit(-1);
		}
	}

	private void closeServerSocket() {
		try {
			if (serverSocket != null) {
				serverSocket.close();
			}
		} catch (IOException e) {
			System.err.println("IOException: An error has occurred.");
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public static void main(String[] args) {
		try {
			System.out.println("[INFO] Socket Server");
			System.out.println("[INFO] Author: LeeHansHinLun");
			
			port = getPort();
			
			ServerSocket serverSocket = new ServerSocket(port);
			Server server = new Server(serverSocket);
			server.startServer();
		} catch (IOException e) {
			System.err.println("IOException: An error has occurred.");
			e.printStackTrace();
			System.exit(-1);
		}
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
	
	private static int getPort() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the socket server port: ");
		String _port = scanner.nextLine();
		if (!isValidPort(_port)) {
			getPort();
		}
		
		scanner.close();
		return Integer.parseInt(_port);
	}
	
}
