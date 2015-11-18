package wxm.com.maca.module;

/**
 * Created by 小明 on 2015/7/20.
 */
public class Score {
    private String creditNumbers;
    private String creditContent;

    public String getCreditNumbers() {
        return creditNumbers;
    }

    public void setCreditNumbers(String creditNumbers) {
        this.creditNumbers = creditNumbers;
    }

    public String getCreditContent() {
        return creditContent;
    }

    public void setCreditContent(String creditContent) {
        this.creditContent = creditContent;
    }

    public Score(String score, String op) {
        this.creditNumbers = score;
        this.creditContent = op;
    }

    @Override
    public String toString() {
        return "Score{" +
                "creditNumbers='" + creditNumbers + '\'' +
                ", creditContent='" + creditContent + '\'' +
                '}';
    }

}
