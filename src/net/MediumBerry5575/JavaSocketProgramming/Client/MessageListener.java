package net.MediumBerry5575.JavaSocketProgramming.Client;

import java.net.*;
import java.io.*;

public class MessageListener implements Runnable {
	
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	
	public MessageListener(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
		this.socket = socket;
		this.bufferedReader = bufferedReader;
		this.bufferedWriter = bufferedWriter;
	}
	
	@Override
	public void run() {
		String messageFromGroupChat;
		
		while (socket.isConnected()) {
			try {
				messageFromGroupChat = bufferedReader.readLine();
				System.out.println(messageFromGroupChat);
			} catch (IOException e) {
				System.err.println("IOException: An error has occurred.");
				closeEverything(socket, bufferedReader, bufferedWriter);
				e.printStackTrace();
				System.exit(-1);
			}
		}
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
	
}
