package ua.foxminded.carrestservice.service;

import ua.foxminded.carrestservice.model.Manufacturer;

public interface ManufacturerService extends CrudService<Manufacturer, Long> {

    Manufacturer findByMake(String make);
}
