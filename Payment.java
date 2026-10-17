public class Payment {

    private int paymentId;
    private double amount;
    private String method;
    private String status;

    public Payment(int paymentId, double amount, String method) {

        this.paymentId = paymentId;
        this.amount = amount;
        this.method = method;
        this.status = "SUCCESS";
    }

    public void displayPayment() {

        System.out.println("\n===== PAYMENT DETAILS =====");
        System.out.println("Payment ID : " + paymentId);
        System.out.println("Amount     : Rs." + amount);
        System.out.println("Method     : " + method);
        System.out.println("Status     : " + status);
    }

    public int getPaymentId() {

        return paymentId;
    }

    public double getAmount() {

        return amount;
    }

    public String getMethod() {

        return method;
    }

    public String getStatus() {

        return status;
    }
}