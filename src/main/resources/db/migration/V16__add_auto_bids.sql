CREATE TABLE auction_auto_bids (
    auction_id UUID NOT NULL REFERENCES auction(id) ON DELETE CASCADE,
    bidder_id UUID NOT NULL,
    limit_amount DECIMAL(10, 2) NOT NULL,
    placed_at TIMESTAMP WITH TIME ZONE NOT NULL,
    PRIMARY KEY(auction_id, placed_at)
);