package org.example.jakartalabs.model;

import java.util.List;

public class CreateWithParamsRequest {

    private Apartment apartment;
    private List<ApartmentParam> params;
    private boolean simulateFailure;

    public CreateWithParamsRequest() {}

    public Apartment getApartment() { return apartment; }
    public void setApartment(Apartment apartment) { this.apartment = apartment; }

    public List<ApartmentParam> getParams() { return params; }
    public void setParams(List<ApartmentParam> params) { this.params = params; }

    public boolean isSimulateFailure() { return simulateFailure; }
    public void setSimulateFailure(boolean simulateFailure) { this.simulateFailure = simulateFailure; }
}
