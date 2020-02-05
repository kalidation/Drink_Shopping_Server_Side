package com.example.drinkshopserver.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DeleteCategoryResponse {

    @SerializedName("error")
    private String error;
    @SerializedName("menus")
    private Category category;

    public DeleteCategoryResponse(String error, Category category) {
        this.error = error;
        this.category = category;
    }

    public DeleteCategoryResponse() {
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
