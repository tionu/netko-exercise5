package persistence.model;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import persistence.exceptions.NonExistingIdException;

public class PatientTest {

	// Datumformat
	private static final DateFormat DATUMSFORMAT = new SimpleDateFormat("dd.MM.yyyy");

	Patient einPatient;

	@Before
	public void setUp() throws Exception {
		einPatient = new Patient("Mauberger", "Marli", DATUMSFORMAT.parse("21.02.1992"));
	}

	@Test
	public void testCreate() throws ParseException {
		einPatient.create();
		assertNotNull(einPatient.getId());
	}

	@Test
	public void testRead() {
		einPatient.create();
		einPatient.read();
		assertNotNull(einPatient.getId());
		assertNotNull(einPatient.getNachname());
		assertNotNull(einPatient.getVorname());
		assertNotNull(einPatient.getGeburtsdatum());
	}

	@Test
	public void testUpdate() throws NonExistingIdException {
		einPatient.create();
		einPatient.setVorname("Marry");
		einPatient.update();
		einPatient.read();
		assertEquals("Marry", einPatient.getVorname());
	}

	@Test
	public void testDelete() {
		einPatient.create();
		einPatient.delete();
		assertNull(einPatient.getId());
		assertNull(einPatient.getNachname());
		assertNull(einPatient.getVorname());
		assertNull(einPatient.getGeburtsdatum());
	}

	@After
	public void tearDown() {
		einPatient.delete();
	}

}
