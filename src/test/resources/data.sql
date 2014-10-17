INSERT INTO customer (id, cooperation_partner_id, registration_date, customer_type, dispatch_type)
VALUES (1, 1, '2014-1-1', 'P', 'M');

INSERT INTO contact (id, first_name, last_name, street, zip_code, country_code, customer_id, contact_type)
VALUES (1, 'Donald', 'Duck', 'Disney street 42', 77777, 1, 1, 'MAIN');
