package org.example.jakartalabs.repository;

import jakarta.ejb.Stateless;
import org.example.jakartalabs.model.Apartment;
import org.example.jakartalabs.model.MockDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Stateless
public class ApartmentRepositoryStub implements ApartmentRepository {

    private static final AtomicInteger idCounter =
            new AtomicInteger(MockDatabase.apartments.size());

    @Override
    public List<Apartment> findAll() {
        return new ArrayList<>(MockDatabase.apartments);
    }

    @Override
    public Optional<Apartment> findById(int id) {
        return MockDatabase.apartments.stream()
                .filter(a -> a.getId() == id)
                .findFirst();
    }

    @Override
    public Apartment save(Apartment apt) {
        apt.setId(idCounter.incrementAndGet());
        MockDatabase.apartments.add(apt);
        return apt;
    }

    @Override
    public boolean deleteById(int id) {
        return MockDatabase.apartments.removeIf(a -> a.getId() == id);
    }
}
