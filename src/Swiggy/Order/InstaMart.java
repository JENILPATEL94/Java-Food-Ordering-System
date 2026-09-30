package Swiggy.Order;
import Swiggy.Billing.Cart;
import Swiggy.Billing.DLL;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class InstaMart {
    Connection connection;
    Scanner scanner;
    Cart cart;
    public InstaMart(Connection connection, Scanner scanner, Cart cart) {
        this.connection = connection;
        this.scanner = scanner;
        this.cart=cart;
    }
    DLL showed_item=new DLL();
    public void instamart(){
        while (true) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("           SWIGGY INSTA MART           ");
            System.out.println("=".repeat(50));
            System.out.println("1. Search Items");
            System.out.println("2. Browse by Category");
            System.out.println("3. View All Products");
            System.out.println("4. View Cart");
            System.out.println("0. Return to Main Menu");
            System.out.println("-".repeat(50));
            showed_item.first = null;
            int choice = -1;
            while (true) {
                try {
                    System.out.print("Please select an option: ");
                    choice = scanner.nextInt();
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number between 0-4");
                    scanner.nextLine();
                }
            }
            switch (choice) {
                case 1: searchByItemName();
                    break;
                case 2: searchByCategory();
                    break;
                case 3: showAllItem();
                    break;
                case 4:
                    cart.ShowBuyingItem(0);
                    cart.editCart(0);
                    break;
                case 0:
                    System.out.println("Returning to main menu...");
                    return;
                default:
                    System.out.println("Invalid selection. Please choose between 0-4");
                    break;
            }
        }
    }
    public void searchByItemName(){
        while (true) {
            try {
                System.out.println("\n" + "=".repeat(50));
                System.out.println("           ITEM SEARCH           ");
                System.out.println("=".repeat(50));
                System.out.print("Enter item name to search: ");
                String itemName = scanner.next();
                String search_query = "select * from instamart_item where item_name like '%" + itemName + "%'";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(search_query);
                boolean itemAvailable = resultSet.next();
                if (itemAvailable) {
                    while (itemAvailable) {
                        int item_id=resultSet.getInt("item_id");
                        String item_name=resultSet.getString("item_name");
                        double item_price=resultSet.getDouble("item_price");
                        showed_item.addToCart(item_id,1);
                        System.out.println( item_id+ ". " +  item_name + " " +item_price);
                        itemAvailable = resultSet.next();
                    }
                    cart.cart(showed_item,0);
                    break;
                } else {
                    System.out.println("No items found matching your search: '" + itemName + "'");
                    System.out.println("Please try a different search term");
                    instamart();
                }
            } catch (SQLException e) {
                System.out.println("Database error during search: " + e.getMessage());
            }
        }
    }
    void searchByCategory()
    {
        while (true) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("           BROWSE BY CATEGORY           ");
            System.out.println("=".repeat(50));
            System.out.println("1. Vegetables");
            System.out.println("2. Fruits");
            System.out.println("3. Dairy Products");
            System.out.println("4. Cereals & Grains");
            System.out.println("5. Beverages");
            System.out.println("0. Back to InstaMart Menu");
            System.out.println("-".repeat(50));
            showed_item.first=null;
            try {
                System.out.print("Please select a category: ");
                int choice = scanner.nextInt();

                    switch (choice) {
                        case 1:
                            showItemByCategory("Vegetables");
                            break;
                        case 2:
                            showItemByCategory("Fruits");
                            break;
                        case 3:
                            showItemByCategory("Dairy");
                            break;
                        case 4:
                            showItemByCategory("Cereal");
                            break;
                        case 5:
                            showItemByCategory("Drinks");
                            break;
                        case 0:
                            System.out.println("Returning to InstaMart menu...");
                            return;
                        default:
                            System.out.println("Invalid selection. Please choose between 0-5");
                    }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number between 0-5");
                scanner.nextLine();
            }
        }
    }
    void showItemByCategory(String category)
    {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("           " + category.toUpperCase() + " CATEGORY           ");
        System.out.println("=".repeat(60));
        System.out.println("ID   Item Name                 Price");
        System.out.println("-".repeat(60));

        String sql = "select * from instamart_item where item_category=?";
        try {
            PreparedStatement preparedStatement= connection.prepareStatement(sql);
            preparedStatement.setString(1,category);
            ResultSet resultSet = preparedStatement.executeQuery();
            boolean itemAvailable = resultSet.next();
            if (itemAvailable)
            {
                while (itemAvailable) {
                    int item_id=resultSet.getInt("item_id");
                    String item_name=resultSet.getString("item_name");
                    String item_category= resultSet.getString("item_category");
                    double item_price=resultSet.getDouble("item_price");
                    showed_item.addToCart(item_id,1);
                    System.out.println(item_id + ". " + item_name + " " +  item_price );
                    itemAvailable= resultSet.next();
                }
                cart.cart(showed_item,0);
            }
            else{
                System.out.println("No items currently available in the " + category + " category");
                System.out.println("Please check back later or browse other categories");
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
    void showAllItem()
    {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("           ALL INSTAMART PRODUCTS           ");
        System.out.println("=".repeat(70));

        String sql="select * from instamart_item";
        try {
            Statement statement= connection.createStatement();
            ResultSet resultSet=statement.executeQuery(sql);
            boolean itemAvailable = resultSet.next();
            if (itemAvailable)
            {
                while (itemAvailable) {
                    int item_id=resultSet.getInt("item_id");
                    String item_name=resultSet.getString("item_name");
                    String item_category= resultSet.getString("item_category");
                    double item_price=resultSet.getDouble("item_price");
                    showed_item.addToCart(item_id,1);
                    System.out.println(item_id + ". " + item_name + " "+item_category+" " +  item_price );
                    itemAvailable= resultSet.next();
                }
                cart.cart(showed_item,0);
            }
            else{
                System.out.println("No products currently available in InstaMart");
                System.out.println("Please check back later");
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}