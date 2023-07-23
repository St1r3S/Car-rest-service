package ua.foxminded.carrestservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "model", nullable = false)
    private String model;
    @Column(name = "manufacture_year", nullable = false)
    private Integer manufactureYear;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manufacturer_id", nullable = false)
    private Manufacturer manufacturer;
    @ManyToMany
    @JoinTable(
            name = "cars_categories",
            joinColumns = @JoinColumn(name = "car_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories;

    public Car(Long id, String model, Integer manufactureYear, Manufacturer manufacturer) {
        this.id = id;
        this.model = model;
        this.manufactureYear = manufactureYear;
        this.manufacturer = manufacturer;
    }

    public Car(String model, Integer manufactureYear, Manufacturer manufacturer) {
        this(null, model, manufactureYear, manufacturer);
    }
}
