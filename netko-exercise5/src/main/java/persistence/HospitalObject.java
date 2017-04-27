package persistence;

public abstract class HospitalObject {

	protected Integer id;
	protected PersistenceProvider persistence;

	public HospitalObject() {
		persistence = Persistence.getInstance();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
