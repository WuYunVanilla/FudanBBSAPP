package dandandateam.dandanda;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.HttpEntity;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

@SuppressLint("Registered")
public class AddQuestionActivity extends AppCompatActivity {
    private boolean tag_stu_choosed = false;
    private boolean tag_cou_choosed = false;
    private boolean tag_sec_choosed = false;
    private boolean tag_rec_choosed = false;
    private boolean tag_oth_choosed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        final EditText _title = findViewById(R.id.post_title);
        final EditText _content = findViewById(R.id.post_content);


        final Button button_save = findViewById(R.id.post_btn_save);
        final Button button_save_craft = findViewById(R.id.post_btn_save_craft);

        final ImageView tag_stu = findViewById(R.id.post_tag_stu);
        final ImageView tag_cou = findViewById(R.id.post_tag_cou);
        final ImageView tag_sec = findViewById(R.id.post_tag_sec);
        final ImageView tag_rec = findViewById(R.id.post_tag_rec);
        final ImageView tag_oth = findViewById(R.id.post_tag_oth);


        tag_stu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tag_stu_choosed) {
                    tag_stu_choosed = false;
                    tag_stu.setImageResource(R.drawable.student_group);
                } else {
                    tag_stu_choosed = true;
                    tag_stu.setImageResource(R.drawable.students_group_ch);
                }
            }
        });
        tag_cou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tag_cou_choosed) {
                    tag_cou_choosed = false;
                    tag_cou.setImageResource(R.drawable.courses);
                } else {
                    tag_cou_choosed = true;
                    tag_cou.setImageResource(R.drawable.courses_ch);
                }
            }
        });
        tag_sec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tag_sec_choosed) {
                    tag_sec_choosed = false;
                    tag_stu.setImageResource(R.drawable.second_hand_market);
                } else {
                    tag_sec_choosed = true;
                    tag_sec.setImageResource(R.drawable.second_hand_market_ch);
                }
            }
        });
        tag_rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tag_rec_choosed) {
                    tag_rec_choosed = false;
                    tag_rec.setImageResource(R.drawable.recruit_intern);
                } else {
                    tag_rec_choosed = true;
                    tag_rec.setImageResource(R.drawable.recruit_intern_ch);
                }
            }
        });
        tag_oth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tag_oth_choosed) {
                    tag_oth_choosed = false;
                    tag_stu.setImageResource(R.drawable.others);
                } else {
                    tag_oth_choosed = true;
                    tag_oth.setImageResource(R.drawable.others_ch);
                }
            }
        });

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = _title.getText().toString();
                String content = _content.getText().toString();
                sendEntity(title, content, false);

            }
        });

        button_save_craft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = _title.getText().toString();
                String content = _content.getText().toString();
                sendEntity(title, content, true);

            }
        });

    }

    private void sendEntity(String title, String content, boolean is_craft) {
        AddQuestionEntity postEntity = new AddQuestionEntity();
        postEntity.title = title;
        postEntity.description = content;
        postEntity.isPublished = !is_craft;
        postEntity.questioner = Global.getInstance().user_id;
        if (tag_stu_choosed)
            postEntity.label.add(Global.tag_stu);
        if (tag_cou_choosed)
            postEntity.label.add(Global.tag_cou);
        if (tag_rec_choosed)
            postEntity.label.add(Global.tag_rec);
        if (tag_sec_choosed)
            postEntity.label.add(Global.tag_sec);
        if (tag_oth_choosed)
            postEntity.label.add(Global.tag_oth);


        HttpEntity httpEntity = Global.getHttpPostEntity(postEntity, Global.url_post);
        if (httpEntity != null) {
            Toast.makeText(getApplicationContext(), "成功",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "失败",
                    Toast.LENGTH_SHORT).show();
        }
    }
}

class AddQuestionEntity extends Entity {
    String questioner;
    boolean isPublished;
    String title;
    String description;
    List<String> label = new LinkedList<>();
}

