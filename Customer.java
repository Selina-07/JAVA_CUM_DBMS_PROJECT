import java.util.ArrayList;

public class Customer extends User implements OrderOperations {

    private ArrayList<Order> orders;

    public Customer(int id, String name, String email) {

        super(id, name, email);

        orders = new ArrayList<>();
    }

    public ArrayList<Order> getOrders() {

        return orders;
    }

    @Override
    public void showRole() {

        System.out.println("Role : Customer");
    }

    public void viewProducts() {

        System.out.println("Customer can view products.");
    }

    @Override
    public void placeOrder() {

        System.out.println("Customer can place an order.");
    }

    @Override
    public void viewOrders() {

        if (orders.isEmpty()) {

            System.out.println("No orders found.");

        } else {

            for (Order order : orders) {

                order.displayOrder();
            }
        }
    }

    @Override
    public void cancelOrder() {

        System.out.println("Customer can cancel an order.");
    }
}