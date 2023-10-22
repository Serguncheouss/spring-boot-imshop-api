package my.app.imshop.controller;

import my.app.imshop.model.Product;
import my.app.imshop.model.dto.ProductDto;
import my.app.imshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/goods")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping({"", "/"})
    public ResponseEntity<?> create(
            @RequestBody Product product
    ) {
        productService.create(product);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping({"", "/"})
    public ResponseEntity<List<ProductDto>> readAll(
            @Nullable @RequestParam Long category_id
    ) {
        List<Product> goods = productService.getAll();
        if (category_id != null) {
            goods = goods.stream()
                    .filter(p -> p.getCategory().getId().equals(category_id))
                    .collect(Collectors.toList());
        }
        return goods != null && !goods.isEmpty() ?
                new ResponseEntity<>(goods.stream()
                        .map(ProductService::convertToDto)
                        .collect(Collectors.toList()), HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductDto> read(
            @PathVariable(name="id") int id
    ) {
        final Product product = productService.get(id);
        return product != null ?
                new ResponseEntity<>(ProductService.convertToDto(product), HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(
            @PathVariable(name="id") int id,
            @RequestBody Product product
    ) {
        return productService.update(id, product) ?
                new ResponseEntity<>(HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(
            @PathVariable(name="id") int id
    ) {
        boolean deleted = productService.delete(id);
        return deleted ?
                new ResponseEntity<>(HttpStatus.OK):
                new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}
