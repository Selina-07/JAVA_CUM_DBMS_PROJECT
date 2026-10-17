import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

// Admin dashboard: Add/View/Update/Delete products, orders, low stock, sales report
public class AdminFrame extends JFrame {

    private JPanel content;

    public AdminFrame() {

        setTitle("Admin Dashboard");
        setSize(640, 560);
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

    private void fillProductTable(JTable table, ArrayList<Product> list) {

        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        for (Product p : list) {

            model.addRow(new Object[] {
                    p.getProductId(), p.getName(), p.getCategory(),
                    p.getPrice(), p.getStock()
            });
        }
    }

    private JPanel backBar() {

        JPanel bar = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton back = new JButton("Back");
        back.addActionListener(e -> showDashboard());
        bar.add(back);
        return bar;
    }

    private static final String[] PRODUCT_COLUMNS =
            {"Product ID", "Name", "Category", "Price", "Stock"};

    // ---------- DASHBOARD ----------
    private void showDashboard() {

        JPanel p = new JPanel(new BorderLayout());
        p.add(title("ADMIN DASHBOARD"), BorderLayout.NORTH);

        JPanel buttons = new JPanel(new GridLayout(8, 1, 0, 10));
        buttons.setBorder(BorderFactory.createEmptyBorder(10, 170, 25, 170));

        JButton addBtn = new JButton("Add Product");
        JButton viewBtn = new JButton("View Products");
        JButton updateBtn = new JButton("Update Stock");
        JButton deleteBtn = new JButton("Delete Product");
        JButton ordersBtn = new JButton("View Orders");
        JButton lowBtn = new JButton("Low Stock");
        JButton salesBtn = new JButton("Sales Report");
        JButton logoutBtn = new JButton("Logout");

        addBtn.addActionListener(e -> showAddProduct());
        viewBtn.addActionListener(e -> showProducts());
        updateBtn.addActionListener(e -> showUpdateStock());
        deleteBtn.addActionListener(e -> showDeleteProduct());
        ordersBtn.addActionListener(e -> showOrders());
        lowBtn.addActionListener(e -> showLowStock());
        salesBtn.addActionListener(e -> showSalesReport());
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        buttons.add(addBtn);
        buttons.add(viewBtn);
        buttons.add(updateBtn);
        buttons.add(deleteBtn);
        buttons.add(ordersBtn);
        buttons.add(lowBtn);
        buttons.add(salesBtn);
        buttons.add(logoutBtn);

        p.add(buttons, BorderLayout.CENTER);

        showScreen(p);
    }

    // ---------- ADD PRODUCT ----------
    private void showAddProduct() {

        JPanel p = new JPanel(new BorderLayout());
        p.add(title("ADD PRODUCT"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(5, 2, 10, 12));
        form.setBorder(BorderFactory.createEmptyBorder(10, 80, 10, 80));

        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField stockField = new JTextField();

        form.add(new JLabel("Product ID:"));
        form.add(idField);
        form.add(new JLabel("Product Name:"));
        form.add(nameField);
        form.add(new JLabel("Category:"));
        form.add(categoryField);
        form.add(new JLabel("Price:"));
        form.add(priceField);
        form.add(new JLabel("Stock:"));
        form.add(stockField);

        JPanel center = new JPanel(new BorderLayout());
        center.add(form, BorderLayout.NORTH);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        JButton addBtn = new JButton("Add Product");
        JButton backBtn = new JButton("Back");
        buttons.add(addBtn);
        buttons.add(backBtn);
        center.add(buttons, BorderLayout.CENTER);

        p.add(center, BorderLayout.CENTER);

        addBtn.addActionListener(e -> {

            int id;
            double price;
            int stock;

            try {
                id = Integer.parseInt(idField.getText().trim());
                price = Double.parseDouble(priceField.getText().trim());
                stock = Integer.parseInt(stockField.getText().trim());
            } catch (NumberFormatException ex) {
                error("Product ID, Price and Stock must be valid numbers.");
                return;
            }

            String name = nameField.getText().trim();
            String category = categoryField.getText().trim();

            try {

                // Product constructor validates ID > 0, name, price > 0, stock >= 0
                Main.addProductGui(id, name, category, price, stock);

                JOptionPane.showMessageDialog(this,
                        "Product added successfully.");

                showAddProduct();   // clear the form

            } catch (IllegalArgumentException ex) {

                error("Error: " + ex.getMessage());
            }
        });

        backBtn.addActionListener(e -> showDashboard());

        showScreen(p);
    }

    // ---------- VIEW PRODUCTS ----------
    private void showProducts() {

        JPanel p = new JPanel(new BorderLayout());
        p.add(title("ALL PRODUCTS"), BorderLayout.NORTH);

        JTable table = makeTable(PRODUCT_COLUMNS);
        fillProductTable(table, Main.products);

        p.add(new JScrollPane(table), BorderLayout.CENTER);
        p.add(backBar(), BorderLayout.SOUTH);

        showScreen(p);
    }

    // ---------- UPDATE STOCK ----------
    private void showUpdateStock() {

        JPanel p = new JPanel(new BorderLayout());
        p.add(title("UPDATE STOCK"), BorderLayout.NORTH);

        JTable table = makeTable(PRODUCT_COLUMNS);
        fillProductTable(table, Main.products);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(500, 180));

        JPanel form = new JPanel(new GridLayout(2, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(15, 80, 5, 80));

        JTextField idField = new JTextField();
        JTextField stockField = new JTextField();

        form.add(new JLabel("Product ID:"));
        form.add(idField);
        form.add(new JLabel("New Stock:"));
        form.add(stockField);

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                idField.setText(table.getValueAt(row, 0).toString());
            }
        });

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton updateBtn = new JButton("Update Stock");
        JButton backBtn = new JButton("Back");
        buttons.add(updateBtn);
        buttons.add(backBtn);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(form, BorderLayout.CENTER);
        bottom.add(buttons, BorderLayout.SOUTH);

