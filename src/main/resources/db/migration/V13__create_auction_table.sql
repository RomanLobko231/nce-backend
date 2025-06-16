CREATE TABLE auction (
    id UUID PRIMARY KEY NOT NULL,
    car_id UUID NOT NULL,
    starting_price DECIMAL(10, 2) NOT NULL,
    minimal_step DECIMAL(10, 2) NOT NULL,
    expected_price DECIMAL(10, 2) NOT NULL,
    highest_bid DECIMAL(10, 2),
    end_date_time TIMESTAMP WITH TIME ZONE NOT NULL,
    status VARCHAR(50) NOT NULL
);

CREATE TABLE auction_bids (
    auction_id UUID NOT NULL REFERENCES auction(id) ON DELETE CASCADE,
    bidder_id UUID NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    placed_at TIMESTAMP WITH TIME ZONE NOT NULL,
    PRIMARY KEY(auction_id, placed_at)
);