package com.example.drinkshopserver.Models;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Drink {

    @SerializedName("ID")
    private int id;
    @SerializedName("Name")
    private String name;
    @SerializedName("Link")
    private String link;
    @SerializedName("Price")
    private String price;
    @SerializedName("MenuID")
    private String menuID;

    public Drink(int id, String name, String link, String price, String menuID) {
        this.id = id;
        this.name = name;
        this.link = link;
        this.price = price;
        this.menuID = menuID;
    }

    public Drink() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMenuID() {
        return menuID;
    }

    public void setMenuID(String menuID) {
        this.menuID = menuID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Drink drink = (Drink) o;
        return
                name.equals(drink.name) &&
                        link.equals(drink.link) &&
                        price.equals(drink.price) &&
                        menuID.equals(drink.menuID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, link, price, menuID);
    }
}
