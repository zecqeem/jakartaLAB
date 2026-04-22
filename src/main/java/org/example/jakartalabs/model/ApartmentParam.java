package org.example.jakartalabs.model;

import jakarta.persistence.*;

@Entity
@Table(name = "apartment_params")
public class ApartmentParam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apartment_id", nullable = false)
    private Apartment apartment;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 200)
    private String value;

    public ApartmentParam() {}

    public ApartmentParam(Apartment apartment, String name, String value) {
        this.apartment = apartment;
        this.name = name;
        this.value = value;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Apartment getApartment() { return apartment; }
    public void setApartment(Apartment apartment) { this.apartment = apartment; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
}
