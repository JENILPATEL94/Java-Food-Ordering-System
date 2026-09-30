package Swiggy.Billing;

import Swiggy.Main.Orders;
import Swiggy.Main.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Cart
{
    Connection connection;
    Scanner scanner;
    Orders orders;
    PaymentWithUPI paymentWithUPI;
    Payment payment;
    User user;
    public Cart(Connection connection, Scanner scanner,Orders orders,User user) {
        this.connection = connection;
        this.scanner = scanner;
        this.orders=orders;
        this.paymentWithUPI=paymentWithUPI;
        this.payment=payment;
        this.user=user;
    }
    DLL itemCart=new DLL();
    public int fromWhere=-1;
    public boolean cartIsEmpty()
    {
        if (itemCart.first==null)
        {
            return true;
        }
        else {
            return false;
        }
    }
    public void cart(DLL dll,int fromWhere)
    {
        this.fromWhere=fromWhere;
        int food_id=0;
        boolean addMore=true;
        System.out.println("Enter Item ID to add to cart: ");
        while (addMore) {
            while (true) {
                try {

                    food_id = scanner.nextInt();
                    if (dll.itemPresent(food_id)) {
                        int quantity = 0;
                        while (true) {
                            System.out.print("Enter quantity: ");
                            try {
                                quantity = scanner.nextInt();
                                break;
                            } catch (InputMismatchException e) {
                                System.out.println("Invalid input. Please enter a number");
                                scanner.nextLine();
                            }
                        }
                        itemCart.addToCart(food_id,quantity);
                        System.out.println("Item added to cart successfully");
                        break;
                    } else {
                        System.out.print("Invalid selection. Please choose an item from the list: ");
                    }

                } catch (InputMismatchException e) {
                    System.out.print("Invalid input. Please enter a valid item ID: ");
                    scanner.nextLine();
                }
            }
            while (true) {
                try {
                    System.out.println("\nCart Options:");
                    System.out.println("1. Add more items");
                    System.out.println("2. View cart");
                    System.out.println("0. Continue browsing");
                    System.out.print("Please select an option: ");
                    int choice = scanner.nextInt();
                    if (choice == 0) {
                        return;
                    } else if (choice == 1) {
                        break;
                    } else if (choice == 2) {
                        ShowBuyingItem(fromWhere);
                        editCart(fromWhere);
                        addMore=false;
                        break;
                    } else {
                        System.out.println("Invalid selection. Please choose between 0-2");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number");
                    scanner.nextLine();
                }
            }
        }
    }
    public void editCart(int fromWhere)
    {
        if (itemCart.first==null)
        {
            System.out.println("Cart is empty");
        }
        else {
            while (true) {
                System.out.println("\n" + "=".repeat(50));
                System.out.println("              CART MANAGEMENT              ");
                System.out.println("=".repeat(50));
                System.out.println("1. Add item");
                System.out.println("2. Remove items from cart");
                System.out.println("3. Place order");
                System.out.println("0. Return to previous menu");
                System.out.println("-".repeat(50));
                int choice1 = -1;
                while (true) {
                    try {
                        System.out.print("Please select an option: ");
                        choice1 = scanner.nextInt();
                        break;
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Please enter a number between 0-3");
                        scanner.nextLine();
                    }
                }
                switch (choice1) {
                    case 1:
                        return;
                    case 2:
                        int deleteItemId = -1;
                        int deleteQuantity=0;
                        while (true) {
                            try {
                                System.out.print("Enter ID of item to remove: ");
                                deleteItemId = scanner.nextInt();
                                break;
                            } catch (InputMismatchException e) {
                                System.out.println("Invalid input. Please enter a valid item ID");
                                scanner.nextLine();
                            }
                        }
                        if (itemCart.itemPresent(deleteItemId)) {
                            while (true) {
                                try {
                                    System.out.print("Enter quantity to remove: ");
                                    deleteQuantity=scanner.nextInt();
                                    break;
                                } catch (InputMismatchException e)
                                {
                                    System.out.println("Quantity must be integer");
                                }
                            }
                            itemCart.removeItem(deleteItemId,deleteQuantity);
                            System.out.println("✓ Cart updated successfully");
                            System.out.println();
                            ShowBuyingItem(fromWhere);
                        } else {
                            System.out.println("No item found with that ID in your cart");
                        }
                        break;
                    case 3:
                        placeOrder(fromWhere);
                        return;
                    case 0:
                        System.out.println("Returning to previous menu...");
                        return;
                    default:
                        System.out.println("Invalid selection. Please choose between 0-3");
                }
            }
        }
    }
    public void ShowBuyingItem(int fromWhere)
    {
        if (fromWhere==1)
        {
            System.out.println("                         FOOD ORDER CART                         ");
            DLL.node temp =itemCart.first;
            while (temp!=null)
            {
                String sql="select id,food_name,restaurant_name,price from food where id=?";
                try {
                    PreparedStatement preparedStatement= connection.prepareStatement(sql);
                    preparedStatement.setInt(1,temp.data);
                    ResultSet resultSet=preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        int food_id = resultSet.getInt("id");
                        String food_name = resultSet.getString("food_name");
                        String restaurant_name = resultSet.getString("restaurant_name");
                        double food_price = resultSet.getDouble("price");
                        int quantity= temp.quantity;
                        System.out.println("ID: "+food_id + ".  food name: " + food_name + " | restaurant name: " + restaurant_name + " | price: " + food_price+" | Quantity: "+quantity);
                    }
                    temp=temp.next;
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                    scanner.nextLine();
                }
            }
        }
        else {
            DLL.node temp =itemCart.first;
            System.out.println("                       INSTAMART ORDER CART                      ");
            while (temp!=null)
            {
                String sql="select * from instamart_item where item_id=?";
                try {
                    PreparedStatement preparedStatement= connection.prepareStatement(sql);
                    preparedStatement.setInt(1,temp.data);
                    ResultSet resultSet=preparedStatement.executeQuery();

                    while (resultSet.next()) {
                        int item_id = resultSet.getInt("item_id");
                        String item_name = resultSet.getString("item_name");
                        String item_category = resultSet.getString("item_category");
                        double item_price = resultSet.getDouble("item_price");
                        int quantity= temp.quantity;
                        System.out.println(" ID:"+item_id + ".  item name: " + item_name + " | category: " + item_category + " | price: " + item_price+" | Quantity: "+quantity);
                    }
                    temp=temp.next;
                } catch (SQLException e) {
                    System.out.println("Database error: " + e.getMessage());
                }
            }
        }

    }
    public void placeOrder(int fromWhere)
    {
        double billWithoutGST=0;
        double totalbill=0;
        if (fromWhere==1)
        {
            DLL.node temp =itemCart.first;
            System.out.println("\n" + "=".repeat(60));
            System.out.println("                     ORDER SUMMARY                     ");
            System.out.println("=".repeat(60));
            while (temp!=null)
            {
                String sql="select id,food_name,restaurant_name,price from food where id=?";
                try {
                    PreparedStatement preparedStatement= connection.prepareStatement(sql);
                    preparedStatement.setInt(1,temp.data);
                    ResultSet resultSet=preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        int food_id = resultSet.getInt("id");
                        String food_name = resultSet.getString("food_name");
                        String restaurant_name = resultSet.getString("restaurant_name");
                        double food_price = resultSet.getDouble("price");
                        int quantity= temp.quantity;
                        billWithoutGST=billWithoutGST+(food_price*quantity);
                        this.print1=" "+print1+"\n"+" ID:"+food_id + ".  food name: " + food_name + " | restaurant name: " + restaurant_name + " | price: " + food_price+" | Quantity: "+quantity;
                        System.out.println("ID: "+food_id + ".  food name: " + food_name + " | restaurant name: " + restaurant_name + " | price: " + food_price+" | Quantity: "+quantity);
                    }
                    temp=temp.next;
                } catch (SQLException e) {
                    System.out.println("Database error: " + e.getMessage());
                    scanner.nextLine();
                }
            }
            totalbill=showBill(billWithoutGST);
        }
        else {
            DLL.node temp =itemCart.first;
            while (temp!=null)
            {
                String sql="select * from instamart_item where item_id=?";
                try {
                    PreparedStatement preparedStatement= connection.prepareStatement(sql);
                    preparedStatement.setInt(1,temp.data);
                    ResultSet resultSet=preparedStatement.executeQuery();

                    while (resultSet.next()) {
                        int item_id = resultSet.getInt("item_id");
                        String item_name = resultSet.getString("item_name");
                        String item_category = resultSet.getString("item_category");
                        double item_price = resultSet.getDouble("item_price");
                        int quantity= temp.quantity;
                        billWithoutGST=billWithoutGST+(item_price*quantity);
                        this.print1=""+print1+"\n"+" ID:"+item_id + ".  item name: " + item_name + " | category: " + item_category + " | price: " + item_price+" | Quantity: "+quantity;
                        System.out.println(" ID:"+item_id + ".  item name: " + item_name + " | category: " + item_category + " | price: " + item_price+" | Quantity: "+quantity);
                    }
                    temp=temp.next;
                } catch (SQLException e) {
                    System.out.println("Database error: " + e.getMessage());
                }
            }
            System.out.println("=".repeat(80));
            totalbill = showBill(billWithoutGST);
            System.out.println("=".repeat(80));
        }
        System.out.println("YOUR TOTAL BILL IS :- " + totalbill);

        while (true) {
            System.out.print("\nConfirm order (yes/no): ");
            String choice = scanner.next();
            if (choice.toUpperCase().equals("YES")) {
                if (processPayment(totalbill)) {
                    orders.newOrder(print1, print2);
                    System.out.println("✓ Order placed successfully!");
                    System.out.println("Thank you for your order. It will be prepared and delivered soon.");
                    itemCart.first = null;
                    return;
                } else {
                    System.out.println("Order cancelled due to payment failure.");
                    break;
                }
            } else if (choice.toUpperCase().equals("NO")) {
                System.out.println("Order cancelled. Your cart has been preserved.");
                break;
            } else {
                System.out.println("Please enter 'yes' to confirm or 'no' to cancel");
            }
            print1="";
            print2="";
        }
    }

    private boolean processPayment(double amount) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("           PAYMENT OPTIONS           ");
        System.out.println("=".repeat(50));
        System.out.println("1. UPI Payment");
        System.out.println("2. Cash on Delivery");
        System.out.println("0. Cancel Order");
        System.out.println("-".repeat(50));

        int paymentChoice = -1;
        boolean isRunning=true;
        while (isRunning) {
            while (true) {
                try {
                    System.out.print("Please select a payment method: ");
                    paymentChoice = scanner.nextInt();
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number between 0-2");
                    scanner.nextLine();
                }
            }

            switch (paymentChoice) {
                case 1:

                    this.payment = new PaymentWithUPI(amount, "" + user.getMobileNumber());
                    isRunning=false;
                    break;
                case 2:
                    this.payment = new PaymentWithCashOnDelivery(amount);
                    isRunning=false;
                    break;
                case 0:
                    System.out.println("Order cancelled.");
                    return false;
                default:
                    System.out.println("Invalid selection.");
            }
        }
        return payment.processPayment();
    }

    String print1="";
    String print2="";
    public double showBill(double billWithoutGST)
    {
        double totalBillWithGST=0;
        double deliveryCharge=0;
        double GST=billWithoutGST*0.05;
        if (billWithoutGST>=100)
        {
            totalBillWithGST=billWithoutGST+GST;
        }
        else
        {
            deliveryCharge=100;
            totalBillWithGST=billWithoutGST+GST+deliveryCharge;
        }
        this.print2="Subtotal: "+billWithoutGST+"\n GST & Charges :- "+GST+"\n Delivery Charge: "+deliveryCharge+"\n TOTAL AMOUNT: "+totalBillWithGST;
        System.out.println("Your bill :- "+billWithoutGST);
        System.out.println("GST & CHARGES :- "+GST);
        System.out.println("DELIVERY CHARGE :- "+deliveryCharge);
        System.out.println("TOTAL BILL :- "+totalBillWithGST);
        return totalBillWithGST;
    }

}