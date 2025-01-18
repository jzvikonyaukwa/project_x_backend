-- copy existing data from quote to consumables_on_quote
START TRANSACTION;
    -- copy quote_price_id from quote to consumables_on_quote
    UPDATE consumables_on_quote
        , quotes
        ,quote_price 
    SET
        consumables_on_quote.quote_price_id = quotes.quote_price_id
    where 
        quotes.id = consumables_on_quote.quote_id
        AND quotes.quote_price_id =quote_price.id;

    -- update sell price to be unit price * mark up
    UPDATE consumables_on_quote
        , quote_price
    SET
        consumables_on_quote.sell_price = cast(consumables_on_quote.unit_price as decimal(34,6)) * cast(quote_price.mark_up as decimal(34,6))
    where
        consumables_on_quote.quote_price_id = quote_price.id;
COMMIT;