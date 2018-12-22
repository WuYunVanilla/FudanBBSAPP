package dandandateam.dandanda.Items;

public class QuestionItem {

    public String content;
    public int follow_num;
    public int answer_num;
    public int question_id;

    public QuestionItem(String c,int f,int a,int question_id){
        content = c;
        follow_num = f;
        answer_num = a;
        this.question_id = question_id;
    }
}
