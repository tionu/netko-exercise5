package servers;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Utilities {
	
	public static String receiveData(Socket socket) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
		int b = in.read();
		if(b == -1)
			in.close();
		int length = in.available();
		byteStream.write(b);
		while(length > 0) {
			byte[] bytes = new byte[length];
			in.read(bytes, 0, length);
			byteStream.write(bytes);
			length = in.available();
		}
		String receivedData = new String(byteStream.toByteArray());
		System.out.println("Empfangen: " + receivedData);
		return receivedData;
	}

	public static void sendData(Socket socket, String data) throws IOException {
		socket.getOutputStream().write(data.getBytes());
		System.out.println("Gesendet: " + data);
	}
	

}
