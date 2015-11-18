package wxm.com.maca.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioButton;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import wxm.com.maca.R;
import wxm.com.maca.module.MyUser;
import wxm.com.maca.module.User;
import wxm.com.maca.network.JsonConnection;
import wxm.com.maca.ui.MainActivity;
import wxm.com.maca.utils.MyBitmapFactory;
import wxm.com.maca.utils.MyUtils;

public class SignUpFragment extends DialogFragment{

    public static final int CHOOSE_PHOTO = 0x1;



    @Bind(R.id.user_photo)
    CircleImageView user_photo;
    @Bind(R.id.password_edit_text)
    EditText password;
    @Bind(R.id.emial_edit_text)
    EditText email;
    @Bind(R.id.password_edit_text_again)
    EditText password_again;
    @Bind(R.id.password_text_input_layout)
    TextInputLayout passwordInput;

    @Bind(R.id.email_text_input_layout)
    TextInputLayout emailInput;
    @Bind(R.id.username_edit_text)
    EditText user_name;

    private Uri selectedImgUri = null;
    String gender="";
    User user;
    Bitmap bitmap = null;
    private int type;

    public static SignUpFragment newInstance(int type){
        SignUpFragment signUpFragment=new SignUpFragment();
        Bundle args=new Bundle();
        args.putInt("Type", type);
        signUpFragment.setArguments(args);
        return signUpFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_signup, container);
        ButterKnife.bind(this, view);
        Point size= MyUtils.getScreenSize(getActivity());
        getDialog().getWindow().setLayout(size.x - 30, size.y / 2);
        return view;
    }

    @OnClick(R.id.user_photo)
    public void chooseImg(CircleImageView v) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        Intent chooseImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        chooseImage.setType("image/*");
        Intent chooserIntent = Intent.createChooser(photoPickerIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{chooseImage});
        photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(chooserIntent, CHOOSE_PHOTO);
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @OnClick(R.id.signup_btn)
    public void signup() {
        String checkEmail = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
//        String checkPhone = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
        if ("".equals(gender)){
            Snackbar.make(user_photo,"请选择性别",Snackbar.LENGTH_SHORT).show();
            return;
        }
        if(password.getText().toString().length()<6){
            passwordInput.setErrorEnabled(true);
            passwordInput.setError("密码不得少于六位");
            return;
        }else {
            passwordInput.setErrorEnabled(false);
            if(!password.getText().toString().equals(password_again.getText().toString())){
                passwordInput.setErrorEnabled(true);
                passwordInput.setError("密码输入不一致");
                return;
            }else {
                passwordInput.setErrorEnabled(false);
            }
        }
        if(!email.getText().toString().matches(checkEmail)){
            emailInput.setErrorEnabled(true);
            emailInput.setError("邮箱格式错误");
            return;
        }else {
            emailInput.setErrorEnabled(false);
        }
        if(selectedImgUri==null){
            Snackbar.make(user_photo,"请设置头像",Snackbar.LENGTH_SHORT).show();
            return;
        }
        user = new User("signupemail", getString(user_name), getString(password), getString(email),
                gender, "", "true", "0");

        BackgroundTask task = new BackgroundTask(getActivity());
        user.setUserId(getString(email));
        task.execute(user);
    }

    @OnClick({R.id.radio_man,R.id.radio_woman})
    public void onRadioButtonClicked(View view){
        boolean checked=((RadioButton)view).isChecked();
        switch (view.getId()){
            case R.id.radio_woman:
                if (checked)
                    gender="女";
                break;
            case R.id.radio_man:
                if (checked)
                    gender="男";
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type=getArguments().getInt("Type");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private class BackgroundTask extends AsyncTask<User, Void, Integer> {
        MaterialDialog materialDialog;
        Context activity;
        String mResult;

        public BackgroundTask(Context activity) {
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            Log.d("Task", "onPreExecute");
            materialDialog = new MaterialDialog.Builder(activity)
                    .title(R.string.signup_title)
                    .content(R.string.please_wait)
                    .progress(true, 0)
                    .progressIndeterminateStyle(false)
                    .show();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            Log.d("Task", "onPostExecute");
            if (integer == 0) {
                materialDialog.dismiss();

                if (type==MyUtils.LOGIN){
                    Intent intent = new Intent(activity,MainActivity.class);
                    dismiss();
                    activity.startActivity(intent);
                    getActivity().finish();
                }
            }  else if (integer == 2) {
                materialDialog.dismiss();
                new MaterialDialog.Builder(activity)
                        .title("注册失败")
                        .positiveText("确定")
                        .show();
            }
        }

        @Override
        protected Integer doInBackground(User... params) {
            Log.d("Task", "doInBackground");
            try {
                    bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), selectedImgUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String icon = MyBitmapFactory.BitmapToString(bitmap);
            if (!isOnline()) {
                return 3;
            }
            user.setUserIcon(icon);
            JSONObject jsonObject= null;
            try {
                jsonObject = new JSONObject(JsonConnection.getJSON(new Gson().toJson(user)));
                mResult=jsonObject.getString("result");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (mResult != "") {
                if (!mResult.contains("false")) {
                    MyUtils.signupHX(mResult,"7777777");
                    MyUser.setUserEmail(getString(email));
                    MyUser.setUserPassword(getString(password));
                    SharedPreferences prefs = activity.getSharedPreferences("wxm.com.androiddesign", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("UserEmail",getString(email));
                    editor.putString("UserPassword", getString(password));
                    editor.putString("LoginType", MyUser.EMAIL);
                    editor.putBoolean("isSignup", true);
                    editor.putString("easemobId",mResult);
                    editor.apply();
                    return 0;
                } else
                    return 2;
            }
            return 2;

        }
    }

    public static String getString(EditText editText) {
        return editText.getText().toString();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri chosenImageUri = data.getData();
            selectedImgUri = chosenImageUri;
            Picasso.with(getContext()).load(selectedImgUri).into(user_photo);
        }
    }
}
