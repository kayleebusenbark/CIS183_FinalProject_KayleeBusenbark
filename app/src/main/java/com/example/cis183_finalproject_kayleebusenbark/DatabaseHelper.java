package com.example.cis183_finalproject_kayleebusenbark;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class DatabaseHelper extends SQLiteOpenHelper
{
    private static final String database_name = "RecipeApp";
    private static final String user_table_name = "Users";
    private static final String ingredients_table_name = "Ingredients";
    private static final String ingredientCategory_table_name = "IngredientCategory";
    private static final String measurement_table_name = "Measurements";
    private static final String recipes_table_name = "Recipes";
    private static final String cuisines_table_name = "Cuisines";

    private static final String userIngredients_table_name = "userIngredients";
    private static final String recipeIngredients_table_name = "recipeIngredients";
    private static final String userFavorites_table_name = "userFavorites";

    public DatabaseHelper(Context c)
    {
        super(c, database_name, null, 31);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + user_table_name + " (userId integer primary key autoincrement not null, username varchar(50), fname varchar(50), lname varchar(50), password varchar(50));");
        db.execSQL("CREATE TABLE " + ingredients_table_name + " (ingredientId integer primary key autoincrement not null, ingredientName varchar(50), ingredientCategoryId integer, foreign key (ingredientCategoryId) references " + ingredientCategory_table_name + " (ingredientCategoryId));");
        db.execSQL("CREATE TABLE " + ingredientCategory_table_name + " (ingredientCategoryId integer primary key autoincrement not null, ingredientCategoryType varchar(50));");
        db.execSQL("CREATE TABLE " + measurement_table_name + " (measurementId integer primary key autoincrement not null, measurementType varchar(50));");

        db.execSQL("CREATE TABLE " + recipes_table_name + " ("
                + "recipeId integer primary key autoincrement not null, "
                + "userId integer, "
                + "recipeTitle varchar(50), "
                + "recipeInstructions varchar(200), "
                + "recipePrepTime float, "
                + "recipePrepTimeCategory varchar(10), "
                + "recipeCategoryId integer, "
                + "foreign key (userId) references " + user_table_name + " (userId), "
                + "foreign key (recipeCategoryId) references " + cuisines_table_name + " (recipeCategoryId));");

        db.execSQL("CREATE TABLE " + cuisines_table_name + " (recipeCategoryId integer primary key autoincrement not null, recipeCategory varchar(50));");

        db.execSQL("CREATE TABLE " + userIngredients_table_name + " ("
                + "userIngredientsId integer primary key autoincrement not null, "
                + "userId integer, "
                + "ingredientId integer, "
                + "userIngredientQuantity float, "
                + "measurementId integer, "
                + "foreign key (userId) references " + user_table_name + " (userId), "
                + "foreign key (ingredientId) references " + ingredients_table_name + " (ingredientId), "
                + "foreign key (measurementId) references " + measurement_table_name + " (measurementId));");

        //junction table: establishes a many to many relationship between recipes and ingredients
        db.execSQL("CREATE TABLE " + recipeIngredients_table_name + " ("
                + "recipeId integer, "
                + "ingredientId integer, "
                + "recipeIngredientQuantity float, "
                + "measurementId integer, "
                + "foreign key (recipeId) references " + recipes_table_name + " (recipeId), "
                + "foreign key (ingredientId) references " + ingredients_table_name + " (ingredientId), "
                + "foreign key (measurementId) references " + measurement_table_name + " (measurementId));");

        Log.d("DatabaseHelper", "Created table: " + recipeIngredients_table_name);


        db.execSQL("CREATE TABLE " + userFavorites_table_name + " ("
                + "userFavoriteId integer primary key autoincrement not null, "
                + "userId, "
                + "recipeId integer, "
                + "foreign key (userId) references " + user_table_name + " (userId), "
                + "foreign key (recipeId) references " + recipes_table_name + " (recipeId));");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1)
    {
        db.execSQL("DROP TABLE IF EXISTS " + user_table_name + ";");
        db.execSQL("DROP TABLE IF EXISTS " + ingredients_table_name + ";");
        db.execSQL("DROP TABLE IF EXISTS " + ingredientCategory_table_name + ";");
        db.execSQL("DROP TABLE IF EXISTS " + measurement_table_name + ";");
        db.execSQL("DROP TABLE IF EXISTS " + recipes_table_name + ";");
        db.execSQL("DROP TABLE IF EXISTS " + cuisines_table_name + ";");
        db.execSQL("DROP TABLE IF EXISTS " + userIngredients_table_name + ";");
        db.execSQL("DROP TABLE IF EXISTS " + recipeIngredients_table_name + ";");
        db.execSQL("DROP TABLE IF EXISTS " + userFavorites_table_name + ";");

        onCreate(db);
    }

    public String getUserDbName()
    {
        return user_table_name;
    }

    public String getIngredientsDbName()
    {
        return ingredients_table_name;
    }

    public String getIngredientCategoryDbName()
    {
        return ingredientCategory_table_name;
    }

    public String getMeasurementDbName()
    {
        return measurement_table_name;
    }

    public String getRecipeDbName()
    {
        return recipes_table_name;
    }

    public String getCuisineDbName()
    {
        return cuisines_table_name;
    }

    public String getUserIngredientsDbName()
    {
        return userIngredients_table_name;
    }

    public String getRecipeIngredientsDbName()
    {
        return recipeIngredients_table_name;
    }

    public String getUserFavoritesDbName()
    {
        return userFavorites_table_name;
    }

    public void initAllTables()
    {
        initUsers();
        initIngredients();
        initIngredientCategories();
        initMeasurements();
        initRecipeCategory();
        initRecipes();
        initRecipeIngredients();

        initUserFavoriteRecipes();
        initUserIngredients();
    }

    //this is where dummy data will go
    private void initUsers()
    {
        if(countRecordsFromTable(user_table_name) == 0)
        {
            SQLiteDatabase db = this.getWritableDatabase();

            db.execSQL("INSERT INTO " + user_table_name + "(username, fname, lname, password) VALUES ('kbusenbark', 'Kaylee', 'Busenbark', '123456');");
            db.execSQL("INSERT INTO " + user_table_name + "(username, fname, lname, password) VALUES ('noahBrown', 'Noah', 'Brown', 'asdf');");
            db.execSQL("INSERT INTO " + user_table_name + "(username, fname, lname, password) VALUES ('avaDavis', 'Ava', 'Davis', 'zxcvb');");
            db.execSQL("INSERT INTO " + user_table_name + "(username, fname, lname, password) VALUES ('willMartinez', 'William', 'Martinez', 'uiop');");
            db.execSQL("INSERT INTO " + user_table_name + "(username, fname, lname, password) VALUES ('jamesWilson', 'James', 'Wilson', '7890');");
            db.execSQL("INSERT INTO " + user_table_name + "(username, fname, lname, password) VALUES ('miaAnderson', 'Mia', 'Anderson', 'Ilovedogs');");
            db.execSQL("INSERT INTO " + user_table_name + "(username, fname, lname, password) VALUES ('BenHernandez', 'Benjamin', 'Hernandez', 'helloWorld');");

            db.close();
        }
    }

    private void initIngredients()
    {
        if(countRecordsFromTable(ingredients_table_name) == 0)
        {
            SQLiteDatabase db = this.getWritableDatabase();


            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Apple', '1');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Banana', '1');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Orange', '1');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Strawberry', '1');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Blueberry', '1');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Lemon', '1');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Lime', '1');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Mango', '1');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Pineapple', '1');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Tomato', '1');");


            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Carrots', '2');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Broccoli', '2');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Spinach', '2');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Onion', '2');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Potato', '2');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Bell Pepper', '2');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Lettuce', '2');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Cabbage', '2');");


            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Rice', '3');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Quinoa', '3');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Oats', '3');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Wheat flour', '3');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Cornmeal', '3');");


            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Chicken', '4');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Beef', '4');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Pork', '4');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Eggs', '4');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Salmon', '4');");


            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Milk', '5');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Cheese', '5');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Yogurt', '5');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Butter', '5');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Sour Cream', '5');");


            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Basil', '6');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Thyme', '6');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Oregano', '6');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Cinnamon', '6');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Cumin', '6');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Tumeric', '6');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Black Pepper', '6');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Paprika', '6');");


            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Sugar', '7');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Honey', '7');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Maple Syrup', '7');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Molasses', '7');");


            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Olive Oil', '8');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Coconut Oil', '8');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Canola Oil', '8');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Vegetable Oil', '8');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Ghee', '8');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Lard', '8');");


            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Water', '9');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Coffee', '9');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Tea', '9');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Vinegar', '9');");


            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Canned Tomatoes', '10');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Canned Tuna', '10');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Pickles', '10');");


            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Ketchup', '11');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Mustard', '11');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Soy Sauce', '11');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Hot Sauce', '11');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Barbecue Sauce', '11');");


            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Almonds', '12');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Walnuts', '12');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Peanuts', '12');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Chia Seeds', '12');");


            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Baking Powder', '13');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('All-purpose flour', '13');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Baking Soda', '13');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Yeast', '13');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Vanilla Extract', '13');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Cocoa Powder', '13');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Chocolate Chips', '13');");


            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Chips', '14');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Popcorn', '14');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Pretzels', '14');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Crackers', '14');");


            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Food Coloring', '15');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Salt', '6');");


            db.close();

        }
    }

    private void initIngredientCategories()
    {
        if(countRecordsFromTable(ingredientCategory_table_name) == 0)
        {
            SQLiteDatabase db = this.getWritableDatabase();

            db.execSQL("INSERT INTO " + ingredientCategory_table_name + "(ingredientCategoryType) VALUES ('Fruits')");
            db.execSQL("INSERT INTO " + ingredientCategory_table_name + "(ingredientCategoryType) VALUES ('Vegetables')");
            db.execSQL("INSERT INTO " + ingredientCategory_table_name + "(ingredientCategoryType) VALUES ('Grains and Starches')");
            db.execSQL("INSERT INTO " + ingredientCategory_table_name + "(ingredientCategoryType) VALUES ('Proteins')");
            db.execSQL("INSERT INTO " + ingredientCategory_table_name + "(ingredientCategoryType) VALUES ('Dairy')");
            db.execSQL("INSERT INTO " + ingredientCategory_table_name + "(ingredientCategoryType) VALUES ('Herbs and Spices')");
            db.execSQL("INSERT INTO " + ingredientCategory_table_name + "(ingredientCategoryType) VALUES ('Sweeteners')");
            db.execSQL("INSERT INTO " + ingredientCategory_table_name + "(ingredientCategoryType) VALUES ('Oils and Fats')");
            db.execSQL("INSERT INTO " + ingredientCategory_table_name + "(ingredientCategoryType) VALUES ('Beverages and Liquids')");
            db.execSQL("INSERT INTO " + ingredientCategory_table_name + "(ingredientCategoryType) VALUES ('Canned and Preserved Items')");
            db.execSQL("INSERT INTO " + ingredientCategory_table_name + "(ingredientCategoryType) VALUES ('Condiments and Sauces')");
            db.execSQL("INSERT INTO " + ingredientCategory_table_name + "(ingredientCategoryType) VALUES ('Nuts and Seeds')");
            db.execSQL("INSERT INTO " + ingredientCategory_table_name + "(ingredientCategoryType) VALUES ('Baking Essentials')");
            db.execSQL("INSERT INTO " + ingredientCategory_table_name + "(ingredientCategoryType) VALUES ('Snacks')");
            db.execSQL("INSERT INTO " + ingredientCategory_table_name + "(ingredientCategoryType) VALUES ('Miscellaneous')");

            db.close();
        }
    }

    private void initMeasurements()
    {
        if(countRecordsFromTable(measurement_table_name) == 0)
        {
            SQLiteDatabase db = this.getWritableDatabase();

            //10
            db.execSQL("INSERT INTO " + measurement_table_name + "(measurementType) VALUES ('tsp')");
            db.execSQL("INSERT INTO " + measurement_table_name + "(measurementType) VALUES ('tbsp')");
            db.execSQL("INSERT INTO " + measurement_table_name + "(measurementType) VALUES ('fl oz')");
            db.execSQL("INSERT INTO " + measurement_table_name + "(measurementType) VALUES ('c')");
            db.execSQL("INSERT INTO " + measurement_table_name + "(measurementType) VALUES ('pt')");
            db.execSQL("INSERT INTO " + measurement_table_name + "(measurementType) VALUES ('qt')");
            db.execSQL("INSERT INTO " + measurement_table_name + "(measurementType) VALUES ('gal')");
            db.execSQL("INSERT INTO " + measurement_table_name + "(measurementType) VALUES ('oz')");
            db.execSQL("INSERT INTO " + measurement_table_name + "(measurementType) VALUES ('lb')");
            db.execSQL("INSERT INTO " + measurement_table_name + "(measurementType) VALUES ('N/A')");

            db.close();
        }
    }

    private void initRecipeCategory()
    {
        if(countRecordsFromTable(cuisines_table_name) == 0)
        {
            SQLiteDatabase db = this.getWritableDatabase();

            db.execSQL("INSERT INTO " + cuisines_table_name + "(recipeCategory) VALUES ('Mexican');");
            db.execSQL("INSERT INTO " + cuisines_table_name + "(recipeCategory) VALUES ('Italian');");
            db.execSQL("INSERT INTO " + cuisines_table_name + "(recipeCategory) VALUES ('Chinese');");
            db.execSQL("INSERT INTO " + cuisines_table_name + "(recipeCategory) VALUES ('Indian');");
            db.execSQL("INSERT INTO " + cuisines_table_name + "(recipeCategory) VALUES ('Middle Eastern');");
            db.execSQL("INSERT INTO " + cuisines_table_name + "(recipeCategory) VALUES ('Thai');");
            db.execSQL("INSERT INTO " + cuisines_table_name + "(recipeCategory) VALUES ('Mediterranean');");
            db.execSQL("INSERT INTO " + cuisines_table_name + "(recipeCategory) VALUES ('American');");
            db.execSQL("INSERT INTO " + cuisines_table_name + "(recipeCategory) VALUES ('Dessert');");
            db.execSQL("INSERT INTO " + cuisines_table_name + "(recipeCategory) VALUES ('N/A');");

            db.close();
        }
    }

    private void initRecipes()
    {
        if(countRecordsFromTable(recipes_table_name) == 0)
        {
            SQLiteDatabase db = this.getWritableDatabase();

            db.execSQL("INSERT INTO " + recipes_table_name + "(userId, recipeTitle, recipeInstructions, recipePrepTime, recipePrepTimeCategory, recipeCategoryId) VALUES (1, 'Spaghetti Bolognese', 'Cook pasta. Prepare sauce with beef, tomatoes, and spices. Combine and serve.', 45, 'minute(s)', 2);");
            db.execSQL("INSERT INTO " + recipes_table_name + "(userId, recipeTitle, recipeInstructions, recipePrepTime, recipePrepTimeCategory, recipeCategoryId) VALUES (2, 'Chicken Tikka Masala', 'Marinate chicken. Cook with masala sauce. Serve with naan or rice.', 1, 'hour(s)', 4);");
            db.execSQL("INSERT INTO " + recipes_table_name + "(userId, recipeTitle, recipeInstructions, recipePrepTime, recipePrepTimeCategory, recipeCategoryId) VALUES (3, 'Vegetable Stir Fry', 'Chop vegetables. Stir-fry with sauce. Serve with rice or noodles.', 20, 'minute(s)', 3);");
            db.execSQL("INSERT INTO " + recipes_table_name + "(userId, recipeTitle, recipeInstructions, recipePrepTime, recipePrepTimeCategory, recipeCategoryId) VALUES (4, 'Mediterranean Salad', 'Mix greens, olives, feta, and vinaigrette. Serve fresh.', 15, 'minute(s)', 7);");
            db.execSQL("INSERT INTO " + recipes_table_name + "(userId, recipeTitle, recipeInstructions, recipePrepTime, recipePrepTimeCategory, recipeCategoryId) VALUES (5, 'Chocolate Cake', 'Mix ingredients. Bake in oven at 350°F for 30 minutes. Let cool and serve.', 50, 'minute(s)', 9);");

            db.close();
        }
    }

    private void initRecipeIngredients()
    {
        if(countRecordsFromTable(recipeIngredients_table_name) == 0)
        {
            SQLiteDatabase db = this.getWritableDatabase();


            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (1, 1, 7.05, 8);");  //Spaghetti: 7.05 oz
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (1, 17, 3.53, 8);"); //Tomato: 3.53 oz
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (1, 24, 8.82, 8);"); //Ground Beef: 8.82 oz
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (1, 14, 2.65, 8);"); //Onion: 2.65 oz
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (1, 37, 2, 10);");   // Garlic: 2 cloves
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (1, 43, 1, 2);");    // Olive Oil: 1 tbsp
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (1, 30, 1, 1);");    // Basil: 1 tsp
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (1, 48, 1, 1);");    // Salt: 1 tsp
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (1, 35, 0.5, 1);");  // Black Pepper: 0.5 tsp
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (1, 29, 1, 8);");    // Parmesan Cheese: 1 oz


            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (2, 24, 17.64, 8);"); // Chicken: 17.64 oz
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (2, 31, 6.76, 3);");  // Yogurt: 6.76 fl oz
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (2, 17, 8, 8);");     // Tomato Sauce: 8 oz
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (2, 38, 1, 2);");     // Cumin: 1 tbsp
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (2, 33, 0.25, 1);");  // Cinnamon: 0.25 tsp
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (2, 37, 3, 10);");    // Garlic: 3 cloves
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (2, 32, 1, 2);");     // Ginger: 1 tbsp
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (2, 34, 0.5, 1);");   // Turmeric: 0.5 tsp
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (2, 36, 1, 1);");     // Paprika: 1 tsp
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (2, 48, 1, 1);");     // Salt: 1 tsp
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (2, 27, 2, 2);");     // Butter: 2 tbsp


            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (3, 13, 5.29, 8);");  // Broccoli: 5.29 oz
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (3, 14, 5.29, 8);");  // Spinach: 5.29 oz
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (3, 11, 3.53, 8);");  // Carrots: 3.53 oz
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (3, 16, 2.65, 8);");  // Bell Pepper: 2.65 oz
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (3, 36, 2, 2);");     // Soy Sauce: 2 tbsp
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (3, 32, 1, 2);");     // Ginger: 1 tbsp
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (3, 37, 2, 10);");    // Garlic: 2 cloves
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (3, 43, 1, 2);");     // Olive Oil: 1 tbsp
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (3, 57, 1, 1);");     // Cornstarch: 1 tsp
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (3, 45, 1, 2);");     // Honey: 1 tbsp


            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (4, 16, 3.53, 8);");  // Lettuce: 3.53 oz
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (4, 29, 1.76, 8);");  // Feta Cheese: 1.76 oz
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (4, 17, 3.53, 8);");  // Tomatoes: 3.53 oz
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (4, 18, 3.53, 8);");  // Cucumber: 3.53 oz
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (4, 19, 2, 8);");     // Olives: 2 oz
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (4, 43, 1, 2);");     // Olive Oil: 1 tbsp
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (4, 44, 1, 2);");     // Lemon Juice: 1 tbsp
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (4, 48, 0.5, 1);");   // Salt: 0.5 tsp
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (4, 35, 0.25, 1);");  // Black Pepper: 0.25 tsp


            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (5, 56, 4, 8);");     // All-purpose Flour: 4 oz
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (5, 59, 2, 8);");     // Cocoa Powder: 2 oz
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (5, 47, 3.53, 8);");  // Sugar: 3.53 oz
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (5, 27, 2, 8);");     // Butter: 2 oz
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (5, 37, 2, 10);");    // Eggs: 2 large
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (5, 58, 1, 1);");     // Baking Powder: 1 tsp
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (5, 55, 1, 1);");     // Vanilla Extract: 1 tsp
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (5, 31, 4, 3);");     // Milk: 4 fl oz
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (5, 60, 2, 8);");     // Chocolate Chips: 2 oz
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (5, 48, 0.5, 1);");   // Salt: 0.5 tsp


            db.close();
        }
    }

    //this is for testing purposes. the user favorite table is something the user will have to do by themselves
    private void initUserFavoriteRecipes()
    {
        if(countRecordsFromTable(userFavorites_table_name) == 0)
        {
            SQLiteDatabase db = this.getWritableDatabase();

            db.execSQL("INSERT INTO " + userFavorites_table_name + "(userId, recipeId) VALUES (2,1)");

            db.close();
        }
    }
    //this is for testing purposes. the user ingredients table is something the user will have to do by themselves
    private void initUserIngredients()
    {
        if(countRecordsFromTable(userIngredients_table_name) == 0)
        {
            SQLiteDatabase db = this.getWritableDatabase();
        }
    }

    public int countRecordsFromTable(String tableName)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        int numRows = (int) DatabaseUtils.queryNumEntries(db, tableName);

        db.close();

        return numRows;
    }

    //CRUD FOR USER
    public boolean usernameExists(String username)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String checkUserName = "SELECT count(userId) FROM " + user_table_name + " WHERE username = '" + username + "';";

        Cursor cursor = db.rawQuery(checkUserName, null);

        cursor.moveToFirst();

        int count = cursor.getInt(0);
        cursor.close();
        db.close();

        if(count!=0)
        {
            return true;
        }

        else
        {
            return false;
        }
    }

    public boolean isLoginValid(String username, String password)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String checkValidLogin = "SELECT count(userId) FROM " + user_table_name + " WHERE username = '" + username + "' AND password = '" + password + "';";

        Cursor cursor = db.rawQuery(checkValidLogin, null);

        cursor.moveToFirst();

        int count = cursor.getInt(0);

        cursor.close();
        db.close();

        if (count !=0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public User getAllUserDataGivenUsername(String username)
    {
        String selectQuery = "SELECT * FROM " + user_table_name + " WHERE username = '" + username + "';";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor != null)
        {
            User loggedInUser = new User();

            cursor.moveToFirst();

            loggedInUser.setUserId(cursor.getInt(0));
            loggedInUser.setUsername(cursor.getString(1));
            loggedInUser.setFname(cursor.getString(2));
            loggedInUser.setLname(cursor.getString(3));
            loggedInUser.setPassword(cursor.getString(4));

            SessionData.setLoggedInUser(loggedInUser);

            cursor.close();
            db.close();
            return loggedInUser;
        }

        else
        {
            SessionData.setLoggedInUser(null);
            Log.d("Error", "Error");
            db.close();
        }
        return null;
    }

    public User getUserById(int userId)
    {
        String selectQuery = "SELECT fname, lname FROM " + user_table_name + " WHERE userId = " + userId + ";";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        cursor.moveToFirst();
        User user = new User();

        user.setFname(cursor.getString(0));
        user.setLname(cursor.getString(1));

        cursor.close();


        db.close();
        return user;
    }




    public void updateUserInfo(User user)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String updateQuery = "UPDATE users SET " +
                             "username = '" + user.getUsername() + "'," +
                             "fname = '" + user.getFname() + "'," +
                             "lname = '" + user.getLname() + "'," +
                             "password = '" + user.getPassword() + "'" +
                             "WHERE userId = '" + user.getUserId() + "';";
        db.execSQL(updateQuery);
        db.close();
    }

    public void deleteUser(int userId)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String deleteUser = "DELETE FROM " + user_table_name + " WHERE userId = '" + userId + "';";
        String deleteRecipes = "DELETE FROM " + recipes_table_name + " WHERE userId = '" + userId + "';";

        db.execSQL(deleteUser);
        db.execSQL(deleteRecipes);


        db.close();
    }

    public void createNewUser(String username, String fname, String lname, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("username", username);
        values.put("fname", fname);
        values.put("lname", lname);
        values.put("password", password);

        db.insert("Users", null, values);
        db.close();
    }



    //FOR SPINNERS
    public ArrayList<String> getAllIngredientNamesForSpinner()
    {
        ArrayList<String> ingredientNamesList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT ingredientName FROM " + ingredients_table_name, null);

        while(cursor.moveToNext())
        {
            String ingredientName = cursor.getString(0);
            ingredientNamesList.add(ingredientName);
        }
        cursor.close();
        db.close();

        return ingredientNamesList;
    }

    public ArrayList<String> getAllIngredientCategoriesForSpinner()
    {
        ArrayList<String> ingredientCategoriesList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT ingredientCategoryType FROM " + ingredientCategory_table_name, null);

        while(cursor.moveToNext())
        {
            String ingredientCategoryType = cursor.getString(0);
            ingredientCategoriesList.add(ingredientCategoryType);
        }
        cursor.close();
        db.close();

        return ingredientCategoriesList;
    }


    public ArrayList<String> getAllMeasurementsForSpinner()
    {
        ArrayList<String> measurementList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT measurementType FROM " + measurement_table_name, null);

        while(cursor.moveToNext())
        {
            String measurementType = cursor.getString(0);
            measurementList.add(measurementType);
        }
        cursor.close();
        db.close();

        return measurementList;
    }

    public ArrayList<String> getAllRecipeCategoriesForSpinner()
    {
        ArrayList<String> recipeCategoriesList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT recipeCategory FROM " + cuisines_table_name, null);
        while(cursor.moveToNext())
        {
            String categoryType = cursor.getString(0);
            recipeCategoriesList.add(categoryType);
        }
        cursor.close();
        db.close();

        return recipeCategoriesList;
    }

    public String getPrepTimeCategoryForRecipe(int recipeId)
    {
        String prepTimeCategory = "";
        SQLiteDatabase db = this.getReadableDatabase();


        String selectStatement = "SELECT recipePrepTimeCategory FROM " + recipes_table_name + " WHERE recipeId = '" + recipeId + "';";

        Cursor cursor = db.rawQuery(selectStatement, null);

        if(cursor != null)
        {
            cursor.moveToFirst();
            prepTimeCategory = cursor.getString(0);
        }
        cursor.close();
        db.close();

        return prepTimeCategory;
    }


    public String getIngredientNameById(int ingredientId)
    {
        String ingredientName = "";

        SQLiteDatabase db = this.getReadableDatabase();

        String selectStatement = "SELECT ingredientName FROM " + ingredients_table_name + " WHERE ingredientId = '" + ingredientId + "';";

        Cursor cursor = db.rawQuery(selectStatement, null);

        if(cursor != null)
        {
            cursor.moveToFirst();
            ingredientName = cursor.getString(0);
        }

        cursor.close();
        db.close();

        return ingredientName;

    }

    public String getMeasurementNamebyId(int measurementId)
    {
        String measurementName = "";

        SQLiteDatabase db = this.getReadableDatabase();

        String selectStatement = "SELECT measurementType FROM " + measurement_table_name + " WHERE measurementId = '" + measurementId + "';";

        Cursor cursor = db.rawQuery(selectStatement, null);

        if(cursor != null)
        {
            cursor.moveToFirst();
            measurementName = cursor.getString(0);
        }

        cursor.close();
        db.close();

        return measurementName;
    }

    public int getIngredientIdByName(String ingredientName)
    {
        int ingredientId = -1;

        SQLiteDatabase db = this.getReadableDatabase();

        String selectStatement = "SELECT ingredientId FROM " + ingredients_table_name + " WHERE ingredientName = '" + ingredientName + "';";

        Cursor cursor = db.rawQuery(selectStatement, null);

        if(cursor != null)
        {
            cursor.moveToFirst();
            ingredientId = cursor.getInt(0);
        }
        cursor.close();
        db.close();

        return  ingredientId;
    }

    public int getMeasurementIdByName(String measurementType)
    {
        int measurementId = -1;

        SQLiteDatabase db = this.getReadableDatabase();

        String selectStatement = "SELECT measurementId FROM " + measurement_table_name + " WHERE measurementType = '" + measurementType + "';";

        Cursor cursor = db.rawQuery(selectStatement, null);

        if(cursor != null)
        {
            cursor.moveToFirst();
            measurementId = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return measurementId;
    }

    public int getIngredientCategoryIdByName(String categoryName)
    {
        int ingredientCategoryId = -1;

        SQLiteDatabase db = this.getReadableDatabase();

        String selectStatement = "SELECT ingredientCategoryId FROM " + ingredientCategory_table_name + " WHERE ingredientCategoryType = '" + categoryName + "';";

        Cursor cursor = db.rawQuery(selectStatement, null);

        if(cursor != null)
        {
            cursor.moveToFirst();
            ingredientCategoryId = cursor.getInt(0);

        }
        cursor.close();
        db.close();
        return ingredientCategoryId;
    }
    public int getRecipeCategoryIdByName(String recipeCategoryName)
    {
        int recipeCategoryId = -1;
        SQLiteDatabase db = this.getReadableDatabase();

        String selectStatment = "SELECT recipeCategoryId FROM " + cuisines_table_name + " WHERE recipeCategory = '" + recipeCategoryName + "';";

        Cursor cursor = db.rawQuery(selectStatment, null);

        if(cursor != null)
        {
            cursor.moveToFirst();
            recipeCategoryId = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return recipeCategoryId;

    }


    public String getRecipeCategoryNameById(int categoryId)
    {
        String reciepeCategoryName = "";

        SQLiteDatabase db = this.getReadableDatabase();


        String selectStatement = "SELECT recipeCategory FROM " + cuisines_table_name + " WHERE recipeCategoryId = " + categoryId + ";";

        Cursor cursor = db.rawQuery(selectStatement, null);

        if(cursor != null)
        {
            cursor.moveToFirst();
            reciepeCategoryName = cursor.getString(0);
        }

        cursor.close();

        db.close();

        return reciepeCategoryName;
    }



    //CRUD FOR USER INGREDIENTS
    public ArrayList<UserIngredient> getUserIngredients(int userId)
    {
        ArrayList<UserIngredient> userIngredientList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectStatement = "SELECT * FROM " + userIngredients_table_name + " WHERE userId = " + userId + ";";

        Cursor cursor = db.rawQuery(selectStatement, null);

        if(cursor != null)
        {
            while(cursor.moveToNext())
            {
                int userIngredientId = cursor.getInt(0);
                //int userId = cursor.getInt(1);
                int ingredientId = cursor.getInt(2);
                float quantity = cursor.getFloat(3);
                int measurementId = cursor.getInt(4);

                UserIngredient userIngredient = new UserIngredient(userIngredientId, ingredientId, quantity, measurementId);
                userIngredientList.add(userIngredient);
            }
            cursor.close();
        }
        db.close();

        return userIngredientList;
    }

    public void updateUserIngredientList(int userId, int userIngredientsId, float quantity, int measurementId)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String updateQuery = "UPDATE userIngredients SET " +
                             "userIngredientQuantity = " + quantity + "," +
                             "measurementId = " + measurementId + " " +
                             " WHERE userIngredientsId = " + userIngredientsId + " AND userId = " + userId + ";";
        db.execSQL(updateQuery);
        db.close();
    }

    public void deleteUserIngredient(int userId, int userIngredientId)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String deleteQuery = "DELETE FROM " + userIngredients_table_name + " WHERE userIngredientsId = " + userIngredientId + " AND userId = " + userId + ";";
        db.execSQL(deleteQuery);
        db.close();
    }

    public void addUserIngredients(int userId, int ingredientId, float quantity, int measurementId)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("userId", userId);
        values.put("ingredientId", ingredientId);
        values.put("userIngredientQuantity", quantity);
        values.put("measurementId", measurementId);

        db.insert(userIngredients_table_name, null, values);

        db.close();
    }

    //CRUD FOR INGREDIENTS
    //we want update and add
    public void addIngredient(String name, int categoryId)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("ingredientName", name);
        values.put("ingredientCategoryId", categoryId);

        db.insert(ingredients_table_name, null, values);

        getAllIngredientNamesForSpinner();
        db.close();
    }

    //RECIPES
    public ArrayList<Recipe> getAllRecipes()
    {
        ArrayList<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + recipes_table_name, null);

        if(cursor != null)
        {
            cursor.moveToNext();
            do{
                int recipeId = cursor.getInt(0);
                int userId = cursor.getInt(1);
                String recipeTitle = cursor.getString(2);
                String recipeInstructions = cursor.getString(3);
                float prepTime = cursor.getFloat(4);
                String prepTimeCategory = cursor.getString(5);
                int recipeCatId = cursor.getInt(6);

                Recipe recipe = new Recipe(recipeId, userId, recipeTitle, recipeInstructions, prepTime, prepTimeCategory, recipeCatId);
                recipes.add(recipe);
            }
            while(cursor.moveToNext());
            cursor.close();
        }

        db.close();
        return recipes;
    }


    public ArrayList<RecipeIngredient> getIngredientsByRecipeId(int recipeId)
    {
        ArrayList<RecipeIngredient> ingredients = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String selectStatement = "SELECT * FROM " + recipeIngredients_table_name + " WHERE recipeId = " + recipeId + ";";

        Cursor cursor = db.rawQuery(selectStatement, null);

        if(cursor.moveToFirst())
        {
            do {
                RecipeIngredient ingredient = new RecipeIngredient();
                ingredient.setRecipeId(cursor.getInt(0));
                ingredient.setIngredientId(cursor.getInt(1));
                ingredient.setIngredientQuantity(cursor.getFloat(2));
                ingredient.setMeasurementId(cursor.getInt(3));

                ingredients.add(ingredient);
            } while (cursor.moveToNext());

        }
        cursor.close();
        db.close();

        return ingredients;
    }

    public int addRecipe(int userId, String title, String instructions, float prepTime, String prepTimeCategory, int recipeCategoryId)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("userId", userId);
        values.put("recipeTitle", title);
        values.put("recipeInstructions", instructions);
        values.put("recipePrepTime", prepTime);
        values.put("recipePrepTimeCategory", prepTimeCategory);
        values.put("recipeCategoryId", recipeCategoryId);

        long recipeId = db.insert(recipes_table_name, null, values);

        db.close();

        return (int) recipeId;
    }

    public void addRecipeIngredient(int recipeId, int ingredientId, float quantity, int measurementId)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("recipeId", recipeId);
        values.put("ingredientId", ingredientId);
        values.put("recipeIngredientQuantity", quantity);
        values.put("measurementId", measurementId);

        db.insert(recipeIngredients_table_name, null, values);
    }

    public void addRecipeToFavorites(int userId, int recipeId)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String checkQuery = "SELECT COUNT(*) FROM " + userFavorites_table_name + " WHERE userId = " + userId + " AND recipeId = " + recipeId + ";";

        Cursor cursor = db.rawQuery(checkQuery, null);

        cursor.moveToNext();

        int count = cursor.getInt(0);
        cursor.close();

        if(count == 0)
        {
            ContentValues values = new ContentValues();
            values.put("userId", userId);
            values.put("recipeId", recipeId);

            db.insert(userFavorites_table_name, null, values);
            Log.d("DATABASEHELPER", "RECIPE ADDED TO FAVORITES");

        }
        else
        {
            Log.d("DATABASEHELPER", "RECIPE ALREADY IN FAVORITES");
        }

        db.close();
    }

    public ArrayList<RecipeMatch> getMatchingRecipes(int userId)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<RecipeMatch> recipeMatches = new ArrayList<>();

        String query = "SELECT r.recipeId, r.recipeTitle, r.recipeInstructions, r.recipePrepTime, r.recipePrepTimeCategory, " +
                        "r.recipeCategoryId, r.userId, COUNT(DISTINCT ri.ingredientId) AS totalIngredients, " +
                        "SUM(CASE WHEN ui.ingredientId IS NOT NULL THEN 1 ELSE 0 END) AS matchedIngredients " +
                        "FROM recipes r " +
                        "JOIN recipeIngredients ri ON r.recipeId = ri.recipeId " +
                        "LEFT JOIN userIngredients ui ON ri.ingredientId = ui.ingredientId AND ui.userId = " + userId + " " +
                        "GROUP BY r.recipeId " +
                        "HAVING (totalIngredients - matchedIngredients) <= 2 " +
                        "ORDER BY matchedIngredients DESC, totalIngredients ASC;";

        Cursor cursor = db.rawQuery(query, null);

        if(cursor != null)
        {
            while(cursor.moveToNext())
            {
                int recipeId = cursor.getInt(0);
                String recipeTitle = cursor.getString(1);
                String recipeInstructions = cursor.getString(2);
                float prepTime = cursor.getFloat(3);
                String prepTimeCategory = cursor.getString(4);
                int recipeCategoryId = cursor.getInt(5);
                int creatorId = cursor.getInt(6);
                int totalIngredients = cursor.getInt(7);
                int matchedIngredients = cursor.getInt(8);

                Recipe recipe = new Recipe(recipeId, creatorId, recipeTitle, recipeInstructions, prepTime, prepTimeCategory, recipeCategoryId);
                RecipeMatch recipeMatch = new RecipeMatch(recipe, matchedIngredients, totalIngredients);
                recipeMatches.add(recipeMatch);
            }
            cursor.close();
        }

        db.close();

        return recipeMatches;
    }

    public ArrayList<Recipe> getRecipesByUserId(int userId)
    {
        ArrayList<Recipe> userRecipes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectStatment = "SELECT * FROM " + recipes_table_name + " WHERE userId = " + userId + ";";

        Cursor cursor = db.rawQuery(selectStatment, null);

        if(cursor != null)
        {
            while(cursor.moveToNext())
            {
                int recipeId = cursor.getInt(0);
                String recipeTitle = cursor.getString(2);
                String recipeInstructions = cursor.getString(3);
                float prepTime = cursor.getFloat(4);
                String prepTimeCategory = cursor.getString(5);
                int recipeCategoryId = cursor.getInt(6);

                Recipe recipe = new Recipe(recipeId, userId, recipeTitle, recipeInstructions, prepTime, prepTimeCategory, recipeCategoryId);
                userRecipes.add(recipe);
            }
            cursor.close();
        }
        db.close();

        return  userRecipes;
    }

    public ArrayList<Recipe> getFavoriteRecipesByUserId(int userId)
    {
        ArrayList<Recipe> favoriteRecipes = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT r.recipeId, r.userId, r.recipeTitle, r.recipeInstructions, r.recipePrepTime, " +
                        "r.recipePrepTimeCategory, r.recipeCategoryId " +
                        "FROM " + recipes_table_name + " r " +
                        "INNER JOIN " + userFavorites_table_name + " uf " +
                        "ON r.recipeId = uf.recipeId " +
                        "WHERE uf.userId = " + userId + ";";

        Cursor cursor = db.rawQuery(query, null);

        if(cursor != null)
        {
            while(cursor.moveToNext())
            {
                int recipeId = cursor.getInt(0);
                int creatorId = cursor.getInt(1);
                String recipeTitle = cursor.getString(2);
                String recipeInstructions = cursor.getString(3);
                float prepTime = cursor.getFloat(4);
                String prepTimeCategory = cursor.getString(5);
                int recipeCategoryId = cursor.getInt(6);

                Recipe recipe = new Recipe(recipeId, creatorId, recipeTitle, recipeInstructions, prepTime, prepTimeCategory, recipeCategoryId);
                favoriteRecipes.add(recipe);
            }
            cursor.close();
        }
        db.close();;
        return favoriteRecipes;

    }

    public ArrayList<Recipe> getRecipesByCategoryId(int categoryId)
    {
        ArrayList<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + recipes_table_name + " WHERE recipeCategoryId = " + categoryId + ";";

        Cursor cursor = db.rawQuery(query, null);

        if(cursor != null)
        {
            while(cursor.moveToNext())
            {
                int recipeId = cursor.getInt(0);
                int userId = cursor.getInt(1);
                String recipeTitle = cursor.getString(2);
                String recipeInstructions = cursor.getString(3);
                float prepTime = cursor.getFloat(4);
                String prepTimeCategory = cursor.getString(5);
                int recipeCatId = cursor.getInt(6);

                Recipe recipe = new Recipe(recipeId, userId, recipeTitle, recipeInstructions, prepTime, prepTimeCategory, recipeCatId);
                recipes.add(recipe);
            }

            cursor.close();
        }
        db.close();

        return recipes;

    }











}
