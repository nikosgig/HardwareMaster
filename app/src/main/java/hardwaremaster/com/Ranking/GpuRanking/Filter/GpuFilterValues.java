package hardwaremaster.com.Ranking.GpuRanking.Filter;

public class GpuFilterValues {
    private double vRamCapacity;
    private double minPrice;
    private double maxPrice;

    public GpuFilterValues() {
        this.maxPrice = 10000.00;

    }

    public double getvRamCapacity() {
        return vRamCapacity;
    }

    public void setvRamCapacity(double vRamCapacity) {
        this.vRamCapacity = vRamCapacity;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }
}
