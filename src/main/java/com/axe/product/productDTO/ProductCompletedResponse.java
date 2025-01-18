package com.axe.product.productDTO;

import com.axe.product.Product;
import java.util.List;

public record ProductCompletedResponse(List<Product> productDTO, long totalElements) {
}
