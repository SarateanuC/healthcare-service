insert into patient(id, firstname, lastname, gender, date_of_birth, creation_date, modify_date)
VALUES ('7fe14f6e-5bd6-11ee-8c99-0242ac120002', 'firstname', 'lastname', 'MALE', '1990-06-02',
        '2020-06-02 11:12:000000', '2021-06-02 11:12:000000');

insert into medication(id, description, dosage, unit, time, patient_id, creation_date, modify_date)
VALUES ('7fe1528e-5bd6-11ee-8c99-0242ac120004', 'test description', 2, 'GRAMS', '17:00:00',
        '7fe14f6e-5bd6-11ee-8c99-0242ac120002',
        '2020-06-02 11:12:000000', null),
    ('7fe1528e-5bd6-11ee-8c99-0242ac120003', 'test description-2', 2, 'TABLET', '10:00:00',
     '7fe14f6e-5bd6-11ee-8c99-0242ac120002',
     '2020-06-02 11:12:000000', null);

