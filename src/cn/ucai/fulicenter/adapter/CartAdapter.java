package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.GoodDetailsBean;
import cn.ucai.fulicenter.task.UpdateCartStatusTask;
import cn.ucai.fulicenter.utils.ImageUtils;
import cn.ucai.fulicenter.utils.Utils;
import cn.ucai.fulicenter.view.FooterViewHolder;

/**
 * Created by leon on 2016/6/21.
 */
public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = CartAdapter.class.getName();
    Context mContext;
    ArrayList<CartBean> mCartList;

    public CartAdapter(Context mContext, ArrayList<CartBean> mCartBeanList) {
        this.mContext = mContext;
        this.mCartList = FuLiCenterApplication.getInstance().getCartList();
    }

    CartItemViewHolder cartHolder;
    FooterViewHolder footerHolder;
    String footerText;
    AddDelCartClickListener listener;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layout = LayoutInflater.from(mContext);
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case I.TYPE_ITEM:
                holder = new CartItemViewHolder(layout.inflate(R.layout.item_cart, parent, false));
                break;
            case I.TYPE_FOOTER:
                holder = new FooterViewHolder(layout.inflate(R.layout.item_footer, parent, false));
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FooterViewHolder) {
            footerHolder = (FooterViewHolder) holder;
            footerHolder.tvFooter.setText(footerText);
            footerHolder.tvFooter.setVisibility(View.VISIBLE);
            return;
        }
        if (holder instanceof CartItemViewHolder) {
            cartHolder = (CartItemViewHolder) holder;
            final CartBean cart = mCartList.get(position);
            final GoodDetailsBean goods = cart.getGoods();
            if (goods != null) {
                cartHolder.tvGoodName.setText(goods.getGoodsName());
                cartHolder.tvCartPrice.setText(goods.getCurrencyPrice());
                cartHolder.tvCartCount.setText("(" + cart.getCount() + ")");
                ImageUtils.setCartImg(cart.getGoods().getGoodsThumb(), cartHolder.nivGoodThumb);

                listener = new AddDelCartClickListener(goods);
                cartHolder.ivAddCart.setOnClickListener(listener);

                cartHolder.ivDelCart.setOnClickListener(listener);

                cartHolder.cbSelect.setChecked(cart.isChecked());
                cartHolder.cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        //更新选中状态
                        cart.setChecked(b);
                        new UpdateCartStatusTask(cart, mContext).execute();
                    }
                });
            }


        }
    }

    @Override
    public int getItemCount() {
        return mCartList == null ? 1 : mCartList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        } else {
            return I.TYPE_ITEM;
        }
    }

    class CartItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvGoodName;
        NetworkImageView nivGoodThumb;
        CheckBox cbSelect;
        ImageView ivAddCart;
        ImageView ivDelCart;
        TextView tvCartCount;
        TextView tvCartPrice;

        public CartItemViewHolder(View itemView) {
            super(itemView);
            tvGoodName = (TextView) itemView.findViewById(R.id.tv_good_name);
            nivGoodThumb = (NetworkImageView) itemView.findViewById(R.id.niv_good_thumb);
            cbSelect = (CheckBox) itemView.findViewById(R.id.cbSelect);
            ivAddCart = (ImageView) itemView.findViewById(R.id.ivAddCart);
            ivDelCart = (ImageView) itemView.findViewById(R.id.ivDelCart);
            tvCartCount = (TextView) itemView.findViewById(R.id.tvCartCount);
            tvCartPrice = (TextView) itemView.findViewById(R.id.tvCartPrice);
        }
    }

    class AddDelCartClickListener implements View.OnClickListener {
        GoodDetailsBean good;

        public AddDelCartClickListener(GoodDetailsBean good) {
            this.good = good;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ivAddCart:
                    Utils.addCart(mContext,good);
                    break;
                case R.id.ivDelCart:
                    Utils.delCart(mContext,good);
                    break;
            }
        }
    }

}
