

import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.SwingUtilities;

public class Main {

    static Scanner sc = new Scanner(System.in);

    static ArrayList<Product> products = new ArrayList<>();
    static ArrayList<Customer> customers = new ArrayList<>();
    static ArrayList<Order> allOrders = new ArrayList<>();

    static int nextCustomerId = 1;
    static int nextPaymentId = 5001;

    public static void main(String[] args) {

        addSampleProducts();

        if (args.length > 0 && args[0].equalsIgnoreCase("terminal")) {

            // Old console version:  java Main terminal
            runTerminal();

        } else {

            // GUI version (default)
            SwingUtilities.invokeLater(() -> new LoginFrame());
        }
    }

    // =====================================================
    // ORIGINAL TERMINAL MENU (kept unchanged)
    // =====================================================

    public static void runTerminal() {

        while (true) {

            System.out.println("\n======================================");
            System.out.println(" E-COMMERCE INVENTORY & ORDER SYSTEM");
            System.out.println("======================================");

            System.out.println("1. Customer");
            System.out.println("2. Admin");
            System.out.println("3. Exit");

            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {

                customerSection();

            } else if (choice == 2) {

                adminSection();

            } else if (choice == 3) {

                System.out.println("Thank you for using the system.");
                break;

            } else {

                System.out.println("Invalid choice.");
            }
        }

        sc.close();
    }

    // =====================================================
    // SAMPLE PRODUCTS
    // =====================================================

    public static void addSampleProducts() {

        products.add(
                new Product(
                        101,
                        "Laptop",
                        "Electronics",
                        50000,
                        10
                )
        );

        products.add(
                new Product(
                        102,
                        "Mouse",
                        "Electronics",
                        800,
                        20
                )
        );

        products.add(
                new Product(
                        103,
                        "Keyboard",
                        "Electronics",
                        1500,
                        15
                )
        );

        products.add(
                new Product(
                        104,
                        "Headphones",
                        "Accessories",
                        2000,
                        5
                )
        );
    }

    // =====================================================
    // CUSTOMER SECTION
    // =====================================================

