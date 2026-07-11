package model;

public class Part {

    private String partCode;
    private String name;
    private String brand;
    private double price;
    private int quantity;
    private String category;
    private String dateAdded;
    private String image;

    // Default constructor
    public Part() {
    }

    //constructor
    public Part(String partCode,String name,String brand,double price,int quantity,String category,String dateAdded,String image) {

        this.partCode = partCode;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.dateAdded = dateAdded;
        this.image = image;
    }

    //getters
    public String getPartCode() {
        return partCode;
    }
    public String getName() {
        return name;
    }
    public String getBrand() {
        return brand;
    }
    public double getPrice() {
        return price;
    }
    public int getQuantity() {
        return quantity;
    }
    public String getCategory() {
        return category;
    }
    public String getDateAdded() {
        return dateAdded;
    }
    public String getImage() {
        return image;
    }

    //setters
    public void setPartCode(String partCode) {
        this.partCode = partCode;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }
    public void setImage(String image) {
        this.image = image;
    }

    //additional method providing the stock value of a part
    public double getTotalValue() {
        return price * quantity;
    }

    @Override
    public String toString() {
        return partCode + "," + name + "," + brand + "," + price + "," + quantity + "," + category + "," + dateAdded + "," + image;
    }
}