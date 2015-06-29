package wxm.com.androiddesign.ui;

import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import wxm.com.androiddesign.R;

public class SignUpActivity extends AppCompatActivity {

        private void setupToolbar(){
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar=getSupportActionBar();
        actionBar.setElevation(10);
        //actionBar.setHomeAsUpIndicator(R.drawable.ic);
        //actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setHomeButtonEnabled(true);
    }
    private void setupTextInputLayout(){
        TextInputLayout usernameLayout=(TextInputLayout)findViewById(R.id.username_text_input_layout);
        usernameLayout.setErrorEnabled(true);

        //usernameLayout.setError("请输入用户名");

        TextInputLayout passwordLayout=(TextInputLayout)findViewById(R.id.password_text_input_layout);
        passwordLayout.setErrorEnabled(false);
        passwordLayout.setError("请输入密码");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        if(Build.VERSION.SDK_INT>= 21){
            getWindow().setExitTransition(new Explode());
        }
        super.onCreate(savedInstanceState);

      //
        setContentView(R.layout.activity_signup);
        setupTextInputLayout();
        setupToolbar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}