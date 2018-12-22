package dandandateam.dandanda.Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import dandandateam.dandanda.Items.QAItem;
import dandandateam.dandanda.R;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.MyViewHolder> {
    private static final String TAG = "CollectionAdapter";

    private QAItem[] mDataset;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView question_view;
        public TextView answer_view;
        public TextView follow_num;
        public TextView answer_num;
        public QAItem qa_item;

        public MyViewHolder(View v) {
            super(v);
            question_view = (TextView)v.findViewById(R.id.collection_question);
            answer_view = (TextView)v.findViewById(R.id.collection_answer);
            follow_num = (TextView)v.findViewById(R.id.collection_follow_num);
            answer_num = (TextView)v.findViewById(R.id.collection_answer_num);

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


    public CollectionAdapter(QAItem[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CollectionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_collection, parent, false);

        CollectionAdapter.MyViewHolder vh = new CollectionAdapter.MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(CollectionAdapter.MyViewHolder holder, int position) {
        //这里用来写赋值给控件
        holder.qa_item = mDataset[position];
        holder.question_view.setText(mDataset[position].question);
        holder.answer_view.setText(mDataset[position].answer);
        holder.follow_num.setText(mDataset[position].follow_num+" 个关注");
        holder.answer_num.setText(mDataset[position].answer_num+" 个回答");
        Log.d(TAG, "onBindViewHolder: follow_num"+mDataset[position].follow_num);
        Log.d(TAG, "onBindViewHolder: answer_num"+mDataset[position].answer_num);

    }

    //点击 RecyclerView 某条的监听
    public interface OnItemClickListener{

        void onItemClick(View view, QAItem clicked_item);

    }

    private OnItemClickListener onItemClickListener;


    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
