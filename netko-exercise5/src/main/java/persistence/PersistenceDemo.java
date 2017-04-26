package persistence;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import persistence.exceptions.NonExistingIdException;
import persistence.model.Arzt;
import persistence.model.Fachbereich;
import persistence.model.Patient;

public class PersistenceDemo {

	// Trenneichen für Tabellendarstellung
	private static final String TR = "|";

	// Datumformat
	private static final DateFormat DATUMSFORMAT = new SimpleDateFormat("dd.MM.yyyy");

	public static void main(String[] args) throws ParseException {

		// Neuen Patienten anlegen
		Patient einPatient = new Patient("Mauberger", "Marli", DATUMSFORMAT.parse("21.02.1992"));
		einPatient.create(); // hiermit wird der Patient abgespeichert und eine
								// ID zugewiesen.

		// Vorname ändern und Änderung speichern
		einPatient.setVorname("Marry");
		try {
			einPatient.update();
		} catch (NonExistingIdException e) {
			System.err.println(e.getMessage());
		}

		// Patient löschen
		einPatient.delete(); // der Patient wird aus der Datenbank gelöscht und
								// alle Felder zurück gesetzt.

		// Patient anhand ID laden
		Patient vorhandenerPatient = new Patient(1);
		vorhandenerPatient.read();

		// PersistenceProvider bietet mit der Methode readAll(...) die
		// Möglichkeit, alle Einträge eines Typs aus der Datenbank zu lesen.
		PersistenceProvider persistence = Persistence.getInstance();

		// alle Patienten ausgeben
		List<Patient> patientenListe = persistence.readAll(Patient.class);
		System.out.println("\nPatientenliste:\nNachname" + TR + "Vorname" + TR + "Geburtsdatum");
		for (Patient patient : patientenListe) {
			System.out.println(patient.getNachname() + TR + patient.getVorname() + TR
					+ DATUMSFORMAT.format(patient.getGeburtsdatum()));
		}

		// alle Ärzte ausgeben
		List<Arzt> arztListe = persistence.readAll(Arzt.class);
		System.out.println("\nArztliste:\nNachname" + TR + "Vorname" + TR + "Titel" + TR + "Username" + TR + "Password"
				+ TR + "Fachbereich");
		for (Arzt arzt : arztListe) {
			System.out.println(arzt.getNachname() + TR + arzt.getVorname() + TR + arzt.getTitel() + TR
					+ arzt.getUsername() + TR + arzt.getPassword() + TR + arzt.getFachbereich().getName());
		}

		// alle Fachbereiche ausgeben
		List<Fachbereich> fachbereichListe = persistence.readAll(Fachbereich.class);
		System.out.println("\nFachbereichliste:\nName");
		for (Fachbereich fachbereich : fachbereichListe) {
			System.out.println(fachbereich.getName());
		}

	}

}
