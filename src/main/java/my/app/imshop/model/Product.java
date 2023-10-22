package my.app.imshop.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "goods")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private Double price;
    private Integer count;
    @ManyToOne
    @JoinColumn(name = "category_id", nullable=false)
    private Category category;
}
