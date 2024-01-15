package pureJavaTests;
import dao.PersonalAccountDao;
import utlis.ConnectionPoolManager;

public class TestPersAccDao {

	public static void main(String[] args) {
		PersonalAccountDao personalAccountDao = PersonalAccountDao.getInstance();
		try {
//			personalAccountDao.insertAccount(new PersonalAccountEntity("chmod", "BLOB", "Russia", "Moscow",
//					"Red Square, st. Pushkin, APT 228", "+79211431222"));
//			personalAccountDao.insertAccount(new PersonalAccountEntity("alex21", "Alexey Smirnov", "Kazakhstan",
//					"Astana", "8 Mangilik Yel avenue. House of Ministries Entrance 13", "+77273122602"));
//			personalAccountDao.insertAccount(new PersonalAccountEntity("Vova1989", "Vova Semenov", "Russia",
//					"Magnitogorsk", "K Marksa, bld. 152, appt. 10", "+79081231327"));
//			personalAccountDao.insertAccount(new PersonalAccountEntity("zWEKmYn", "Anatolii Zhukov", "Russia",
//					"Smolensk", "Gorodnyanskogo Ul., bld. 1, appt. 9", "+74812416528"));
//			personalAccountDao.insertAccount(new PersonalAccountEntity("ezhik777", "Zhenya Baranova", "Russia",
//					"Magnitogorsk", "Sirenevyy Proez, bld. 12, appt. 43", "+79212322399"));
//			personalAccountDao.insertAccount(new PersonalAccountEntity("randomName", "Vasiliy Egorov", "Russia",
//					"Khabarovsk", "Voroshilova Ul., bld. 50, appt. 25", "+78617533132"));
//			personalAccountDao.insertAccount(new PersonalAccountEntity("polygloth", "Ivan Ivanoff", "Belarus", "Minsk",
//					"Gurevskiy Pr., bld. 11/К. 1, appt. 437", "80332424353"));
//			personalAccountDao.insertAccount(new PersonalAccountEntity("bellaJay", "Jekaterina Ivanova", "Russia",
//					"Sankt-peterburg", "Kronshtadtskaya Ul., bld. 4/39, appt. 63", "+79105630187"));
//			System.out.println(PersonalAccountDao.getByLogin("chmod"));
//			var personalAccountEntity = new PersonalAccountEntity();
//			personalAccountEntity.setCountry("Russia");
		} finally {
			ConnectionPoolManager.closePool();
		}
	}
}
