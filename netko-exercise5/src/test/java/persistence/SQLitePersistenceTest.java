package persistence;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import persistence.exceptions.NonExistingIdException;
import persistence.model.Arzt;
import persistence.model.Arztbrief;
import persistence.model.Fachbereich;
import persistence.model.Hospitalisation;
import persistence.model.Patient;

public class SQLitePersistenceTest {

	private static Path dbSupplyPath = Paths.get("./src/test/resources/persistence/Patienten.db");
	private static Path dbTestPath = Paths.get("./src/test/java/persistence/Patienten.db");

	private SQLitePersistence persist = SQLitePersistence.getInstance(dbTestPath);

	@BeforeClass
	public static void setUpOnce() throws Exception {
		Files.copy(dbSupplyPath, dbTestPath, REPLACE_EXISTING);
		System.out.println("test db ready.");
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testReadPatient() {
		Integer id = 1;
		Patient patient = persist.read(Patient.class, id);
		assertEquals("Winfried", patient.getVorname());
		assertEquals("Huber", patient.getNachname());
		assertEquals("05.02.1938", SQLitePersistence.DATE_FORMAT.format(patient.getGeburtsdatum()));
	}

	@Test
	public void testReadFachbereich() {
		Integer id = 1;
		Fachbereich fachbereich = persist.read(Fachbereich.class, id);
		assertEquals(id, fachbereich.getId());
		assertEquals("Chirurgie", fachbereich.getName());
	}

	@Test
	public void testReadArzt() {
		Integer id = 1;
		Arzt arzt = persist.read(Arzt.class, id);
		Fachbereich fachbereich = arzt.getFachbereich();
		assertEquals(id, arzt.getId());
		assertEquals("Wiegand", arzt.getNachname());
		assertEquals("Maria", arzt.getVorname());
		assertEquals("Dr.", arzt.getTitel());
		assertEquals("mwiegand", arzt.getUsername());
		assertEquals("abc123", arzt.getPassword());
		assertEquals("Chirurgie", fachbereich.getName());
	}

	@Test
	public void testReadHospitalisation() {
		Integer id = 1;
		Hospitalisation hospitalisation = persist.read(Hospitalisation.class, id);
		Fachbereich fachbereich = hospitalisation.getFachbereich();
		Patient patient = hospitalisation.getPatient();
		assertEquals(id, hospitalisation.getId());
		assertEquals("24.04.2017", SQLitePersistence.DATE_FORMAT.format(hospitalisation.getAufnahmeDatum()));
		assertNull(hospitalisation.getEntlassDatum());
		assertEquals("Chirurgie", fachbereich.getName());
		assertEquals("Winfried", patient.getVorname());
		assertEquals("Huber", patient.getNachname());
		assertEquals("05.02.1938", SQLitePersistence.DATE_FORMAT.format(patient.getGeburtsdatum()));
	}

	@Test
	public void testReadArztbrief() {
		Integer id = 1;
		Arztbrief arztbrief = persist.read(Arztbrief.class, id);
		Hospitalisation hospitalisation = arztbrief.getHospitalisation();
		Arzt arzt = arztbrief.getArzt();
		Fachbereich fachbereichHospitalisation = hospitalisation.getFachbereich();
		Patient patientHospitalisation = hospitalisation.getPatient();
		Fachbereich fachbereichArzt = arzt.getFachbereich();
		assertEquals(id, arztbrief.getId());
		assertEquals("Neuer Titel", arztbrief.getTitel());
		assertEquals(
				"Vielen Dank für die Überweisung des Patienten.\nDer Patient konnte geheilt entlassen werden.\nMit freundlichen Grüßen",
				arztbrief.getText());
		assertEquals("24.04.2017", SQLitePersistence.DATE_FORMAT.format(hospitalisation.getAufnahmeDatum()));
		assertNull(hospitalisation.getEntlassDatum());
		assertEquals("Chirurgie", fachbereichHospitalisation.getName());
		assertEquals("Winfried", patientHospitalisation.getVorname());
		assertEquals("Huber", patientHospitalisation.getNachname());
		assertEquals("05.02.1938", SQLitePersistence.DATE_FORMAT.format(patientHospitalisation.getGeburtsdatum()));
		assertEquals("Wiegand", arzt.getNachname());
		assertEquals("Maria", arzt.getVorname());
		assertEquals("Dr.", arzt.getTitel());
		assertEquals("mwiegand", arzt.getUsername());
		assertEquals("abc123", arzt.getPassword());
		assertEquals("Chirurgie", fachbereichArzt.getName());
	}

	@Test
	public void testReadAllArztbrief() {
		List<Arztbrief> arztbriefList = persist.readAll(Arztbrief.class);
		assertEquals(2, arztbriefList.size());
	}

	@Test
	public void testReadAllArzt() {
		List<Arzt> arztList = persist.readAll(Arzt.class);
		assertEquals(2, arztList.size());
	}

	@Test
	public void testReadAllFachbereich() {
		List<Fachbereich> fachbereichList = persist.readAll(Fachbereich.class);
		assertEquals(2, fachbereichList.size());
	}

	@Test
	public void testCreatePatient() {
		boolean pass = false;
		Patient patientCreate = new Patient();
		Date geburtsDatum = new Date();
		patientCreate.setVorname("Marli");
		patientCreate.setNachname("Maurer");
		patientCreate.setGeburtsdatum(geburtsDatum);
		persist.create(patientCreate);
		List<Patient> allPatients = persist.readAll(Patient.class);
		for (Patient patient : allPatients) {
			if (patient.getVorname().equals("Marli") && patient.getNachname().equals("Maurer")
					&& SQLitePersistence.DATE_FORMAT.format(patient.getGeburtsdatum())
							.equals(SQLitePersistence.DATE_FORMAT.format(geburtsDatum))) {
				pass = true;
				break;
			}
		}
		assertTrue(pass);
	}

	@Test
	public void testCreateArztbrief() {
		boolean pass = false;
		Arztbrief arztbriefCreate = persist.read(Arztbrief.class, 1);
		arztbriefCreate.setId(null);
		arztbriefCreate.setTitel("Neuer Titel");
		arztbriefCreate.setText("Neuer Text");
		persist.create(arztbriefCreate);
		List<Arztbrief> allArztbrief = persist.readAll(Arztbrief.class);
		for (Arztbrief arztbrief : allArztbrief) {
			if (arztbrief.getTitel().equals("Neuer Titel") && arztbrief.getText().equals("Neuer Text")) {
				pass = true;
				break;
			}
		}
		assertTrue(pass);
	}

	@Test
	public void testCreateArzt() {
		boolean pass = false;
		Arzt arztCreate = persist.read(Arzt.class, 1);
		arztCreate.setId(null);
		arztCreate.setTitel("Prof.");
		arztCreate.setNachname("Murmel");
		persist.create(arztCreate);
		List<Arzt> allArzt = persist.readAll(Arzt.class);
		for (Arzt arzt : allArzt) {
			if (arzt.getTitel().equals("Prof.") && arzt.getNachname().equals("Murmel")) {
				pass = true;
				break;
			}
		}
		assertTrue(pass);
	}

	@Test
	public void testCreateHospitalisation() {
		boolean pass = false;
		Date entlassDatum = new Date();
		Hospitalisation hospitalisationCreate = persist.read(Hospitalisation.class, 1);
		hospitalisationCreate.setId(null);
		hospitalisationCreate.setEntlassDatum(entlassDatum);
		persist.create(hospitalisationCreate);
		List<Hospitalisation> allHospitalisation = persist.readAll(Hospitalisation.class);
		for (Hospitalisation hospitalisation : allHospitalisation) {
			if (hospitalisation.getEntlassDatum() != null) {
				if (SQLitePersistence.DATE_FORMAT.format(hospitalisation.getEntlassDatum())
						.equals(SQLitePersistence.DATE_FORMAT.format(entlassDatum))) {
					pass = true;
					break;
				}
			}
		}
		assertTrue(pass);
	}

	@Test
	public void testCreateFachbereich() {
		boolean pass = false;
		Fachbereich fachbereichCreate = new Fachbereich("Dermatologie");
		persist.create(fachbereichCreate);
		List<Fachbereich> allFachbereich = persist.readAll(Fachbereich.class);
		for (Fachbereich fachbereich : allFachbereich) {
			if (fachbereich.getName().equals("Dermatologie")) {
				pass = true;
				break;
			}
		}
		assertTrue(pass);
	}

	@Test
	public void testUpdateOrCreateFachbereichCreate() {
		boolean pass = false;
		Fachbereich fachbereichCreate = new Fachbereich("Neurologie");
		persist.create(fachbereichCreate);
		List<Fachbereich> allFachbereich = persist.readAll(Fachbereich.class);
		for (Fachbereich fachbereich : allFachbereich) {
			if (fachbereich.getName().equals("Neurologie")) {
				pass = true;
				break;
			}
		}
		assertTrue(pass);
	}

	@Test
	public void testUpdateOrCreateFachbereichUpdateCreate() {
		boolean pass = false;
		Fachbereich fachbereichCreate = new Fachbereich("Kardiologie");
		fachbereichCreate.setId(1000);
		persist.create(fachbereichCreate);
		List<Fachbereich> allFachbereich = persist.readAll(Fachbereich.class);
		for (Fachbereich fachbereich : allFachbereich) {
			if (fachbereich.getName().equals("Kardiologie")) {
				pass = true;
				break;
			}
		}
		assertTrue(pass);
	}

	@Test
	public void testUpdateOrCreateFachbereichUpdate() {
		Integer id = 3;
		Fachbereich fachbereichUpdate = persist.read(Fachbereich.class, id);
		fachbereichUpdate.setName("Labor");
		persist.updateOrCreate(fachbereichUpdate);
		Fachbereich fachbereichRead = persist.read(Fachbereich.class, id);
		assertEquals(id, fachbereichRead.getId());
		assertEquals("Labor", fachbereichRead.getName());
	}

	@Test
	public void testUpdatePatient() throws NonExistingIdException {
		Integer id = 2;
		Patient patientUpdate = persist.read(Patient.class, id);
		patientUpdate.setVorname("Neuer Vorname");
		persist.update(patientUpdate);
		Patient patientRead = persist.read(Patient.class, id);
		assertEquals(id, patientRead.getId());
		assertEquals("Neuer Vorname", patientRead.getVorname());
	}

	@Test
	public void testUpdateArztbrief() throws NonExistingIdException {
		Integer id = 1;
		Arztbrief arztbriefUpdate = persist.read(Arztbrief.class, id);
		arztbriefUpdate.setTitel("Neuer Titel");
		persist.update(arztbriefUpdate);
		Arztbrief arztbriefRead = persist.read(Arztbrief.class, id);
		assertEquals(id, arztbriefRead.getId());
		assertEquals("Neuer Titel", arztbriefRead.getTitel());
	}

	@Test
	public void testUpdateArzt() throws NonExistingIdException {
		Integer id = 3;
		Arzt arztUpdate = persist.read(Arzt.class, id);
		arztUpdate.setVorname("Neuer Vorname");
		persist.update(arztUpdate);
		Arzt arztRead = persist.read(Arzt.class, id);
		assertEquals(id, arztRead.getId());
		assertEquals("Neuer Vorname", arztRead.getVorname());
	}

	@Test
	public void testUpdateFachbereich() throws NonExistingIdException {
		Integer id = 2;
		Fachbereich fachbereichUpdate = persist.read(Fachbereich.class, id);
		fachbereichUpdate.setName("Radiologie");
		persist.update(fachbereichUpdate);
		Fachbereich fachbereichRead = persist.read(Fachbereich.class, id);
		assertEquals(id, fachbereichRead.getId());
		assertEquals("Radiologie", fachbereichRead.getName());
	}

	@Test
	public void testDeletePatient() {
		Integer id = 2;
		persist.delete(Patient.class, id);
		Patient patient = persist.read(Patient.class, id);
		assertNull(patient);
	}

	@Test
	public void testDeleteArztbrief() {
		Integer id = 2;
		persist.delete(Arztbrief.class, id);
		Arztbrief arztbrief = persist.read(Arztbrief.class, id);
		assertNull(arztbrief);
	}

	@Test
	public void testDeleteArzt() {
		Integer id = 2;
		persist.delete(Arzt.class, id);
		Arzt arzt = persist.read(Arzt.class, id);
		assertNull(arzt);
	}

	@Test
	public void testDeleteFachbereich() {
		Integer id = 2;
		persist.delete(Fachbereich.class, id);
		Fachbereich fachbereich = persist.read(Fachbereich.class, id);
		assertNull(fachbereich);
	}

	@Test
	public void testDeleteHospitalisation() {
		Integer id = 2;
		persist.delete(Hospitalisation.class, id);
		Hospitalisation hospitalisation = persist.read(Hospitalisation.class, id);
		assertNull(hospitalisation);
	}

}
