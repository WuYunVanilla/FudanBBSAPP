package dandandateam.dandanda;

import android.content.Intent;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.gigamole.navigationtabstrip.NavigationTabStrip;

import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends HttpActivity implements HotFragment.OnListHotFragmentInteractionListener,
        RecommFragment.OnListRecommFragmentInteractionListener, NavigationTabStrip.OnTabStripSelectedIndexListener {
    NavigationTabStrip navigation;
    Button search_btn;
    TextView navi_main;
    TextView navi_my;
    TextView search_txt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        httpHandler = new ActivityHTTPHandler();


        navi_main = findViewById(R.id.navi_main);
        navi_my = findViewById(R.id.navi_my);
        search_btn = findViewById(R.id.btn_search);
        search_txt = findViewById(R.id.txt_search);
        navigation = (NavigationTabStrip) findViewById(R.id.navigation);
        navigation.setOnTabStripSelectedIndexListener(this);
        navigation.setTabIndex(1);

        sendGetRequest(HTTPUtil.main_page_url());

        search_btn_click();
        navi_main_click();
        navi_my_click();
        search_txt_enter();

    }

    public void onListHotFragmentInteraction(MainQuestionList.MainQuestionItem item) {
        //TODO:wuyun
        Intent intent = new Intent();
        intent.putExtra("question_id",item.ques_id );
        intent.setClass(MainActivity.this, SearchActivity.class);
        startActivity(intent);
        MainActivity.this.finish();
    }

    public void onListRecommFragmentInteraction(MainQuestionList.MainQuestionItem item) {
        //TODO:wuyun
        Intent intent = new Intent();
        intent.putExtra("question_id",item.ques_id );
        intent.setClass(MainActivity.this, SearchActivity.class);
        startActivity(intent);
        MainActivity.this.finish();
    }

    public void onStartTabSelected(final String title, final int index) {

    }


    public void onEndTabSelected(final String title, final int index) {

        if (index == 1) {
            Log.d("op: ", "main page");
            sendGetRequest(HTTPUtil.main_page_url());
        } else {
            Log.d("op: ", "recommend page");
            sendGetRequest(HTTPUtil.recomm_page_url(Global.getInstance().user_id));

        }

    }

    public void search_btn_click() {
        //TODO:wuyun 跳到发帖页面
        search_btn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
            }

        });
    }

    public void navi_main_click() {
        navi_main.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

            }

        });
    }

    public void navi_my_click() {
        navi_my.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                MainActivity.this.finish();
            }

        });
    }

    public void search_txt_enter() {
        search_txt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    String input_txt = search_txt.getText().toString();
                    if (input_txt.equals("")) {
                        search_txt.setHint("请输入搜索关键词");
                        return false;
                    }
                    Intent intent = new Intent();
                    intent.putExtra("search_keyword", input_txt);
                    intent.setClass(MainActivity.this, SearchActivity.class);
                    startActivity(intent);
                    MainActivity.this.finish();
                    return true;
                }
                return false;
            }
        });
    }


    class ActivityHTTPHandler extends HttpActivity.HTTPHandler {
        @Override
        public void handleMessage(Message msg) {
            Log.d("main_page", msg.what + "");
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (msg.what == HTTPUtil.REQUEST_OK) {
                if (navigation.getTabIndex() == 0) {
                    transaction.replace(R.id.fragment_container, new RecommFragment());

                } else {
                    transaction.replace(R.id.fragment_container, new HotFragment());
                }
            }
            transaction.commit();
        }
    }


}

