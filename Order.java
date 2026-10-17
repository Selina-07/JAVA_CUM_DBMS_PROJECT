import java.util.ArrayList;

public class Order {

    private static int nextOrderId = 1001;

    private int orderId;
    private int customerId;
    private ArrayList<OrderItem> items;
    private String status;

    public Order(int customerId) {

        this.orderId = nextOrderId;
        nextOrderId++;

        this.customerId = customerId;

        items = new ArrayList<>();

        status = "PENDING";
    }

    public int getOrderId() {
        return orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void addItem(Product product, int quantity)
            throws InsufficientStockException {

        product.reduceStock(quantity);

        OrderItem item = new OrderItem(product, quantity);

        items.add(item);
    }

    public ArrayList<OrderItem> getItems() {

    return items;
    }

    public double calculateTotal() {

        double total = 0;

        for (OrderItem item : items) {

            total = total + item.getSubtotal();
        }

        return total;
    }

    public void confirmOrder() {

        status = "CONFIRMED";
    }

    public void displayOrder() {

        System.out.println("\n================================");

        System.out.println("Order ID    : " + orderId);
        System.out.println("Customer ID : " + customerId);
        System.out.println("Status      : " + status);

        System.out.println("--------------------------------");

        for (OrderItem item : items) {

            item.displayItem();
        }

        System.out.println("--------------------------------");

        System.out.println(
                "Total Amount: Rs." + calculateTotal()
        );

        System.out.println("================================");
    }
}