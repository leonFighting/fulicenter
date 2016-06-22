package cn.ucai.fulicenter.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import cn.ucai.fulicenter.D;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.AlbumBean;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.GoodDetailsBean;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.data.ApiParams;
import cn.ucai.fulicenter.data.GsonRequest;
import cn.ucai.fulicenter.task.DownloadCollectCountTask;
import cn.ucai.fulicenter.utils.ImageUtils;
import cn.ucai.fulicenter.utils.Utils;
import cn.ucai.fulicenter.view.DisplayUtils;
import cn.ucai.fulicenter.view.FlowIndicator;
import cn.ucai.fulicenter.view.SlideAutoLoopView;

/**
 * Created by leon on 2016/6/16.
 */
public class GoodDetailActivity extends BaseActivity {
    public static final String TAG = GoodDetailActivity.class.getName();
    GoodDetailActivity mContext;
    GoodDetailsBean mGoodDetails;

    LinearLayout mLayoutColors;
    ImageView mivAddCart;
    ImageView mivShare;
    ImageView mivCollect;
    TextView mtvCartCount;
    TextView mtvGoodEnglishName;
    TextView mtvGoodName;
    TextView mtvShopPrice;
    TextView mtvCurrencyPrice;
    SlideAutoLoopView mSlideAutoLoopView;
    FlowIndicator mFlowIndicator;
    WebView mWebView;

    //当前轮播图片的颜色
    int mCurrentColor;

    int goodId;
    //商品是否被收藏
    boolean isCollect;
    int action;

