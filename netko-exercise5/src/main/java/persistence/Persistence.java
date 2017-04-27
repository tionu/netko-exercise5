package persistence;

public class Persistence {

	private static PersistenceProvider instance;

	private Persistence() {
	}

	public static synchronized PersistenceProvider getInstance() {
		if (Persistence.instance == null) {
			Persistence.instance = SQLitePersistence.getInstance();
		}
		return Persistence.instance;
	}
}
