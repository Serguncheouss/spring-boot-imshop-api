package my.app.imshop.service;

import my.app.imshop.model.Category;
import my.app.imshop.model.dto.CategoryDto;
import my.app.imshop.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    public void create(Category category) {
        categoryRepository.save(category);
    }

    @Nullable
    public Category get(long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    public boolean update(long id, Category newCategory) {
        if (categoryRepository.existsById(id)) {
            newCategory.setId(id);
            categoryRepository.save(newCategory);
            return true;
        }

        return false;
    }

    public boolean delete(long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);

            return true;
        }

        return false;
    }

    public static CategoryDto convertToDto(Category category) {
        return category == null
                ? null
                : new CategoryDto(
                        category.getId(),
                        category.getTitle(),
                        category.getGoods().stream()
                                .map(ProductService::convertToDto)
                                .collect(Collectors.toList())
        );
    }
}
