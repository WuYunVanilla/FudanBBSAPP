package dandandateam.dandanda;

public class AnswerItem {
    private String answerer_name;
    private String answer_content;
    private int answer_agree_num;
    private int answer_collection_num;

    public AnswerItem(String answer_name, String answer_content, int answer_agree_num, int answer_collection_num) {
        this.answer_agree_num = answer_agree_num;
        this.answerer_name = answer_name;
        this.answer_content = answer_content;
        this.answer_collection_num = answer_collection_num;
    }

    public String getAnswer_name() {
        return answerer_name;
    }

    public void setAnswer_name(String answer_name) {
        this.answerer_name = answer_name;
    }

    public String getAnswer_content() {
        return answer_content;
    }

    public void setAnswer_content(String answer_content) {
        this.answer_content = answer_content;
    }

    public int getAnswer_agree_num() {
        return answer_agree_num;
    }

    public void setAnswer_agree_num(int answer_agree_num) {
        this.answer_agree_num = answer_agree_num;
    }

    public int getAnswer_collection_num() {
        return answer_collection_num;
    }

    public void setAnswer_collection_num(int answer_collection_num) {
        this.answer_collection_num = answer_collection_num;
    }


}
