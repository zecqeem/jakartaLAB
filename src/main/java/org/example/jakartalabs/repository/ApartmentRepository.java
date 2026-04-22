package org.example.jakartalabs.repository;

import jakarta.ejb.Local;
import org.example.jakartalabs.model.Apartment;

import java.util.List;
import java.util.Optional;

@Local
public interface ApartmentRepository {

    List<Apartment> findAll();

    Optional<Apartment> findById(int id);

    Apartment save(Apartment apt);

    boolean deleteById(int id);
}
