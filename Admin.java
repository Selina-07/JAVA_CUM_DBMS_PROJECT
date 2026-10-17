public class Admin extends User {

    public Admin(int id, String name, String email) {

        super(id, name, email);
    }

    @Override
    public void showRole() {

        System.out.println("Role : Admin");
    }

    public void addProduct() {

        System.out.println("Admin can add products.");
    }

    public void updateProduct() {

        System.out.println("Admin can update products.");
    }

    public void deleteProduct() {

        System.out.println("Admin can delete products.");
    }

    public void viewOrders() {

        System.out.println("Admin can view all orders.");
    }

    public void viewInventory() {

        System.out.println("Admin can view inventory.");
    }
}
