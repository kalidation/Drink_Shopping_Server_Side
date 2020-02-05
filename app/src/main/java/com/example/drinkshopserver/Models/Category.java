package com.example.drinkshopserver.Models;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Category {
    @SerializedName("ID")
    private int iD;
    @SerializedName("Name")
    private String name;
    @SerializedName("Link")
    private String link;

    public Category(int ID , String name, String link ) {
        this.name = name;
        this.link = link;
        this.iD = ID ;
    }

    public Category() {
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

    public int getiD() {
        return iD;
    }

    public void setiD(int iD) {
        this.iD = iD;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(name, category.name) &&
                Objects.equals(link, category.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, link);
    }
}
