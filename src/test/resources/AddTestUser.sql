INSERT INTO PERSONAL_ACCOUNT (email, password, name, surname, image, birthday, country, city, address, phone_number,
                              genderEnum)
VALUES ('noemail@email.ru', crypt('123', gen_salt('bf')), 'Sasha', 'nonamich', '', '1990-01-01', 'RUSSIA', 'no city',
        'no address', '+79214050505', 'MALE');