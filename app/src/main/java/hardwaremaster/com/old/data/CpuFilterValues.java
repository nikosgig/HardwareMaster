package hardwaremaster.com.old.data;

public class CpuFilterValues extends FilterValues {

    private double singleScoreMax;
    private double singleScoreMin;
    private double multiCoreMax;
    private double multiCoreMin;
    private double quadCoreMax;
    private double quadCoreMin;

    public double getMultiCoreMax() {
        return multiCoreMax;
    }

    public void setMultiCoreMax(double multiCoreMax) {
        this.multiCoreMax = multiCoreMax;
    }

    public double getMultiCoreMin() {
        return multiCoreMin;
    }

    public void setMultiCoreMin(double multiCoreMin) {
        this.multiCoreMin = multiCoreMin;
    }

    public double getQuadCoreMax() {
        return quadCoreMax;
    }

    public void setQuadCoreMax(double quadCoreMax) {
        this.quadCoreMax = quadCoreMax;
    }

    public double getQuadCoreMin() {
        return quadCoreMin;
    }

    public void setQuadCoreMin(double quadCoreMin) {
        this.quadCoreMin = quadCoreMin;
    }

    public double getSingleScoreMax() {
        return singleScoreMax;
    }

    public void setSingleScoreMax(double singleScoreMax) {
        this.singleScoreMax = singleScoreMax;
    }

    public double getSingleScoreMin() {
        return singleScoreMin;
    }

    public void setSingleScoreMin(double singleScoreMin) {
        this.singleScoreMin = singleScoreMin;
    }
}
