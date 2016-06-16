package cn.ucai.fulicenter.activity;

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

import cn.ucai.fulicenter.D;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.AlbumBean;
import cn.ucai.fulicenter.bean.GoodDetailsBean;
import cn.ucai.fulicenter.data.ApiParams;
import cn.ucai.fulicenter.data.GsonRequest;
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
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_good_details);
        mContext = this;
        initView();
        initData();
    }

    private void initData() {
        int goodId = getIntent().getIntExtra(D.NewGood.KEY_GOODS_ID, 0);
        try {
            String path = new ApiParams()
                    .with(D.NewGood.KEY_GOODS_ID, goodId + "")
                    .getRequestUrl(I.REQUEST_FIND_GOOD_DETAILS);
            Log.e(TAG,"path="+path);
            executeRequest(new GsonRequest<GoodDetailsBean>(path,GoodDetailsBean.class,
                    responseDownloadGoodDetailsListener(),errorListener()));
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
            ImageUtils.setGoodDetailsThumb(colorImg,ivColor);
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
        for (int j=0;j<albumImgUrl.length;j++) {
            albumImgUrl[j] = albums[j].getImgUrl();
        }
        mSlideAutoLoopView.startPlayLoop(mFlowIndicator,albumImgUrl,albumImgUrl.length);
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
}
