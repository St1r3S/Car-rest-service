package ua.foxminded.carrestservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.foxminded.carrestservice.model.Car;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long>, PagingAndSortingRepository<Car, Long> {

    @Query("SELECT DISTINCT c FROM Car c " +
            "WHERE (:manufacturer IS NULL OR c.manufacturer.make = :manufacturer) " +
            "AND (:model IS NULL OR c.model = :model) " +
            "AND (:minYear IS NULL OR c.manufactureYear >= :minYear) " +
            "AND (:maxYear IS NULL OR c.manufactureYear <= :maxYear) " +
            "AND (:category IS NULL OR :category IN (SELECT cat.category FROM c.categories cat))")
    List<Car> findCarsByFilters(
            @Param("manufacturer") String manufacturer,
            @Param("model") String model,
            @Param("minYear") Integer minYear,
            @Param("maxYear") Integer maxYear,
            @Param("category") String category
    );
}
