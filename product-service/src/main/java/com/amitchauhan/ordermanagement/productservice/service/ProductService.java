package com.amitchauhan.ordermanagement.productservice.service;

import java.util.List;
import com.amitchauhan.ordermanagement.productservice.dto.ProductAvailabilityResponse;
import com.amitchauhan.ordermanagement.productservice.dto.ProductDTO;

public interface ProductService {

  void reduceProductQuantity(String skuCode, int quantity);

  void increaseProductQuantity(String skuCode, int quantity);

  List<ProductDTO> createProduct(List<ProductDTO> productDTO);

  ProductDTO updateProduct(Long id, ProductDTO productDTO);

  ProductAvailabilityResponse checkProductAvailability(List<ProductDTO> products);

  void deleteProduct(Long id);

  ProductDTO getProductById(Long id);

  List<ProductDTO> getAllProducts();
  
}
