package servers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SMTP {
	
	public static void main(String[] args) {
		SMTP smtp = new SMTP();
		while(true){
			smtp.start();
		}
	}

	private Socket socket;

	public void start() {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(2500);
			socket = serverSocket.accept();
			Utilities.sendData(socket, "220 ready\r\n");
			String receivedData = Utilities.receiveData(socket);
			if(receivedData.startsWith("EHLO"))
				Utilities.sendData(socket, "250-localhost\r\n"
						+ "250-SIZE 54525952\r\n"
						+ "250-AUTH LOGIN\r\n"
						+ "250-AUTH=PLAIN LOGIN\r\n"
						+ "250 8BITMIME\r\n");
			else 
				Utilities.sendData(socket, "502 5.5.2 Error: command not recognized\r\n");
//			receivedData = Utilities.receiveData(socket);
//			if(receivedData.startsWith("HELO"))
//				Utilities.sendData(socket, "250 OK\r\n");
//			else 
//				Utilities.sendData(socket, "502 5.5.2 Error: command not recognized\r\n");
			receivedData = Utilities.receiveData(socket);
			if(receivedData.startsWith("MAIL FROM:"))
				Utilities.sendData(socket, "250 OK\r\n");
			else
				Utilities.sendData(socket, "502 5.5.2 Error: command not recognized\r\n");
			receivedData = Utilities.receiveData(socket);
			if(receivedData.startsWith("RCPT TO:"))
				Utilities.sendData(socket, "250 OK\r\n");
			else
				Utilities.sendData(socket, "502 5.5.2 Error: command not recognized\r\n");
			receivedData = Utilities.receiveData(socket);
			while(!receivedData.equals("QUIT\r\n")) {
				if(receivedData.equals("DATA\r\n")) {
					Utilities.sendData(socket, "354 Start Main Input\r\n");
					receivedData = Utilities.receiveData(socket);
					verarbeiteNachricht(receivedData);
					Utilities.sendData(socket, "250 2.0.0 Ok: queued as E1BD78017C\r\n");
				} else if(receivedData.equals(".\r\n")) {
					Utilities.sendData(socket, "250 OK\r\n");
				}else
					Utilities.sendData(socket, "502 5.5.2 Error: command not recognized\r\n");
				receivedData = Utilities.receiveData(socket);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void verarbeiteNachricht(String receivedData) {
//		System.out.println("Die Nachricht war " + receivedData);
	}
	

}
