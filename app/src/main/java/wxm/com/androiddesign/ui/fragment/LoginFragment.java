package wxm.com.androiddesign.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.ui.MainActivity;
import wxm.com.androiddesign.ui.SignUpActivity;

/**
 * Created by zero on 2015/6/29.
 */
public class LoginFragment extends DialogFragment{

    //@Bind(R.id.fab_login)
    //FloatingActionButton fab_longin;

    LoginCallBack loginCallBack;
    @Bind(R.id.username_edit_text)EditText user_name;
    @Bind(R.id.password_edit_text)EditText password;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_login, container);
        ButterKnife.bind(this,view);
        return view;
    }

    @OnClick(R.id.login_btn)
    public void Login(){
        loginCallBack.onLongin(user_name.getText().toString(),"zerowxm@gmail.com");
        dismiss();
    }

    @OnClick(R.id.signup_btn)
    public void SignUp(){
        dismiss();
        Intent intent=new Intent(getActivity(), SignUpActivity.class);
        getActivity().startActivity(intent);
    }


    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof MainActivity){
            loginCallBack=(LoginCallBack)activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
        //@OnClick(R.id.fab_login)


       // ((TextView)((MainActivity)getActivity()).findViewById(R.id.user_name)).setText(user_name.getText().toString());
       // ((TextView)((MainActivity)getActivity()).findViewById(R.id.user_email)).setText("zerowxm@gmail.com");


    public interface LoginCallBack{
        public void onLongin(String name,String email);
    }
}
