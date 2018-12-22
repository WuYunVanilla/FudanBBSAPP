package dandandateam.dandanda.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import dandandateam.dandanda.Adapter.PersonalAdapter;
import dandandateam.dandanda.Global;
import dandandateam.dandanda.Items.ProfileItem;
import dandandateam.dandanda.R;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PersonalFragment extends Fragment {

    // 这里用来实现gridlayout的列数，可以不管
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    //fragment内动作的监听器
    private OnListFragmentInteractionListener mListener;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PersonalFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // 找到layout中的fragment 对应的view
        View view = inflater.inflate(R.layout.fragment_personal, container, false);

        // view应该是一个recycleview
        if (view instanceof RecyclerView) {
            Context context = view.getContext();

            // 获得 recyclerview 的实例
            RecyclerView recyclerView = (RecyclerView) view;

            // 添加分割线 divider
            DividerItemDecoration divider = new DividerItemDecoration(context,DividerItemDecoration.VERTICAL);
            divider.setDrawable(ContextCompat.getDrawable(context,R.drawable.custom_divider));
            recyclerView.addItemDecoration(divider);

            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }


            //设置 adapter 来给布局提供数据
            recyclerView.setAdapter(new PersonalAdapter(Global.getInstance().personalItem, mListener,context));
        }
        return view;
    }


    /**
     * 链接fragment的时候检查，链接的活动有没有提供OnListFragmentInteractionListener
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    /**
     * 和页面断开的时候，不能再次占用监听器了
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(ProfileItem item);
    }
}
