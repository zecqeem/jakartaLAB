package org.example.jakartalabs.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.example.jakartalabs.validation.ValidApartment;
import org.example.jakartalabs.validation.ValidPrice;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "apartments")
@ValidApartment
public class Apartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Назва квартири не може бути порожньою")
    @Size(max = 100, message = "Назва не може бути довшою за 100 символів")
    @Column(nullable = false, length = 100)
    private String title;

    @Min(value = 1, message = "Кількість кімнат має бути не менше 1")
    @Max(value = 10, message = "Кількість кімнат не може перевищувати 10")
    @Column(nullable = false)
    private int rooms;

    @Min(value = 0, message = "Ціна не може бути від'ємною")
    @ValidPrice
    @Column(nullable = false)
    private int price;

    @Size(max = 500, message = "Опис не може бути довшим за 500 символів")
    @Column(length = 500)
    private String description;

    @OneToMany(mappedBy = "apartment", cascade = CascadeType.ALL,
               fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ApartmentParam> params = new ArrayList<>();

    public Apartment() {}

    public Apartment(int id, String title, int rooms, int price, String description) {
        this.id = id;
        this.title = title;
        this.rooms = rooms;
        this.price = price;
        this.description = description;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getRooms() { return rooms; }
    public void setRooms(int rooms) { this.rooms = rooms; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<ApartmentParam> getParams() { return params; }
    public void setParams(List<ApartmentParam> params) { this.params = params; }

    @Override
    public String toString() {
        return "Apartment{id=" + id + ", title='" + title + "', rooms=" + rooms +
               ", price=" + price + ", description='" + description + "'}";
    }
}