ALTER TABLE buyer
    DROP COLUMN street_address,
    DROP COLUMN postal_location,
    DROP COLUMN postal_code;

ALTER TABLE buyer
    ADD COLUMN country VARCHAR(100) NOT NULL,
    ADD COLUMN street_address VARCHAR(255) NOT NULL,
    ADD COLUMN postal_location VARCHAR(255) NOT NULL,
    ADD COLUMN postal_code VARCHAR(50) NOT NULL;
