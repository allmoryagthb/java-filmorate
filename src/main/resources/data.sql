-- INSERT INTO rating (name)
-- VALUES ('G'),
--        ('PG'),
--        ('PG-13'),
--        ('R'),
--        ('NC-17');
--
-- INSERT INTO genre (name)
-- VALUES ('Драма'),
--        ('Комедия'),
--        ('Ужасы'),
--        ('Фантастика'),
--        ('Боевик'),
--        ('Романтика');

MERGE INTO rating (name)
    KEY (name)
VALUES ('G');
MERGE INTO rating (name)
    KEY (name)
VALUES ('PG');
MERGE INTO rating (name)
    KEY (name)
VALUES ('PG-13');
MERGE INTO rating (name)
    KEY (name)
VALUES ('R');
MERGE INTO rating (name)
    KEY (name)
VALUES ('NC-17');



MERGE INTO genre (name)
    KEY (name)
VALUES ('Драма');
MERGE INTO genre (name)
    KEY (name)
VALUES ('Комедия');
MERGE INTO genre (name)
    KEY (name)
VALUES ('Ужасы');
MERGE INTO genre (name)
    KEY (name)
VALUES ('Фантастика');
MERGE INTO genre (name)
    KEY (name)
VALUES ('Боевик');
MERGE INTO genre (name)
    KEY (name)
VALUES ('Романтика');