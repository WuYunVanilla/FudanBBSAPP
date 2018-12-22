package dandandateam.dandanda;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for providing sample ques_title for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class MainQuestionList {

    /**
     * An array of sample (dummy) items.
     */
    public static List<MainQuestionItem> ITEMS = new ArrayList<MainQuestionItem>();

    private static int COUNT = 0;

    public static void init() {
        ITEMS.add(MainQuestionItem.default_item());
    }

    public static void addItem(MainQuestionItem item) {
        ITEMS.add(item);
        COUNT += 1;
    }

    public static void clear() {
        ITEMS.clear();
    }

    public static String print() {
        String ret = "";
        for (int i = 0; i < ITEMS.size(); i++) {
            ret += ITEMS.get(i).toString();
        }
        return ret;
    }


    /**
     * A dummy item representing a piece of ques_title.
     */
    public static class MainQuestionItem {
        public final String topic;
        public final String ques_title;
        public final String ques_answer;
        public final String ques_follow_num;
        public final String ques_answer_num;
        public final String ques_id;

        public MainQuestionItem(String topic, String title, String details, String ques_follow_num, String ques_answer_num, String ques_id) {
            this.topic = topic;
            this.ques_title = title;
            this.ques_answer = details;
            this.ques_follow_num = ques_follow_num;
            this.ques_answer_num = ques_answer_num;
            this.ques_id = ques_id;
        }

        @Override
        public String toString() {
            return ques_title;
        }

        public static MainQuestionItem default_item() {
            return new MainQuestionItem(
                    "来自话题：二手市场",
                    "马基有人要吗",
                    "2015年的",
                    "关注：0",
                    "回答：0",
                    "0"
            );
        }
    }
}
