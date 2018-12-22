package dandandateam.dandanda.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import dandandateam.dandanda.Items.QAItem;
import dandandateam.dandanda.Items.QuestionItem;
import dandandateam.dandanda.PostActivity;
import dandandateam.dandanda.R;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {
    private QAItem[] mDataset;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView question_view;
        public TextView answer_view;
        public TextView date_view;
        public TextView draft_view;
        public QAItem qa_item;

        public MyViewHolder(View v) {
            super(v);
            question_view = (TextView)v.findViewById(R.id.post_question);
            answer_view = (TextView)v.findViewById(R.id.post_answer);
            date_view = (TextView)v.findViewById(R.id.post_date);
            draft_view = (TextView)v.findViewById(R.id.post_draft);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onItemClickListener != null){
                        onItemClickListener.onItemClick(view, mDataset[getLayoutPosition()]);

                    }
                }
            });

        }
    }


    public PostAdapter(QAItem[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PostAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //这里用来写赋值给控件
        holder.qa_item = mDataset[position];
        holder.question_view.setText(mDataset[position].question);
        holder.date_view.setText(mDataset[position].date_string);
        holder.answer_view.setText(mDataset[position].answer);
        String draft_string = "";
        if(mDataset[position].isDraft){
            draft_string = "草稿中";
        }
        holder.draft_view.setText(draft_string);

    }

    //点击 RecyclerView 某条的监听
    public interface OnItemClickListener{

        void onItemClick(View view, QAItem clicked_item);

    }

    private PostAdapter.OnItemClickListener onItemClickListener;


    public void setOnItemClickListener(PostAdapter.OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }

}
