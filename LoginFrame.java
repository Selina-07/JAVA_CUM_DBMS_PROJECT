import javax.swing.*;
import java.awt.*;

// Home screen + Customer (Register / Login) + Admin login
public class LoginFrame extends JFrame {

    private JPanel content;

    public LoginFrame() {

        setTitle("E-Commerce Inventory & Order Management System");
        setSize(520, 440);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        content = new JPanel(new BorderLayout());
        add(content);

        showHome();

        setVisible(true);
    }

    // ---------- helper: replace the screen ----------
    private void showScreen(JPanel panel) {

        content.removeAll();
        content.add(panel, BorderLayout.CENTER);
        content.revalidate();
        content.repaint();
    }

    private JLabel title(String text) {

        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 20));
        label.setBorder(BorderFactory.createEmptyBorder(25, 10, 25, 10));
        return label;
    }

    private JButton bigButton(String text) {

        JButton b = new JButton(text);
        b.setPreferredSize(new Dimension(220, 38));
        return b;
    }

    // ---------- SCREEN 1: HOME ----------
    private void showHome() {

        JPanel p = new JPanel(new BorderLayout());

        p.add(title("<html><center>E-COMMERCE INVENTORY<br>& ORDER MANAGEMENT SYSTEM</center></html>"),
                BorderLayout.NORTH);

        JPanel buttons = new JPanel(new GridLayout(3, 1, 0, 15));
        buttons.setBorder(BorderFactory.createEmptyBorder(20, 130, 50, 130));

        JButton customerBtn = bigButton("Customer");
        JButton adminBtn = bigButton("Admin");
        JButton exitBtn = bigButton("Exit");

        customerBtn.addActionListener(e -> showCustomerMenu());
        adminBtn.addActionListener(e -> showAdminLogin());
        exitBtn.addActionListener(e -> System.exit(0));

        buttons.add(customerBtn);
        buttons.add(adminBtn);
        buttons.add(exitBtn);

        p.add(buttons, BorderLayout.CENTER);

        showScreen(p);
    }

    // ---------- SCREEN 2: CUSTOMER (Register / Login / Back) ----------
    private void showCustomerMenu() {

        JPanel p = new JPanel(new BorderLayout());

        p.add(title("CUSTOMER"), BorderLayout.NORTH);

        JPanel buttons = new JPanel(new GridLayout(3, 1, 0, 15));
        buttons.setBorder(BorderFactory.createEmptyBorder(20, 130, 50, 130));

        JButton registerBtn = bigButton("Register");
        JButton loginBtn = bigButton("Login");
        JButton backBtn = bigButton("Back");

        registerBtn.addActionListener(e -> showRegister());
        loginBtn.addActionListener(e -> showCustomerLogin());
        backBtn.addActionListener(e -> showHome());

        buttons.add(registerBtn);
        buttons.add(loginBtn);
        buttons.add(backBtn);

        p.add(buttons, BorderLayout.CENTER);

        showScreen(p);
    }

    // ---------- REGISTER ----------
    private void showRegister() {

        JPanel p = new JPanel(new BorderLayout());
        p.add(title("CUSTOMER REGISTRATION"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(2, 2, 10, 15));
        form.setBorder(BorderFactory.createEmptyBorder(10, 60, 10, 60));

        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();

        form.add(new JLabel("Name:"));
        form.add(nameField);
        form.add(new JLabel("Email:"));
        form.add(emailField);

        JPanel center = new JPanel(new BorderLayout());
        center.add(form, BorderLayout.NORTH);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 25));
        JButton registerBtn = new JButton("Register");
        JButton backBtn = new JButton("Back");
        buttons.add(registerBtn);
        buttons.add(backBtn);
        center.add(buttons, BorderLayout.CENTER);

        p.add(center, BorderLayout.CENTER);

        registerBtn.addActionListener(e -> {

            String name = nameField.getText().trim();
            String email = emailField.getText().trim();

            if (name.isEmpty() || email.isEmpty()) {

                JOptionPane.showMessageDialog(this,
                        "Please enter both name and email.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // uses the existing customer list + ID counter in Main
            Customer customer = Main.registerCustomerGui(name, email);

            JOptionPane.showMessageDialog(this,
                    "Customer registered successfully.\nYour Customer ID is: "
                            + customer.getId());

            dispose();
            new CustomerFrame(customer);
        });

        backBtn.addActionListener(e -> showCustomerMenu());

        showScreen(p);
    }

    // ---------- CUSTOMER LOGIN ----------
    private void showCustomerLogin() {

        JPanel p = new JPanel(new BorderLayout());
        p.add(title("CUSTOMER LOGIN"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(1, 2, 10, 15));
        form.setBorder(BorderFactory.createEmptyBorder(10, 60, 10, 60));

        JTextField idField = new JTextField();
        form.add(new JLabel("Customer ID:"));
        form.add(idField);

        JPanel center = new JPanel(new BorderLayout());
        center.add(form, BorderLayout.NORTH);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 25));
        JButton loginBtn = new JButton("Login");
        JButton backBtn = new JButton("Back");
        buttons.add(loginBtn);
        buttons.add(backBtn);
        center.add(buttons, BorderLayout.CENTER);

        p.add(center, BorderLayout.CENTER);

        loginBtn.addActionListener(e -> {

            int id;

            try {
                id = Integer.parseInt(idField.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid numeric Customer ID.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Customer customer = Main.findCustomer(id);

            if (customer == null) {

                JOptionPane.showMessageDialog(this,
                        "Customer not found.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            dispose();
            new CustomerFrame(customer);
        });

        backBtn.addActionListener(e -> showCustomerMenu());

        showScreen(p);
    }

    // ---------- ADMIN LOGIN ----------
    private void showAdminLogin() {

        JPanel p = new JPanel(new BorderLayout());
        p.add(title("ADMIN LOGIN"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(1, 2, 10, 15));
        form.setBorder(BorderFactory.createEmptyBorder(10, 60, 10, 60));

        JTextField idField = new JTextField();
        form.add(new JLabel("Admin ID:"));
        form.add(idField);

        JPanel center = new JPanel(new BorderLayout());
        center.add(form, BorderLayout.NORTH);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 25));
        JButton loginBtn = new JButton("Login");
        JButton backBtn = new JButton("Back");
        buttons.add(loginBtn);
        buttons.add(backBtn);
        center.add(buttons, BorderLayout.CENTER);

        p.add(center, BorderLayout.CENTER);

        loginBtn.addActionListener(e -> {

            int id;

            try {
                id = Integer.parseInt(idField.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Invalid Admin ID.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (id == 100) {          // same rule as the terminal version

                dispose();
                new AdminFrame();

            } else {

                JOptionPane.showMessageDialog(this,
                        "Invalid Admin ID.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backBtn.addActionListener(e -> showHome());

        showScreen(p);
    }
}
