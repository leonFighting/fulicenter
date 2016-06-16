package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;

import cn.ucai.fulicenter.D;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.GoodDetailsBean;
import cn.ucai.fulicenter.data.ApiParams;
import cn.ucai.fulicenter.data.GsonRequest;
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

                }
            }
        };
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