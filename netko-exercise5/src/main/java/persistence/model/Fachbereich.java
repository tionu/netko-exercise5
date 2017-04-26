package persistence.model;

import persistence.HospitalObject;
import persistence.HospitalObjectPersistence;
import persistence.exceptions.NonExistingIdException;

public class Fachbereich extends HospitalObject implements HospitalObjectPersistence {

	private String name;

	public Fachbereich() {
		super();
	}

	public Fachbereich(int id) {
		super();
		this.id = id;
	}

	public Fachbereich(String name) {
		super();
		this.name = name;
	}

	public Fachbereich(int id, String name) {
		this(name);
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void create() {
		this.id = persistence.create(this);
	}

	@Override
	public void read() {
		Fachbereich readFachbereich = persistence.read(this.getClass(), id);
		if (readFachbereich != null) {
			setId(readFachbereich.getId());
			setName(readFachbereich.getName());
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
		setName(null);
	}

	@Override
	public void updateOrCreate() {
		this.id = persistence.updateOrCreate(this);
	}

}