    public static void customerSection() {

        System.out.println("\n========== CUSTOMER ==========");

        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Back");

        System.out.print("Enter choice: ");
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice == 1) {

            registerCustomer();

        } else if (choice == 2) {

            loginCustomer();

        } else if (choice == 3) {

            return;

        } else {

            System.out.println("Invalid choice.");
        }
    }

    // =====================================================
    // REGISTER CUSTOMER
    // =====================================================

    public static void registerCustomer() {

        System.out.print("Enter name: ");
        String name = sc.nextLine();

        System.out.print("Enter email: ");
        String email = sc.nextLine();

        Customer customer =
                new Customer(
                        nextCustomerId,
                        name,
                        email
                );

        customers.add(customer);

        System.out.println(
                "\nCustomer registered successfully."
        );

        System.out.println(
                "Your Customer ID is: "
                        + nextCustomerId
        );

        nextCustomerId++;

        customerMenu(customer);
    }

    // =====================================================
    // LOGIN CUSTOMER
    // =====================================================

    public static void loginCustomer() {

        System.out.print("Enter Customer ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        Customer customer = findCustomer(id);

        if (customer == null) {

            System.out.println("Customer not found.");

        } else {

            System.out.println(
                    "\nWelcome, " + customer.getName()
            );

            customerMenu(customer);
        }
    }

    // =====================================================
    // FIND CUSTOMER
    // =====================================================

    public static Customer findCustomer(int id) {

        for (Customer customer : customers) {

            if (customer.getId() == id) {

                return customer;
            }
        }

        return null;
    }

    // =====================================================
    // CUSTOMER MENU
    // =====================================================

    public static void customerMenu(Customer customer) {

        while (true) {

            System.out.println("\n========== CUSTOMER MENU ==========");
            System.out.println("1. View Products");
            System.out.println("2. Place Order");
            System.out.println("3. View My Orders");
            System.out.println("4. Cancel Order");
            System.out.println("5. Logout");

            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {

                viewProducts();

            } else if (choice == 2) {

                placeOrder(customer);

            } else if (choice == 3) {

                customer.viewOrders();

            } else if (choice == 4) {

                cancelOrder(customer);

            } else if (choice == 5) {

                System.out.println("Logged out.");
                return;

            } else {

                System.out.println("Invalid choice.");
            }
        }
    }

    // =====================================================
    // VIEW PRODUCTS
    // =====================================================

    public static void viewProducts() {

        System.out.println("\n========== PRODUCTS ==========");

        if (products.isEmpty()) {

            System.out.println("No products available.");
            return;
        }

        for (Product product : products) {

            product.displayProduct();
        }
    }

    // =====================================================
    // FIND PRODUCT
    // =====================================================

    public static Product findProduct(int id) {

        for (Product product : products) {

            if (product.getProductId() == id) {

                return product;
            }
        }

        return null;
    }

    // =====================================================
    // PLACE ORDER
    // =====================================================

    public static void placeOrder(Customer customer) {

        viewProducts();

        System.out.print("\nEnter Product ID: ");
        int productId = sc.nextInt();

        Product product = findProduct(productId);

        if (product == null) {

            System.out.println("Product not found.");
            return;
        }

        System.out.print("Enter quantity: ");
        int quantity = sc.nextInt();
        sc.nextLine();

        Order order =
                new Order(customer.getId());

        try {

            order.addItem(product, quantity);

            System.out.println(
                    "\nOrder created successfully."
            );

            order.displayOrder();

            System.out.println("\n========== PAYMENT ==========");

            System.out.println("1. UPI");
            System.out.println("2. Credit Card");
            System.out.println("3. Cash on Delivery");

            System.out.print("Choose payment method: ");
            int paymentChoice = sc.nextInt();
            sc.nextLine();

            String method;

            if (paymentChoice == 1) {

                method = "UPI";

            } else if (paymentChoice == 2) {

                method = "Credit Card";

            } else if (paymentChoice == 3) {

                method = "Cash on Delivery";

            } else {

                System.out.println("Invalid payment method.");

                for (OrderItem item : order.getItems()) {

                    item.getProduct().addStock(
                            item.getQuantity()
                    );
                }

                return;
            }

            Payment payment =
                    new Payment(
                            nextPaymentId,
                            order.calculateTotal(),
                            method
                    );

            nextPaymentId++;

            payment.displayPayment();

            order.confirmOrder();

            customer.getOrders().add(order);

            allOrders.add(order);

            System.out.println(
                    "\nOrder placed successfully."
            );

            System.out.println(
                    "Order Status: "
                            + order.getStatus()
            );

        } catch (InsufficientStockException e) {

            System.out.println(
                    "\nOrder failed: "
                            + e.getMessage()
            );

        } catch (IllegalArgumentException e) {

            System.out.println(
                    "\nError: "
                            + e.getMessage()
            );
        }
    }

    // =====================================================
    // CANCEL ORDER
    // =====================================================

    public static void cancelOrder(Customer customer) {

        if (customer.getOrders().isEmpty()) {

            System.out.println("You have no orders.");
            return;
        }

        customer.viewOrders();

        System.out.print(
                "\nEnter Order ID to cancel: "
        );

        int orderId = sc.nextInt();
        sc.nextLine();

        System.out.println(cancelOrderById(customer, orderId));
    }

    // =====================================================
    // ADMIN SECTION
    // =====================================================

    public static void adminSection() {

        System.out.println("\n========== ADMIN LOGIN ==========");

        System.out.print("Enter Admin ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        if (id == 100) {

            adminMenu();

        } else {

            System.out.println("Invalid Admin ID.");
        }
    }

    // =====================================================
    // ADMIN MENU
    // =====================================================

    public static void adminMenu() {

        while (true) {

            System.out.println("\n========== ADMIN MENU ==========");
            System.out.println("1. Add Product");
            System.out.println("2. View Products");
            System.out.println("3. Update Stock");
            System.out.println("4. Delete Product");
            System.out.println("5. View All Orders");
            System.out.println("6. Low Stock Products");
            System.out.println("7. Sales Report");
            System.out.println("8. Logout");

            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {

                addProduct();

            } else if (choice == 2) {

                viewProducts();

            } else if (choice == 3) {

                updateStock();

            } else if (choice == 4) {

                deleteProduct();

            } else if (choice == 5) {

                viewAllOrders();

            } else if (choice == 6) {

                lowStockProducts();

            } else if (choice == 7) {

                salesReport();

            } else if (choice == 8) {

                System.out.println("Logged out.");
                return;

            } else {

                System.out.println("Invalid choice.");
            }
        }
    }

    // =====================================================
    // ADD PRODUCT
    // =====================================================

    public static void addProduct() {

        System.out.print("Enter Product ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter Product Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Category: ");
        String category = sc.nextLine();

        System.out.print("Enter Price: ");
        double price = sc.nextDouble();

        System.out.print("Enter Stock: ");
        int stock = sc.nextInt();
        sc.nextLine();

        try {

            Product product =
                    new Product(
                            id,
                            name,
                            category,
                            price,
                            stock
                    );

            products.add(product);

            System.out.println(
                    "Product added successfully."
            );

        } catch (IllegalArgumentException e) {

            System.out.println(
                    "Error: " + e.getMessage()
            );
        }
    }

    // =====================================================
    // UPDATE STOCK
    // =====================================================

    public static void updateStock() {

        System.out.print("Enter Product ID: ");
        int id = sc.nextInt();

        Product product = findProduct(id);

        if (product == null) {

            System.out.println("Product not found.");
            return;
        }

        System.out.print("Enter new stock: ");
        int stock = sc.nextInt();
        sc.nextLine();

        try {

            product.setStock(stock);

            System.out.println(
                    "Stock updated successfully."
            );

        } catch (IllegalArgumentException e) {

            System.out.println(
                    "Error: " + e.getMessage()
            );
        }
    }

    // =====================================================
    // DELETE PRODUCT
    // =====================================================

    public static void deleteProduct() {

        System.out.print("Enter Product ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        Product product = findProduct(id);

        if (product == null) {

            System.out.println("Product not found.");
            return;
        }

        products.remove(product);

        System.out.println(
                "Product deleted successfully."
        );
    }

    // =====================================================
    // VIEW ALL ORDERS
    // =====================================================

    public static void viewAllOrders() {

        if (allOrders.isEmpty()) {

            System.out.println("No orders found.");
            return;
        }

        System.out.println("\n========== ALL ORDERS ==========");

        for (Order order : allOrders) {

            order.displayOrder();
        }
    }

    // =====================================================
    // LOW STOCK PRODUCTS
    // =====================================================

    public static void lowStockProducts() {

        boolean found = false;

        System.out.println(
                "\n========== LOW STOCK PRODUCTS =========="
        );

        for (Product product : getLowStockProducts()) {

            product.displayProduct();

            found = true;
        }

        if (!found) {

            System.out.println(
                    "No low-stock products."
            );
        }
    }

    // =====================================================
    // SALES REPORT
    // =====================================================

    public static void salesReport() {

        double totalSales = calculateTotalSales();
        int totalOrders = countConfirmedOrders();

        System.out.println(
                "\n========== SALES REPORT =========="
        );

        System.out.println(
                "Total Orders : " + totalOrders
        );

        System.out.println(
                "Total Sales  : Rs." + totalSales
        );
    }

    // =====================================================
    // SHARED LOGIC (used by BOTH terminal menu and GUI)
    // =====================================================

    public static final int LOW_STOCK_LIMIT = 5;

    // Cancel an order: restores stock + sets status CANCELLED
    public static String cancelOrderById(Customer customer, int orderId) {

        for (Order order : customer.getOrders()) {

            if (order.getOrderId() == orderId) {

                if (order.getStatus().equals("CANCELLED")) {

                    return "Order is already cancelled.";
                }

                for (OrderItem item : order.getItems()) {

                    item.getProduct().addStock(
                            item.getQuantity()
                    );
                }

                order.setStatus("CANCELLED");

                return "Order cancelled successfully.";
            }
        }

        return "Order not found.";
    }

    // Sales report: only CONFIRMED orders count
    public static double calculateTotalSales() {

        double totalSales = 0;

        for (Order order : allOrders) {

            if (order.getStatus().equals("CONFIRMED")) {

                totalSales = totalSales + order.calculateTotal();
            }
        }

        return totalSales;
    }

    public static int countConfirmedOrders() {

        int totalOrders = 0;

        for (Order order : allOrders) {

            if (order.getStatus().equals("CONFIRMED")) {

                totalOrders++;
            }
        }

        return totalOrders;
    }

    // Products with stock <= 5
    public static ArrayList<Product> getLowStockProducts() {

        ArrayList<Product> low = new ArrayList<>();

        for (Product product : products) {

            if (product.getStock() <= LOW_STOCK_LIMIT) {

                low.add(product);
            }
        }

        return low;
    }

    // =====================================================
    // GUI WRAPPER METHODS (same logic as the terminal menu,
    // but without Scanner / System.out)
    // =====================================================

    // Payment of the most recent successful order (for the GUI to display)
    public static Payment lastPayment = null;

    public static Customer registerCustomerGui(String name, String email) {

        Customer customer =
                new Customer(nextCustomerId, name, email);

        customers.add(customer);

        nextCustomerId++;

        return customer;
    }

    public static Order placeOrderGui(Customer customer, int productId,
                                      int quantity, String method)
            throws InsufficientStockException {

        Product product = findProduct(productId);

        if (product == null) {

            throw new IllegalArgumentException("Product not found.");
        }

        Order order = new Order(customer.getId());

        order.addItem(product, quantity);   // checks + reduces stock

        Payment payment =
                new Payment(nextPaymentId, order.calculateTotal(), method);

        nextPaymentId++;

        order.confirmOrder();

        customer.getOrders().add(order);

        allOrders.add(order);

        lastPayment = payment;

        return order;
    }

    public static void addProductGui(int id, String name, String category,
                                     double price, int stock) {

        if (findProduct(id) != null) {

            throw new IllegalArgumentException(
                    "A product with this ID already exists."
            );
        }

        products.add(new Product(id, name, category, price, stock));
    }

    public static void updateStockGui(int id, int stock) {

        Product product = findProduct(id);

        if (product == null) {

            throw new IllegalArgumentException("Product not found.");
        }

        product.setStock(stock);
    }

    public static boolean deleteProductGui(int id) {

        Product product = findProduct(id);

        if (product == null) {

            return false;
        }

        products.remove(product);

        return true;
    }
}