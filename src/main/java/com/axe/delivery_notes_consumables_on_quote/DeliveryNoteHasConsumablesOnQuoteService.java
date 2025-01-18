package com.axe.delivery_notes_consumables_on_quote;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryNoteHasConsumablesOnQuoteService {


    private final DeliveryNoteHasConsumablesOnQuoteRepository deliveryNoteHasConsumablesOnQuoteRepository;

    public DeliveryNoteHasConsumablesOnQuoteService(DeliveryNoteHasConsumablesOnQuoteRepository deliveryNoteHasConsumablesOnQuoteRepository) {
        this.deliveryNoteHasConsumablesOnQuoteRepository = deliveryNoteHasConsumablesOnQuoteRepository;
    }

    public DeliveryNoteHasConsumablesOnQuote create(DeliveryNoteHasConsumablesOnQuote dto) {
        return deliveryNoteHasConsumablesOnQuoteRepository.save(dto);
    }

    public List<DeliveryNoteHasConsumablesOnQuote> getAll() {
        return deliveryNoteHasConsumablesOnQuoteRepository.findAll();
    }

    public DeliveryNoteHasConsumablesOnQuote getById(Long id) {
        return deliveryNoteHasConsumablesOnQuoteRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Invalid id"));
    }

    public DeliveryNoteHasConsumablesOnQuote update(Long id, DeliveryNoteHasConsumablesOnQuote deliveryNoteHasConsumablesOnQuote) {
       return   deliveryNoteHasConsumablesOnQuoteRepository.findById(id)
               .map(entity -> {
                   entity.setDeliveryNote(deliveryNoteHasConsumablesOnQuote.getDeliveryNote());
//                   entity.setConsumableOnQuote(deliveryNoteHasConsumablesOnQuote.getConsumableOnQuote());
                   return deliveryNoteHasConsumablesOnQuoteRepository.save(entity);
               }).
               orElseThrow(()-> new IllegalArgumentException("Invalid id"));
    }

    public void delete(Long id) {
        deliveryNoteHasConsumablesOnQuoteRepository.deleteById(id);
    }
}

