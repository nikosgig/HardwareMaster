package hardwaremaster.com.data;

public class FilterValues {

    private String model;
    private double singleScoreLow;
    private double singleScoreHigh;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getSingleScoreLow() {
        return singleScoreLow;
    }

    public void setSingleScoreLow(double singleScoreLow) {
        this.singleScoreLow = singleScoreLow;
    }

    public double getSingleScoreHigh() {
        return singleScoreHigh;
    }

    public void setSingleScoreHigh(double singleScoreHigh) {
        this.singleScoreHigh = singleScoreHigh;
    }
}
