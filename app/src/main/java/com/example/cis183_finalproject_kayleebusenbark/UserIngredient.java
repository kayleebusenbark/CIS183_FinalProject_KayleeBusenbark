package com.example.cis183_finalproject_kayleebusenbark;

import java.io.Serializable;

public class UserIngredient implements Serializable
{
    private int userIngredientId;
    private int ingredientId;
    private float quantity;
    private int measurementId;

    public UserIngredient()
    {

    }

    public UserIngredient(int ui_id, int i_id, float q, int m_id)
    {
        userIngredientId = ui_id;
        ingredientId = i_id;
        quantity = q;
        measurementId = m_id;
    }

    //GETTERS
    public int getUserIngredientId() {
        return userIngredientId;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public float getQuantity() {
        return quantity;
    }

    public int getMeasurementId() {
        return measurementId;
    }

    //SETTERS
    public void setUserIngredientId(int userIngredientId) {
        this.userIngredientId = userIngredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public void setMeasurementId(int measurementId) {
        this.measurementId = measurementId;
    }
}
