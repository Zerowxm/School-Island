package wxm.com.androiddesign;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import wxm.com.androiddesign.AppIntro;
import wxm.com.androiddesign.MainActivity;
import wxm.com.androiddesign.UiTestFg1;

/**
 * Created by zero on 2015/6/26.
 */
public class MyIntro extends AppIntro{
    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        addSlide(new Guide1(),getApplicationContext());
        addSlide(new Guide1(),getApplicationContext());
        addSlide(new Guide1(),getApplicationContext());
        
    }

    private void loadMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDonePressed() {
        loadMainActivity();
    }

    public void getStarted(View v){
        loadMainActivity();
    }
}

