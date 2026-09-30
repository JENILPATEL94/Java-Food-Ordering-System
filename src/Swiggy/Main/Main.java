package Swiggy.Main;

import Swiggy.Billing.Cart;
import Swiggy.Billing.Payment;
import Swiggy.Order.FoodOrder;
import Swiggy.Order.InstaMart;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main
{
    private static final String url = "jdbc:mysql://localhost:3306/Swiggy";
    private static final String username = "root";
    private static final String password = "";
    public static void main(String[] args) {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Scanner scanner = new Scanner(System.in);
            User user = new User(connection,scanner);
            Orders orders=new Orders(connection,scanner,user);
            Cart cart=new Cart(connection,scanner,orders,user);
            FoodOrder foodOrder=new FoodOrder(connection,scanner,cart);
            InstaMart instaMart=new InstaMart(connection,scanner,cart);
            if (connection != null) {
                System.out.println("Database connection established successfully");
            } else {
                System.out.println("Failed to establish database connection");
            }
            while (true)
            {
                System.out.println("=======================================");
                System.out.println("           SWIGGY CLONE             ");
                System.out.println("=======================================");

                System.out.println();
                System.out.println("1. Register New Account");
                System.out.println("2. Login to Existing Account");
                System.out.println("0. Exit Applic");
                System.out.print("Please enter your choice: ");
                int choice1=-1;
                try {
                    choice1 = scanner.nextInt();
                }catch (InputMismatchException e)
                {
                    System.out.println("Invalid input. Please enter a number between 0-2");
                    scanner.nextLine();
                    continue;
                }
                switch (choice1)
                {
                    case 1:
                        user.register();
                        break;
                    case 2:
                        long mobileNumber=0;
                        System.out.println("=======================================");
                        System.out.println("             USER LOGIN                ");
                        System.out.println("=======================================");
                        while (true) {
                            try {
                                System.out.print("Enter Mobile Number: ");
                                mobileNumber = scanner.nextLong();
                                System.out.println();
                                break;
                            }catch (InputMismatchException e)
                            {
                                System.out.println("Invalid input. Mobile number must contain only digits");
                                scanner.nextLine();
                            }
                        }
                        System.out.print("Enter Password: ");
                        String password=scanner.next();
                        if (user.login(mobileNumber,password))
                        {
                            System.out.print("Enter your delivery address: ");
                            String userAddress=scanner.nextLine();
                            scanner.nextLine();
                            boolean returnBack=false;
                            while (!returnBack)
                            {
                                System.out.println("===============================");
                                System.out.println("           MAIN MENU           ");
                                System.out.println("===============================");
                                System.out.println("1. Food Delivery");
                                System.out.println("2. InstaMart (Groceries)");
                                System.out.println("3. Order History");
                                System.out.println("0. Return to Login menu");
                                System.out.println("================================");
                                System.out.print("Enter your choice: ");
                                int choice2=-1;
                                try {
                                    choice2 = scanner.nextInt();
                                }catch (InputMismatchException e)
                                {
                                    System.out.println("Invalid input. Please enter a number");
                                    scanner.nextLine();
                                    continue;
                                }
                                switch (choice2)
                                {
                                    case 1:
                                        if (!cart.cartIsEmpty()&& cart.fromWhere==0)
                                        {
                                            System.out.println("Please complete your InstaMart order before starting a food order");
                                        }
                                        else {
                                            foodOrder.search();
                                        }
                                        break;
                                    case 2:
                                        if (!cart.cartIsEmpty()&&cart.fromWhere==1)
                                        {
                                            System.out.println("Please complete your food order before starting an InstaMart order");
                                        }
                                        else {
                                            instaMart.instamart();
                                        }
                                        break;
                                    case 3:orders.showMyOrders();
                                        System.out.println("Your order history has been saved to a file");
                                        break;
                                    case 0:returnBack=true;
                                        break;
                                    default:
                                        System.out.println("Invalid input. Please enter a number between 0-3");
                                }
                            }
                        }
                        else {
                            System.out.println(" LOGIN FAILED: INVALID MOBILE NUMBER OR PASSWORD, PLEASE TRY AGAIN.");
                        }
                        break;
                    case 0:
                        System.out.println("Thank you for using our service");
                        System.out.println("Exiting application...");
                        return;
                    default:
                        System.out.println("Invalid selection. Please choose between 0-2");
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}