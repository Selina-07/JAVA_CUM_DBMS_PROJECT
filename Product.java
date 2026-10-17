


public class Product {

    private int productId;
    private String name;
    private String category;
    private double price;
    private int stock;

    public Product(int productId, String name, String category,
                   double price, int stock) {

        if (productId <= 0) {
            throw new IllegalArgumentException(
                    "Product ID must be greater than 0."
            );
        }

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException(
                    "Product name cannot be empty."
            );
        }

        if (price <= 0) {
            throw new IllegalArgumentException(
                    "Price must be greater than 0."
            );
        }

        if (stock < 0) {
            throw new IllegalArgumentException(
                    "Stock cannot be negative."
            );
        }

        this.productId = productId;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {

        if (stock < 0) {

            throw new IllegalArgumentException(
                    "Stock cannot be negative."
            );
        }

        this.stock = stock;
    }

    public void addStock(int quantity) {

        if (quantity <= 0) {

            throw new IllegalArgumentException(
                    "Quantity must be greater than 0."
            );
        }

        stock = stock + quantity;
    }

    public void reduceStock(int quantity)
            throws InsufficientStockException {

        if (quantity <= 0) {

            throw new IllegalArgumentException(
                    "Quantity must be greater than 0."
            );
        }

        if (quantity > stock) {

            throw new InsufficientStockException(
                    "Only " + stock
                    + " units of " + name
                    + " are available."
            );
        }

        stock = stock - quantity;
    }

    public void displayProduct() {

        System.out.println("----------------------------");
        System.out.println("Product ID : " + productId);
        System.out.println("Name       : " + name);
        System.out.println("Category   : " + category);
        System.out.println("Price      : Rs." + price);
        System.out.println("Stock      : " + stock);
        System.out.println("----------------------------");
    }
}