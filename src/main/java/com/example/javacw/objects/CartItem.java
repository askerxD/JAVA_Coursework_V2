package model;

public class CartItem {

    private Part part;
    private int quantity;
    public CartItem() {
    }
    public CartItem(Part part, int quantity) {
        this.part = part;
        this.quantity = quantity;
    }

    //getters and setters
    public Part getPart() {
        return part;
    }
    public void setPart(Part part) {
        this.part = part;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // subtotal for this item (before cart-level discounts)
    public double getSubtotal() {
        return part.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return part.getPartCode() + " x " + quantity;
    }
}