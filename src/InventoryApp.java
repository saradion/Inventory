import javax.swing.*;
import java.util.*;

    // Parent class
    abstract class Product {
        protected static int counter = 1;
        protected int id;
        protected String name;
        protected double price;
        protected int stock;

        public Product(String name, double price, int stock) {
            this.id = counter++;
            this.name = name;
            this.price = price;
            this.stock = stock;
        }

        // Abstract method
        public abstract String getDetails();

        @Override
        public String toString() {
            return "ID: " + id + " | " + name + " | $" + price + " | Stock: " + stock;
        }
    }

    // Electronics subclass
    class ElectronicProduct extends Product {
        private int warrantymonths;

        public ElectronicProduct(String name, double price, int stock, int warrantymonths) {
            super(name, price, stock);
            this.warrantymonths = warrantymonths;
        }

        @Override
        public String getDetails() {
            return toString() + " | Warranty: " + warrantymonths + " months";
        }
    }

    // Food subclass
    class FoodProduct extends Product {
        private String expiredate;

        public FoodProduct(String name, double price, int stock, String expiredate) {
            super(name, price, stock);
            this.expiredate = expiredate;
        }

        @Override
        public String getDetails() {
            return toString() + " | Expires: " + expiredate;
        }
    }

    //Inventory Manager
    class Inventory {
        private ArrayList<Product> items = new ArrayList<>();

        // Add product to inventory
        public void add(Product p) {
            items.add(p);
            JOptionPane.showMessageDialog(null, p.name + " added to inventory.");
        }

        // Show all products
        public void showAll() {
            if (items.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Inventory is empty!");
                return;
            }
            StringBuilder sb = new StringBuilder("Inventory:\n");
            double total = 0;
            for (Product p : items) {
                sb.append(p.getDetails()).append("\n");
                if (p.stock < 3) sb.append("Low stock alert!\n");
                total += p.price * p.stock;
            }
            sb.append("\nTotal inventory value: $" + total);
            JOptionPane.showMessageDialog(null, sb.toString());
        }

        // Sell a product by ID
        public void sell(int id, int amount) {
            for (Product p : items) {
                if (p.id == id) {
                    if (p.stock >= amount) {
                        p.stock -= amount;
                        JOptionPane.showMessageDialog(null, "Sold " + amount + " of " + p.name);
                    } else {
                        JOptionPane.showMessageDialog(null, "Not enough stock for " + p.name);
                    }
                    return;
                }
            }
            JOptionPane.showMessageDialog(null, "Product ID not found!");
        }

        // Find by name
        public void search(String name) {
            for (Product p : items) {
                if (p.name.equalsIgnoreCase(name)) {
                    JOptionPane.showMessageDialog(null, "Found: " + p.getDetails());
                    return;
                }
            }
            JOptionPane.showMessageDialog(null, "Product not found.");
        }
    }

    //Main app logic
    public class InventoryApp {
        public static void main(String[] args) {
            Inventory inv = new Inventory();

            // Preload some products
            inv.add(new ElectronicProduct("Laptop", 50000, 5, 24));
            inv.add(new FoodProduct("Bread", 2, 2, "2025-11-01"));
            inv.add(new FoodProduct("Milk", 5, 10, "2025-10-30"));

            // Menu loop
            while (true) {
                String[] options = {"Add", "Show All", "Sell", "Search", "Exit"};
                String choice = (String) JOptionPane.showInputDialog(null, "Choose an action:", "Inventory System",
                        JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                if (choice == null || choice.equals("Exit")) break;

                switch (choice) {
                    case "Add":
                        String type = (String) JOptionPane.showInputDialog(null, "Select product type:", "Add Product",
                                JOptionPane.QUESTION_MESSAGE, null, new String[]{"Electronic", "Food"}, "Electronic");

                        String name = JOptionPane.showInputDialog("Enter product name:");
                        double price = Double.parseDouble(JOptionPane.showInputDialog("Enter price:"));
                        int stock = Integer.parseInt(JOptionPane.showInputDialog("Enter stock quantity:"));

                        if (type.equals("Electronic")) {
                            int warranty = Integer.parseInt(JOptionPane.showInputDialog("Warranty (months):"));
                            inv.add(new ElectronicProduct(name, price, stock, warranty));
                        } else {
                            String exp = JOptionPane.showInputDialog("Expiry date (YYYY-MM-DD):");
                            inv.add(new FoodProduct(name, price, stock, exp));
                        }
                        break;

                    case "Show All":
                        inv.showAll();
                        break;

                    case "Sell":
                        int id = Integer.parseInt(JOptionPane.showInputDialog("Enter product ID to sell:"));
                        int amt = Integer.parseInt(JOptionPane.showInputDialog("Quantity to sell:"));
                        inv.sell(id, amt);
                        break;

                    case "Search":
                        String searchName = JOptionPane.showInputDialog("Enter product name to search:");
                        inv.search(searchName);
                        break;
                }
            }

            JOptionPane.showMessageDialog(null, "Exiting");
        }
    }
