package Swiggy.Main;

import java.io.*;
import java.sql.*;
import java.util.Scanner;

public class Orders {
    Connection connection;
    Scanner scanner;
    User user;

    public Orders(Connection connection, Scanner scanner, User user) {
        this.connection = connection;
        this.scanner = scanner;
        this.user = user;
    }

    public void newOrder(String print1, String print2) {
        File file = new File("new1.txt");
        try {
            file.createNewFile();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        FileReader fileReader = null;
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("LIST OF ITEMS \n");
            fileWriter.write(print1 + "\n \n \n");
            fileWriter.write("BIll \n \n");
            fileWriter.write(print2);
            fileWriter.close();
            fileReader = new FileReader(file);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        String sql = "INSERT INTO order_history (user_name,mobile_number,bill) values(?,?,?);";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setLong(2, user.getMobileNumber());
            preparedStatement.setClob(3, fileReader);
            int r = preparedStatement.executeUpdate();
            System.out.println(r);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void showMyOrders()
    {
        String sql="Select bill,date from order_history where mobile_number=?";
        try {
            PreparedStatement preparedStatement= connection.prepareStatement(sql);
            preparedStatement.setLong(1,user.getMobileNumber());
            ResultSet resultSet= preparedStatement.executeQuery();
            FileWriter fileWriter=new FileWriter("C:\\Users\\Vedant\\Downloads\\"+user.getName()+"_bill.txt");
            while(resultSet.next())
            {
                Reader reader=resultSet.getCharacterStream("bill");
                String date = resultSet.getString("date");
                fileWriter.write("DATE : "+date);
                fileWriter.write("\n \n");
                int i;
                while ((i= reader.read())!=-1)
                {
                    fileWriter.write(i);
                }
                fileWriter.write("\n \n===============================================================\n \n \n");
            }
            fileWriter.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
