ALTER TABLE buyer RENAME TO buyer_company;
ALTER TABLE buyer_company RENAME COLUMN buyer_id TO buyer_company_id;

ALTER TABLE org_licence_urls RENAME COLUMN buyer_id TO buyer_company_id;

ALTER TABLE org_licence_urls DROP CONSTRAINT IF EXISTS org_licence_urls_buyer_id_fkey;
ALTER TABLE org_licence_urls ADD CONSTRAINT org_licence_urls_buyer_company_id_fkey
FOREIGN KEY (buyer_company_id) REFERENCES buyer_company(buyer_company_id) ON DELETE CASCADE;

ALTER TABLE org_licence_urls DROP CONSTRAINT IF EXISTS org_licence_urls_pkey;
ALTER TABLE org_licence_urls ADD CONSTRAINT org_licence_urls_pkey PRIMARY KEY (buyer_company_id, url);