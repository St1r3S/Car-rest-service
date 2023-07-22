CREATE TABLE manufacturers
(
    id   BIGSERIAL PRIMARY KEY,
    make TEXT UNIQUE NOT NULL
);

CREATE TABLE categories
(
    id       BIGSERIAL PRIMARY KEY,
    category TEXT UNIQUE NOT NULL
);

CREATE TABLE cars
(
    id               BIGSERIAL PRIMARY KEY,
    model            TEXT    NOT NULL,
    manufacture_year INTEGER NOT NULL,
    manufacturer_id  BIGINT  NOT NULL REFERENCES manufacturers (id) ON UPDATE CASCADE ON DELETE CASCADE,
    UNIQUE (model, manufacture_year)
);

CREATE TABLE cars_categories
(
    car_id      BIGINT NOT NULL REFERENCES cars (id) ON UPDATE CASCADE ON DELETE CASCADE,
    category_id BIGINT NOT NULL REFERENCES categories (id) ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (car_id, category_id)
);

