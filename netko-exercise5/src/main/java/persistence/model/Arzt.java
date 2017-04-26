package persistence.model;

import persistence.HospitalObject;
import persistence.HospitalObjectPersistence;
import persistence.exceptions.NonExistingIdException;

public class Arzt extends HospitalObject implements HospitalObjectPersistence {

	private String nachname;
	private String vorname;
	private String titel;
	private Fachbereich fachbereich;
	private String username;
	private String password;

	public Arzt() {
		super();
	}

	public Arzt(int id) {
		super();
		this.id = id;
	}

	public Arzt(String nachname, String vorname, String titel, Fachbereich fachbereich, String username,
			String password) {
		super();
		this.nachname = nachname;
		this.vorname = vorname;
		this.titel = titel;
		this.fachbereich = fachbereich;
		this.username = username;
		this.password = password;
	}

	public Arzt(int id, String nachname, String vorname, String titel, Fachbereich fachbereich, String username,
			String password) {
		this(nachname, vorname, titel, fachbereich, username, password);
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

	public String getTitel() {
		return titel;
	}

	public void setTitel(String titel) {
		this.titel = titel;
	}

	public Fachbereich getFachbereich() {
		return fachbereich;
	}

	public void setFachbereich(Fachbereich fachbereich) {
		this.fachbereich = fachbereich;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public void create() {
		this.id = persistence.create(this);
	}

	@Override
	public void read() {
		Arzt readArzt = persistence.read(this.getClass(), id);
		if (readArzt != null){
			setId(readArzt.getId());
			setNachname(readArzt.getNachname());
			setVorname(readArzt.getVorname());
			setTitel(readArzt.getTitel());
			setUsername(readArzt.getUsername());
			setPassword(readArzt.getPassword());
			setFachbereich(readArzt.getFachbereich());
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
		setTitel(null);
		setUsername(null);
		setPassword(null);
		setFachbereich(null);
	}

	@Override
	public void updateOrCreate() {
		this.id = persistence.updateOrCreate(this);
	}

}
