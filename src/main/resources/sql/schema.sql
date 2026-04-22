CREATE TABLE IF NOT EXISTS apartments (
    id          SERIAL PRIMARY KEY,
    title       VARCHAR(100)  NOT NULL,
    rooms       INTEGER       NOT NULL CHECK (rooms >= 1 AND rooms <= 10),
    price       INTEGER       NOT NULL CHECK (price >= 0),
    description VARCHAR(500)
);

CREATE TABLE IF NOT EXISTS apartment_params (
    id           SERIAL PRIMARY KEY,
    apartment_id INTEGER NOT NULL REFERENCES apartments(id) ON DELETE CASCADE,
    name         VARCHAR(50)  NOT NULL,
    value        VARCHAR(200) NOT NULL
);

INSERT INTO apartments (title, rooms, price, description)
SELECT 'Квартира в центрі', 2, 15000, 'Гарний вид.'
WHERE NOT EXISTS (SELECT 1 FROM apartments WHERE title = 'Квартира в центрі');

INSERT INTO apartments (title, rooms, price, description)
SELECT 'Студія на околиці', 1, 8000, 'Дешево та сердито.'
WHERE NOT EXISTS (SELECT 1 FROM apartments WHERE title = 'Студія на околиці');

INSERT INTO apartments (title, rooms, price, description)
SELECT 'Елітні апартаменти', 3, 35000, 'Преміум клас.'
WHERE NOT EXISTS (SELECT 1 FROM apartments WHERE title = 'Елітні апартаменти');

INSERT INTO apartment_params (apartment_id, name, value)
SELECT a.id, 'Поверх', '5'
FROM apartments a WHERE a.title = 'Квартира в центрі'
AND NOT EXISTS (SELECT 1 FROM apartment_params p WHERE p.apartment_id = a.id AND p.name = 'Поверх');

INSERT INTO apartment_params (apartment_id, name, value)
SELECT a.id, 'Площа', '65 м²'
FROM apartments a WHERE a.title = 'Квартира в центрі'
AND NOT EXISTS (SELECT 1 FROM apartment_params p WHERE p.apartment_id = a.id AND p.name = 'Площа');

INSERT INTO apartment_params (apartment_id, name, value)
SELECT a.id, 'Поверх', '2'
FROM apartments a WHERE a.title = 'Студія на околиці'
AND NOT EXISTS (SELECT 1 FROM apartment_params p WHERE p.apartment_id = a.id AND p.name = 'Поверх');

INSERT INTO apartment_params (apartment_id, name, value)
SELECT a.id, 'Площа', '28 м²'
FROM apartments a WHERE a.title = 'Студія на околиці'
AND NOT EXISTS (SELECT 1 FROM apartment_params p WHERE p.apartment_id = a.id AND p.name = 'Площа');

INSERT INTO apartment_params (apartment_id, name, value)
SELECT a.id, 'Поверх', '15'
FROM apartments a WHERE a.title = 'Елітні апартаменти'
AND NOT EXISTS (SELECT 1 FROM apartment_params p WHERE p.apartment_id = a.id AND p.name = 'Поверх');

INSERT INTO apartment_params (apartment_id, name, value)
SELECT a.id, 'Площа', '120 м²'
FROM apartments a WHERE a.title = 'Елітні апартаменти'
AND NOT EXISTS (SELECT 1 FROM apartment_params p WHERE p.apartment_id = a.id AND p.name = 'Площа');

INSERT INTO apartment_params (apartment_id, name, value)
SELECT a.id, 'Паркінг', 'Так'
FROM apartments a WHERE a.title = 'Елітні апартаменти'
AND NOT EXISTS (SELECT 1 FROM apartment_params p WHERE p.apartment_id = a.id AND p.name = 'Паркінг');
