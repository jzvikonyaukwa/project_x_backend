
-- add has_custom_markup column
ALTER TABLE consumables_on_quote ADD has_custom_markup bit(1) NOT NULL DEFAULT b'0';

-- save the current data
UPDATE consumables_on_quote SET has_custom_markup = 1 where quote_price_id not in(1,2);

-- drop foreign key constraint
ALTER TABLE consumables_on_quote DROP FOREIGN KEY consumables_on_quote_quote_price_FK;

-- drop the quote_price_id column
ALTER TABLE consumables_on_quote DROP COLUMN quote_price_id;