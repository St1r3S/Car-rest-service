package ua.foxminded.carrestservice.service;

import ua.foxminded.carrestservice.model.Car;

import java.util.List;

public interface CarService extends CrudService<Car, Long> {

    List<Car> findCarsByFilters(String manufacturer, String model, Integer minYear, Integer maxYear, String category);
}
