package servers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HTTP {

	public static void main(String[] args) {
		HTTP http = new HTTP(8000);
		http.start();
	}

	private int port;
	private String[] lines;
	private ServerSocket serverSocket;

	public HTTP(int port) {
		this.port = port;
	}

	public void start() {
		try {
			serverSocket = new ServerSocket(port);
			Socket socket = serverSocket.accept();
			String receivedData = Utilities.receiveData(socket);
			lines = receivedData.split("\r\n");
			System.out.println("Empfangene Daten: ");
			for (String line : lines)
				System.out.println(line);
			String content = getContent();
			String response = "HTTP/1.1 200 OK\r\n" + "Content-Length: " + content.length() + "\r\n" + "\r\n" + content;
			Utilities.sendData(socket, response);
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getPatientID() {
		String[] firstLine = lines[0].split(" ");
		String[] paIDLine = firstLine[1].split("/");

		String paID = paIDLine[2];
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return paID;
	}

	private String getContent() {
		String webseite = "<html>\r\n" + "<table>\r\n" + "<tr><th>Eigenschaft</th><th>Wert</th></tr>\r\n"
				+ "<tr><td>Vorname</td><td>Kunigunde</td></tr>\r\n"
				+ "<tr><td>Nachname</td><td>Entenmooser</td></tr>\r\n"
				+ "<tr><td>Geburtsdatum</td><td>01.02.1937</td></tr>\r\n" + "</table>\r\n" + "</html>\r\n";
		return webseite;
	}

}
