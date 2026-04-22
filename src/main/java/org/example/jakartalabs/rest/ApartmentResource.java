package org.example.jakartalabs.rest;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.jakartalabs.ejb.ApartmentService;
import org.example.jakartalabs.model.Apartment;

import java.util.Map;
import java.util.Optional;

@Path("/apartments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ApartmentResource {

    @EJB
    private ApartmentService apartmentService;

    @GET
    public Response getAll(
            @QueryParam("rooms")    Integer rooms,
            @QueryParam("maxPrice") Integer maxPrice,
            @QueryParam("page")     @DefaultValue("0")  int page,
            @QueryParam("size")     @DefaultValue("10") int size
    ) {
        Map<String, Object> result = apartmentService.search(rooms, maxPrice, page, size);
        return Response.ok(result).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") int id) {
        Optional<Apartment> found = apartmentService.findById(id);
        return found.isPresent()
                ? Response.ok(found.get()).build()
                : notFound(id);
    }

    @POST
    public Response create(Apartment apt) {
        if (apt == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Тіло запиту порожнє"))
                    .build();
        }
        Apartment created = apartmentService.create(apt);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") int id, Apartment updated) {
        if (updated == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Тіло запиту порожнє"))
                    .build();
        }
        Optional<Apartment> result = apartmentService.update(id, updated);
        return result.isPresent()
                ? Response.ok(result.get()).build()
                : notFound(id);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) {
        return apartmentService.delete(id)
                ? Response.noContent().build()
                : notFound(id);
    }

    private Response notFound(int id) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of(
                        "status", 404,
                        "error",  "Квартиру з id=" + id + " не знайдено"
                ))
                .build();
    }
}