    @Override

    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_good_details);
        mContext = this;
        initView();
        initData();
        setListener();
    }

    private void setListener() {
        setCollectClickListener();
        setCartClickListener();
        registerCartChangedReceiver();
    }

    private void setCartClickListener() {
        mivAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.addCart(mContext,mGoodDetails);
            }
        });
    }

    private void setCollectClickListener() {
        mivCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = FuLiCenterApplication.getInstance().getUser();
                if (user == null) {
                    startActivity(new Intent(GoodDetailActivity.this, LoginActivity.class));
                } else {
                    String userName = user.getMUserName();
                    try {
                        String path = "";
                        if (isCollect) {
                            path = new ApiParams()
                                    .with(I.Collect.GOODS_ID, goodId + "")
                                    .with(I.Collect.USER_NAME, userName)
                                    .getRequestUrl(I.REQUEST_DELETE_COLLECT);
                            Log.e(TAG, "path=" + path);
                            action = I.ACTION_DELETE_COLLECT;
                        } else {
                            path = new ApiParams()
                                    .with(I.Collect.GOODS_ID, goodId + "")
                                    .with(I.Collect.USER_NAME, userName)
                                    .with(I.Collect.GOODS_NAME, mGoodDetails.getGoodsName())
                                    .with(I.Collect.GOODS_ENGLISH_NAME, mGoodDetails.getGoodsEnglishName())
                                    .with(I.Collect.GOODS_THUMB, mGoodDetails.getGoodsThumb())
                                    .with(I.Collect.GOODS_IMG, mGoodDetails.getGoodsImg())
                                    .with(I.Collect.ADD_TIME, mGoodDetails.getAddTime() + "")
                                    .getRequestUrl(I.REQUEST_ADD_COLLECT);
                            Log.e(TAG, "path=" + path);
                            action = I.ACTION_ADD_COLLECT;
                        }
                        executeRequest(new GsonRequest<MessageBean>(path, MessageBean.class,
                                responseSetCollectListener(action), errorListener()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }


        });
    }

    private Response.Listener<MessageBean> responseSetCollectListener(final int actionCollect) {
        return new Response.Listener<MessageBean>() {
            @Override
            public void onResponse(MessageBean messageBean) {
                if(messageBean.isSuccess()){
                    if(actionCollect == I.ACTION_ADD_COLLECT){
                        isCollect = true;
                        mivCollect.setImageResource(R.drawable.bg_collect_out);
                    }
                    if(action == I.ACTION_DELETE_COLLECT){
                        isCollect = false;
                        mivCollect.setImageResource(R.drawable.bg_collect_in);
                    }
                    new DownloadCollectCountTask(mContext).execute();
                }
                Utils.showToast(mContext,messageBean.getMsg(),Toast.LENGTH_SHORT);
            }
        };
    }

    private void initData() {
        goodId = getIntent().getIntExtra(D.NewGood.KEY_GOODS_ID, 0);
        try {
            String path = new ApiParams()
                    .with(D.NewGood.KEY_GOODS_ID, goodId + "")
                    .getRequestUrl(I.REQUEST_FIND_GOOD_DETAILS);
            Log.e(TAG, "path=" + path);
            executeRequest(new GsonRequest<GoodDetailsBean>(path, GoodDetailsBean.class,
                    responseDownloadGoodDetailsListener(), errorListener()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Response.Listener<GoodDetailsBean> responseDownloadGoodDetailsListener() {
        return new Response.Listener<GoodDetailsBean>() {
            @Override
            public void onResponse(GoodDetailsBean goodDetailsBean) {
                if (goodDetailsBean != null) {
                    mGoodDetails = goodDetailsBean;
                    //设置商品名称，价格，webView简介
                    DisplayUtils.initBackWithTitle(GoodDetailActivity.this, getResources().getString(R.string.title_good_details));
                    mtvGoodEnglishName.setText(mGoodDetails.getGoodsEnglishName());
                    mtvGoodName.setText(mGoodDetails.getGoodsName());
                    mtvCurrencyPrice.setText(mGoodDetails.getCurrencyPrice());
                    mWebView.loadDataWithBaseURL(null, mGoodDetails.getGoodsBrief().trim(), D.TEXT_HTML, D.UTF_8, null);

                    //初始化颜色面板
                    initColorsBanner();
                } else {
                    Utils.showToast(mContext, "商品详情下载失败", Toast.LENGTH_LONG);
                    finish();
                }
            }
        };
    }

    private void initColorsBanner() {
        //设置第一个颜色的图片轮播
        updateColor(0);
        //设置商品属性,商品及颜色
        for (int i = 0; i < mGoodDetails.getProperties().length; i++) {
            mCurrentColor = i;
            View layout = View.inflate(mContext, R.layout.layout_property_color, null);
            NetworkImageView ivColor = (NetworkImageView) layout.findViewById(R.id.ivColorItem);
            Log.e(TAG, "initColorsBanner.gooDetails=" + mGoodDetails.getProperties().toString());
            String colorImg = mGoodDetails.getProperties()[i].getColorImg();
            if (colorImg.isEmpty()) {
                continue;
            }
            ImageUtils.setGoodDetailsThumb(colorImg, ivColor);
            mLayoutColors.addView(layout);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateColor(mCurrentColor);
                }
            });
        }
    }

    //设置指定属性的图片轮播
    private void updateColor(int i) {
        AlbumBean[] albums = mGoodDetails.getProperties()[i].getAlbums();
        String[] albumImgUrl = new String[albums.length];
        for (int j = 0; j < albumImgUrl.length; j++) {
            albumImgUrl[j] = albums[j].getImgUrl();
        }
        mSlideAutoLoopView.startPlayLoop(mFlowIndicator, albumImgUrl, albumImgUrl.length);
    }

    private void initView() {
        mivAddCart = (ImageView) findViewById(R.id.ivAddCart);
        mivShare = (ImageView) findViewById(R.id.ivShare);
        mivCollect = (ImageView) findViewById(R.id.ivCollect);
        mtvCartCount = (TextView) findViewById(R.id.tvCartCount);
        mLayoutColors = (LinearLayout) findViewById(R.id.layoutColorSelector);
        mtvGoodEnglishName = (TextView) findViewById(R.id.tvGoodEnglishName);
        mtvGoodName = (TextView) findViewById(R.id.tvGoodName);
        mtvShopPrice = (TextView) findViewById(R.id.tvShopPrice);
        mtvCurrencyPrice = (TextView) findViewById(R.id.tvCurrencyPrice);
        mSlideAutoLoopView = (SlideAutoLoopView) findViewById(R.id.salv);
        mFlowIndicator = (FlowIndicator) findViewById(R.id.indicator);
        mWebView = (WebView) findViewById(R.id.wvGoodBrief);
        WebSettings settings = mWebView.getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setBuiltInZoomControls(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //判断是否商品已被收藏
        initCollectStatus();
        initCartCount();
    }

    private void initCartCount() {
        ArrayList<CartBean> cartList = FuLiCenterApplication.getInstance().getCartList();
        if(cartList!=null&&cartList.size()>0){
            mtvCartCount.setVisibility(View.VISIBLE);
            int count=Utils.sumCartCount();
            Log.e(TAG,"CartChangedReceiver.count="+count);
            mtvCartCount.setText(""+count);
        }
//        if (count > 0) {
//            mtvCartCount.setVisibility(View.VISIBLE);
//            mtvCartCount.setText(""+count);
//        } else {
//            mtvCartCount.setVisibility(View.GONE);
//            mtvCartCount.setText("0");
//        }
    }

    public void initCollectStatus() {
        User user = FuLiCenterApplication.getInstance().getUser();
        Log.e(TAG, "initCollectStatus,user=" + user);
        if (user != null) {
            String userName = user.getMUserName();
            try {
                String path = new ApiParams()
                        .with(I.Collect.GOODS_ID, goodId + "")
                        .with(I.Collect.USER_NAME, userName)
                        .getRequestUrl(I.REQUEST_IS_COLLECT);
                Log.e(TAG, "initCollectStatus,path=" + path);
                executeRequest(new GsonRequest<MessageBean>(path, MessageBean.class,
                        responseIsCollectListener(), errorListener()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            isCollect = false;
            mivCollect.setImageResource(R.drawable.bg_collect_in);
        }
    }

    private Response.Listener<MessageBean> responseIsCollectListener() {
        return new Response.Listener<MessageBean>() {
            @Override
            public void onResponse(MessageBean messageBean) {
                if (messageBean.isSuccess()) {
                    isCollect = true;
                    mivCollect.setImageResource(R.drawable.bg_collect_out);
                } else {
                    isCollect = false;
                    mivCollect.setImageResource(R.drawable.bg_collect_in);
                }
            }
        };
    }

    CartChangedReceiver mCartChangedReceiver;
    class CartChangedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            initCartCount();
        }
    }

    private void registerCartChangedReceiver() {
        mCartChangedReceiver=new CartChangedReceiver();
        IntentFilter filter=new IntentFilter("update_cart");
        registerReceiver(mCartChangedReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mCartChangedReceiver!=null){
            unregisterReceiver(mCartChangedReceiver);
        }
    }
}
