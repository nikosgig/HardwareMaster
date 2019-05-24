package hardwaremaster.com.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Cpu extends RealmObject {


    /**
     * Model : Intel Core i7-8700K
     * QuadScore : 520
     * SingleScore : 137
     * MultiScore : 1062
     * Cine15 : 1447
     * Mark3d : 19449
     */

    @Required
    @PrimaryKey
    private String Model;
    private String QuadScore;
    private String SingleScore;
    private String MultiScore;
    private String Cine15;
    private String Mark3d;

    public Cpu(String model, String quadScore, String singleScore, String multiScore, String cine15, String mark3d) {
        this.Model = model;
        this.QuadScore = quadScore;
        this.SingleScore = singleScore;
        this.MultiScore = multiScore;
        this.Cine15 = cine15;
        this.Mark3d = mark3d;
    }

    public Cpu() {
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        this.Model = model;
    }

    public String getQuadScore() {
        return QuadScore;
    }

    public void setQuadScore(String quadScore) {
        this.QuadScore = quadScore;
    }

    public String getSingleScore() {
        return SingleScore;
    }

    public void setSingleScore(String singleScore) {
        this.SingleScore = singleScore;
    }

    public String getMultiScore() {
        return MultiScore;
    }

    public void setMultiScore(String multiScore) {
        this.MultiScore = multiScore;
    }

    public String getCine15() {
        return Cine15;
    }

    public void setCine15(String cine15) {
        this.Cine15 = cine15;
    }

    public String getMark3d() {
        return Mark3d;
    }

    public void setMark3d(String mark3d) {
        this.Mark3d = mark3d;
    }
}
