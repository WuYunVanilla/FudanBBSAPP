package dandandateam.dandanda.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import dandandateam.dandanda.Fragment.DetailsFragment.OnListFragmentInteractionListener;
import dandandateam.dandanda.Items.ProfileItem;
import dandandateam.dandanda.R;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ProfileItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewHolder> {

    private final ProfileItem[] mValues;
    private final OnListFragmentInteractionListener mListener;
    Context context;
    Resources resources;

    public DetailAdapter(ProfileItem[] items, OnListFragmentInteractionListener listener, Context context) {
        mValues = items;
        mListener = listener;
        this.context = context;
        resources = context.getResources();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues[position];
        int imgID = resources.getIdentifier(mValues[position].imageSrc, "drawable", context.getPackageName());
        holder.mImgView.setImageResource(imgID);
        holder.mLabelView.setText(mValues[position].label);
        holder.mContentView.setText(mValues[position].content);

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

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImgView;
        public final TextView mLabelView;
        public final TextView mContentView;
        public ProfileItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImgView = (ImageView) view.findViewById(R.id.list_icon2);
            mLabelView = (TextView) view.findViewById(R.id.list_label2);
            mContentView = (TextView) view.findViewById(R.id.list_content2);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mLabelView.getText() + "'";
        }
    }
}
