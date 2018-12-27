package dandandateam.dandanda;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

@SuppressLint("Registered")
public class AnswerDetailActivity extends AppCompatActivity {
    private String stdid = Global.getInstance().user_id;
    private String ansid;
    private String url_params;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_detail);
        //ansid = getIntent().getStringExtra("ansid");
        ansid="1";
        url_params = "stdid=" + stdid + "&ansid=" +ansid;

        final TextView user_name = findViewById(R.id.user_name);
        final TextView answer_content = findViewById(R.id.answer_detail_content);
        final TextView answer_time = findViewById(R.id.answer_time);
        final TextView agree_num = findViewById(R.id.agree_num);
        final TextView garner_num = findViewById(R.id.garner_num);

        JSONObject jsonObject = getPageEntity();
        try {
            String answerer_name = (String) jsonObject.get("answerer_name");
            String content = (String) jsonObject.get("content");
            String date = (String) jsonObject.get("date");
            String numAgree = (int) jsonObject.get("numAgree")+"";
            String numCollect = (int) jsonObject.get("numCollect")+"";

            user_name.setText(answerer_name);
            answer_content.setText(content);
            answer_time.setText("发布于"+date);
            agree_num.setText(numAgree);
            garner_num.setText(numCollect);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        final ImageView answer_agree_click = findViewById(R.id.answer_agree_click);
        final ImageView answer_collect_click = findViewById(R.id.answer_collect_click);

        answer_agree_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject tmp = Global.getHttpGetEntity(Global.url_add_agree, url_params);
                if (tmp != null) {
                    Toast.makeText(getApplicationContext(), "成功",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "失败",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
        answer_collect_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject tmp = Global.getHttpGetEntity(Global.url_add_collection, url_params);
                if (tmp != null) {
                    Toast.makeText(getApplicationContext(), "成功",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "失败",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private JSONObject getPageEntity() {

        JSONObject jsonObject = Global.getHttpGetEntity(Global.url_answer_detail, url_params);

        return jsonObject;
    }
}