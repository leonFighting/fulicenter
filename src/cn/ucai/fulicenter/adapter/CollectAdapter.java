package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.CollectActivity;
import cn.ucai.fulicenter.bean.CollectBean;
import cn.ucai.fulicenter.utils.ImageUtils;
import cn.ucai.fulicenter.view.FooterViewHolder;

/**
 * Created by leon on 2016/6/21.
 */
public class CollectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    CollectActivity mContext;
    ArrayList<CollectBean> collectList;

    CollectViewHolder collectViewHolder;
    FooterViewHolder footerHolder;
    String footerText;
    boolean isMore;

    public void setFooterText(String footerText) {
        this.footerText = footerText;
        notifyDataSetChanged();
    }

    public void setMore(boolean more) {
        isMore = more;
    }

    public boolean isMore() {
        return isMore;
    }

    public CollectAdapter(Context mContext, ArrayList<CollectBean> collectList) {
        this.mContext = (CollectActivity) mContext;
        this.collectList = collectList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        RecyclerView.ViewHolder holder = null;
        switch (viewType){
            case I.TYPE_FOOTER:
                holder = new FooterViewHolder(inflater.inflate(R.layout.item_footer,parent,false));
                break;
            case I.TYPE_ITEM:
                holder = new CollectViewHolder(inflater.inflate(R.layout.item_collect,parent,false));
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof FooterViewHolder){
            footerHolder = (FooterViewHolder) holder;
            footerHolder.tvFooter.setText(footerText);
            footerHolder.tvFooter.setVisibility(View.VISIBLE);
        }
        if(holder instanceof CollectViewHolder){
            collectViewHolder = (CollectViewHolder) holder;
            final CollectBean collect = collectList.get(position);
            collectViewHolder.tvName.setText(collect.getGoodsName());
            ImageUtils.setNewGoodThumb(collect.getGoodsThumb(), collectViewHolder.iv);

        }
    }


    @Override
    public int getItemCount() {
        return collectList ==null?1: collectList.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==getItemCount()-1){
            return I.TYPE_FOOTER;
        }else{
            return I.TYPE_ITEM;
        }
    }

    public void initItems(ArrayList<CollectBean> list) {
        if(collectList !=null && !collectList.isEmpty()){
            collectList.clear();
        }
        collectList.addAll(list);
        notifyDataSetChanged();
    }

    public void addItems(ArrayList<CollectBean> list) {
        collectList.addAll(list);
        notifyDataSetChanged();
    }

    class CollectViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layoutItem;
        NetworkImageView iv;
        TextView tvName;
        ImageView ivDel;

        public CollectViewHolder(View itemView) {
            super(itemView);
            layoutItem=(LinearLayout) itemView.findViewById(R.id.layout_collect);
            iv=(NetworkImageView) itemView.findViewById(R.id.niv_collect_thumb);
            tvName=(TextView) itemView.findViewById(R.id.tv_collect_name);
            ivDel=(ImageView) itemView.findViewById(R.id.iv_del_collect);
        }
    }
}