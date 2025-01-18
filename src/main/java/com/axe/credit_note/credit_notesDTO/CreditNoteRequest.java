package com.axe.credit_note.credit_notesDTO;

import com.axe.returned_products.ReturnedProducts;

import java.time.LocalDate;
import java.util.List;

public record CreditNoteRequest (LocalDate dateCreated,
                                 Long deliveryNoteId,
                                 Long quoteId,
                                 Long projectId,
                                 List<ReturnedProducts> returnedProducts
                                 ){}
