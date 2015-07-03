package wxm.com.androiddesign.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;
import android.view.ViewGroup.LayoutParams;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wxm.com.androiddesign.R;

public class ReleaseActivity extends AppCompatActivity {

    @Bind(R.id.add_image)ImageView add_image;
    @Bind(R.id.imageView)LinearLayout imageContains;
    @Bind(R.id.aty_content)
    EditText aty_content;
   // @Bind(R.id.image_show)ViewFlipper viewFlipper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testrelase);
        ButterKnife.bind(this);
        aty_content.setSelection(aty_content.getText().length());
        aty_content.getSelectionStart();
//        viewFlipper.setAutoStart(true);
//        viewFlipper.setFlipInterval(2000);
//        if(viewFlipper.isAutoStart() && !viewFlipper.isFlipping()){
//            viewFlipper.startFlipping();
//        }
    }

    @OnClick(R.id.add_image)
    public void addImg(){
        ImageView imageView=new ImageView(this);
        //imageView.setImageResource(R.drawable.miao);
        imageView.setBackgroundResource(R.drawable.miao);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //viewFlipper.addView(imageView, new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        //LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
       // imageView.setLayoutParams(layoutParams);

        imageContains.addView(imageView);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_release, menu);
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
