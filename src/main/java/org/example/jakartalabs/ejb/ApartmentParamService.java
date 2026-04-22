package org.example.jakartalabs.ejb;

import jakarta.ejb.Local;
import org.example.jakartalabs.model.Apartment;
import org.example.jakartalabs.model.ApartmentParam;

import java.util.List;

@Local
public interface ApartmentParamService {

    void addParams(Apartment apt, List<ApartmentParam> params);

    void addParamsOrFail(Apartment apt, List<ApartmentParam> params, boolean simulateFailure);
}
