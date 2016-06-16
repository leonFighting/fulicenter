package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.utils.ImageUtils;
import cn.ucai.fulicenter.view.FooterViewHolder;

/**
 * Created by leon on 2016/6/17.
 */
public class BoutiqueAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    ArrayList<BoutiqueBean> mBoutiqueList;
    BoutiqueViewHolder boutiqueViewHolder;
    public BoutiqueAdapter(Context mContext, ArrayList<BoutiqueBean> mBoutiqueList) {
        this.mContext = mContext;
        this.mBoutiqueList = mBoutiqueList;
    }

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

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        RecyclerView.ViewHolder holder = null;
        switch (viewType){
            case I.TYPE_FOOTER:
                holder = new FooterViewHolder(inflater.inflate(R.layout.item_footer,parent,false));
                break;
            case I.TYPE_ITEM:
                holder = new BoutiqueViewHolder(inflater.inflate(R.layout.item_boutique,parent,false));
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof FooterViewHolder){
            ((FooterViewHolder) holder).tvFooter.setText(footerText);
            ((FooterViewHolder) holder).tvFooter.setVisibility(View.VISIBLE);
        }
        if(holder instanceof BoutiqueViewHolder){
            boutiqueViewHolder = (BoutiqueViewHolder) holder;
            final BoutiqueBean boutique = mBoutiqueList.get(position);
            boutiqueViewHolder.mtvBoutiqueTitle.setText(boutique.getTitle());
            boutiqueViewHolder.mtvBoutiqueName.setText(boutique.getName());
            boutiqueViewHolder.mtvBoutiqueDesc.setText(boutique.getDescription());
            ImageUtils.setNewGoodThumb(boutique.getImageurl(),boutiqueViewHolder.mnivBoutiqueImg);

//            boutiqueViewHolder.mLayoutItemBoutique.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent=new Intent(mContext, BoutiqueChildActivity.class);
//                    intent.putExtra(I.Boutique.ID, boutique.getId());
//                    intent.putExtra(I.Boutique.NAME, boutique.getName());
//                    mContext.startActivity(intent);
//                }
//            });
        }
    }

    @Override
    public int getItemCount() {
        return mBoutiqueList==null?1:mBoutiqueList.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==getItemCount()-1){
            return I.TYPE_FOOTER;
        }else{
            return I.TYPE_ITEM;
        }
    }

    public void initItems(ArrayList<BoutiqueBean> list) {
        if(mBoutiqueList!=null && !mBoutiqueList.isEmpty()){
            mBoutiqueList.clear();
        }
        mBoutiqueList.addAll(list);
        notifyDataSetChanged();
    }

    public void addItems(ArrayList<BoutiqueBean> list) {
        mBoutiqueList.addAll(list);
        notifyDataSetChanged();
    }

    class BoutiqueViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout mLayoutItemBoutique;
        NetworkImageView mnivBoutiqueImg;
        TextView mtvBoutiqueTitle;
        TextView mtvBoutiqueName;
        TextView mtvBoutiqueDesc;

        public BoutiqueViewHolder(View itemView) {
            super(itemView);
            mLayoutItemBoutique = (RelativeLayout) itemView.findViewById(R.id.layout_item_boutique);
            mnivBoutiqueImg = (NetworkImageView) itemView.findViewById(R.id.nivBoutiqueImg);
            mtvBoutiqueTitle = (TextView) itemView.findViewById(R.id.tvBoutiqueTitle);
            mtvBoutiqueName = (TextView) itemView.findViewById(R.id.tvBoutiqueName);
            mtvBoutiqueDesc = (TextView) itemView.findViewById(R.id.tvBoutiqueDesc);
        }
    }

}
