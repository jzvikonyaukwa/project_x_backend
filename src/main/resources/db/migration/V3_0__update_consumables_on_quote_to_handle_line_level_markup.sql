-- change unit price from float to decimal | bigdecimal on java side
ALTER TABLE consumables_on_quote MODIFY COLUMN unit_price decimal(32,6) NULL;

-- add sell price column
ALTER TABLE consumables_on_quote ADD COLUMN sell_price decimal(32,6) NULL;

-- add quote_price_id column
ALTER TABLE consumables_on_quote ADD quote_price_id BIGINT NULL;
-- add foreign key constraint
ALTER TABLE consumables_on_quote ADD CONSTRAINT consumables_on_quote_quote_price_FK FOREIGN KEY (quote_price_id) REFERENCES quote_price(id);

-- change mark_up from float to decimal | bigdecimal on java side
ALTER TABLE quote_price MODIFY COLUMN mark_up decimal(36,4) NULL;