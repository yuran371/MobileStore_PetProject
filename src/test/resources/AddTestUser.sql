INSERT INTO PERSONAL_ACCOUNT (email, password, name, surname, image, birthday, countryEnum, city, address, phone_number,
                              gender)
VALUES ('noemail@email.ru', crypt('123', gen_salt('bf')), 'Sasha', 'nonamich', '', '1990-01-01', 'RUSSIA', 'no city',
        'no address', '+79214050505', 'MALE');