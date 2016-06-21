package cn.ucai.fulicenter.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.ucai.fulicenter.R;

/**
 * Created by leon on 2016/6/20.
 */
public class CartFragment extends Fragment {
    public static final String TAG = CartFragment.class.getName();
    Context mContext;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout=View.inflate(getActivity(), R.layout.fragment_cart, null);
        mContext = getActivity();
        return layout;
    }
}
