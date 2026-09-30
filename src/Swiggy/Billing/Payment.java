package Swiggy.Billing;

import Swiggy.Main.User;

import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class Payment {
    protected double amount;
    protected boolean paymentStatus;
    public Payment(double amount) {
        this.amount = amount;
        this.paymentStatus = false;
    }

    public boolean processPayment() {
        return paymentStatus;
    }

    public String getPaymentDetails() {
        return "Amount: ₹" + amount + ", Status: " + (paymentStatus ? "Success" : "Failed");
    }
}

class PaymentWithUPI extends Payment {
    private String upiId;
    private final String OTP_FILE = "C:\\Users\\Vedant\\Downloads\\payment_otp.txt";
    public String OTP;
    public PaymentWithUPI(double amount, String upiId) {
        super(amount);
        this.upiId = upiId;
        this.OTP=generateOTP();
    }

    public boolean processPayment() {
        System.out.println("Processing UPI Payment...");
        System.out.println("UPI ID: " + upiId);
        System.out.println("Amount: ₹" + amount);

        saveOTPToFile(this.OTP);

        System.out.println("OTP has been saved to " + OTP_FILE);
        System.out.println("Please check the file and enter the OTP to complete payment.");

        // Verify OTP
        if (verifyOTP()) {
            paymentStatus = true;
            System.out.println("✓ UPI Payment Successful!");
            return true;
        } else {
            System.out.println("✗ Payment Failed: Invalid OTP");
            return false;
        }
    }

    private String generateOTP() {
        Random random = new Random();
        return ""+random.nextInt(100000,1000000);
    }

    private void saveOTPToFile(String otp) {
        try  {
            FileWriter writer = new FileWriter(OTP_FILE);
            writer.write("SWIGGY PAYMENT OTP\n");
            writer.write("==================\n");
            writer.write("Amount: ₹" + amount + "\n");
            writer.write("UPI ID: " + upiId + "\n");
            writer.write("OTP: " + this.OTP + "\n");
            writer.write("==================\n");
            writer.write("Enter this OTP in the application to complete payment\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println("Error creating OTP file: " + e.getMessage());
        }
    }

    private boolean verifyOTP() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter OTP from file: ");
        String enteredOTP = scanner.next().trim();

        if (enteredOTP.equals(this.OTP))
        {
            return true;
        }
        else {
            return false;
        }
    }

    public String getPaymentDetails() {
        return super.getPaymentDetails() + ", UPI ID: " + upiId ;
    }
}

class PaymentWithCashOnDelivery extends Payment {
    public PaymentWithCashOnDelivery(double amount) {
        super(amount);
    }

    public boolean processPayment() {
        System.out.println("\nCash on Delivery Selected");
        System.out.println("Amount to pay on delivery: ₹" + amount);
        System.out.println("Please keep exact change ready for the delivery executive.");
        paymentStatus = true; // COD is always successful as payment happens later
        return true;
    }

    public String getPaymentDetails() {
        return super.getPaymentDetails() + ", Method: Cash on Delivery";
    }
}