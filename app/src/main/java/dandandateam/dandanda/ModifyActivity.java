package dandandateam.dandanda;

import android.content.Context;
import android.graphics.Color;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ModifyActivity extends AppCompatActivity{

    private static final String TAG = "ModifyActivity";

    private String modifyURL = "http://116.62.236.184:5000/modify/?";

    private String label="默认";
    private String label_english = "nickname";

    private EditText inputEditText;
    private MaterialButton submitButton;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        //分辨是 昵称 、 年级、个性签名
        String label_o = getIntent().getStringExtra("label");
        if(label_o!=null) {
            this.label = label_o;

            if(label.equals("昵称")){
                label_english="nickname";
            }else if(label.equals("年级")){
                label_english="grade";
            }else if(label.equals("个性签名")){
                label_english="signature";
            }else if(label.equals("密码")){
                label_english="password";
            }
        }
        setTitle("修改"+label);


        initView();
        setListener();
    }

    private void initView(){
        inputEditText = (EditText)findViewById(R.id.modify_input);
        inputEditText.setHint("输入新的"+label);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(inputEditText,0);

        submitButton = (MaterialButton)findViewById(R.id.modify_button);

        context = ModifyActivity.this;
    }

    private void setListener(){
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这里要发送post请求，同时要展示一个正在发送的dialog
                final SweetAlertDialog pDialog = new SweetAlertDialog(ModifyActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#7188C3"));
                pDialog.setTitleText("修改中...");
                pDialog.setCancelable(false);
                pDialog.show();

                final String modify_value = inputEditText.getText().toString();


                OkHttpClient okHttpClient  = new OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(10,TimeUnit.SECONDS)
                        .readTimeout(20, TimeUnit.SECONDS)
                        .build();


                JsonObject form = new JsonObject();
                form.addProperty("stdid",Global.getInstance().user_id);
                form.addProperty("key",label_english);
                form.addProperty("value",modify_value);



                RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                        , form.toString());

                final Request request = new Request.Builder()
                        .url(modifyURL)//请求的url
                        .post(requestBody)
                        .build();


                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        System.out.println("连接失败");
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String modify_result = response.body().string();
                        Log.d(TAG, "onResponse: "+modify_result);

                        if(modify_result.equals("modify_ok")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pDialog.cancel();

                                    if(label.equals("昵称")){
                                        Global.getInstance().nickname = modify_value;
                                        Global.getInstance().detailItem[0].content = modify_value;
                                    }else if(label.equals("年级")){
                                        Global.getInstance().grade = modify_value;
                                        Global.getInstance().detailItem[2].content = modify_value;
                                    }else if(label.equals("个性签名")){
                                        Global.getInstance().signature=modify_value;
                                        Global.getInstance().detailItem[3].content=modify_value;
                                    }

                                    final SweetAlertDialog successDialog = new SweetAlertDialog(context,SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("成功")
                                            .setContentText("修改成功！")
                                            .setConfirmButton("好的", new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    ModifyActivity.this.finish();
                                                }
                                            });



                                    successDialog.show();


                                }
                            });
                        }else{
                            // 修改失败了
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pDialog.cancel();

                                    SweetAlertDialog errorDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("提示")
                                            .setContentText("修改失败！")
                                            .setConfirmButton("好的", new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    ModifyActivity.this.finish();
                                                }
                                            });

                                    errorDialog.show();

                                }
                            });
                        }



                    }
                });
            }
        });
    }


}
