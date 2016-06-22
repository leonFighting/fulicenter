package cn.ucai.fulicenter.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Response;

import java.util.ArrayList;

import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.activity.BaseActivity;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.GoodDetailsBean;
import cn.ucai.fulicenter.data.ApiParams;
import cn.ucai.fulicenter.data.GsonRequest;
import cn.ucai.fulicenter.utils.Utils;

/**
 * Created by leon on 2016/6/21.
 */
public class DownloadCartListTask extends BaseActivity {
    public static final String TAG = DownloadCartListTask.class.getName();
    Context mContext;
    String userName;
    int pageId;
    int pageSize;
    String path;
    int listSize;
    ArrayList<CartBean> list;

    public DownloadCartListTask(Context context, String userName, int pageId, int pageSize) {
        this.mContext = context;
        this.userName = FuLiCenterApplication.getInstance().getUserName();
        this.pageId = pageId;
        this.pageSize = pageSize;
        initPath();
    }

    private void initPath(){
        try {
            path = new ApiParams()
                    .with(I.Cart.USER_NAME, userName)
                    .with(I.PAGE_ID, pageId + "")
                    .with(I.PAGE_SIZE, pageSize + "")
                    .getRequestUrl(I.REQUEST_FIND_CARTS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void execute(){
        executeRequest(new GsonRequest<CartBean[]>(path,CartBean[].class,
                responseDownloadCartListListener(), errorListener()));
    }

    private Response.Listener<CartBean[]> responseDownloadCartListListener() {
        return new Response.Listener<CartBean[]>(){
            @Override
            public void onResponse(CartBean[] carts) {
                if(carts == null){
                    return;
                }
                final ArrayList<CartBean> cartList = FuLiCenterApplication.getInstance().getCartList();
                list = Utils.array2List(carts);
                try {
                    for(int i=0;i<list.size();i++){
                        CartBean cart = list.get(i);
                        if(!cartList.contains(cart)){
                            cartList.add(cart);
                            path = new ApiParams().with(I.CategoryGood.GOODS_ID, cart.getGoodsId()+"").getRequestUrl(I.REQUEST_FIND_GOOD_DETAILS);
                            Log.e(TAG, "path=" + path);
                            executeRequest(new GsonRequest<GoodDetailsBean>(path, GoodDetailsBean.class,
                                    responseDownloadGoodDetailListener(cart), errorListener()));
                        }else{
                            Log.e(TAG,"cart is exists,cart="+cart);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private Response.Listener<GoodDetailsBean> responseDownloadGoodDetailListener(final CartBean cart) {
        return new Response.Listener<GoodDetailsBean>() {
            @Override
            public void onResponse(GoodDetailsBean goodDetailsBean) {
                listSize++;
                Log.e(TAG,"goodDetailsBean="+goodDetailsBean);
                if(goodDetailsBean!=null){
                    cart.setGoods(goodDetailsBean);
                    ArrayList<CartBean> cartList = FuLiCenterApplication.getInstance().getCartList();
                    if (!cartList.contains(cart)) {
                        cartList.add(cart);
                    }
                }
                if (listSize == list.size()) {
                    Intent intent = new Intent("update_cart");
                    mContext.sendStickyBroadcast(intent);
                }
            }
        };
    }
}
