CREATE TABLE IF NOT EXISTS one_time_seller (
    one_time_seller_id UUID PRIMARY KEY REFERENCES base_user(id) ON DELETE CASCADE,
    car_id UUID NOT NULL
);