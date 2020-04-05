package hardwaremaster.com.a_old.data;

public class UserSpecificPrice {

    private String Model;
    private Double Price;

    public UserSpecificPrice(String model, Double price) {
        Model = model;
        Price = price;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
    }
}
