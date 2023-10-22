package my.app.imshop.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String title;
    private Double price;
    private Integer count;
    private Long category;
}
