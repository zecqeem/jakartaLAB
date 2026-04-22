package org.example.jakartalabs.ejb;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.example.jakartalabs.model.Apartment;
import org.example.jakartalabs.model.ApartmentParam;
import org.example.jakartalabs.repository.ApartmentRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ApartmentServiceBean implements ApartmentService {

    @EJB
    private ApartmentRepository repository;

    @EJB
    private ApartmentParamService paramService;

    private static final Validator validator;
    static {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
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
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
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

    @Override
    public Apartment createWithParams(Apartment apt, List<ApartmentParam> params, boolean simulateFailure) {
        validateOrThrow(apt);
        Apartment created = repository.save(apt);
        paramService.addParamsOrFail(created, params, simulateFailure);
        return created;
    }

    @Override
    public int bulkUpdatePrice(Integer rooms, int newPrice) {
        if (newPrice % 100 != 0) {
            throw new BusinessException(
                    "Ціна " + newPrice + " не кратна 100 — відкат усіх змін");
        }
        List<Apartment> targets = repository.findByCriteria(rooms, null);
        for (Apartment apt : targets) {
            apt.setPrice(newPrice);
            repository.update(apt);
        }
        return targets.size();
    }

    private void validateOrThrow(Apartment apt) {
        Set<ConstraintViolation<Apartment>> violations = validator.validate(apt);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
