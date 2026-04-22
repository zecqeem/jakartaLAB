package org.example.jakartalabs.rest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.jakartalabs.model.Apartment;
import org.example.jakartalabs.model.MockDatabase;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Path("/apartments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ApartmentResource {

    // Лічильник ID для нових квартир (потокобезпечний)
    private static final AtomicInteger idCounter =
            new AtomicInteger(MockDatabase.apartments.size());

    // Валідатор Bean Validation (ручна ініціалізація без CDI)
    private static final Validator validator;
    static {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @GET
    public Response getAll(
            @QueryParam("rooms")    Integer rooms,
            @QueryParam("maxPrice") Integer maxPrice,
            @QueryParam("page")     @DefaultValue("0") int page,
            @QueryParam("size")     @DefaultValue("10") int size
    ) {
        List<Apartment> filtered = MockDatabase.apartments.stream()
                .filter(a -> rooms    == null || a.getRooms() == rooms)
                .filter(a -> maxPrice == null || a.getPrice() <= maxPrice)
                .collect(Collectors.toList());

        // Пагінація
        int total = filtered.size();
        int fromIndex = Math.min(page * size, total);
        int toIndex   = Math.min(fromIndex + size, total);
        List<Apartment> page_data = filtered.subList(fromIndex, toIndex);

        return Response.ok()
                .entity(java.util.Map.of(
                        "total",   total,
                        "page",    page,
                        "size",    size,
                        "results", page_data
                ))
                .build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") int id) {
        Optional<Apartment> found = MockDatabase.apartments.stream()
                .filter(a -> a.getId() == id)
                .findFirst();

        if (found.isEmpty()) {
            return notFound(id);
        }
        return Response.ok(found.get()).build();
    }

    @POST
    public Response create(Apartment apt) {
        Response validation = validate(apt);
        if (validation != null) return validation;

        int newId = idCounter.incrementAndGet();
        apt.setId(newId);
        MockDatabase.apartments.add(apt);

        return Response.status(Response.Status.CREATED).entity(apt).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") int id, Apartment updated) {
        Response validation = validate(updated);
        if (validation != null) return validation;

        Optional<Apartment> existing = MockDatabase.apartments.stream()
                .filter(a -> a.getId() == id)
                .findFirst();

        if (existing.isEmpty()) {
            return notFound(id);
        }

        Apartment apt = existing.get();
        apt.setTitle(updated.getTitle());
        apt.setRooms(updated.getRooms());
        apt.setPrice(updated.getPrice());
        apt.setDescription(updated.getDescription());

        return Response.ok(apt).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) {
        boolean removed = MockDatabase.apartments.removeIf(a -> a.getId() == id);
        if (!removed) {
            return notFound(id);
        }
        return Response.noContent().build();
    }

    /** Запускає Bean Validation і повертає 400 якщо є порушення, або null якщо все ок */
    private Response validate(Apartment apt) {
        if (apt == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(java.util.Map.of("error", "Тіло запиту порожнє"))
                    .build();
        }
        Set<ConstraintViolation<Apartment>> violations = validator.validate(apt);
        if (!violations.isEmpty()) {
            List<String> messages = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .sorted()
                    .collect(Collectors.toList());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(java.util.Map.of(
                            "status",   400,
                            "error",    "Помилка валідації",
                            "messages", messages
                    ))
                    .build();
        }
        return null;
    }

    /** Стандартна 404-відповідь */
    private Response notFound(int id) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(java.util.Map.of(
                        "status", 404,
                        "error",  "Квартиру з id=" + id + " не знайдено"
                ))
                .build();
    }
}
