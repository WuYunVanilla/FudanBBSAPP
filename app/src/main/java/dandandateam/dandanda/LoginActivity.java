package dandandateam.dandanda;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dandandateam.dandanda.Items.ProfileItem;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.lang.String.valueOf;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private String loginURL = "http://116.62.236.184:5000/login/?";
    private String forgetURL = "http://116.62.236.184:5000/modify_password/?";
    AlertDialog alertDialog;
    MyHandler myHandler;

    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_login) Button _loginButton;
    @BindView(R.id.link_signup) TextView _signupLink;
    @BindView(R.id.link_forgetPass) TextView _forgetPassLink;

    private String stdid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        myHandler = new MyHandler();

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        _forgetPassLink.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                //向服务器发送请求忘记密码的请求
                forgetPass();

            }
        });
    }

    private class MyHandler extends Handler {
        public MyHandler() {
        }

        public MyHandler(Looper L) {
            super(L);
        }

        @Override
        public void handleMessage(Message msg) {
            Log.d("MyHandler", "handleMessage");
            super.handleMessage(msg);
            // 此处可以更新UI
            Bundle b = msg.getData();
            String login_result = b.getString("login_result");

            Log.d(TAG, "handler login: "+login_result);

            if(login_result!=null) {
                if (login_result.equals("password_error")) {
                    onLoginFailed("密码错误");
                } else if (login_result.equals("user_not_exist")) {
                    onLoginFailed("用户不存在，请先注册");
                }else{
                    onLoginSuccess(login_result);
                }
            }

            String forget_result = b.getString("forget_result");
            Log.d(TAG, "handler forget: "+forget_result);

            if(forget_result!=null) {
                if (forget_result.equals("user_not_exist")) {
                    onLoginFailed("用户不存在，请先注册");
                } else if (forget_result.equals("ignore")) {
                    Log.d(TAG, "handleMessage: forget 忘记密码成功");

                    //新建一个alertDialog，提醒用户去看邮箱
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("新密码生成完成");
                    builder.setIcon(R.mipmap.ic_launcher);
                    builder.setMessage("请查看学号邮箱，新密码已发送");
                    builder.setPositiveButton("知道啦", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            _loginButton.setEnabled(true);
                        }
                    });
                    alertDialog = builder.create();

                    if (alertDialog != null && !alertDialog.isShowing()) {
                        alertDialog.show();
                    }
                }
            }

        }
    }

    public void forgetPass(){
        Log.d(TAG, "forgetPass");

        //检查学号是否填写了
        if(!validateEmail()){
            onLoginFailed("请先填写学号再按忘记密码");
            return;
        }

        //让登录按钮失效，避免重复登录
        _loginButton.setEnabled(false);

        //向服务器发送忘记密码的请求
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("加载中...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        final String urlString = forgetURL+"stdid="+email;

        new Thread(new Runnable() {//创建子线程
            @Override
            public void run() {
                // 向服务器发送用户名和密码
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(reader);

                    StringBuffer buffer = new StringBuffer();
                    String temp = null;

                    while ((temp = bufferedReader.readLine()) != null) {
                        buffer.append(temp);
                    }

                    bufferedReader.close();//记得关闭
                    reader.close();
                    inputStream.close();

                    String forget_result = buffer.toString();
                    Log.d(TAG,forget_result);

                    Message msg = new Message();
                    Bundle b = new Bundle();
                    b.putString("forget_result",forget_result);
                    msg.setData(b);
                    LoginActivity.this.myHandler.sendMessage(msg);

                    progressDialog.dismiss();


                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();//启动子线程
    }

    public void login() {
        Log.d(TAG, "Login");

        //检查输入是否合法
        if (!validate()) {
            onLoginFailed("输入不合法");
            return;
        }

        //让登录按钮失效，避免重复登录
        _loginButton.setEnabled(false);

        //设置一个登录的进度框
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("登录中...");
        progressDialog.show();

        final String email = _emailText.getText().toString();
        stdid = email;
        final String password = _passwordText.getText().toString();

        Log.d(TAG, "login: email"+email);
        Log.d(TAG, "login: password"+password);

        new Thread(new Runnable() {//创建子线程
            @Override
            public void run() {
                // 向服务器发送用户名和密码
                String urlString = loginURL + "stdid=" + email + "&password=" + password;
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(reader);

                    StringBuffer buffer = new StringBuffer();
                    String temp = null;

                    while ((temp = bufferedReader.readLine()) != null) {
                        buffer.append(temp);
                    }

                    bufferedReader.close();//记得关闭
                    reader.close();
                    inputStream.close();

                    String login_result = buffer.toString();
                    Log.d(TAG,login_result);

                    Message msg = new Message();
                    Bundle b = new Bundle();
                    b.putString("login_result",login_result);
                    msg.setData(b);
                    LoginActivity.this.myHandler.sendMessage(msg);

                    Global.getInstance().user_id = email;
                    progressDialog.dismiss();


                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();//启动子线程


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                //this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess(String login_result) {
        try {
            JSONObject result = new JSONObject(login_result);
            String gender = result.getString("gender");
            String grade = result.getString("grade");
            String nickname = result.getString("nickname");
            int num_answer = result.getInt("num_answer");
            int num_collection = result.getInt("num_collection");
            int num_follow = result.getInt("num_follow");
            int num_posting = result.getInt("num_posting");
            int num_view = result.getInt("num_view");
            String signature = result.getString("signature");

            Global.getInstance().gender = gender;
            Global.getInstance().grade = grade;
            Global.getInstance().nickname = nickname;
            Global.getInstance().num_answer = num_answer;
            Global.getInstance().num_collection = num_collection;
            Global.getInstance().num_follow = num_follow;
            Global.getInstance().num_posting = num_posting;
            Global.getInstance().num_view = num_view;
            Global.getInstance().signature = signature;

            Global.getInstance().user_id =stdid;


            ProfileItem[] personalItem = new ProfileItem[5];
            personalItem[0] = new ProfileItem("ic_chat","我的发帖",valueOf(Global.getInstance().num_posting));
            personalItem[1] = new ProfileItem("ic_create","我的回答",valueOf(Global.getInstance().num_answer));
            personalItem[2] = new ProfileItem("ic_question_answer","我的关注",valueOf(Global.getInstance().num_follow));
            personalItem[3] = new ProfileItem("ic_star","我的收藏",valueOf(Global.getInstance().num_collection));
            personalItem[4] = new ProfileItem("ic_file","我的浏览",valueOf(Global.getInstance().num_view));

            ProfileItem[] detailItem = new ProfileItem[4];
            detailItem[0] = new ProfileItem("ic_person","昵称",Global.getInstance().nickname);
            detailItem[1]=new ProfileItem("gender","性别",Global.getInstance().gender);
            detailItem[2]=new ProfileItem("ic_schoo","年级",Global.getInstance().grade);
            detailItem[3]=new ProfileItem("signing","个性签名",Global.getInstance().signature);

            Global.getInstance().personalItem = personalItem;
            Global.getInstance().detailItem = detailItem;

            _loginButton.setEnabled(true);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            LoginActivity.this.finish();

        }catch (Exception e){
            Log.d(TAG, "onLoginSuccess: "+e.toString());
        }
    }

    public void onLoginFailed(String errorText) {
        Toast.makeText(getBaseContext(), errorText, Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty()) {
            _emailText.setError("请输入正确的学号");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty()) {
            _passwordText.setError("请输入密码");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    public boolean validateEmail(){
        boolean valid = true;

        String email = _emailText.getText().toString();

        if(email.isEmpty()){
            _emailText.setError("请输入学号");
            valid = false;
        }else {
            _emailText.setError(null);
        }

        return valid;
    }
}
