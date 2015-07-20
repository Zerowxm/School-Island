package wxm.com.androiddesign.module;

/**
 * Created by 小明 on 2015/7/20.
 */
public class Score {
    private String score;
    private String op;


    public Score(String score, String op) {
        this.score = score;
        this.op = op;
    }

    @Override
    public String toString() {
        return "Score{" +
                "score='" + score + '\'' +
                ", op='" + op + '\'' +
                '}';
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

}
