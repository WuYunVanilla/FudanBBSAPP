package dandandateam.dandanda;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.json.JSONObject;

import java.io.IOException;

import dandandateam.dandanda.Adapter.QuestionLIstAdapter;
import dandandateam.dandanda.Items.QuestionItem;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class QuestionListActivity extends AppCompatActivity {

    private static final String TAG = "QuestionListActivity";

    private RecyclerView mRecyclerView;
    private QuestionLIstAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private String label;
    private String stdid = "15302010056";

    private QuestionItem[] responseData;


    // 用来展示的数据集
    public static QuestionItem[] myDataset;
    static {
        myDataset = new QuestionItem[5];
        myDataset[0] = new QuestionItem("各种健身房器械的正确使用方法？",33,5,1);
        myDataset[1] = new QuestionItem("有哪些好用的在线原型工具？",32,3,2);
        myDataset[2] = new QuestionItem("哪些话一说出口就知道是致死flag？",3,4,2);
        myDataset[3] = new QuestionItem("留学的简历应该怎么写？",1,1,4);
        myDataset[4] = new QuestionItem("最后一个是测试？",36,2,6);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);

        stdid = Global.getInstance().user_id;

        //分辨是 关注 还是 浏览
        String label_o = getIntent().getStringExtra("label");
        this.label = label_o;
        setTitle(label);

        getListContent();


    }

    private void getListContent(){
        String url_word = "";
        if(label.equals("我的关注")){
            url_word = "follow";
        }else{
            url_word = "view";
        }

        String url = "http://116.62.236.184:5000/"+url_word+"/?stdid="+stdid;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().get().url(url).build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String responseStr = response.body().string();
                final JSONObject response_json;
                try {
                    response_json = new JSONObject(responseStr);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "run: "+responseStr);
                        try {
                            int num = response_json.getInt("question_number");

                            //这里要把数据拿出来放到responseData
                            responseData = new QuestionItem[num];
                            for(int i = 0;i<num;i++){
                                String key = "question"+i;
                                JSONObject question = response_json.getJSONObject(key);
                                QuestionItem item  = new QuestionItem(question.getString("question"),question.getInt("num_followers"),question.getInt("num_answers"),question.getInt("question_id"));
                                responseData[i] = item;
                            }

                            initView();

                        }catch (Exception e){
                            Log.e(TAG,e.toString());
                        }
                    }
                });
                }catch (Exception e){
                    Log.e(TAG,e.toString());
                }
            }
        });


    }

    private void initView(){

        mRecyclerView = (RecyclerView) findViewById(R.id.question_list);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new QuestionLIstAdapter(responseData);
        mAdapter.setOnItemClickListener(new QuestionLIstAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, QuestionItem clicked_item) {
                int question_id = clicked_item.question_id;

                Log.d(TAG, "onItemClick: question_id = "+question_id);

                //TODO:wuyun 我的关注/浏览-跳转到-问题详情页面，传递了question_id
                Intent intent = new Intent(QuestionListActivity.this,AnswersListActivity.class);
                intent.putExtra("queid",question_id+"");
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration divider = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this,R.drawable.custom_divider));
        mRecyclerView.addItemDecoration(divider);
    }
}
