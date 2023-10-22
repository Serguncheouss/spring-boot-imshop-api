package my.app.imshop.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CategoryDto {
    private Long id;
    private String title;
    private List<ProductDto> goods;
}
