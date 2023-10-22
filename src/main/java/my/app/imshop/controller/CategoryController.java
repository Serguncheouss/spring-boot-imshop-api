package my.app.imshop.controller;

import my.app.imshop.model.Category;
import my.app.imshop.model.dto.CategoryDto;
import my.app.imshop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping({"", "/"})
    public ResponseEntity<?> create(
            @RequestBody Category category
    ) {
        categoryService.create(category);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping({"", "/"})
    public ResponseEntity<List<CategoryDto>> readAll() {
        final List<Category> categories = categoryService.getAll();
        return categories != null && !categories.isEmpty() ?
                new ResponseEntity<>(categories.stream()
                        .map(CategoryService::convertToDto)
                        .collect(Collectors.toList()), HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<CategoryDto> read(
            @PathVariable(name="id") int id
    ) {
        final Category category = categoryService.get(id);
        return category != null ?
                new ResponseEntity<>(CategoryService.convertToDto(category), HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(
            @PathVariable(name="id") int id,
            @RequestBody Category category
    ) {
        return categoryService.update(id, category) ?
                new ResponseEntity<>(HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(
            @PathVariable(name="id") int id
    ) {
        boolean deleted = categoryService.delete(id);
        return deleted ?
                new ResponseEntity<>(HttpStatus.OK):
                new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}
