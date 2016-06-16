package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.NewGoodBean;

/**
 * Created by leon on 2016/6/15.
 */
public class GoodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_FOOTER = 0;
    public static final int TYPE_ITEM = 1;

    Context mContext;
    ArrayList<NewGoodBean> mGoodList;

    GoodItemViewHolder mGoodItemViewHolder;
    FooterViewHolder mFooterViewHolder;

    public GoodAdapter(Context mContext, ArrayList<NewGoodBean> mGoodList) {
        this.mContext = mContext;
        this.mGoodList = mGoodList;
    }

    private String footerText;
    private boolean isMore;
    public void setFooterText(String footerText) {
        this.footerText = footerText;
        notifyDataSetChanged();
    }

    public void setMore(boolean more) {
        this.isMore = more;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case TYPE_FOOTER:
                View inflate = inflater.inflate(R.layout.item_footer, parent, false);
                holder = new FooterViewHolder(inflate);
                break;
            case TYPE_ITEM:
                View inflate2 = inflater.inflate(R.layout.item_new_good,parent,false);
                holder = new GoodItemViewHolder(inflate2);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FooterViewHolder) {
            mFooterViewHolder = (FooterViewHolder) holder;
            mFooterViewHolder.tvFooter.setText(footerText);
            mFooterViewHolder.tvFooter.setVisibility(View.VISIBLE);
        }
        if (position == mGoodList.size()) {
            return;
        }
        if (holder instanceof GoodItemViewHolder) {
            mGoodItemViewHolder = (GoodItemViewHolder) holder;
            NewGoodBean newGoodBean = mGoodList.get(position);
            mGoodItemViewHolder.tvGoodPrice.setText(newGoodBean.getPromotePrice());
            mGoodItemViewHolder.tvGoodName.setText(newGoodBean.getGoodsName());
        }
    }

    @Override
    public int getItemCount() {
        return mGoodList==null?1:mGoodList.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount()-1) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    public void initList(ArrayList<NewGoodBean> list) {
        if (mGoodList != null) {
            mGoodList.clear();
        }
        mGoodList.addAll(list);
        notifyDataSetChanged();
    }
    public void addItem(ArrayList<NewGoodBean> list) {
        mGoodList.addAll(list);
        notifyDataSetChanged();
    }

    public boolean isMore() {
        return isMore;
    }

    class GoodItemViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        NetworkImageView nivGoodThumb;
        TextView tvGoodName;
        TextView tvGoodPrice;
        public GoodItemViewHolder(View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.layout_good);
            nivGoodThumb = (NetworkImageView) itemView.findViewById(R.id.niv_good_thumb);
            tvGoodName = (TextView) itemView.findViewById(R.id.tv_good_name);
            tvGoodPrice = (TextView) itemView.findViewById(R.id.tv_good_price);
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        TextView tvFooter;
        public FooterViewHolder(View itemView) {
            super(itemView);
            tvFooter = (TextView) itemView.findViewById(R.id.tv_footer);
        }

    }
}
