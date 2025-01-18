package com.axe.returned_products;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReturnedProductsService {
    private  final ReturnedProductsRepository returnedProductsRepository;
    ReturnedProductsService(ReturnedProductsRepository returnedProductsRepository){
        this.returnedProductsRepository = returnedProductsRepository;
    }

    public ReturnedProducts createReturnedProduct(ReturnedProducts returnedProduct) {
        return returnedProductsRepository.save(returnedProduct);
    }

    public List<ReturnedProducts> getReturnedProduct() {
        return returnedProductsRepository.findAll();
    }


    public ReturnedProducts getReturnedProductById(Long id) {
      return returnedProductsRepository.findById(id)
              .orElseThrow(() -> new RuntimeException("Returned Product not found"));
    }

    public ReturnedProducts updateReturnedProducts(Long id, ReturnedProducts returnedProducts){
        return returnedProductsRepository.findById(id)
                .map(returnedProductMap ->
                {
                    returnedProductMap.setReason(returnedProducts.getReason());
                    returnedProductMap.setReturnedDate(returnedProducts.getReturnedDate());
//                    returnedProductMap.setInventory(returnedProducts.getInventory());
//                    returnedProductMap.setDeliveryNote(returnedProducts.getDeliveryNote());

                    returnedProductMap.setCreditNote(returnedProducts.getCreditNote());
                    return returnedProductsRepository.save(returnedProductMap);
                })
                .orElseThrow(()->new RuntimeException("Returned Product not found"));
    }

    public void deleteReturnedProduct(Long id) {
        if (returnedProductsRepository.existsById(id)) {
            returnedProductsRepository.deleteById(id);
        } else {
            throw new RuntimeException("Returned Product not found");
        }
    }

//    public List<ReturnedProducts> getReturnedProductsByDeliveryNoteId(Long deliveryNoteId) {
//        return returnedProductsRepository.findByDeliveryNoteId(deliveryNoteId);
//    }
//
//    public List<ReturnedProducts> getReturnedProductsByCreditNoteId(Long creditNoteId) {
//        return returnedProductsRepository.findByCreditNoteId(creditNoteId);
//    }
}
