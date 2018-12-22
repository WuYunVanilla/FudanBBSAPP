package dandandateam.dandanda.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import dandandateam.dandanda.Fragment.PersonalFragment.OnListFragmentInteractionListener;
import dandandateam.dandanda.Items.ProfileItem;
import dandandateam.dandanda.R;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ProfileItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class PersonalAdapter extends RecyclerView.Adapter<PersonalAdapter.ViewHolder> {

    //这里用来存储list中应该有的数据
    private final ProfileItem[] mValues;
    private final OnListFragmentInteractionListener mListener;
    Context context;
    Resources resources;


    //构建方法，外部传入List items，形成内容
    public PersonalAdapter(ProfileItem[] items, OnListFragmentInteractionListener listener, Context context) {
        mValues = items;
        mListener = listener;
        this.context = context;
        resources = context.getResources();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_personal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.mItem = mValues[position];
        int imgID = resources.getIdentifier(mValues[position].imageSrc, "drawable", context.getPackageName());
        holder.mImgView.setImageResource(imgID);
        holder.mLabelView.setText(mValues[position].label);
        holder.mNumView.setText(mValues[position].content);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.length;
    }



    //viewHolder是用来存储单一的列表item中的所有view
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImgView;
        public final TextView mLabelView;
        public final TextView mNumView;
        public ProfileItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImgView = (ImageView) view.findViewById(R.id.list_icon);
            mLabelView = (TextView) view.findViewById(R.id.list_label);
            mNumView = (TextView) view.findViewById(R.id.list_num);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mLabelView.getText() + "'";
        }
    }
}
