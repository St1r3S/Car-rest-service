package ua.foxminded.carrestservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "category", unique = true, nullable = false)
    private String category;
    @ManyToMany(mappedBy = "categories")
    private List<Car> cars;

    public Category(Long id, String category) {
        this.id = id;
        this.category = category;
    }

    public Category(String category) {
        this(null, category);
    }
}
