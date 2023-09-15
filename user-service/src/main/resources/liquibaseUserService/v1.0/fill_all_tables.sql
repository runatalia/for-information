--liquibase formatted sql
--changeset Natalia:fill-all-tables
INSERT INTO country(id, name)
VALUES (1, 'Россия'),
       (2, 'Грузия'),
       (3, 'Белоруссия'),
       (4, 'Турция'),
       (5, 'США'),
       (6, 'Египет');

INSERT INTO city(id, id_country, name)
VALUES (1, 2, 'Тбилиси'),
       (2, 1, 'Москва'),
       (3, 3, 'Минск'),
       (4, 3, 'Брест'),
       (5, 1, 'Хабаровск'),
       (6, 1, 'Владивосток'),
       (7, 1, 'Пермь'),
       (8, 3, 'Поставы');

INSERT INTO skill(id, name)
VALUES (1, 'Spring MVC'),
       (2, 'JAVA'),
       (3, 'JPA'),
       (4, 'Spring Data'),
       (5, 'Hibernate'),
       (6, 'Spring cloud');

INSERT INTO specialization(id, name)
VALUES (1, 'инженер-эколог'),
       (2, 'инженер-программист'),
       (3, 'экономист'),
       (4, 'инженер-радиотехник'),
       (5, 'инженер по автоматизации');

INSERT INTO institution(id, short_name, full_name)
VALUES (1, 'МТУСИ', 'Московский технический университет связи и информатики'),
       (2, 'МИСИС', 'Национальный исследовательский технологический университет'),
       (3, 'ДВГУПС', 'Дальневосточный государственный университет путей сообщения'),
       (4, 'МИП', 'Московский институт психоанализа'),
       (5, 'РГСУ', 'Российский государственный социальный университет');


INSERT INTO users(id, email, image, birthdate, id_city, experience, himself_description, is_blocked)
VALUES (1, 'leha@yandex.ru', 'valera.img', '2001-04-04', 1, 'NOEXPERIENCE', 'отличный парень, умный и красивый', false),
       (2, 'lenchik@yahoo.com', 'vovan.png', '1999-05-05', 1, 'NOEXPERIENCE', 'занял первые места в десяти хакатонах',
        false),
       (3, 'nadya@google.com', 'nadya.png', '1998-10-10', 2, 'LESS1YEAR',
        'пройдено множество курсов, прочтено множество книг', false),
       (4, 'polina.gj1@yandex.ru', 'leha.img', '2001-04-29', 3, 'LESS1YEAR',
        'внедрил автоматизированную систему в ларек', false),
       (5, 'voroninana1990@mail.ru', 'lenchik.png', '2002-09-09', 4, 'YEAR1', 'работал на google и yandex', false);



INSERT INTO education(id, id_user, level, id_institution, faculty, id_specialization, year_of_ending)
VALUES (1, 1, 'SECONDARY', 5, 'филологический', 1, 1997),
       (2, 2, 'SECONDARY', 4, 'философский', 1, 1999),
       (3, 3, 'INCOMPLETEHIGHER', 3, 'социологии', 3, 1987),
       (4, 4, 'INCOMPLETEHIGHER', 2, 'исторический', 4, 2003),
       (5, 5, 'INCOMPLETEHIGHER', 1, 'юридический', 4, 1990);


INSERT INTO user_skill(id_user, id_skill)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (2, 4),
       (2, 5),
       (3, 6),
       (4, 1),
       (5, 2);
