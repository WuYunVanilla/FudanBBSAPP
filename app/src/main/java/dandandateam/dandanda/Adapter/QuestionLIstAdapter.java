package dandandateam.dandanda.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import dandandateam.dandanda.Items.QAItem;
import dandandateam.dandanda.Items.QuestionItem;
import dandandateam.dandanda.QuestionListActivity;
import dandandateam.dandanda.R;

public class QuestionLIstAdapter extends RecyclerView.Adapter<QuestionLIstAdapter.MyViewHolder>{
    private QuestionItem[] mDataset;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView content_view;
        public TextView follow_view;
        public TextView answer_view;
        public QuestionItem question_item;

        public MyViewHolder(View v) {
            super(v);
            content_view = (TextView)v.findViewById(R.id.content_textView);
            follow_view = (TextView)v.findViewById(R.id.follow_textView);
            answer_view = (TextView)v.findViewById(R.id.answer_textView);

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


    public QuestionLIstAdapter(QuestionItem[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public QuestionLIstAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_question, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //这里用来写赋值给控件
        holder.question_item = mDataset[position];
        holder.content_view.setText(mDataset[position].content);
        holder.follow_view.setText(mDataset[position].follow_num+" 个关注");
        holder.answer_view.setText(mDataset[position].answer_num+" 个回答");

    }

    //点击 RecyclerView 某条的监听
    public interface OnItemClickListener{

        void onItemClick(View view, QuestionItem clicked_item);

    }

    private QuestionLIstAdapter.OnItemClickListener onItemClickListener;


    public void setOnItemClickListener(QuestionLIstAdapter.OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }

}
