CREATE TABLE IF NOT EXISTS base_user (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone_number VARCHAR(50) NOT NULL,
    role VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS app_admin (
    admin_id UUID PRIMARY KEY REFERENCES base_user(id) ON DELETE CASCADE,
    fallback_email VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS seller (
    seller_id UUID PRIMARY KEY REFERENCES base_user(id) ON DELETE CASCADE,
    street_address VARCHAR(255) NOT NULL,
    postal_location VARCHAR(255) NOT NULL,
    postal_code INT NOT NULL
);

CREATE TABLE IF NOT EXISTS seller_car_ids (
    seller_id UUID REFERENCES seller(seller_id) ON DELETE CASCADE,
    car_id UUID NOT NULL,
    PRIMARY KEY (seller_id, car_id)
);

CREATE TABLE IF NOT EXISTS buyer (
    buyer_id UUID PRIMARY KEY REFERENCES base_user(id) ON DELETE CASCADE,
    organisation_name VARCHAR(255) NOT NULL,
    organisation_number VARCHAR(255) UNIQUE NOT NULL,
    street_address VARCHAR(255) NOT NULL,
    postal_location VARCHAR(255) NOT NULL,
    postal_code INT NOT NULL
);

CREATE TABLE IF NOT EXISTS  org_licence_urls (
    buyer_id UUID REFERENCES buyer(buyer_id) ON DELETE CASCADE,
    url TEXT NOT NULL,
    PRIMARY KEY (buyer_id, url)
);


