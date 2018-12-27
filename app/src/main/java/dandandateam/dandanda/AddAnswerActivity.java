package dandandateam.dandanda;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

@SuppressLint("Registered")
public class AddAnswerActivity extends AppCompatActivity {
    private String queid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_answer);
        queid = getIntent().getStringExtra("queid");

        final EditText _content = findViewById(R.id.add_answer_content);
        final Button button_save = findViewById(R.id.answer_btn_save);
        final Button button_save_craft = findViewById(R.id.answer_btn_save_craft);

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = _content.getText().toString();
                sendEntity(content, true);

            }
        });
        button_save_craft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = _content.getText().toString();
                sendEntity(content, false);

            }
        });

    }

    private void sendEntity(String content, boolean isPublished) {
        AddAnswerEntity answerEntity = new AddAnswerEntity();
        answerEntity.answerer = Global.getInstance().user_id;
        answerEntity.content = content;
        answerEntity.questionID = queid;
        answerEntity.isPublished = isPublished;

        HttpEntity httpEntity = Global.getHttpPostEntity(answerEntity, Global.url_answer);
        if (httpEntity != null) {
            Toast.makeText(getApplicationContext(), "成功",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "失败",
                    Toast.LENGTH_SHORT).show();
        }
    }
}

class AddAnswerEntity extends Entity {
    String answerer;
    String questionID;
    String content;
    boolean isPublished;
}
