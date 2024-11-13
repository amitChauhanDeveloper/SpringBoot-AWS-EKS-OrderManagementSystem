package com.amitchauhan.ordermanagement.productservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.amitchauhan.ordermanagement.productservice.dto.ProductAvailabilityResponse;
import com.amitchauhan.ordermanagement.productservice.dto.ProductDTO;
import com.amitchauhan.ordermanagement.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
   public List<ProductDTO> getAllProducts() {
       return productService.getAllProducts();
   }

   @GetMapping("/{id}")
   public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
       ProductDTO productDTO = productService.getProductById(id);
       return ResponseEntity.ok(productDTO);
   }

    @PostMapping
    public ResponseEntity<List<ProductDTO>> createProduct(@RequestBody List<ProductDTO> productDTO) {
        List<ProductDTO> createdProduct = productService.createProduct(productDTO);
        return ResponseEntity.ok(createdProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
        return ResponseEntity.ok(updatedProduct);
    }

    
       @DeleteMapping("/{id}")
       public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
           productService.deleteProduct(id);
           return ResponseEntity.ok("Product deleted successfully");
       }


    @PostMapping("/availability")
    public ResponseEntity<ProductAvailabilityResponse> checkProductAvailability(@RequestBody List<ProductDTO> products) {
        ProductAvailabilityResponse availabilityList = productService.checkProductAvailability(products);
        ResponseEntity<ProductAvailabilityResponse> availabilityResponse =  ResponseEntity.status(HttpStatus.OK).body(availabilityList);
        return availabilityResponse;
    }
}
