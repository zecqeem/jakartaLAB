package org.example.jakartalabs.ejb;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.example.jakartalabs.model.Apartment;
import org.example.jakartalabs.repository.ApartmentRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Stateless
public class ApartmentServiceBean implements ApartmentService {

    @EJB
    private ApartmentRepository repository;

    private static final Validator validator;
    static {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Override
    public Map<String, Object> search(Integer rooms, Integer maxPrice, int page, int size) {
        List<Apartment> filtered = repository.findByCriteria(rooms, maxPrice);

        int total     = filtered.size();
        int fromIndex = Math.min(page * size, total);
        int toIndex   = Math.min(fromIndex + size, total);

        return Map.of(
                "total",   total,
                "page",    page,
                "size",    size,
                "results", filtered.subList(fromIndex, toIndex)
        );
    }

    @Override
    public Optional<Apartment> findById(int id) {
        return repository.findById(id);
    }

    @Override
    public Apartment create(Apartment apt) {
        validateOrThrow(apt);
        return repository.save(apt);
    }

    @Override
    public Optional<Apartment> update(int id, Apartment updated) {
        validateOrThrow(updated);

        Optional<Apartment> existing = repository.findById(id);
        existing.ifPresent(apt -> {
            apt.setTitle(updated.getTitle());
            apt.setRooms(updated.getRooms());
            apt.setPrice(updated.getPrice());
            apt.setDescription(updated.getDescription());
            repository.update(apt);
        });
        return existing;
    }

    @Override
    public boolean delete(int id) {
        return repository.deleteById(id);
    }

    private void validateOrThrow(Apartment apt) {
        Set<ConstraintViolation<Apartment>> violations = validator.validate(apt);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
