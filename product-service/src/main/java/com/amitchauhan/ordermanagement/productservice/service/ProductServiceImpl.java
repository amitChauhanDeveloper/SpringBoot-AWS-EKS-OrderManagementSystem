package com.amitchauhan.ordermanagement.productservice.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.amitchauhan.ordermanagement.productservice.dto.ProductAvailability;
import com.amitchauhan.ordermanagement.productservice.dto.ProductAvailabilityResponse;
import com.amitchauhan.ordermanagement.productservice.dto.ProductDTO;
import com.amitchauhan.ordermanagement.productservice.entity.Product;
import com.amitchauhan.ordermanagement.productservice.exception.ResourceNotFoundException;
import com.amitchauhan.ordermanagement.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

  @Override
  public void reduceProductQuantity(String skuCode, int quantity) {

    Optional<Product> optionalProduct = productRepository.findBySkuCode(skuCode);

    //check if product is present
    if (optionalProduct.isPresent()) {

    Product product = optionalProduct.get(); //Get the product object
      
    //Decrease the product's quantity by the specified amount
    product.setQuantity(product.getQuantity() - quantity);

    //Save the updated product to the database
    productRepository.save(product); // Persist the updated quantity in the database
    
  }else{
      throw new ResourceNotFoundException("Product","Sky Code",skuCode);
    }
    
  }

  @Override
  public void increaseProductQuantity(String skuCode, int quantity) {

      Optional<Product> optionalProduct = productRepository.findBySkuCode(skuCode);

      //check if product is present
      if (optionalProduct.isPresent()) {

      Product product = optionalProduct.get(); //Get the product object
        
      //Increase the product's quantity by the specified amount
      product.setQuantity(product.getQuantity() + quantity);

      //Save the updated product to the database
      productRepository.save(product); // Persist the updated quantity in the database
      
    }else{
        throw new ResourceNotFoundException("Product","Sky Code",skuCode);
      }
   
  }

  @Override
  public List<ProductDTO> createProduct(List<ProductDTO> productDTO) {

    List<Product> product = productDTO.stream().map(this::convertToEntity).collect(Collectors.toList());

    List<Product> savedProducts = productRepository.saveAll(product); //saveAll query insert single single record

    return savedProducts.stream().map(this::convertToDto).collect(Collectors.toList());

  }

  @Override
  public ProductDTO updateProduct(Long id, ProductDTO productDTO) {

    Product product = productRepository.findById(id)
    .orElseThrow(() -> new ResourceNotFoundException("Product", "product id", id));

    product.setName(productDTO.getName());
    product.setDescription(productDTO.getDescription());
    product.setPrice(productDTO.getPrice());
    product.setQuantity(productDTO.getQuantity());
    product.setSkuCode(productDTO.getSkuCode());

    // Save the updated product to the database
    Product updatedProduct = productRepository.save(product);

    return convertToDto(updatedProduct);

  }

  @Override
  public ProductAvailabilityResponse checkProductAvailability(List<ProductDTO> productDTOs) {
        ProductAvailabilityResponse productAvailabilityResponse = new ProductAvailabilityResponse();
        productAvailabilityResponse.setProductAvailabilityList(
            productDTOs.stream().map(productDTO -> {
                Product product = productRepository.findBySkuCode(productDTO.getSkuCode())
                    .orElseThrow(() -> new ResourceNotFoundException("Product","SKU code",
                    productDTO.getSkuCode()));
                boolean available = product.getQuantity() >= productDTO.getQuantity();
                return new ProductAvailability(product.getSkuCode(), available);
            }).collect(Collectors.toList())
        );
        return productAvailabilityResponse;
    }

  @Override
  public void deleteProduct(Long id) {

    productRepository.findById(id)
    .orElseThrow(() -> new ResourceNotFoundException("Product","product id",id));

    productRepository.deleteById(id);
    
  }

  @Override
  public ProductDTO getProductById(Long id) {

    Product product = productRepository.findById(id).orElseThrow(() -> 
    new ResourceNotFoundException("Product","product id",id));
    return convertToDto(product);
  }

  @Override
  public List<ProductDTO> getAllProducts() {
    return productRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
  }

  private ProductDTO convertToDto(Product product) {
    return ProductDTO.builder()
            .id(product.getId())
            .name(product.getName())
            .skuCode(product.getSkuCode())
            .description(product.getDescription())
            .quantity(product.getQuantity())
            .price(product.getPrice())
            .build();
  }

  private Product convertToEntity(ProductDTO productDTO) {
    return Product.builder()
            .name(productDTO.getName())
            .skuCode(productDTO.getSkuCode())
            .description(productDTO.getDescription())
            .quantity(productDTO.getQuantity())
            .price(productDTO.getPrice())
            .build();
  }
  
}
