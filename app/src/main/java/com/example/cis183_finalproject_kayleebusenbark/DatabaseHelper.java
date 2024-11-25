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
        super(c, database_name, null, 11);
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

            //1-10
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

            //11-19
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Carrots', '2');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Broccoli', '2');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Spinach', '2');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Onion', '2');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Potato', '2');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Bell Pepper', '2');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Lettuce', '2');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Cabbage', '2');");

            //20-24
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Rice', '3');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Quinoa', '3');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Oats', '3');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Wheat flour', '3');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Cornmeal', '3');");

            //25-29
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Chicken', '4');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Beef', '4');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Pork', '4');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Eggs', '4');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Salmon', '4');");

            //30-34
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Milk', '5');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Cheese', '5');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Yogurt', '5');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Butter', '5');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Sour Cream', '5');");

            //35-42
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Basil', '6');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Thyme', '6');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Oregano', '6');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Cinnamon', '6');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Cumin', '6');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Tumeric', '6');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Black Pepper', '6');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Paprika', '6');");

            //43-46
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Sugar', '7');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Honey', '7');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Maple Syrup', '7');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Molasses', '7');");

            //47-52
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Olive Oil', '8');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Coconut Oil', '8');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Canola Oil', '8');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Vegetable Oil', '8');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Ghee', '8');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Lard', '8');");

            //53-56
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Water', '9');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Coffee', '9');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Tea', '9');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Vinegar', '9');");


            //57-59
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Canned Tomatoes', '10');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Canned Tuna', '10');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Pickles', '10');");

            //60-64
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Ketchup', '11');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Mustard', '11');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Soy Sauce', '11');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Hot Sauce', '11');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Barbecue Sauce', '11');");

            //65-68
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Almonds', '12');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Walnuts', '12');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Peanuts', '12');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Chia Seeds', '12');");

            //69-75
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Baking Powder', '13');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('All-purpose flour', '13');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Baking Soda', '13');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Yeast', '13');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Vanilla Extract', '13');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Cocoa Powder', '13');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Chocolate Chips', '13');");

            //76-79
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Chips', '14');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Popcorn', '14');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Pretzels', '14');");
            db.execSQL("INSERT INTO " + ingredients_table_name + "(ingredientName, ingredientCategoryId) VALUES ('Crackers', '14');");

            //80
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

            db.execSQL("INSERT INTO " + recipes_table_name + "(userId, recipeTitle, recipeInstructions, recipePrepTime, recipePrepTimeCategory, recipeCategoryId) VALUES (1, 'Chocolate Cake', '1. Preheat Oven to 350F. Grease and flour two 9-inch round cake pans. 2. Mix Dry Ingredients: In a large bowl, whisk together the flour, sugar, cocoa powder, baking powder, baking soda, and salt. 3. Add Wet Ingredients: Add the eggs, milk, vegetable oil, and vanilla extract to the dry ingredients. Mix until smooth and well combined. 5. Bake: Pour batter evenly into the pans and bake for 30 to 35 mins, or until a toothpick inserted into the center of the cakes comes out clean. 6 Cool: Let cakes cool in pans for 10 minutes and then take them out of the pans', 1, 'hour', 9)");

            db.close();
        }
    }

    private void initRecipeIngredients()
    {
        if(countRecordsFromTable(recipeIngredients_table_name) == 0)
        {
            SQLiteDatabase db = this.getWritableDatabase();

            //chocolate cake
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (1, 70, 1.75, 4)");
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (1, 43, 1.5, 4)");
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (1, 74, 1.75, 4)");
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (1, 69, 1.5, 1)");
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (1, 71, 1.5, 1)");
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (1, 81, 1, 1)");
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (1, 28, 2, 10)");
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (1, 30, 1, 4)");
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (1, 50, 0.5, 4)");
            db.execSQL("INSERT INTO " + recipeIngredients_table_name + "(recipeId, ingredientId, recipeIngredientQuantity, measurementId) VALUES (1, 73, 2, 1)");

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

            db.execSQL("INSERT INTO " + userIngredients_table_name + "(userId, ingredientId, userIngredientQuantity, measurementId) VALUES (1, 2, 3, 10)");
            db.execSQL("INSERT INTO " + userIngredients_table_name + "(userId, ingredientId, userIngredientQuantity, measurementId) VALUES (1, 2, 3, 10)");
            db.execSQL("INSERT INTO " + userIngredients_table_name + "(userId, ingredientId, userIngredientQuantity, measurementId) VALUES (1, 2, 3, 10)");
            db.execSQL("INSERT INTO " + userIngredients_table_name + "(userId, ingredientId, userIngredientQuantity, measurementId) VALUES (1, 2, 3, 10)");

            db.execSQL("INSERT INTO " + userIngredients_table_name + "(userId, ingredientId, userIngredientQuantity, measurementId) VALUES (3, 29, 1, 7)");
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

    public void getAllUserDataGivenUsername(String username)
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
        }

        else
        {
            SessionData.setLoggedInUser(null);
            Log.d("Error", "Error");
        }
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

        String selectStatement = "DELETE FROM " + user_table_name + " WHERE userId = '" + userId + "';";

        db.execSQL(selectStatement);

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





















}
