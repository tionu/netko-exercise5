package persistence;

import persistence.exceptions.NonExistingIdException;

public interface HospitalObjectPersistence {

	/**
	 * creates this object in persistence layer and sets id of newly created
	 * object.
	 */
	public void create();

	/**
	 * reads this object from persistence layer by its id. Resets fields if
	 * requested id is not present.
	 */
	public void read();

	/**
	 * updates this object in persistence layer.
	 * 
	 * @throws NonExistingIdException
	 */
	public void update() throws NonExistingIdException;

	/**
	 * updates this object in persistence layer. Creates this object if given id
	 * is not present.
	 */
	public void updateOrCreate();

	/**
	 * deletes this object from persistence layer and resets fields.
	 */
	public void delete();

}
