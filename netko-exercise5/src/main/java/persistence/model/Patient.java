package persistence.model;

import java.util.Date;

import persistence.HospitalObject;
import persistence.HospitalObjectPersistence;
import persistence.exceptions.NonExistingIdException;

public class Patient extends HospitalObject implements HospitalObjectPersistence {

	private String nachname;
	private String vorname;
	private Date geburtsdatum;

	public Patient() {
		super();
	}

	public Patient(int id) {
		super();
		this.id = id;
	}

	public Patient(String nachname, String vorname, Date gebDat) {
		super();
		this.nachname = nachname;
		this.vorname = vorname;
		this.geburtsdatum = gebDat;
	}

	public Patient(int id, String nachname, String vorname, Date gebDat) {
		this(nachname, vorname, gebDat);
		this.id = id;
	}

	public String getNachname() {
		return nachname;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public Date getGeburtsdatum() {
		return geburtsdatum;
	}

	public void setGeburtsdatum(Date gebDat) {
		this.geburtsdatum = gebDat;
	}

	@Override
	public void create() {
		this.id = persistence.create(this);
	}

	@Override
	public void read() {
		Patient readPatient = persistence.read(this.getClass(), id);
		if (readPatient != null) {
			setId(readPatient.getId());
			setNachname(readPatient.getNachname());
			setVorname(readPatient.getVorname());
			setGeburtsdatum(readPatient.getGeburtsdatum());
		} else {
			resetFields();
		}
	}

	@Override
	public void update() throws NonExistingIdException {
		persistence.update(this);
	}

	@Override
	public void delete() {
		if (getId() != null) {
			persistence.delete(this.getClass(), getId());
		}
		resetFields();
	}

	private void resetFields() {
		setId(null);
		setNachname(null);
		setVorname(null);
		setGeburtsdatum(null);
	}

	@Override
	public void updateOrCreate() {
		this.id = persistence.updateOrCreate(this);
	}

}
