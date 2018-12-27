package dandandateam.dandanda;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

@SuppressLint("Registered")
public class AnswersListActivity extends AppCompatActivity {
    private String stdid = Global.getInstance().user_id;
    private String queid;
    private String url_params;

    private List<AnswerItem> mData = null;
    private AnswerItemAdapter mAdapter = null;
    private ListView listAnswerItem;

    private Map<String, Integer> answerMap = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers_list);

        queid =  getIntent().getStringExtra("queid");
        url_params = "stdid=" + stdid + "&queid=" + queid;

        JSONObject jsonObject = getPageEntity();

        //fill upper components
        try {
            TextView pdt = (TextView) findViewById(R.id.post_detail_title);
            pdt.setText((String) jsonObject.get("title"));
            TextView pdc = (TextView) findViewById(R.id.post_detail_content);
            pdc.setText((String) jsonObject.get("description"));
            TextView pfn = (TextView) findViewById(R.id.post_follow_num);
            pfn.setText(jsonObject.get("numFollowers") + "个关注");
            TextView pan = (TextView) findViewById(R.id.post_answer_num);
            pan.setText(jsonObject.get("num") + "个回答");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //fill listview
        listAnswerItem = (ListView) findViewById(R.id.list_answer_item);
        mData = new LinkedList<AnswerItem>();
        try {
            Gson gson = new Gson();

            int answer_num = (int) jsonObject.get("num");
            for (int i = 0; i < answer_num; i++) {
                AnswerItemFromHttpGet aifhg = gson.fromJson(jsonObject.get("answer" + i).toString(), AnswerItemFromHttpGet.class);
                answerMap.put(aifhg.content, aifhg.answerId);
                mData.add(new AnswerItem(aifhg.answerer_name, aifhg.content, aifhg.numAgree, aifhg.numCollect));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        mAdapter = new AnswerItemAdapter((LinkedList<AnswerItem>) mData, this);
        listAnswerItem.setAdapter(mAdapter);
        listAnswerItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String text = (String) mData.get(position).getAnswer_content();
                    String ansid = answerMap.get(text)+"";
                    Intent intent = new Intent(AnswersListActivity.this, AnswerDetailActivity.class);
                    intent.putExtra("ansid", ansid);
                    startActivity(intent);
            }
        });

        //
        final Button post_want_follow = findViewById(R.id.post_want_follow);
        final Button post_want_answer = findViewById(R.id.post_want_answer);

        post_want_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject tmp = Global.getHttpGetEntity(Global.url_add_follow, url_params);
                if (tmp != null) {
                    Toast.makeText(getApplicationContext(), "成功",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "失败",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        post_want_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AnswersListActivity.this, AddAnswerActivity.class);
                intent.putExtra("queid", queid);
                startActivity(intent);
            }
        });
    }

    private JSONObject getPageEntity() {
        JSONObject jsonObject = Global.getHttpGetEntity(Global.url_answers_list, url_params);
        return jsonObject;
    }
}

class AnswerItemFromHttpGet {
    int answerId;
    String answerer;
    String answerer_name;
    String content;
    String date;
    int numAgree;
    int numCollect;
}