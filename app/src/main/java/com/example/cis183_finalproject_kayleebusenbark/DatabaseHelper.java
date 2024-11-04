package com.example.cis183_finalproject_kayleebusenbark;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper
{
    private static final String database_name = "RecipeApp";
    private static final String user_table_name = "Users";
    private static final String ingredients_table_name = "Ingredients";
    private static final String ingredientCategory_table_name = "IngredientCategory";
    private static final String measurement_table_name = "Measurements";
    private static final String recipes_table_name = "Recipes";
    private static final String cuisines_table_name = "Cuisines";
    private static final String dietaryPreference_tabe_name = "DietaryPreference";

    private static final String userIngredients_table_name = "userIngredients";
    private static final String recipeIngredients_table_name = "recipeIngredients";
    private static final String userFavorites_table_name = "userFavorites";

    public DatabaseHelper(Context c)
    {
        super(c, database_name, null, 0);
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + user_table_name + " (username varchar(50) primary key, fname varchar(50), lname varchar(50), password varchar(50));");
        db.execSQL("CREATE TABLE " + ingredients_table_name + " (ingredientId integer primary key autoincrement not null, ingredientName varchar(50), ingredientCategoryId integer, foreign key (ingredientCategoryId) references " + ingredientCategory_table_name + " (ingredientCategoryId));");
        db.execSQL("CREATE TABLE " + ingredientCategory_table_name + " (ingredientCategoryId integer primary key autoincrement not null, ingredientCategoryType varchar)" );

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
