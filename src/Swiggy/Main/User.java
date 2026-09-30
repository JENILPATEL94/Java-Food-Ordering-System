package Swiggy.Main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class User
{
    private Connection connection;
    private Scanner scanner;


    public User(Connection connection, Scanner scanner)
    {
        this.connection = connection;
        this.scanner = scanner;
    }
    String name="";
    long mobileNumber;
    public void register()
    {
        System.out.println("=".repeat(50));
        System.out.println("           NEW ACCOUNT REGISTRATION           ");
        System.out.println("=".repeat(50));
        System.out.print("First Name: ");
        String first_name=scanner.next();
        System.out.print("Last Name: ");
        String last_name=scanner.next();
        String full_name = first_name+last_name;
        long mobileNumber=0;
        while (true) {
            try {
                System.out.print("Mobile Number: ");
                mobileNumber= scanner.nextLong();
                if (mobileNumber/1000000000!=9&&mobileNumber/1000000000!=8&&mobileNumber/1000000000!=7&&mobileNumber/1000000000!=6)
                {
                    System.out.println("Invalid mobile number. Please enter a valid 10-digit number starting with 6, 7, 8, or 9");
                }
                else {
                    break;
                }
            }catch (InputMismatchException e) {
                System.out.println("Invalid input. Mobile number must contain only digits");
                scanner.nextLine();
            }
        }
        if (user_exist(mobileNumber))
        {
            System.out.println("An account already exists with this mobile number. Please login instead");
            return;
        }
        System.out.print("Password: ");
        String password = scanner.next();
        String register_query="Insert into user (name,mobile_number,password) values(?,?,?)";
        try {
            PreparedStatement preparedStatement= connection.prepareStatement(register_query);
            preparedStatement.setString(1,full_name);
            preparedStatement.setLong(2,mobileNumber);
            preparedStatement.setString(3,password);
            int effectedRows = preparedStatement.executeUpdate();
            preparedStatement.close();
            if (effectedRows > 0) {
                System.out.println("Registration successful! You can now login with your credentials");
            } else {
                System.out.println("Registration failed. Please try again");
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());

        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }
    public boolean login(long mobileNumber,String password)
    {
        String login_query="Select * from user where Mobile_number= ? and password = ?";
        try {
            PreparedStatement preparedStatement= connection.prepareStatement(login_query);
            preparedStatement.setLong(1,mobileNumber);
            preparedStatement.setString(2,password);
            ResultSet resultSet= preparedStatement.executeQuery();
            if (resultSet.next())
            {
                setName(resultSet.getString("name"));
                setMobileNumber(mobileNumber);
                System.out.println("Login successful! Welcome back, " + this.name);
                return true;
            }else {
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return false;
    }
    public boolean user_exist(long mobileNumber)
    {
        String query= "select * from user where Mobile_number = ?";
        try {
            PreparedStatement preparedStatement= connection.prepareStatement(query);
            preparedStatement.setLong(1,mobileNumber);
            ResultSet resultSet= preparedStatement.executeQuery();
            if (resultSet.next())
            {
                return true;
            }
            else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return false;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMobileNumber(long mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getName() {
        return name;
    }

    public long getMobileNumber() {
        return mobileNumber;
    }
}
