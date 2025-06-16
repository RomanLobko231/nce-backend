CREATE TABLE IF NOT EXISTS buyer_representative (
    buyer_representative_id UUID PRIMARY KEY REFERENCES base_user(id) ON DELETE CASCADE,
    buyer_company_id UUID NOT NULL REFERENCES buyer_company(buyer_company_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS representative_car_ids (
    buyer_representative_id UUID REFERENCES buyer_representative(buyer_representative_id) ON DELETE CASCADE,
    car_id UUID NOT NULL,
    PRIMARY KEY (buyer_representative_id, car_id)
);
