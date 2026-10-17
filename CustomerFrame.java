import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

// Customer dashboard: View Products, Place Order, My Orders, Cancel Order, Logout
public class CustomerFrame extends JFrame {

    private Customer customer;
    private JPanel content;

    public CustomerFrame(Customer customer) {

        this.customer = customer;

        setTitle("Customer Dashboard");
        setSize(620, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        content = new JPanel(new BorderLayout());
        add(content);

        showDashboard();

        setVisible(true);
    }

    // ---------- helpers ----------
    private void showScreen(JPanel panel) {

        content.removeAll();
        content.add(panel, BorderLayout.CENTER);
        content.revalidate();
        content.repaint();
    }

    private JLabel title(String text) {

        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 18));
        label.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        return label;
    }

    private void error(String message) {

        JOptionPane.showMessageDialog(this, message,
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    private JTable makeTable(String[] columns) {

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        return new JTable(model);
    }

    private void fillProductTable(JTable table) {

        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        for (Product p : Main.products) {

            model.addRow(new Object[] {
                    p.getProductId(), p.getName(), p.getCategory(),
                    p.getPrice(), p.getStock()
            });
        }
    }

    private void fillOrderTable(JTable table) {

        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        for (Order o : customer.getOrders()) {

            model.addRow(new Object[] {
                    o.getOrderId(), o.getCustomerId(),
                    o.getStatus(), o.calculateTotal()
            });
        }
    }

    private JPanel backBar(String text) {

        JPanel bar = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton back = new JButton(text);
        back.addActionListener(e -> showDashboard());
        bar.add(back);
        return bar;
    }

    // ---------- DASHBOARD ----------
    private void showDashboard() {

        JPanel p = new JPanel(new BorderLayout());

        p.add(title("Welcome, " + customer.getName()), BorderLayout.NORTH);

        JPanel buttons = new JPanel(new GridLayout(5, 1, 0, 12));
        buttons.setBorder(BorderFactory.createEmptyBorder(15, 160, 30, 160));

        JButton viewBtn = new JButton("View Products");
        JButton orderBtn = new JButton("Place Order");
        JButton myBtn = new JButton("My Orders");
        JButton cancelBtn = new JButton("Cancel Order");
        JButton logoutBtn = new JButton("Logout");

        viewBtn.addActionListener(e -> showProducts());
        orderBtn.addActionListener(e -> showPlaceOrder());
        myBtn.addActionListener(e -> showMyOrders());
        cancelBtn.addActionListener(e -> showCancelOrder());
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        buttons.add(viewBtn);
        buttons.add(orderBtn);
        buttons.add(myBtn);
        buttons.add(cancelBtn);
        buttons.add(logoutBtn);

        p.add(buttons, BorderLayout.CENTER);

        showScreen(p);
    }

    // ---------- VIEW PRODUCTS ----------
    private void showProducts() {

        JPanel p = new JPanel(new BorderLayout());
        p.add(title("PRODUCTS"), BorderLayout.NORTH);

        JTable table = makeTable(
                new String[] {"Product ID", "Name", "Category", "Price", "Stock"});
        fillProductTable(table);

        p.add(new JScrollPane(table), BorderLayout.CENTER);
        p.add(backBar("Back"), BorderLayout.SOUTH);

        showScreen(p);
    }

    // ---------- PLACE ORDER ----------
    private void showPlaceOrder() {

        JPanel p = new JPanel(new BorderLayout());
        p.add(title("PLACE ORDER"), BorderLayout.NORTH);

        // product list on top (click a row to fill the Product ID)
        JTable table = makeTable(
                new String[] {"Product ID", "Name", "Category", "Price", "Stock"});
        fillProductTable(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(500, 150));

        // form
        JPanel form = new JPanel(new GridLayout(3, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(15, 80, 5, 80));

        JTextField productField = new JTextField();
        JTextField quantityField = new JTextField();
        JComboBox<String> paymentBox = new JComboBox<>(
                new String[] {"UPI", "Credit Card", "Cash on Delivery"});

        form.add(new JLabel("Product ID:"));
        form.add(productField);
        form.add(new JLabel("Quantity:"));
        form.add(quantityField);
        form.add(new JLabel("Payment Method:"));
        form.add(paymentBox);

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                productField.setText(table.getValueAt(row, 0).toString());
            }
        });

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton placeBtn = new JButton("Place Order");
        JButton backBtn = new JButton("Back");
        buttons.add(placeBtn);
        buttons.add(backBtn);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(form, BorderLayout.CENTER);
        bottom.add(buttons, BorderLayout.SOUTH);

        p.add(scroll, BorderLayout.CENTER);
        p.add(bottom, BorderLayout.SOUTH);

        placeBtn.addActionListener(e -> {

            int productId;
            int quantity;

            try {
                productId = Integer.parseInt(productField.getText().trim());
                quantity = Integer.parseInt(quantityField.getText().trim());
            } catch (NumberFormatException ex) {
                error("Please enter valid numbers for Product ID and Quantity.");
                return;
            }

            String method = (String) paymentBox.getSelectedItem();

            try {

                // real order logic (stock check -> reduce -> order -> payment -> confirm)
                Order order = Main.placeOrderGui(customer, productId, quantity, method);
                Payment payment = Main.lastPayment;

                JOptionPane.showMessageDialog(this,
                        "Order placed successfully!\n\n"
                                + "Order ID       : " + order.getOrderId() + "\n"
                                + "Payment ID     : " + payment.getPaymentId() + "\n"
                                + "Total Amount   : Rs." + order.calculateTotal() + "\n"
                                + "Payment Method : " + payment.getMethod() + "\n"
                                + "Status         : " + order.getStatus());

                showPlaceOrder();   // refresh stock in the table

            } catch (InsufficientStockException ex) {

                error("Order failed: " + ex.getMessage());

            } catch (IllegalArgumentException ex) {

                error(ex.getMessage());
            }
        });

        backBtn.addActionListener(e -> showDashboard());

        showScreen(p);
    }

    // ---------- MY ORDERS ----------
    private void showMyOrders() {

        JPanel p = new JPanel(new BorderLayout());
        p.add(title("MY ORDERS"), BorderLayout.NORTH);

        JTable table = makeTable(
                new String[] {"Order ID", "Customer ID", "Status", "Total Amount"});
        fillOrderTable(table);

        p.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton detailsBtn = new JButton("View Selected Order");
        JButton backBtn = new JButton("Back");
        buttons.add(detailsBtn);
        buttons.add(backBtn);
        p.add(buttons, BorderLayout.SOUTH);

        detailsBtn.addActionListener(e -> {

            int row = table.getSelectedRow();

            if (row < 0) {
                error("Please select an order first.");
                return;
            }

            int orderId = (int) table.getValueAt(row, 0);

            for (Order o : customer.getOrders()) {

                if (o.getOrderId() == orderId) {

                    StringBuilder sb = new StringBuilder();
                    sb.append("Order ID    : ").append(o.getOrderId()).append("\n");
                    sb.append("Customer ID : ").append(o.getCustomerId()).append("\n");
                    sb.append("Status      : ").append(o.getStatus()).append("\n\n");

                    for (OrderItem item : o.getItems()) {
                        sb.append(item.getProduct().getName())
                          .append("  x ").append(item.getQuantity())
                          .append("  @ Rs.").append(item.getProduct().getPrice())
                          .append("  = Rs.").append(item.getSubtotal())
                          .append("\n");
                    }

                    sb.append("\nTotal Amount: Rs.").append(o.calculateTotal());

                    JOptionPane.showMessageDialog(this, sb.toString(),
                            "Order Details", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }
        });

        backBtn.addActionListener(e -> showDashboard());

        showScreen(p);
    }

    // ---------- CANCEL ORDER ----------
    private void showCancelOrder() {

        JPanel p = new JPanel(new BorderLayout());
        p.add(title("CANCEL ORDER"), BorderLayout.NORTH);

        JTable table = makeTable(
                new String[] {"Order ID", "Customer ID", "Status", "Total Amount"});
        fillOrderTable(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(500, 180));

        JPanel form = new JPanel(new GridLayout(1, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(15, 80, 5, 80));

        JTextField orderField = new JTextField();
        form.add(new JLabel("Order ID to cancel:"));
        form.add(orderField);

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                orderField.setText(table.getValueAt(row, 0).toString());
            }
        });

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton cancelBtn = new JButton("Cancel Order");
        JButton backBtn = new JButton("Back");
        buttons.add(cancelBtn);
        buttons.add(backBtn);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(form, BorderLayout.CENTER);
        bottom.add(buttons, BorderLayout.SOUTH);

        p.add(scroll, BorderLayout.CENTER);
        p.add(bottom, BorderLayout.SOUTH);

        cancelBtn.addActionListener(e -> {

            if (customer.getOrders().isEmpty()) {
                error("You have no orders.");
                return;
            }

            int orderId;

            try {
                orderId = Integer.parseInt(orderField.getText().trim());
            } catch (NumberFormatException ex) {
                error("Please enter a valid Order ID.");
                return;
            }

            // existing cancellation logic (restores stock + status CANCELLED)
            String result = Main.cancelOrderById(customer, orderId);

            if (result.equals("Order cancelled successfully.")) {

                JOptionPane.showMessageDialog(this, result);
                showCancelOrder();      // refresh table

            } else {

                error(result);
            }
        });

        backBtn.addActionListener(e -> showDashboard());

        showScreen(p);
    }
}
