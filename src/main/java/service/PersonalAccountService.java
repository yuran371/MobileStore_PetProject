package service;

import dao.PersonalAccountDao;
import dto.personalAccount.AuthUserDto;
import dto.personalAccount.CreateAccountDto;
import dto.personalAccount.ReadUserInfoDto;
import dto.personalAccount.UpdateUserDto;
import entity.PersonalAccountEntity;
import entity.SellHistoryEntity;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import mapper.CreateAccountMapper;
import mapper.ReadUserInfoMapper;
import mapper.UpdateUserMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.graph.GraphSemantic;
import validator.CreateUpdateUserGroup;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
public class PersonalAccountService {


    private final SessionFactory sessionFactory;
    private final Validator validator;
    private final PersonalAccountDao personalAccountDao;

    @Inject
    public PersonalAccountService(SessionFactory sessionFactory, Validator validator,
                                  PersonalAccountDao personalAccountDao) {
        this.sessionFactory = sessionFactory;
        this.validator = validator;
        this.personalAccountDao = personalAccountDao;
    }

    public Either<Long, Set<? extends ConstraintViolation<?>>> createUser(CreateAccountDto createAccountDto) {
        Set<ConstraintViolation<CreateAccountDto>> dtoViolations = validator.validate(createAccountDto);
        if (!dtoViolations.isEmpty()) {
            return Either.right(dtoViolations);
        }
        PersonalAccountEntity personalAccountEntity =
                CreateAccountMapper.INSTANCE.createAccountDtoToPersonalAccountEntity(createAccountDto);
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.beginTransaction();
        Set<ConstraintViolation<PersonalAccountEntity>> entityViolations = validator.validate(personalAccountEntity,
                                                                                              CreateUpdateUserGroup.class);
        currentSession.getTransaction().commit();
        if (!entityViolations.isEmpty()) {
            return Either.right(entityViolations);
        }
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Optional<PersonalAccountEntity> readUserEntity = personalAccountDao.insert(personalAccountEntity);
        session.getTransaction().commit();
        return Either.left(personalAccountEntity.getId());
    }

    public Either<Boolean, Set<? extends ConstraintViolation<?>>> update(UpdateUserDto updateUserDto) {
        Set<ConstraintViolation<UpdateUserDto>> dtoViolations = validator.validate(updateUserDto);
        if (!dtoViolations.isEmpty()) {
            return Either.right(dtoViolations);
        }
        PersonalAccountEntity personalAccountEntity =
                UpdateUserMapper.INSTANCE.updateUserDtoToPersonalAccountEntity(updateUserDto);
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.beginTransaction();
        Set<ConstraintViolation<PersonalAccountEntity>> entityViolations = validator.validate(personalAccountEntity,
                                                                                              CreateUpdateUserGroup.class);
        currentSession.getTransaction().commit();
        if (!entityViolations.isEmpty()) {
            return Either.right(entityViolations);
        }
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Optional<PersonalAccountEntity> personalAccountForCheck =
                personalAccountDao.getById(personalAccountEntity.getId());
        Boolean result = personalAccountForCheck.map(entity -> {
            session.detach(entity);
            personalAccountDao.update(personalAccountEntity);
            return true;
        }).orElse(false);
        session.getTransaction().commit();
        return Either.left(result && !personalAccountForCheck.get().equals(personalAccountEntity));
    }

    public boolean delete(Long id) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.beginTransaction();
        Optional<PersonalAccountEntity> personalAccountEntity = personalAccountDao.getById(id);
        boolean result = personalAccountEntity.map(entity -> {
            personalAccountDao.delete(entity);
            return true;
        }).orElse(false);
        currentSession.getTransaction().commit();
        return result;
    }

    public Optional<ReadUserInfoDto> readUser(Long id) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.beginTransaction();
        var graph = currentSession.createEntityGraph(PersonalAccountEntity.class);
        graph.addAttributeNodes("phonePurchases");
        var subGraph = graph.addSubgraph("phonePurchases", SellHistoryEntity.class);
        subGraph.addAttributeNodes("itemId");
        Optional<PersonalAccountEntity> personalAccountEntity = personalAccountDao.getById(
                id, Map.of(GraphSemantic.LOAD.getJakartaHintName(), graph));
        Optional<ReadUserInfoDto> readUserInfoDto =
                personalAccountEntity.map(entity -> ReadUserInfoMapper.INSTANCE.personalAccountEntityToUserInfoDto(entity));
        currentSession.getTransaction().commit();
        return readUserInfoDto;
    }

    public Either<Optional<ReadUserInfoDto>, Set<? extends ConstraintViolation<?>>> authUser(AuthUserDto authUserDto) {
        Set<ConstraintViolation<AuthUserDto>> dtoViolations = validator.validate(authUserDto);
        if (!dtoViolations.isEmpty()) {
            return Either.right(dtoViolations);
        }
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.beginTransaction();
        Optional<PersonalAccountEntity> personalAccountEntity =
                personalAccountDao.validateAuth(authUserDto.getEmail(), authUserDto.getPassword());
        ReadUserInfoDto readUserInfoDto = personalAccountEntity.map(entity -> ReadUserInfoMapper.INSTANCE.personalAccountEntityToUserInfoDto(entity))
                .orElse(null);
        currentSession.getTransaction().commit();
        return Either.left(Optional.ofNullable(readUserInfoDto));
    }
}
