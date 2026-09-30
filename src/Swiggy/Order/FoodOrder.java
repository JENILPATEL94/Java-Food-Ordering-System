package Swiggy.Order;

import Swiggy.Billing.Cart;
import Swiggy.Billing.DLL;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class FoodOrder
{
    Connection connection;
    Scanner scanner;
    Cart cart;
    public FoodOrder(Connection connection, Scanner scanner, Cart cart) {
        this.connection = connection;
        this.scanner = scanner;
        this.cart=cart;
    }
    DLL showed_item=new DLL();
    DLL showed_restaurants=new DLL();
    public void search()
    {
        while(true)
        {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("           FOOD SEARCH OPTIONS           ");
            System.out.println("=".repeat(50));
            System.out.println("1. Search by Food Name");
            System.out.println("2. Search by Restaurant Name");
            System.out.println("3. Browse All Food Items");
            System.out.println("4. View Cart");
            System.out.println("0. Return to Main Menu");
            System.out.println("-".repeat(50));
            showed_item.first=null;
            showed_restaurants.first=null;
            int choice3=-1;
            try {
                System.out.print("Please enter your choice: ");
                choice3 = scanner.nextInt();
            }catch (InputMismatchException e)
            {
                System.out.println("Invalid input. Please enter a number between 0-4");
                scanner.nextLine();
                continue;
            }
            switch (choice3)
            {
                case 1: searchByFoodName();
                    break;
                case 2 :searchByRestaurantsName();
                    break;
                case 3:displayAllFoodItems();
                    break;
                case 4:
                    cart.ShowBuyingItem(1);
                    cart.editCart(1);
                    break;
                case 0: return;
                default:
                    System.out.println("Invalid selection. Please choose between 0-4");
            }
        }
    }
    public void searchByRestaurantsName()
    {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("           RESTAURANT SEARCH           ");
        System.out.println("=".repeat(50));
        System.out.print("Enter Restaurant Name: ");
        String restaurantsName= scanner.next();
        String search_query="select * from restaurant where name like '"+restaurantsName+"%'";
        try {
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery(search_query);
            boolean restaurant_available=resultSet.next();
            if (restaurant_available) {
                System.out.println("\nFound Restaurants:");
                while (restaurant_available) {
                     int restaurantsId=resultSet.getInt("id");
                     String restaurantName=resultSet.getString("name");
                     String restaurantAddress=resultSet.getString("address");
                    System.out.println("ID: "+restaurantsId + " | Name: " +  restaurantName+ " | Address: " + restaurantAddress);
                    showed_restaurants.addToCart(restaurantsId,1);
                    restaurant_available=resultSet.next();
                }
                System.out.print("Enter Restaurant ID to view menu: ");

                int restaurant_id=0;
                while (true) {
                    try {
                        restaurant_id = scanner.nextInt();
                        if (showed_restaurants.itemPresent(restaurant_id))
                        {
                            searchByRestaurantsId(restaurant_id);
                            break;
                        }
                        else {
                            System.out.print("Invalid selection. Please choose from the list: ");
                        }
                    } catch (InputMismatchException e) {
                        System.out.print("Invalid input. Please enter a valid restaurant ID: ");
                        scanner.nextLine();
                    }
                }
            }
            else {
                System.out.println("No restaurants found with that name");
                search();
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
    public void searchByRestaurantsId(int id)
    {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("           RESTAURANT MENU           ");
        System.out.println("=".repeat(60));

        try {
            String search_query="select id,restaurant_name ,food_name,price from food where restaurant_id=?;";
            PreparedStatement preparedStatement= connection.prepareStatement(search_query);
            preparedStatement.setInt(1,id);
            ResultSet resultSet=preparedStatement.executeQuery();
            while (resultSet.next())
            {
                int foodId=resultSet.getInt("id");
                String restaurantName = resultSet.getString("restaurant_name");
                String foodName=resultSet.getString("food_name");
                double foodPrice=resultSet.getDouble("price");
                System.out.println(foodId+". "+foodName+" "+foodPrice);
                showed_item.addToCart(foodId,1);
            }
            cart.cart(showed_item,1);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
    public void searchByFoodName(){
        System.out.println("\n" + "=".repeat(50));
        System.out.println("           FOOD SEARCH           ");
        System.out.println("=".repeat(50));
        System.out.print("Enter food name: ");
        String foodName = scanner.next();
        String search_query = "select id,restaurant_name ,food_name,price from food where food_name like '%" + foodName + "%'";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(search_query);
            boolean foodAvailable = resultSet.next();
            if (foodAvailable) {
                System.out.println("\nSearch Results:");
                while (foodAvailable) {
                    int foodId=resultSet.getInt("id");
                    String restaurantName=resultSet.getString("restaurant_name");
                    String food_name=resultSet.getString("food_name");
                    double foodPrice=resultSet.getDouble("price");
                    showed_item.addToCart(foodId,1);
                    System.out.println(foodId + ". " + food_name + " " +  restaurantName+ " " +foodPrice );
                    foodAvailable= resultSet.next();
                }
                cart.cart(showed_item,1);
            } else {
                System.out.println("No food items found matching: '" + foodName + "'");
                System.out.println("Please try a different search term");
                search();
            }
        } catch (SQLException e) {
            System.out.println("Database error during search: " + e.getMessage());
        }
    }
    public void displayAllFoodItems(){
        System.out.println("\n" + "=".repeat(70));
        System.out.println("           ALL FOOD ITEMS           ");
        System.out.println("=".repeat(70));

        String display_query="select*from food";
        try {
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery(display_query);

            boolean hasItems = false;
            String currentRestaurant = "";

            while (resultSet.next()) {
                hasItems = true;
                int foodId = resultSet.getInt("id");
                String restaurantName = resultSet.getString("restaurant_name");
                String foodName = resultSet.getString("food_name");
                double foodPrice = resultSet.getDouble("price");

                if (!restaurantName.equals(currentRestaurant)) {
                    currentRestaurant = restaurantName;
                    System.out.println("-".repeat(70));
                    System.out.println("RESTAURANT: " + currentRestaurant.toUpperCase());
                    System.out.println("-".repeat(70));
                }
                showed_item.addToCart(foodId,1);
                System.out.println(foodId+" | "+foodName+" | "+restaurantName+" | "+foodPrice);
            }
            if (hasItems) {
                System.out.println("-".repeat(70));
                cart.cart(showed_item, 1);
            } else {
                System.out.println("No food items currently available");
                System.out.println("Please check back later");
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}