package integration;

import dao.PersonalAccountDao;
import entity.enums.CountryEnum;
import entity.enums.GenderEnum;
import entity.PersonalAccountEntity;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import utlis.ConnectionPoolManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

@Tag("Integration")
@ExtendWith({MockitoExtension.class})
public class IntegrationTestBase {

    public static PersonalAccountEntity addUser() throws SQLException, IOException {
        Path path = new File(PersonalAccountDao.class.getClassLoader().getResource("AddTestUser.sql")
                                       .getFile()).toPath();
        try (Connection connection = ConnectionPoolManager.get()) {
            ScriptRunner scriptRunner = new ScriptRunner(connection);
            scriptRunner.setSendFullScript(true);
            scriptRunner.setStopOnError(true);
            scriptRunner.runScript(Files.newBufferedReader(path));
        }
        return PersonalAccountEntity.builder().address("no address").birthday(LocalDate.of(1990, 1, 1)).city("no city")
                .countryEnum(CountryEnum.KAZAKHSTAN).email("noemail@email.ru").genderEnum(GenderEnum.MALE).image("").name("Sasha")
                .password("123").phoneNumber("+79214050505").surname("nonamich").build();

    }

    public static void deleteUser() throws IOException, SQLException {
        Path path = new File(PersonalAccountDao.class.getClassLoader().getResource("deleteUser.sql")
                                     .getFile()).toPath();
        try (Connection connection = ConnectionPoolManager.get()) {
            ScriptRunner scriptRunner = new ScriptRunner(connection);
            scriptRunner.setSendFullScript(true);
            scriptRunner.setStopOnError(true);
            scriptRunner.runScript(Files.newBufferedReader(path));
        }
    }
}