package org.example.jakartalabs.repository;

import org.example.jakartalabs.model.Apartment;
import org.example.jakartalabs.model.MockDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
    public List<Apartment> findByCriteria(Integer rooms, Integer maxPrice) {
        return MockDatabase.apartments.stream()
                .filter(a -> rooms    == null || a.getRooms() == rooms)
                .filter(a -> maxPrice == null || a.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    @Override
    public Apartment save(Apartment apt) {
        apt.setId(idCounter.incrementAndGet());
        MockDatabase.apartments.add(apt);
        return apt;
    }

    @Override
    public Apartment update(Apartment apt) {
        MockDatabase.apartments.stream()
                .filter(a -> a.getId() == apt.getId())
                .findFirst()
                .ifPresent(existing -> {
                    existing.setTitle(apt.getTitle());
                    existing.setRooms(apt.getRooms());
                    existing.setPrice(apt.getPrice());
                    existing.setDescription(apt.getDescription());
                });
        return apt;
    }

    @Override
    public boolean deleteById(int id) {
        return MockDatabase.apartments.removeIf(a -> a.getId() == id);
    }
}
