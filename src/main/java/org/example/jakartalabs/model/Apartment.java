package org.example.jakartalabs.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Apartment {
  private int id;
  private String title;
  private int rooms;
  private int price;
  private String description;
}