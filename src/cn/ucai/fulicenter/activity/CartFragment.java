package cn.ucai.fulicenter.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.CartAdapter;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.GoodDetailsBean;

/**
 * Created by leon on 2016/6/20.
 */
public class CartFragment extends Fragment {
    public static final String TAG = CartFragment.class.getName();
    Context mContext;
    CartAdapter mAdapter;
    ArrayList<CartBean> mCartList;
    RecyclerView mrvCart;
    LinearLayoutManager mLinearLayoutManager;

    TextView mtvSumPrice;
    TextView mtvSavePrice;
    TextView mtvNothing;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout=View.inflate(getActivity(), R.layout.fragment_cart, null);
        mContext = getActivity();
        mCartList = new ArrayList<CartBean>();
        initView(layout);
        initCartList();
        registerCartChangedReceiver();
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        initCartList();
    }

    private void initCartList() {
        ArrayList<CartBean> cartList = FuLiCenterApplication.getInstance().getCartList();
        mCartList.clear();
        mCartList.addAll(cartList);
        mAdapter.notifyDataSetChanged();
        sumPrice();
        if(mCartList!=null&&mCartList.size()>0) {
            mtvNothing.setVisibility(View.GONE);
        }else{
            mtvNothing.setVisibility(View.VISIBLE);
        }
    }

    //合计:计算购物车商品被选中时总价tvSumPrice和节省的钱tvSavePrice
    private void sumPrice() {
        int sumPrice = 0;//总价
        int rankPrice = 0;//打折价
        if (mCartList != null && mCartList.size() > 0) {
            for (CartBean cart : mCartList) {
                GoodDetailsBean goods = cart.getGoods();
                if (goods != null && cart.isChecked()) {
                    sumPrice += price2int(goods.getCurrencyPrice()) * cart.getCount();
                    rankPrice += price2int(goods.getRankPrice()) * cart.getCount();
                }
            }
        }
        int savePrice = sumPrice - rankPrice;
        mtvSumPrice.setText("合计:￥"+sumPrice);
        mtvSavePrice.setText("节省：￥"+savePrice);
    }

    public int price2int(String strPrice) {
        int intPrice=Integer.parseInt(strPrice.substring(strPrice.indexOf("￥") + 1));
        return intPrice;
    }

    private void initView(View layout) {
        mtvSavePrice= (TextView) layout.findViewById(R.id.tvSavePrice);
        mtvNothing = (TextView) layout.findViewById(R.id.tv_nothing);
        mtvSumPrice = (TextView) layout.findViewById(R.id.tvSumPrice);
        mrvCart = (RecyclerView) layout.findViewById(R.id.rv_cart);
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        mAdapter = new CartAdapter(mContext, mCartList);
        mrvCart.setAdapter(mAdapter);
        mrvCart.setLayoutManager(mLinearLayoutManager);
        mtvNothing.setVisibility(View.VISIBLE);

    }

    class CartChangedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            initCartList();
        }
    }

    CartChangedReceiver mCartChangedReceiver;
    private void registerCartChangedReceiver() {
        mCartChangedReceiver=new CartChangedReceiver();
        IntentFilter filter=new IntentFilter("update_cart");
        mContext.registerReceiver(mCartChangedReceiver, filter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mCartChangedReceiver!=null) {
            mContext.unregisterReceiver(mCartChangedReceiver);
        }
    }
}
