package dandandateam.dandanda;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import dandandateam.dandanda.HotFragment.OnListHotFragmentInteractionListener;
import dandandateam.dandanda.MainQuestionList.MainQuestionItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link MainQuestionItem} and makes a call to the
 * specified {@link OnListHotFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyHotRecyclerViewAdapter extends RecyclerView.Adapter<MyHotRecyclerViewAdapter.ViewHolder> {

    private final List<MainQuestionItem> mValues;
    private final OnListHotFragmentInteractionListener mListener;

    public MyHotRecyclerViewAdapter(List<MainQuestionItem> items, OnListHotFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_question, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.topic.setText(mValues.get(position).topic);
        holder.ques_title.setText(mValues.get(position).ques_title);
        holder.ques_answer.setText(mValues.get(position).ques_answer);
        holder.ques_follow_num.setText(mValues.get(position).ques_follow_num);
        holder.ques_answer_num.setText(mValues.get(position).ques_answer_num);
        holder.ques_id.setText(mValues.get(position).ques_id);


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListHotFragmentInteraction(holder.mItem);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView topic;
        public final TextView ques_title;
        public final TextView ques_answer;
        public final TextView ques_follow_num;
        public final TextView ques_answer_num;
        public final TextView ques_id;
        public MainQuestionItem mItem;

        public ViewHolder(View view) {

            super(view);
            mView = view;
            topic = (TextView) view.findViewById(R.id.topic);
            ques_title = (TextView) view.findViewById(R.id.ques_title);
            ques_answer = (TextView) view.findViewById(R.id.ques_answer);
            ques_follow_num = (TextView) view.findViewById(R.id.ques_follow_num);
            ques_answer_num = (TextView) view.findViewById(R.id.ques_answer_num);
            ques_id = (TextView) view.findViewById(R.id.ques_id);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + ques_title.getText() + "'";
        }
    }


}
