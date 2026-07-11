package com.example.javacw.service;

import com.example.javacw.objects.CartItem;
import com.example.javacw.objects.Part;
import java.util.ArrayList;

public class CartService {
    private ArrayList<CartItem> cart = new ArrayList<>();
    // Add item to cart
    public boolean addToCart(Part part, int quantity) {
        if (quantity <= 0) return false;
        if (part == null) return false;
        if (quantity > part.getQuantity()) return false;
        // check if already exists
        for (CartItem item : cart) {
            if (item.getPart().getPartCode().equals(part.getPartCode())) {
                int newQty = item.getQuantity() + quantity;
                if (newQty > part.getQuantity()) return false;
                item.setQuantity(newQty);
                return true;
            }
        }
        cart.add(new CartItem(part, quantity));
        return true;
    }
    // Remove item from cart
    public void removeFromCart(String partCode) {
        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).getPart().getPartCode().equals(partCode)) {
                cart.remove(i);
                return;
            }
        }
    }
    // Get cart items
    public ArrayList<CartItem> getCartItems() {
        return cart;
    }
    // Clear cart
    public void clearCart() {
        cart.clear();
    }
    // Calculate total with discounts
    public double calculateTotal() {
        double total = 0;
        boolean hasEngine = false;
        boolean hasElectrical = false;
        for (CartItem item : cart) {
            Part part = item.getPart();
            int qty = item.getQuantity();
            double price = part.getPrice();
            double subtotal = price * qty;
            // BULK DISCOUNT (5%)
            if (qty >= 3) {
                subtotal = subtotal * 0.95;
            }
            total += subtotal;
            // check synergy conditions
            String category = part.getCategory().toUpperCase();
            if (category.contains("ENGINE")) {
                hasEngine = true;
            }
            if (category.contains("ELECTRICAL")) {
                hasElectrical = true;
            }
        }
        // SYNERGY DISCOUNT (10%)
        if (hasEngine && hasElectrical) {
            total = total * 0.90;
        }
        return total;
    }
    // Validate checkout
    public boolean canCheckout() {
        return !cart.isEmpty();
    }
}