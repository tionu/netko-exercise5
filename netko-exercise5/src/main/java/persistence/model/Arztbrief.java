package persistence.model;

import persistence.HospitalObject;
import persistence.HospitalObjectPersistence;
import persistence.exceptions.NonExistingIdException;

public class Arztbrief extends HospitalObject implements HospitalObjectPersistence {

	private Hospitalisation hospitalisation;
	private Arzt arzt;
	private String titel;
	private String text;

	public Arztbrief() {
		super();
	}

	public Arztbrief(int id) {
		super();
		this.id = id;
	}

	public Arztbrief(Hospitalisation hospitalisation, Arzt arzt, String titel, String text) {
		super();
		this.hospitalisation = hospitalisation;
		this.arzt = arzt;
		this.titel = titel;
		this.text = text;
	}

	public Arztbrief(int id, Hospitalisation hospitalisation, Arzt arzt, String titel, String text) {
		this(hospitalisation, arzt, titel, text);
		this.id = id;
	}

	public Hospitalisation getHospitalisation() {
		return hospitalisation;
	}

	public void setHospitalisation(Hospitalisation hospitalisation) {
		this.hospitalisation = hospitalisation;
	}

	public Arzt getArzt() {
		return arzt;
	}

	public void setArzt(Arzt arzt) {
		this.arzt = arzt;
	}

	public String getTitel() {
		return titel;
	}

	public void setTitel(String titel) {
		this.titel = titel;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public void create() {
		this.id = persistence.create(this);
	}

	@Override
	public void read() {
		Arztbrief readArztbrief = persistence.read(this.getClass(), id);
		if (readArztbrief != null) {
			setId(readArztbrief.getId());
			setTitel(readArztbrief.getTitel());
			setText(readArztbrief.getText());
			setArzt(readArztbrief.getArzt());
			setHospitalisation(readArztbrief.getHospitalisation());
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
		setTitel(null);
		setText(null);
		setArzt(null);
		setHospitalisation(null);
	}

	@Override
	public void updateOrCreate() {
		this.id = persistence.updateOrCreate(this);
	}

}
