package wxm.com.androiddesign.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.module.Score;

/**
 * Created by zero on 2015/7/15.
 */
public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.MyScoreViewHolder> {

    List<Score> scores = new ArrayList<Score>();


    public ScoreAdapter(List<Score> scores){
        this.scores = scores;
    }

    @Override
    public MyScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyScoreViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.score_item, parent, false
                ));
    }

    @Override
    public void onBindViewHolder(MyScoreViewHolder holder, int position) {
        Score score=scores.get(position);
        holder.score.setText(score.getCreditNumbers());
        holder.op.setText(score.getCreditContent());
    }

    @Override
    public int getItemCount() {
        return scores.size();
    }

    public static class MyScoreViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.score)TextView score;
        @Bind(R.id.op)TextView op;
        public MyScoreViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
