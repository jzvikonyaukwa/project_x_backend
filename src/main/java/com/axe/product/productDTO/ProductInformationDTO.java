package com.axe.product.productDTO;

import com.axe.product.Product;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ProductInformationDTO {
    String clientName;
    String projectName;
    String quoteStatus;
    LocalDate quoteAcceptedDate;
    Product product;
}
