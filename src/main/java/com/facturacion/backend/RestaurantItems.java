package com.facturacion.backend;

public class RestaurantItems {
    public static class RecipeIngredient {
        int plate_id;
        int ingredient_id;
        float ingredient_needed;
    
        public RecipeIngredient(int _plate_id, int _ingredient_id, float _ingredient_needed) {
            ingredient_needed = _ingredient_needed;
            ingredient_id = _ingredient_id;
            plate_id = _plate_id;
        }
    
    }
    
    public static class Plate {
        public String name;
        public int id;
    
        public Plate(String _name, int _id) {
            name = _name;
            id = _id;
        }
    }
    
    public static class Ingredient {
        public float quantity;
        public String name;
        public int id;
    
        public Ingredient(String _name, float _quantity, int _id) {
            quantity = _quantity;
            name = _name;
            id = _id;
        }
    }
}