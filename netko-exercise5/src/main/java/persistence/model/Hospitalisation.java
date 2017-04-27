package persistence.model;

import java.util.Date;

import persistence.HospitalObject;
import persistence.HospitalObjectPersistence;
import persistence.exceptions.NonExistingIdException;

public class Hospitalisation extends HospitalObject implements HospitalObjectPersistence {

	private Date aufnahmeDatum;
	private Date entlassDatum;
	private Patient patient;
	private Fachbereich fachbereich;

	public Hospitalisation() {
		super();
	}

	public Hospitalisation(int id) {
		super();
		this.id = id;
	}

	public Hospitalisation(Date aufnahmeDatum, Date entlassDatum, Patient patient, Fachbereich fachbereich) {
		super();
		this.aufnahmeDatum = aufnahmeDatum;
		this.entlassDatum = entlassDatum;
		this.patient = patient;
		this.fachbereich = fachbereich;
	}

	public Hospitalisation(int id, Date aufnahmeDatum, Date entlassDatum, Patient patient, Fachbereich fachbereich) {
		this(aufnahmeDatum, entlassDatum, patient, fachbereich);
		this.id = id;
	}

	public Date getAufnahmeDatum() {
		return aufnahmeDatum;
	}

	public void setAufnahmeDatum(Date aufnahmeDatum) {
		this.aufnahmeDatum = aufnahmeDatum;
	}

	public Date getEntlassDatum() {
		return entlassDatum;
	}

	public void setEntlassDatum(Date entlassDatum) {
		this.entlassDatum = entlassDatum;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Fachbereich getFachbereich() {
		return fachbereich;
	}

	public void setFachbereich(Fachbereich fachbereich) {
		this.fachbereich = fachbereich;
	}

	@Override
	public void create() {
		this.id = persistence.create(this);
	}

	@Override
	public void read() {
		Hospitalisation readHospitalisation = persistence.read(this.getClass(), id);
		if (readHospitalisation != null) {
			setId(readHospitalisation.getId());
			setAufnahmeDatum(readHospitalisation.getAufnahmeDatum());
			setEntlassDatum(readHospitalisation.getEntlassDatum());
			setFachbereich(readHospitalisation.getFachbereich());
			setPatient(readHospitalisation.getPatient());
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
		setAufnahmeDatum(null);
		setEntlassDatum(null);
		setFachbereich(null);
		setPatient(null);
	}

	@Override
	public void updateOrCreate() {
		this.id = persistence.updateOrCreate(this);
	}

}
