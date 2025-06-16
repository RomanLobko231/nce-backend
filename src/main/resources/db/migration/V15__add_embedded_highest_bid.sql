ALTER TABLE auction DROP COLUMN highest_bid;

ALTER TABLE auction
ADD COLUMN highest_bid_bidder_id UUID,
ADD COLUMN highest_bid_amount DECIMAL(10, 2),
ADD COLUMN highest_bid_placed_at TIMESTAMP WITH TIME ZONE;