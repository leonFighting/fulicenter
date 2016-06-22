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
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.data.ApiParams;
import cn.ucai.fulicenter.data.GsonRequest;

/**
 * Created by leon on 2016/6/21.
 * 更新任务车数据的任务类
 */
public class UpdateCartStatusTask extends BaseActivity {
    public static final String TAG = UpdateCartStatusTask.class.getName();
    Context mContext;
    CartBean mCart;
    String path;
    int actionType = 0;//--0删除 --1更新 --2添加
    public UpdateCartStatusTask(CartBean mCart, Context mContext) {
        this.mCart = mCart;
        this.mContext = mContext;
        initPath();
    }

    private void initPath() {
        ArrayList<CartBean> cartList = FuLiCenterApplication.getInstance().getCartList();
        try {
            if(cartList!=null&&cartList.contains(mCart)){
                if(mCart.getCount()<=0){
                    actionType = 0;
                    path = new ApiParams().with(I.Cart.ID, mCart.getId()+"")
                            .getRequestUrl(I.REQUEST_DELETE_CART);
                    Log.e(TAG, "path=" + path);
                }else{
                    actionType = 1;
                    path = new ApiParams().with(I.Cart.IS_CHECKED, mCart.isChecked()+"")
                            .with(I.Cart.ID, mCart.getId()+"")
                            .with(I.Cart.COUNT, mCart.getCount()+"")
                            .getRequestUrl(I.REQUEST_UPDATE_CART);
                    Log.e(TAG, "path=" + path);
                }
            }else{
                actionType = 2;
                path = new ApiParams().with(I.Cart.COUNT, mCart.getCount()+"")
                        .with(I.Cart.GOODS_ID, mCart.getGoodsId()+"")
                        .with(I.Cart.IS_CHECKED, mCart.isChecked()+"")
                        .with(I.Cart.USER_NAME,FuLiCenterApplication.getInstance().getUserName())
                        .getRequestUrl(I.REQUEST_ADD_CART);
                Log.e(TAG, "path=" + path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void execute() {
        if (path != null) {
            executeRequest(new GsonRequest<MessageBean>(path,MessageBean.class,
                    responUpdateCartStatusListener(),errorListener()));
        }
    }

    private Response.Listener<MessageBean> responUpdateCartStatusListener() {
        return new Response.Listener<MessageBean>() {
            @Override
            public void onResponse(MessageBean messageBean) {
                if (messageBean.isSuccess()) {
                    ArrayList<CartBean> cartList = FuLiCenterApplication.getInstance().getCartList();
                    if (actionType == 0) {
                        cartList.remove(mCart);
                    }
                    if (actionType == 1) {
                        cartList.set(cartList.indexOf(mCart), mCart);
                    }
                    if (actionType == 2) {
                        mCart.setId(Integer.parseInt(messageBean.getMsg()));
                        cartList.add(mCart);
                    }
                    mContext.sendStickyBroadcast(new Intent("update_cart"));
                }
            }
        };
    }


}
