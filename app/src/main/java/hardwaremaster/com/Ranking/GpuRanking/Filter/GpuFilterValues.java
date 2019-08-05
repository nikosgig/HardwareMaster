package hardwaremaster.com.Ranking.GpuRanking.Filter;

public class GpuFilterValues {
    private double vRamCapacity;
    private double minPrice;
    private double maxPrice;

    private double minFps1080p;
    private double maxFps1080p;

    private double minFps2k;
    private double maxFps2k;

    private double minFps4k;
    private double maxFps4k;

    private double minFirestrike;
    private double maxFirestrike;

    private double minPassmark;
    private double maxPassmark;

    public GpuFilterValues() {
        this.maxPrice = Double.MAX_VALUE;
        this.maxFps1080p = Double.MAX_VALUE;
        this.maxFps2k = Double.MAX_VALUE;
        this.maxFps4k = Double.MAX_VALUE;
        this.maxFirestrike = Double.MAX_VALUE;
        this.maxPassmark = Double.MAX_VALUE;

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

    public double getMinFps1080p() {
        return minFps1080p;
    }

    public void setMinFps1080p(double minFps1080p) {
        this.minFps1080p = minFps1080p;
    }

    public double getMaxFps1080p() {
        return maxFps1080p;
    }

    public void setMaxFps1080p(double maxFps1080p) {
        this.maxFps1080p = maxFps1080p;
    }

    public double getMinFps2k() {
        return minFps2k;
    }

    public void setMinFps2k(double minFps2k) {
        this.minFps2k = minFps2k;
    }

    public double getMaxFps2k() {
        return maxFps2k;
    }

    public void setMaxFps2k(double maxFps2k) {
        this.maxFps2k = maxFps2k;
    }

    public double getMinFps4k() {
        return minFps4k;
    }

    public void setMinFps4k(double minFps4k) {
        this.minFps4k = minFps4k;
    }

    public double getMaxFps4k() {
        return maxFps4k;
    }

    public void setMaxFps4k(double maxFps4k) {
        this.maxFps4k = maxFps4k;
    }

    public double getMinFirestrike() {
        return minFirestrike;
    }

    public void setMinFirestrike(double minFirestrike) {
        this.minFirestrike = minFirestrike;
    }

    public double getMaxFirestrike() {
        return maxFirestrike;
    }

    public void setMaxFirestrike(double maxFirestrike) {
        this.maxFirestrike = maxFirestrike;
    }

    public double getMinPassmark() {
        return minPassmark;
    }

    public void setMinPassmark(double minPassmark) {
        this.minPassmark = minPassmark;
    }

    public double getMaxPassmark() {
        return maxPassmark;
    }

    public void setMaxPassmark(double maxPassmark) {
        this.maxPassmark = maxPassmark;
    }
}
