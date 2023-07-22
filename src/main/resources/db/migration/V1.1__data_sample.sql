insert into manufacturers (id, make)
values (default, 'Audi');
insert into manufacturers (id, make)
values (default, 'Chevrolet');
insert into manufacturers (id, make)
values (default, 'Cadillac');
insert into manufacturers (id, make)
values (default, 'Acura');
insert into manufacturers (id, make)
values (default, 'BMW');
insert into manufacturers (id, make)
values (default, 'Chrysler');

insert into categories (id, category)
values (default, 'SUV');
insert into categories (id, category)
values (default, 'Sedan');
insert into categories (id, category)
values (default, 'Coupe');
insert into categories (id, category)
values (default, 'Convertible');
insert into categories (id, category)
values (default, 'Pickup');
insert into categories (id, category)
values (default, 'Van/Minivan');

insert into cars (id, model, manufacture_year, manufacturer_id)
values (default, 'Q3', 2020, 1);
insert into cars (id, model, manufacture_year, manufacturer_id)
values (default, 'Malibu', 2020, 2);
insert into cars (id, model, manufacture_year, manufacturer_id)
values (default, 'Escalade ESV', 2020, 3);
insert into cars (id, model, manufacture_year, manufacturer_id)
values (default, 'Corvette', 2020, 2);
insert into cars (id, model, manufacture_year, manufacturer_id)
values (default, 'RLX', 2020, 4);
insert into cars (id, model, manufacture_year, manufacturer_id)
values (default, 'Silverado 2500 HD Crew Cab', 2020, 2);
insert into cars (id, model, manufacture_year, manufacturer_id)
values (default, '3 Series', 2020, 5);
insert into cars (id, model, manufacture_year, manufacturer_id)
values (default, 'Pacifica', 2020, 6);
insert into cars (id, model, manufacture_year, manufacturer_id)
values (default, 'Colorado Crew Cab', 2020, 2);
insert into cars (id, model, manufacture_year, manufacturer_id)
values (default, 'X3', 2020, 5);

insert into cars_categories(car_id, category_id)
VALUES (1, 1);
insert into cars_categories(car_id, category_id)
VALUES (2, 2);
insert into cars_categories(car_id, category_id)
VALUES (3, 1);
insert into cars_categories(car_id, category_id)
VALUES (4, 3);
insert into cars_categories(car_id, category_id)
VALUES (4, 4);
insert into cars_categories(car_id, category_id)
VALUES (5, 2);
insert into cars_categories(car_id, category_id)
VALUES (6, 5);
insert into cars_categories(car_id, category_id)
VALUES (7, 2);
insert into cars_categories(car_id, category_id)
VALUES (8, 6);
insert into cars_categories(car_id, category_id)
VALUES (9, 5);
insert into cars_categories(car_id, category_id)
VALUES (10, 1);
