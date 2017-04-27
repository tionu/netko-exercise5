package servers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class POP3 {

	public static void main(String[] args) {
		POP3 server = new POP3(1100);
		server.start();
	}

	private int port;
	private SimpleDateFormat dateForUid;

	public POP3(int port) {
		this.port = port;
		this.dateForUid = new SimpleDateFormat("yyMMddhhmmss");
	}

	public void start() {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);
			Socket socket = serverSocket.accept();
			Utilities.sendData(socket, "+OK localhost\r\n");
			String receivedData = Utilities.receiveData(socket);
			if (receivedData.equals("AUTH\r\n")) {
				Utilities.sendData(socket, "-ERR\r\n");
				receivedData = Utilities.receiveData(socket);
			}
			if (receivedData.equals("CAPA\r\n")) {
				Utilities.sendData(socket,
						"+OK List of capabilities follows\r\n" + "TOP\r\n" + "USER\r\n" + "UIDL\r\n" + ".\r\n");
				receivedData = Utilities.receiveData(socket);
			}
			if (receivedData.equals("USER gib\r\n"))
				Utilities.sendData(socket, "+OK user is correct\r\n");
			else
				Utilities.sendData(socket, "-ERR unknown User\r\n");
			receivedData = Utilities.receiveData(socket);
			if (receivedData.equals("PASS strenggeheim\r\n"))
				Utilities.sendData(socket, "+OK Mailbox locked and ready\r\n");
			else
				Utilities.sendData(socket, "-ERR Password incorrect\r\n");
			receivedData = Utilities.receiveData(socket);
			while (!receivedData.equals("QUIT\r\n")) {
				if (receivedData.equals("STAT\r\n"))
					Utilities.sendData(socket, "+OK 3 48\r\n");
				else if (receivedData.equals("LIST\r\n"))
					Utilities.sendData(socket,
							"+OK scan listing follows\r\n" + "1 17\r\n" + "2 14\r\n" + "3 17\r\n" + ".\r\n");
				else if (receivedData.startsWith("TOP")) {
					receivedData = receivedData.substring(0, receivedData.length() - 2);
					String[] string = receivedData.split(" ");
					int index = Integer.valueOf(string[1]);
					int rowCount = Integer.valueOf(string[2]);
					String sendString = getHeader(index);
					for (int i = 0; i < rowCount; i++) {
						sendString += "Zeile " + (i + 1) + "\r\n";
					}
					sendString += ".\r\n";
					Utilities.sendData(socket, sendString);
				} else if (receivedData.equals("RETR 1\r\n"))
					Utilities.sendData(socket,
							"+OK Message follows\r\n" + getHeader(1) + "\r\n" + "Das ist ein Test.\r\n" + ".\r\n");
				else if (receivedData.equals("RETR 2\r\n"))
					Utilities.sendData(socket,
							"+OK Message follows\r\n" + getHeader(2) + "\r\n" + "Noch ein Test.\r\n" + ".\r\n");
				else if (receivedData.equals("RETR 3\r\n"))
					Utilities.sendData(socket,
							"+OK Message follows\r\n" + getHeader(3) + "\r\n" + "Ein letzter Test.\r\n" + ".\r\n");
				else if (receivedData.equals("UIDL\r\n")) {
					Date now = new Date();
					String uidroot = dateForUid.format(now);
					Utilities.sendData(socket, "+OK UID-List follows\r\n" + "1 " + uidroot + "1\r\n" + "2 " + uidroot
							+ "2\r\n" + "3 " + uidroot + "3\r\n" + ".\r\n");
				} else
					Utilities.sendData(socket, "-ERR unrecognized command\r\n");
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

	private String getHeader(int index) {
		String header = "MIME-Version: 1.0\r\n" + "Date: Tue, 04 Nov 2008 14:54:33 -0600\r\n" + "From: Sender\r\n"
				+ "To: Empfaenger\r\n" + "Subject: Nachricht " + index + "\r\n";
		return header;
	}
}
