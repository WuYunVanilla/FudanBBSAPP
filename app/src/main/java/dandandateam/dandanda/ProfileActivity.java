package dandandateam.dandanda;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.google.gson.JsonObject;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dandandateam.dandanda.Adapter.GenderAdapter;
import dandandateam.dandanda.Fragment.DetailsFragment;
import dandandateam.dandanda.Fragment.PersonalFragment;
import dandandateam.dandanda.Items.GenderOption;
import dandandateam.dandanda.Items.ProfileItem;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static java.lang.String.valueOf;

public class ProfileActivity extends AppCompatActivity implements NavigationTabStrip.OnTabStripSelectedIndexListener,PersonalFragment.OnListFragmentInteractionListener,DetailsFragment.OnListFragmentInteractionListener,LovelyChoiceDialog.OnItemSelectedListener<GenderOption>{

    private static final String TAG = "ProfileActivity";

    private String modifyURL = "http://116.62.236.184:5000/modify/?";

    //页面中的tab
    NavigationTabStrip navigationTabStrip;
    TextView navi_main;
    TextView navi_my;

    //"个人主页" 和 "详细信息" 的两个Fragment
    private Fragment mFragPersonal;
    private Fragment mFragDetail;


    private TextView text_nick;
    private TextView text_sig;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //获取页面中的view，并设置对应的listener
        initViews();

        //初始化页面tabs为"个人主页"
        selectPersonalFragment();


    }


    protected void onStart(){
        super.onStart();

        refreshView();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.modify_pass:
                Intent intent = new Intent(ProfileActivity.this,ModifyActivity.class);
                intent.putExtra("label","密码");
                startActivity(intent);
                break;
            default:
                break;
            }
        return super.onOptionsItemSelected(item);
    }

    private void refreshView(){
        text_nick.setText(Global.getInstance().nickname);
        text_sig.setText(Global.getInstance().signature);

        mFragPersonal = null;
        mFragDetail = null;

        //将tab设置为选中第一个
        navigationTabStrip.setTabIndex(0,true);

        //fragment 替代为 个人主页
        this.onEndTabSelected("个人主页",0);

    }


    /**
     * 获取页面中的view，并设置对应的listener
     */
    private void initViews() {

        // 页面中的tabs，listener为this
        navigationTabStrip = (NavigationTabStrip) findViewById(R.id.navtabs);
        navigationTabStrip.setOnTabStripSelectedIndexListener(this);

        navi_main = (TextView)findViewById(R.id.navi_main);
        navi_my = (TextView)findViewById(R.id.navi_my);

        navi_main_click();
        navi_my_click();

        text_nick = (TextView)findViewById(R.id.profile_nick);
        text_sig = (TextView)findViewById(R.id.profile_sig);

        text_nick.setText(Global.getInstance().nickname);
        text_sig.setText(Global.getInstance().signature);

    }


    /**
     * 选择 个人主页 的tab作为初始页面
     */
    private void selectPersonalFragment(){
        //将tab设置为选中第一个
        navigationTabStrip.setTabIndex(0,true);

        //fragment 替代为 个人主页
        this.onEndTabSelected("个人主页",0);
    }


    /**
     * 实现tab点击的时候listener动作，开始点击的时候不做动作
     * @param title
     * @param index
     */
    @Override
    public void onStartTabSelected(String title, int index) {

    }

    /**
     * 实现tab点击的时候listener动作，结束点击的时候切换fragment
     * @param title
     * @param i
     */
    @Override
    public void onEndTabSelected(String title, int i) {
        //获取FragmentManager对象
        FragmentManager manager = getSupportFragmentManager();

        //获取FragmentTransaction对象
        FragmentTransaction transaction = manager.beginTransaction();
        switch (i) {
            case 0:
                //如果"个人主页"对应的Fragment没有实例化，则进行实例化，并显示出来
                if (mFragPersonal == null) {
                    mFragPersonal = new PersonalFragment();
                    transaction.replace(R.id.id_content, mFragPersonal);
                }else {
                    //如果"个人主页"对应的Fragment已经实例化，则直接显示出来
                    transaction.replace(R.id.id_content, mFragPersonal);
                }
                break;
            case 1:
                //如果"详细信息"对应的Fragment没有实例化，则进行实例化，并显示出来
                if (mFragDetail == null) {
                    mFragDetail = new DetailsFragment();
                    transaction.replace(R.id.id_content, mFragDetail);
                }else {
                    transaction.replace(R.id.id_content, mFragDetail);
                }
                break;
        }
        //提交事务，实现tab下的fragment转化
        transaction.commit();
    }

    public void navi_main_click() {
        navi_main.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                ProfileActivity.this.finish();
            }

        });
    }

    public void navi_my_click() {
        navi_my.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
            }

        });
    }


    /**
     * 点击fragment里面的item的时候的监听器
     * @param item
     */
    public void onListFragmentInteraction(ProfileItem item){
        Log.d(TAG, "onListFragmentInteraction: "+item.label);

        String label = item.label;


        if(label.equals("我的关注") || label.equals("我的浏览")){
            Intent intent = new Intent(ProfileActivity.this,QuestionListActivity.class);
            intent.putExtra("label",label);
            startActivity(intent);
        }else if(label.equals("我的发帖")||label.equals("我的回答")){
            Intent intent = new Intent(ProfileActivity.this,PostActivity.class);
            intent.putExtra("label",label);
            startActivity(intent);
        }else if(label.equals("我的收藏")){
            Intent intent = new Intent(ProfileActivity.this,CollectionActivity.class);
            startActivity(intent);
        }else if(label.equals("性别")){
            List<GenderOption> choice_list = new LinkedList<>();
            choice_list.add(new GenderOption("男"));
            choice_list.add(new GenderOption("女"));
            ArrayAdapter<GenderOption> adapter = new GenderAdapter(this, choice_list);
            new LovelyChoiceDialog(this)
                    .setTopColorRes(R.color.primary)
                    .setTitle("修改性别")
                    .setTopTitle("修改性别")
                    .setIcon(R.drawable.gender)
                    .setItems(adapter, this).show();
        }else{
            Intent intent = new Intent(ProfileActivity.this,ModifyActivity.class);
            intent.putExtra("label",label);
            startActivity(intent);
        }

    }

    @Override
    public void onItemSelected(int position, GenderOption item) {
        final String gender = item.gender;
        final Context context = ProfileActivity.this;

        OkHttpClient okHttpClient  = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();


        JsonObject form = new JsonObject();
        form.addProperty("stdid",Global.getInstance().user_id);
        form.addProperty("key","gender");
        form.addProperty("value",gender);



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

                            Global.getInstance().gender = gender;
                            Global.getInstance().detailItem[1].content = gender;

                            ProfileActivity.this.refreshView();


                            final SweetAlertDialog successDialog = new SweetAlertDialog(context,SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("成功")
                                    .setContentText("修改成功！");



                            successDialog.show();


                        }
                    });
                }else{
                    // 修改失败了
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            SweetAlertDialog errorDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("提示")
                                    .setContentText("修改失败！")
                                    .setConfirmButton("好的", new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                                        }
                                    });

                            errorDialog.show();

                        }
                    });
                }



            }
        });
    }


}
