package dandandateam.dandanda;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.LinkedList;

public class AnswerItemAdapter  extends BaseAdapter {

    private LinkedList<AnswerItem> mData;
    private Context mContext;

    public AnswerItemAdapter(LinkedList<AnswerItem> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_answer,parent,false);
        TextView answer_name = (TextView) convertView.findViewById(R.id.answer_name);
        TextView answer_content = (TextView) convertView.findViewById(R.id.answer_content);
        TextView answer_agree_num = (TextView) convertView.findViewById(R.id.answer_agree_num);
        TextView answer_collection_num = (TextView) convertView.findViewById(R.id.answer_collection_num);
        answer_name.setText(mData.get(position).getAnswer_name());
        answer_content.setText(mData.get(position).getAnswer_content());
        answer_agree_num.setText(mData.get(position).getAnswer_agree_num()+"");
        answer_collection_num.setText(mData.get(position).getAnswer_collection_num()+"");
        return convertView;
    }
}