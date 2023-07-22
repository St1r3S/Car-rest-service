truncate table cars_categories cascade;
truncate table categories cascade;
truncate table cars cascade;
truncate table manufacturers cascade;

alter sequence cars_id_seq restart;
alter sequence categories_id_seq restart;
alter sequence manufacturers_id_seq restart;