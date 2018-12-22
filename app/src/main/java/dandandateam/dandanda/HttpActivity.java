package dandandateam.dandanda;

import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpActivity extends AppCompatActivity {
    HTTPHandler httpHandler;
    private static final String TAG = "HttpActivity";

    @Override
    protected Dialog onCreateDialog(int id) {
        return super.onCreateDialog(id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        httpHandler = new HTTPHandler();
    }


    public void sendGetRequest(String url) {
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(url).method("GET", null).build();
        final Call call = okHttpClient.newCall(request);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = httpHandler.obtainMessage();
                try {
                    Response response = call.execute();
                    MainQuestionList.clear();
                    String responseData = response.body().string();
                    Log.d(TAG, "http_response"+responseData);
                    try {
                        JSONObject questions = new JSONObject(responseData);
                        Iterator<String> it = questions.keys();

                        while (it.hasNext()) {
                            String value = questions.getString(it.next());

                            JSONObject ques_json = new JSONObject(value);
                            String topic = ques_json.getString("label");
                            String ques_title = ques_json.getString("title");
                            String ques_answer = ques_json.getString("description");
                            String ques_follow_num = ques_json.getString("numFollowers");
                            String ques_answer_num = ques_json.getString("numAnswers");
                            String ques_id = ques_json.getString("questionId");

                            MainQuestionList.MainQuestionItem ques = new MainQuestionList.MainQuestionItem(
                                    "来自话题：" + topic,
                                    ques_title,
                                    ques_answer,
                                    "关注：" + ques_follow_num,
                                    "回答：" + ques_answer_num,
                                    ques_id
                            );
                            MainQuestionList.addItem(ques);
                        }
                    } catch (JSONException e) {
                        msg.what = HTTPUtil.REQUEST_FAIL;
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    msg.what = HTTPUtil.REQUEST_FAIL;
                    e.printStackTrace();
                }
                msg.what = HTTPUtil.REQUEST_OK;
                Log.d("http_response_finish", MainQuestionList.ITEMS.size() + "");
                msg.sendToTarget();
                Log.d("http_response_finish", MainQuestionList.ITEMS.size() + "");
            }

        }).start();
    }

    class HTTPHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {

        }
    }
}
