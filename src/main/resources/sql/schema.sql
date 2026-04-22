CREATE TABLE IF NOT EXISTS apartments (
    id          SERIAL PRIMARY KEY,
    title       VARCHAR(100)  NOT NULL,
    rooms       INTEGER       NOT NULL CHECK (rooms >= 1 AND rooms <= 10),
    price       INTEGER       NOT NULL CHECK (price >= 0),
    description VARCHAR(500)
);

INSERT INTO apartments (title, rooms, price, description)
SELECT 'Квартира в центрі', 2, 15000, 'Гарний вид.'
WHERE NOT EXISTS (SELECT 1 FROM apartments);

INSERT INTO apartments (title, rooms, price, description)
SELECT 'Студія на околиці', 1, 8000, 'Дешево та сердито.'
WHERE NOT EXISTS (SELECT 1 FROM apartments WHERE title = 'Студія на околиці');

INSERT INTO apartments (title, rooms, price, description)
SELECT 'Елітні апартаменти', 3, 35000, 'Преміум клас.'
WHERE NOT EXISTS (SELECT 1 FROM apartments WHERE title = 'Елітні апартаменти');