        p.add(scroll, BorderLayout.CENTER);
        p.add(bottom, BorderLayout.SOUTH);

        updateBtn.addActionListener(e -> {

            int id;
            int stock;

            try {
                id = Integer.parseInt(idField.getText().trim());
                stock = Integer.parseInt(stockField.getText().trim());
            } catch (NumberFormatException ex) {
                error("Product ID and Stock must be valid numbers.");
                return;
            }

            try {

                // Product.setStock rejects negative stock
                Main.updateStockGui(id, stock);

                JOptionPane.showMessageDialog(this,
                        "Stock updated successfully.");

                showUpdateStock();  // refresh

            } catch (IllegalArgumentException ex) {

                error("Error: " + ex.getMessage());
            }
        });

        backBtn.addActionListener(e -> showDashboard());

        showScreen(p);
    }

    // ---------- DELETE PRODUCT ----------
    private void showDeleteProduct() {

        JPanel p = new JPanel(new BorderLayout());
        p.add(title("DELETE PRODUCT"), BorderLayout.NORTH);

        JTable table = makeTable(PRODUCT_COLUMNS);
        fillProductTable(table, Main.products);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(500, 200));

        JPanel form = new JPanel(new GridLayout(1, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(15, 80, 5, 80));

        JTextField idField = new JTextField();
        form.add(new JLabel("Product ID:"));
        form.add(idField);

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                idField.setText(table.getValueAt(row, 0).toString());
            }
        });

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton deleteBtn = new JButton("Delete Product");
        JButton backBtn = new JButton("Back");
        buttons.add(deleteBtn);
        buttons.add(backBtn);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(form, BorderLayout.CENTER);
        bottom.add(buttons, BorderLayout.SOUTH);

        p.add(scroll, BorderLayout.CENTER);
        p.add(bottom, BorderLayout.SOUTH);

        deleteBtn.addActionListener(e -> {

            int id;

            try {
                id = Integer.parseInt(idField.getText().trim());
            } catch (NumberFormatException ex) {
                error("Please enter a valid Product ID.");
                return;
            }

            if (Main.findProduct(id) == null) {
                error("Product not found.");
                return;
            }

            int choice = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this product?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {

                Main.deleteProductGui(id);

                JOptionPane.showMessageDialog(this,
                        "Product deleted successfully.");

                showDeleteProduct();    // refresh
            }
        });

        backBtn.addActionListener(e -> showDashboard());

        showScreen(p);
    }

    // ---------- VIEW ALL ORDERS ----------
    private void showOrders() {

        JPanel p = new JPanel(new BorderLayout());
        p.add(title(Main.allOrders.isEmpty() ? "ALL ORDERS - No orders found." : "ALL ORDERS"),
                BorderLayout.NORTH);

        JTable table = makeTable(
                new String[] {"Order ID", "Customer ID", "Status", "Total Amount"});

        DefaultTableModel model = (DefaultTableModel) table.getModel();

        for (Order o : Main.allOrders) {

            model.addRow(new Object[] {
                    o.getOrderId(), o.getCustomerId(),
                    o.getStatus(), o.calculateTotal()
            });
        }

        p.add(new JScrollPane(table), BorderLayout.CENTER);

        p.add(backBar(), BorderLayout.SOUTH);

        showScreen(p);
    }

    // ---------- LOW STOCK ----------
    private void showLowStock() {

        JPanel p = new JPanel(new BorderLayout());
        p.add(title("LOW STOCK PRODUCTS (stock <= "
                + Main.LOW_STOCK_LIMIT + ")"), BorderLayout.NORTH);

        JTable table = makeTable(PRODUCT_COLUMNS);

        ArrayList<Product> low = Main.getLowStockProducts();
        fillProductTable(table, low);

        p.add(new JScrollPane(table), BorderLayout.CENTER);

        if (low.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No low-stock products.");
        }

        p.add(backBar(), BorderLayout.SOUTH);

        showScreen(p);
    }

    // ---------- SALES REPORT ----------
    private void showSalesReport() {

        JPanel p = new JPanel(new BorderLayout());
        p.add(title("SALES REPORT"), BorderLayout.NORTH);

        JPanel info = new JPanel(new GridLayout(2, 1, 0, 15));
        info.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel ordersLabel = new JLabel(
                "Total Orders : " + Main.countConfirmedOrders(),
                SwingConstants.CENTER);
        JLabel salesLabel = new JLabel(
                "Total Sales  : Rs." + Main.calculateTotalSales(),
                SwingConstants.CENTER);

        ordersLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        salesLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));

        info.add(ordersLabel);
        info.add(salesLabel);

        p.add(info, BorderLayout.CENTER);
        p.add(backBar(), BorderLayout.SOUTH);

        showScreen(p);
    }
}
