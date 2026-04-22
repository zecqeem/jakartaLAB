package org.example.jakartalabs.model;

public class BulkPriceRequest {

    private Integer rooms;
    private int newPrice;

    public BulkPriceRequest() {}

    public Integer getRooms() { return rooms; }
    public void setRooms(Integer rooms) { this.rooms = rooms; }

    public int getNewPrice() { return newPrice; }
    public void setNewPrice(int newPrice) { this.newPrice = newPrice; }
}
