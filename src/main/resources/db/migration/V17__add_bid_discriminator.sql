ALTER TABLE auction_bids
ADD COLUMN bid_discriminator UUID NOT NULL;

ALTER TABLE auction_bids
DROP CONSTRAINT auction_bids_pkey;

ALTER TABLE auction_bids
ADD CONSTRAINT auction_bids_pkey PRIMARY KEY (auction_id, bid_discriminator);

ALTER TABLE auction
ADD COLUMN highest_bid_bid_discriminator UUID;