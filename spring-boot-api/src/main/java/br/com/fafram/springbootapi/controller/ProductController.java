package br.com.fafram.springbootapi.controller;

import br.com.fafram.springbootapi.entity.Product;
import br.com.fafram.springbootapi.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    /* delega ao Spring Boot a inicialização do objeto
    injeção de dependência
     */
    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    /* ResponseEntity representa a response HTTP, incluindo headers,
    body e status */
    @GetMapping("/{id}")
    public ResponseEntity<Product> findProductById(@PathVariable(value = "id") long id) {
        // evitar exceções quando não ter valor
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            return new ResponseEntity<Product>(product.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public Product saveProduct(@Validated @RequestBody Product product) {
        return productRepository.save(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable(value = "id") long id,
                                                 @Validated @RequestBody Product newProduct) {
        Optional<Product> oldProduct = productRepository.findById(id);
        if (oldProduct.isPresent()) {
            Product product = oldProduct.get();
            product.setDescription(newProduct.getDescription());
            product.setPrice(newProduct.getPrice());
            productRepository.save(product);
            return new ResponseEntity<Product>(product, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable(value = "id") long id) {
        Optional<Product> product = productRepository.findById(id);
        if(product.isPresent()) {
            productRepository.delete(product.get());
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}