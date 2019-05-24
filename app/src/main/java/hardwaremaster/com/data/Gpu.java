package hardwaremaster.com.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Gpu extends RealmObject {

    @Required
    @PrimaryKey
    private String Model;
    private Double AvgFps1080p;
    private Double AvgFps2k;
    private Double AvgFps4k;
    private Integer Passmark;
    private Integer Firestrike;
    private String ReleaseDate;
    private Integer GraphicsRamSize;
    private String GraphicsRamType;
    private String MemoryBus;
    private Integer GPUClock;
    private Integer MemoryClock;


    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        this.Model = model;
    }

    public Double getAvgFps1080p() {
        return AvgFps1080p;
    }

    public void setAvgFps1080p(Double avgFps1080p) {
        this.AvgFps1080p = avgFps1080p;
    }

    public Double getAvgFps2k() {
        return AvgFps2k;
    }

    public void setAvgFps2k(Double avgFps2k) {
        this.AvgFps2k = avgFps2k;
    }

    public Double getAvgFps4k() {
        return AvgFps4k;
    }

    public void setAvgFps4k(Double avgFps4k) {
        this.AvgFps4k = avgFps4k;
    }

    public Integer getPassmark() {
        return Passmark;
    }

    public void setPassmark(Integer passmark) {
        this.Passmark = passmark;
    }

    public Integer getFirestrike() {
        return Firestrike;
    }

    public void setFirestrike(Integer firestrike) {
        this.Firestrike = firestrike;
    }

    public String getReleaseDate() {
        return ReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.ReleaseDate = releaseDate;
    }

    public Integer getGraphicsRamSize() {
        return GraphicsRamSize;
    }

    public void setGraphicsRamSize(Integer graphicsRamSize) {
        this.GraphicsRamSize = graphicsRamSize;
    }

    public String getGraphicsRamType() {
        return GraphicsRamType;
    }

    public void setGraphicsRamType(String graphicsRamType) {
        this.GraphicsRamType = graphicsRamType;
    }

    public String getMemoryBus() {
        return MemoryBus;
    }

    public void setMemoryBus(String memoryBus) {
        this.MemoryBus = memoryBus;
    }

    public Integer getGPUClock() {
        return GPUClock;
    }

    public void setGPUClock(Integer gPUClock) {
        this.GPUClock = gPUClock;
    }

    public Integer getMemoryClock() {
        return MemoryClock;
    }

    public void setMemoryClock(Integer memoryClock) {
        this.MemoryClock = memoryClock;
    }
}
