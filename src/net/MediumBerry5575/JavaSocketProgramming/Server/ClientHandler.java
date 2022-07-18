package net.MediumBerry5575.JavaSocketProgramming.Server;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
	
	public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private String clientUsername;
	
	public ClientHandler(Socket socket) {
		try {
			this.socket = socket;
			this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.clientUsername = bufferedReader.readLine();
			clientHandlers.add(this);
			broadcastMessage("[SERVER] " + clientUsername + " has entered the chat!");
		} catch (IOException e) {
			System.err.println("IOException: An error has occurred.");
			e.printStackTrace();
			closeEverything(socket, bufferedReader, bufferedWriter, true);
			System.exit(-1);
		}
	}
	
	private void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter, boolean removeClientHandler) {
		if (removeClientHandler) {
			removeClientHandler();
		}
		
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

	@Override
	public void run() {
		String messageFromClient;
		
		while (socket.isConnected()) {
			try {
				messageFromClient = bufferedReader.readLine();
				broadcastMessage(messageFromClient);
			} catch (IOException e) {
				closeEverything(socket, bufferedReader, bufferedWriter, true);
				break;
			}
		}
	}

	private void broadcastMessage(String messageFromClient) {
		for (ClientHandler clientHandler : clientHandlers) {
			try {
				if (!clientHandler.clientUsername.equals(clientUsername)) {
					clientHandler.bufferedWriter.write(messageFromClient);
					clientHandler.bufferedWriter.newLine();
					clientHandler.bufferedWriter.flush();
				}
			} catch (IOException e) {
				closeEverything(socket, bufferedReader, bufferedWriter, true);
			}
		}
	}
	
	private void removeClientHandler() {
		clientHandlers.remove(this);
		broadcastMessage("[SERVER] " + clientUsername + " has left the chat!");
	}
	
}
