package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;

import cn.ucai.fulicenter.bean.BoutiqueBean;

/**
 * Created by leon on 2016/6/17.
 */
public class BoutiqueAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    ArrayList<BoutiqueBean> mBoutiqueList;

    public BoutiqueAdapter(Context mContext, ArrayList<BoutiqueBean> mBoutiqueList) {
        this.mContext = mContext;
        this.mBoutiqueList = mBoutiqueList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
