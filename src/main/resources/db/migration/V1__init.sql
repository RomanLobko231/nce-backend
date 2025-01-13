CREATE TABLE IF NOT EXISTS car (
    id UUID PRIMARY KEY,
    registration_number VARCHAR(255) NOT NULL,
    kilometers INTEGER,
    make VARCHAR(255),
    model VARCHAR(255),
    first_time_registered_in_norway DATE,
    engine_type VARCHAR(255),
    engine_volume INTEGER,
    bodywork VARCHAR(255),
    number_of_seats INTEGER,
    number_of_doors INTEGER,
    color VARCHAR(255),
    gearbox_type VARCHAR(50),
    operating_mode VARCHAR(50),
    weight INTEGER,
    next_eu_control DATE,
    name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    status VARCHAR(50) NOT NULL,
    additional_information TEXT
);

CREATE TABLE IF NOT EXISTS car_image_paths (
    car_id UUID REFERENCES car(id) ON DELETE CASCADE,
    image_path TEXT,
    PRIMARY KEY (car_id, image_path)
);
