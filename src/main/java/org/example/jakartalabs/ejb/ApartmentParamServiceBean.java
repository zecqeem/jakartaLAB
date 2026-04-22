package org.example.jakartalabs.ejb;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.jakartalabs.model.Apartment;
import org.example.jakartalabs.model.ApartmentParam;

import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ApartmentParamServiceBean implements ApartmentParamService {

    @PersistenceContext(unitName = "jakartalabsPU")
    private EntityManager em;

    @Override
    public void addParams(Apartment apt, List<ApartmentParam> params) {
        if (params == null || params.isEmpty()) return;
        for (ApartmentParam p : params) {
            p.setApartment(apt);
            em.persist(p);
        }
    }

    @Override
    public void addParamsOrFail(Apartment apt, List<ApartmentParam> params, boolean simulateFailure) {
        addParams(apt, params);
        if (simulateFailure) {
            throw new BusinessException("Симуляція збою в ApartmentParamService — відкат усієї транзакції");
        }
    }
}
