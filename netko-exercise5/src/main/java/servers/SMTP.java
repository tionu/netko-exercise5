package servers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import persistence.Persistence;
import persistence.PersistenceProvider;
import persistence.model.Arzt;
import persistence.model.Arztbrief;
import persistence.model.Hospitalisation;

public class SMTP {

	PersistenceProvider persistence = Persistence.getInstance();

	private String receiveLetterMail = "arztbrief@kranken.haus";

	public String getReceiveLetterMail() {
		return receiveLetterMail;
	}

	public void setReceiveLetterMail(String receiveLetterMail) {
		receiveLetterMail = receiveLetterMail;
	}

	public static void main(String[] args) {
		SMTP smtp = new SMTP();
		while (true) {
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
			if (receivedData.startsWith("EHLO"))
				Utilities.sendData(socket, "250-localhost\r\n" + "250-SIZE 54525952\r\n" + "250-AUTH LOGIN\r\n"
						+ "250-AUTH=PLAIN LOGIN\r\n" + "250 8BITMIME\r\n");
			else
				Utilities.sendData(socket, "502 5.5.2 Error: command not recognized\r\n");
			// receivedData = Utilities.receiveData(socket);
			// if(receivedData.startsWith("HELO"))
			// Utilities.sendData(socket, "250 OK\r\n");
			// else
			// Utilities.sendData(socket, "502 5.5.2 Error: command not
			// recognized\r\n");
			receivedData = Utilities.receiveData(socket);
			if (receivedData.startsWith("MAIL FROM:"))
				Utilities.sendData(socket, "250 OK\r\n");
			else
				Utilities.sendData(socket, "502 5.5.2 Error: command not recognized\r\n");
			receivedData = Utilities.receiveData(socket);
			if (receivedData.startsWith("RCPT TO:"))
				Utilities.sendData(socket, "250 OK\r\n");
			else
				Utilities.sendData(socket, "502 5.5.2 Error: command not recognized\r\n");
			receivedData = Utilities.receiveData(socket);
			while (!receivedData.equals("QUIT\r\n")) {
				if (receivedData.equals("DATA\r\n")) {
					Utilities.sendData(socket, "354 Start Main Input\r\n");
					receivedData = Utilities.receiveData(socket);
					verarbeiteNachricht(receivedData);
					Utilities.sendData(socket, "250 2.0.0 Ok: queued as E1BD78017C\r\n");
				} else if (receivedData.equals(".\r\n")) {
					Utilities.sendData(socket, "250 OK\r\n");
				} else
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
		String toMail = getToMail(receivedData);
		if (!toMail.equalsIgnoreCase(receiveLetterMail)) {
			System.err.println("Mail not send to " + receiveLetterMail);
			return;
		}
		Integer arztID = getArztIdByMail(getFromMail(receivedData));
		Arzt arzt = new Arzt(arztID);
		arzt.read();

		String subject = getSubject(receivedData);
		Integer hospitalisationId = getHospitalisationIdFromSubject(subject);
		Hospitalisation hospitalisation = new Hospitalisation(hospitalisationId);
		hospitalisation.read();

		String title = getTitleFromSubject(subject);
		String mailBody = getMailBody(receivedData);

		Arztbrief arztbrief = new Arztbrief(hospitalisation, arzt, title, mailBody);
		arztbrief.create();
		System.out.println("\"Arztbrief\" created:\nHospitalisation ID: " + arztbrief.getHospitalisation().getId()
				+ "\nArzt ID: " + arztbrief.getArzt().getId() + "\nTitel: " + arztbrief.getTitel() + "\nText: "
				+ arztbrief.getText());
	}

	private String getMailBody(String receivedData) {
		String uptoEndOfBody = receivedData.split("\\n[\\n]{1}\\.")[0];
		return uptoEndOfBody.substring(uptoEndOfBody.indexOf("\r\n\r\n") + 2, uptoEndOfBody.length() - 3);
	}

	private String getToMail(String receivedData) {
		Matcher toLine = Pattern.compile("To: <.*@.*\\..*>").matcher(receivedData);
		if (toLine.find()) {
			Matcher toMail = Pattern.compile("<.*>").matcher(toLine.group());
			if (toMail.find()) {
				return toMail.group().substring(1, toMail.group().length() - 1);
			}
		}
		return null;
	}

	private String getTitleFromSubject(String subject) {
		return subject.split(":")[1];
	}

	private Integer getHospitalisationIdFromSubject(String subject) {
		return Integer.valueOf(subject.split(":")[0]);
	}

	private String getSubject(String receivedData) {
		Matcher subjectLine = Pattern.compile("Subject: .*").matcher(receivedData);
		if (subjectLine.find()) {
			return subjectLine.group().substring(9);
		}
		return null;
	}

	private String getFromMail(String receivedData) {
		Matcher fromLine = Pattern.compile("From:.+<.*@.*\\..*>").matcher(receivedData);
		if (fromLine.find()) {
			Matcher fromMail = Pattern.compile("<.*>").matcher(fromLine.group());
			if (fromMail.find()) {
				return fromMail.group().substring(1, fromMail.group().length() - 1);
			}
		}
		return null;
	}

	private Integer getArztIdByMail(String eMailAddress) {
		String firstName = eMailAddress.split("\\.|@")[0];
		String secondName = eMailAddress.split("\\.|@")[1];
		List<Arzt> alleAerzte = persistence.readAll(Arzt.class);
		for (Arzt arzt : alleAerzte) {
			if (arzt.getVorname().equalsIgnoreCase(firstName) && arzt.getNachname().equalsIgnoreCase(secondName)) {
				return arzt.getId();
			}
		}
		return null;
	}

}
