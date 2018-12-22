package dandandateam.dandanda.Items;

import java.io.Serializable;

public class QAItem implements Serializable{

    public String question;
    public String answer;
    public String date_string;
    public boolean isDraft;
    public int follow_num;
    public int answer_num;
    public int question_id;
    public int answer_id;

    public QAItem(String question,String answer,String date_string,boolean isDraft,int question_id,int answer_id){
        this.question = question;
        this.answer = answer;
        this.date_string = date_string;
        this.isDraft = isDraft;
        this.question_id = question_id;
        this.answer_id = answer_id;
    }

    public QAItem(String question,String answer,String date_string,boolean isDraft,int question_id){
        this.question = question;
        this.answer = answer;
        this.date_string = date_string;
        this.isDraft = isDraft;
        this.question_id = question_id;
    }

    public QAItem(String question,String answer,int follow_num,int answer_num,int question_id,int answer_id){
        this.question = question;
        this.answer = answer;
        this.follow_num = follow_num;
        this.answer_num = answer_num;
        this.question_id = question_id;
        this.answer_id = answer_id;
    }

    public String toString(){
        return "follow :"+follow_num+" answerNum: "+answer_num;
    }
}
