package com.example.drinkshopserver.Models;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Cart {

    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("link")
    public String link;

    @SerializedName("amount")
    public int amount;

    @SerializedName("price")
    public double price;

    @SerializedName("size")
    public int size;

    @SerializedName("sugar")
    public int sugar ;

    @SerializedName("ice")
    public int ice ;

    @SerializedName("toppingExtras")
    public String toppingExtras;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return id == cart.id &&
                amount == cart.amount &&
                Double.compare(cart.price, price) == 0 &&
                size == cart.size &&
                sugar == cart.sugar &&
                ice == cart.ice &&
                Objects.equals(name, cart.name) &&
                Objects.equals(link, cart.link) &&
                Objects.equals(toppingExtras, cart.toppingExtras);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, link, amount, price, size, sugar, ice, toppingExtras);
    }

}
