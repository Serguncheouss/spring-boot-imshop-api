package my.app.imshop.service;

import my.app.imshop.model.Product;
import my.app.imshop.model.dto.ProductDto;
import my.app.imshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public void create(Product product) {
        productRepository.save(product);
    }

    @Nullable
    public Product get(long id) {
        return productRepository.findById(id).orElse(null);
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public boolean update(long id, Product newProduct) {
        if (productRepository.existsById(id)) {
            newProduct.setId(id);
            productRepository.save(newProduct);
            return true;
        }

        return false;
    }

    public boolean delete(long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);

            return true;
        }

        return false;
    }

    public static ProductDto convertToDto(Product product) {
        return product == null
                ? null
                : new ProductDto(
                product.getId(),
                product.getTitle(),
                product.getPrice(),
                product.getCount(),
                product.getCategory().getId()
        );
    }
}
