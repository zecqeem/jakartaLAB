package org.example.jakartalabs.repository;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.example.jakartalabs.model.Apartment;

import java.util.List;
import java.util.Optional;

@Stateless
public class ApartmentRepositoryJpa implements ApartmentRepository {

    @PersistenceContext(unitName = "jakartalabsPU")
    private EntityManager em;

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Apartment> findAll() {
        return em.createQuery("SELECT a FROM Apartment a", Apartment.class)
                 .getResultList();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Optional<Apartment> findById(int id) {
        return Optional.ofNullable(em.find(Apartment.class, id));
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Apartment> findByCriteria(Integer rooms, Integer maxPrice) {
        TypedQuery<Apartment> query = em.createQuery(
                "SELECT a FROM Apartment a " +
                "WHERE (:rooms IS NULL OR a.rooms = :rooms) " +
                "AND (:maxPrice IS NULL OR a.price <= :maxPrice)",
                Apartment.class
        );
        query.setParameter("rooms",    rooms);
        query.setParameter("maxPrice", maxPrice);
        return query.getResultList();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public Apartment save(Apartment apt) {
        em.persist(apt);
        em.flush();
        return apt;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public Apartment update(Apartment apt) {
        return em.merge(apt);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public boolean deleteById(int id) {
        Apartment apt = em.find(Apartment.class, id);
        if (apt == null) return false;
        em.remove(apt);
        return true;
    }
}
