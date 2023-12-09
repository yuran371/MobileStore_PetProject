import entity.PersonalAccountEntity;
import utlis.ConnectionPoolManager;

public class TestPersAccDao {

	public static void main(String[] args) {
		try {
//			PersonalAccountDao.insertAccount(new PersonalAccountEntity("chmod", "BLOB", "Russia", "Moscow",
//					"Red Square, st. Pushkin, APT 228", "+79211431222"));
//			System.out.println(PersonalAccountDao.getByLogin("chmod"));
			var personalAccountEntity = new PersonalAccountEntity();
			personalAccountEntity.setCountry("Russia");
		} finally {
			ConnectionPoolManager.closePool();
		}
	}
}
