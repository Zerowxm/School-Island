package wxm.com.maca.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import wxm.com.maca.ui.guide.Guide1;
import wxm.com.maca.ui.guide.Guide2;
import wxm.com.maca.ui.guide.Guide3;

/**
 * Created by zero on 2015/6/26.
 */
public class MyIntro extends AppIntro {
    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        addSlide(new Guide1(), getApplicationContext());
        addSlide(new Guide2(), getApplicationContext());
        addSlide(new Guide3(), getApplicationContext());

    }

    private void loadMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDonePressed() {
        loadMainActivity();
    }

    public void getStarted(View v) {
        loadMainActivity();
    }
}

