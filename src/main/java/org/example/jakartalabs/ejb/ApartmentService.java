package org.example.jakartalabs.ejb;

import jakarta.ejb.Local;
import org.example.jakartalabs.model.Apartment;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Local
public interface ApartmentService {

    Map<String, Object> search(Integer rooms, Integer maxPrice, int page, int size);

    Optional<Apartment> findById(int id);

    Apartment create(Apartment apt);

    Optional<Apartment> update(int id, Apartment updated);

    boolean delete(int id);
}
