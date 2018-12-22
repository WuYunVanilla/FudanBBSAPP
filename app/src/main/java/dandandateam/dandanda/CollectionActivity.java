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

import dandandateam.dandanda.Adapter.CollectionAdapter;
import dandandateam.dandanda.Adapter.PostAdapter;
import dandandateam.dandanda.Items.GenderOption;
import dandandateam.dandanda.Items.QAItem;
import dandandateam.dandanda.Items.QuestionItem;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CollectionActivity extends AppCompatActivity {
    private static final String TAG = "CollectionActivity";

    private RecyclerView mRecyclerView;
    private CollectionAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private String stdid = "15302010056";

    private QAItem[] responseData;

    public static QAItem[] myDataset;
    static {
        myDataset = new QAItem[3];
        myDataset[0] = new QAItem("产品原型工具有哪些？","推荐xiaopiu，今年新推出的一款原型设计工具",33,5,1,2);
        myDataset[1] = new QAItem("Axure设计产品原型是否繁琐？","其实这个要看你产品面向的用户是谁",3,1,1,2);
        myDataset[2] = new QAItem("你觉得什么气味特别好闻？","路边烧烤摊的烤肉味",6,3,1,2);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        stdid = Global.getInstance().user_id;

        getListContent();
    }

    private void getListContent(){
        String url = "http://116.62.236.184:5000/collection/?stdid="+stdid;

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
                                int num = response_json.getInt("answer_number");

                                //这里要把数据拿出来放到responseData
                                responseData = new QAItem[num];
                                for(int i = 0;i<num;i++){
                                    String key = "answer"+i;
                                    JSONObject question = response_json.getJSONObject(key);
                                    QAItem item  = new QAItem(question.getString("question"),question.getString("answer"),question.getInt("num_followers"),question.getInt("num_answers"),question.getInt("question_id"),question.getInt("answer_id"));
                                    responseData[i] = item;
                                    Log.d(TAG, "run: "+item.toString());
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
        mRecyclerView = (RecyclerView) findViewById(R.id.collection_list);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new CollectionAdapter(responseData);
        mAdapter.setOnItemClickListener(new CollectionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, QAItem clicked_item) {
                int question_id = clicked_item.question_id;
                int answer_id = clicked_item.answer_id;

                Log.d(TAG, "onItemClick: question_id = "+question_id+" answer_id = " + answer_id);

                //TODO:wuyun 我的收藏-跳转到-回答页面，传递了question_id 和 answer_id
                /*Intent intent = new Intent(CollectionActivity.this,);
                intent.putExtra("question_id",question_id);
                intent.putExtra("answer_id",answer_id);
                startActivity(intent);*/
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration divider = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this,R.drawable.custom_divider));
        mRecyclerView.addItemDecoration(divider);
    }
}
