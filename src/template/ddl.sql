CREATE TABLE customer (
  id                     SERIAL PRIMARY KEY,
  cooperation_partner_id BIGINT       NOT NULL,
  customer_type          VARCHAR(255) NOT NULL,
  dispatch_type          VARCHAR(255) NOT NULL,
  registration_date      DATE         NOT NULL
);

CREATE TABLE customer_properties (
  customer_id    BIGINT NOT NULL REFERENCES customer,
  properties     VARCHAR(255),
  properties_key VARCHAR(255),
  PRIMARY KEY (customer_id, properties_key)
);

CREATE TABLE contact (
  id           SERIAL PRIMARY KEY,
  contact_type VARCHAR(255),
  country_code VARCHAR(255),
  first_name   VARCHAR(255),
  last_name    VARCHAR(255),
  street       VARCHAR(255),
  zip_code     VARCHAR(255),
  customer_id  BIGINT NOT NULL REFERENCES customer
);

CREATE TABLE contact_communications (
  contact_id         BIGINT NOT NULL REFERENCES contact,
  communications     VARCHAR(255),
  communications_key VARCHAR(255),
  PRIMARY KEY (contact_id, communications_key)
);

