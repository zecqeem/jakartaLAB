package org.example.jakartalabs.model;

import java.util.ArrayList;
import java.util.List;

public class MockDatabase {
  public static final List<Apartment> apartments = new ArrayList<>();

  static {
    apartments.add(new Apartment(1, "Квартира в центрі", 2, 15000, "Гарний вид."));
    apartments.add(new Apartment(2, "Студія на околиці", 1, 8000, "Дешево та сердито."));
    apartments.add(new Apartment(3, "Елітні апартаменти", 3, 35000, "<script>alert('XSS Attack!');</script>"));
  }
}
