package persistence;

import java.util.List;

import persistence.exceptions.NonExistingIdException;

public interface PersistenceProvider {

	/**
	 * creates a new object of sub-type {@link <HospitalObject>} in persistence
	 * layer.
	 * 
	 * @param hospitalObject
	 * @return key of newly created object
	 */
	public Integer create(HospitalObject hospitalObject);

	/**
	 * reads a object of sub-type {@link <HospitalObject>} from persistence
	 * layer by given id. returns null if requested id is not present.
	 * 
	 * @param hospitalObjectClass
	 * @param id
	 * @return hospitalObject
	 */
	public <T extends HospitalObject> T read(Class<T> hospitalObjectClass, int id);

	/**
	 * reads all objects of sub-type {@link <HospitalObject>} from persistence
	 * layer.
	 * 
	 * @param hospitalObject:
	 *            acts as a filter, i.e. only objects with matching fields are
	 *            returned. If empty object is given, no filter will be applied.
	 * @return List<HospitalObject>
	 */
	public <T extends HospitalObject> List<T> readAll(Class<T> hospitalObjectClass);

	/**
	 * updates an object of sub-type {@link <HospitalObject>} in persistence
	 * layer.
	 * 
	 * @param hospitalObject
	 * @throws NonExistingIdException
	 */
	public void update(HospitalObject hospitalObject) throws NonExistingIdException;

	/**
	 * updates an object of sub-type {@link <HospitalObject>} in persistence
	 * layer. Creates a new object if given id is not present.
	 * 
	 * @param hospitalObject
	 * @return key of object
	 */
	public Integer updateOrCreate(HospitalObject hospitalObject);

	/**
	 * deletes an object of sub-type {@link <HospitalObject>} from persistence
	 * layer by given id.
	 * 
	 * @param hospitalObjectClass
	 * @param id
	 */
	public void delete(Class<? extends HospitalObject> hospitalObjectClass, int id);

}
