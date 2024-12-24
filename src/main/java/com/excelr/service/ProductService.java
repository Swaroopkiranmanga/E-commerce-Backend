package com.excelr.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.excelr.model.Product;
import com.excelr.model.Productdto;
import com.excelr.model.Subcategory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.excelr.repository.ProductRepository;
import com.excelr.repository.SubcategoryRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final SubcategoryRepository subcategoryRepository;
    
    public ProductService(ProductRepository productRepository, SubcategoryRepository subcategoryRepository) {
        this.productRepository = productRepository;
		this.subcategoryRepository = subcategoryRepository;
    }

    public Page<Productdto> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
        		.map(this::mapProductToProductDTO);
    }

    public Productdto getProductById(long id) {
    	Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return mapProductToProductDTO(product);
    }

    public Productdto createProduct(Productdto productDto) {
    	Subcategory subcategory = subcategoryRepository.findById(productDto.getSubcategoryId())
                .orElseThrow(() -> new RuntimeException( 
                    "Subcategory not found with id: " + productDto.getSubcategoryId()
                ));
            Product product = new Product();
            product.setName(productDto.getName());
            product.setPrice(productDto.getPrice());
            product.setDescription(productDto.getDescription());
            product.setBrand(productDto.getBrand());
            product.setImage(productDto.getImage());
            product.setRating(productDto.getRating());
            product.setQuantity(productDto.getQuantity());
            product.setSubcategory(subcategory);
            Product savedProduct = productRepository.save(product);
            return convertToDto(savedProduct);
        }

    public Productdto updateProduct(long id, Productdto productDto) {
    	Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                    "Product not found with id: " + id
                ));
            Subcategory subcategory = null;
            if (productDto.getSubcategoryId() != null) {
                subcategory = subcategoryRepository.findById(productDto.getSubcategoryId())
                    .orElseThrow(() -> new RuntimeException(
                        "Subcategory not found with id: " + productDto.getSubcategoryId()
                    ));
            }
            existingProduct.setName(productDto.getName() != null ? productDto.getName() : existingProduct.getName());
            existingProduct.setPrice(productDto.getPrice() != 0 ? productDto.getPrice() : existingProduct.getPrice());
            existingProduct.setDescription(productDto.getDescription() != null ? productDto.getDescription() : existingProduct.getDescription());
            existingProduct.setBrand(productDto.getBrand() != null ? productDto.getBrand() : existingProduct.getBrand());
            existingProduct.setImage(productDto.getImage() != null ? productDto.getImage() : existingProduct.getImage());
            existingProduct.setRating(productDto.getRating() != null ? productDto.getRating() : existingProduct.getRating());
            existingProduct.setQuantity(productDto.getQuantity() != null ? productDto.getQuantity() : existingProduct.getQuantity());
            
            if (subcategory != null) {
                existingProduct.setSubcategory(subcategory);
            }
            Product updatedProduct = productRepository.save(existingProduct);

            // Convert updated entity to DTO
            return convertToDto(updatedProduct);
    }



    public void deleteProduct(long id) {
        productRepository.deleteById(id);
    }

	public List<Productdto> searchProducts(String keyword) {
		List<Product> products=productRepository.searchProducts(keyword);
		return products.stream()
				.map(this::mapProductToProductDTO) 
				.collect(Collectors.toList());
	}
    
	private Productdto mapProductToProductDTO(Product product) {
	    return new Productdto(
	            product.getId(),
	            product.getName(),
	            product.getPrice(),
	            product.getDescription(),
	            product.getSubcategory() != null ? product.getSubcategory().getId() : null, 
	            product.getSubcategory() != null ? product.getSubcategory().getName() : null, 
	            product.getBrand(),
	            product.getImage(),
	            product.getRating(),
	            product.getQuantity()
	    );
	}
	
	private Productdto convertToDto(Product product) {
        Productdto dto = new Productdto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setDescription(product.getDescription());
        dto.setSubcategoryId(product.getSubcategory().getId());
        dto.setSubcategoryName(product.getSubcategory().getName()); 
        dto.setBrand(product.getBrand());
        dto.setImage(product.getImage());
        dto.setRating(product.getRating());
        dto.setQuantity(product.getQuantity());
        return dto;
    }

}