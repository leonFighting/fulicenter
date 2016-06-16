package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import cn.ucai.fulicenter.D;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.GoodDetailActivity;
import cn.ucai.fulicenter.bean.NewGoodBean;
import cn.ucai.fulicenter.utils.ImageUtils;
import cn.ucai.fulicenter.view.FooterViewHolder;

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

    int sortBy;

    public GoodAdapter(Context mContext, ArrayList<NewGoodBean> mGoodList, int sortBy) {
        this.mContext = mContext;
        this.mGoodList = mGoodList;
        this.sortBy = sortBy;
    }

    public void setSortBy(int sortBy) {
        this.sortBy = sortBy;
        sort(sortBy);
        notifyDataSetChanged();
    }

    private void sort(final int sortBy) {
        Collections.sort(mGoodList, new Comparator<NewGoodBean>() {
            @Override
            public int compare(NewGoodBean n1, NewGoodBean n2) {
                int result = 0;
                switch (sortBy) {
                    case I.SORT_BY_ADDTIME_ASC:
                        result = (int) (n1.getAddTime() - n2.getAddTime());
                        break;
                    case I.SORT_BY_ADDTIME_DESC:
                        result = (int) (n2.getAddTime() - n1.getAddTime());
                        break;
                    case I.SORT_BY_PRICE_ASC: {
                        int p1 = convertPrice(n1.getCurrencyPrice());
                        int p2 = convertPrice(n2.getCurrencyPrice());
                        result = p1 - p2;
                    }
                    break;
                    case I.SORT_BY_PRICE_DESC: {
                        int p1 = convertPrice(n1.getCurrencyPrice());
                        int p2 = convertPrice(n2.getCurrencyPrice());
                        result = p2 - p1;
                    }
                    break;

                }
                return result;
            }

            private int convertPrice(String price) {
                price = price.substring(price.indexOf("￥") + 1);
                int p1 = Integer.parseInt(price);
                return p1;
            }
        });
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
                View inflate2 = inflater.inflate(R.layout.item_new_good, parent, false);
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
            final NewGoodBean newGoodBean = mGoodList.get(position);
            mGoodItemViewHolder.tvGoodPrice.setText(newGoodBean.getPromotePrice());
            mGoodItemViewHolder.tvGoodName.setText(newGoodBean.getGoodsName());
            ImageUtils.setNewGoodThumb(newGoodBean.getGoodsThumb(), mGoodItemViewHolder.nivGoodThumb);

            mGoodItemViewHolder.layoutGood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mContext.startActivity(new Intent(mContext, GoodDetailActivity.class)
                            .putExtra(D.NewGood.KEY_GOODS_ID,newGoodBean.getGoodsId()));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mGoodList == null ? 1 : mGoodList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
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
        sort(sortBy);
        notifyDataSetChanged();
    }

    public void addItem(ArrayList<NewGoodBean> list) {
        mGoodList.addAll(list);
        sort(sortBy);
        notifyDataSetChanged();
    }

    public boolean isMore() {
        return isMore;
    }

    class GoodItemViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutGood;
        NetworkImageView nivGoodThumb;
        TextView tvGoodName;
        TextView tvGoodPrice;

        public GoodItemViewHolder(View itemView) {
            super(itemView);
            layoutGood = (LinearLayout) itemView.findViewById(R.id.layout_good);
            nivGoodThumb = (NetworkImageView) itemView.findViewById(R.id.niv_good_thumb);
            tvGoodName = (TextView) itemView.findViewById(R.id.tv_good_name);
            tvGoodPrice = (TextView) itemView.findViewById(R.id.tv_good_price);
        }
    }

}
