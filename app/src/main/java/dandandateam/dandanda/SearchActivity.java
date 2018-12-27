package dandandateam.dandanda;

import android.content.Intent;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class SearchActivity extends HttpActivity implements SearchFragment.OnListSearchFragmentInteractionListener {
    Button back_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();
        String search_keyword = intent.getStringExtra("search_keyword");
        httpHandler = new SearchHTTPHandler();
        sendGetRequest(HTTPUtil.search_page_urL(search_keyword));

        back_btn = findViewById(R.id.btn_back);
        back_btn_click();
    }

    public void onListSearchFragmentInteraction(MainQuestionList.MainQuestionItem item) {
        //TODO:wuyun

        Log.d("click on search item", "la");
        Intent intent = new Intent();
        intent.putExtra("queid", item.ques_id);
        intent.setClass(SearchActivity.this, AnswersListActivity.class);
        startActivity(intent);
        SearchActivity.this.finish();
    }

    public void back_btn_click() {
        back_btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SearchActivity.this, MainActivity.class);
                startActivity(intent);
                SearchActivity.this.finish();
            }

        });
    }


    class SearchHTTPHandler extends HttpActivity.HTTPHandler {
        @Override
        public void handleMessage(Message msg) {
            Log.d("search_page", msg.what + "");
            if (msg.what == HTTPUtil.REQUEST_OK) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.fragment_container, new SearchFragment());
                transaction.commit();
            }
        }
    }

}


