package cn.ucai.fulicenter.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.fragment.NewGoodFragment;
import cn.ucai.fulicenter.utils.Utils;

public class FuliCenterMainActivity extends BaseActivity {
    public static final String TAG = FuliCenterMainActivity.class.getName();

    Fragment[] mFragments = new Fragment[5];
    NewGoodFragment mNewGoodFragment;
    BoutiqueFragment mBoutiqueFragment;
    CategoryFragment mCategoryFragment;
    CartFragment mCartFragment;
    PersonalCenterFragment mPersonalCenterFragment;

    TextView mtvCartHint;
    RadioButton mRadioNewGood;
    RadioButton mRadioBoutique;
    RadioButton mRadioCategory;
    RadioButton mRadioCart;
    RadioButton mRadioPersonalCenter;
    RadioButton[] mRadios = new RadioButton[5];
    private int index;

    //当前fragment的index
    private int currentTabIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuli_center_main);
        initView();
        initFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mNewGoodFragment)
                .add(R.id.fragment_container, mBoutiqueFragment)
                .add(R.id.fragment_container, mCategoryFragment)
                .add(R.id.fragment_container, mCartFragment)
                .add(R.id.fragment_container, mPersonalCenterFragment)
                .hide(mCartFragment)
                .hide(mPersonalCenterFragment)
                .hide(mBoutiqueFragment)
                .hide(mCategoryFragment)
//                .add(R.id.fragment_container, contactListFragment)
//                .hide(contactListFragment)
                .show(mNewGoodFragment)
                .commit();
        registerCartChangedReceiver();
    }

    private void initFragment() {
        mNewGoodFragment = new NewGoodFragment();
        mBoutiqueFragment = new BoutiqueFragment();
        mCategoryFragment = new CategoryFragment();
        mCartFragment = new CartFragment();
        mPersonalCenterFragment = new PersonalCenterFragment();
        mFragments[0] = mNewGoodFragment;
        mFragments[1] = mBoutiqueFragment;
        mFragments[2] = mCategoryFragment;
        mFragments[3] = mCartFragment;
        mFragments[4] = mPersonalCenterFragment;

    }

    private void initView() {
        mtvCartHint = (TextView) findViewById(R.id.tv_CartHint);

        mRadioNewGood = (RadioButton) findViewById(R.id.layout_new_good);
        mRadioBoutique = (RadioButton) findViewById(R.id.layout_boutique);
        mRadioCategory = (RadioButton) findViewById(R.id.layout_category);
        mRadioCart = (RadioButton) findViewById(R.id.layout_cart);
        mRadioPersonalCenter = (RadioButton) findViewById(R.id.layout_personal_center);

        mRadios[0] = mRadioNewGood;
        mRadios[1] = mRadioBoutique;
        mRadios[2] = mRadioCategory;
        mRadios[3] = mRadioCart;
        mRadios[4] = mRadioPersonalCenter;
    }

    public void onCheckedChange(View view) {
        switch (view.getId()) {
            case R.id.layout_new_good:
                index = 0;
                break;
            case R.id.layout_boutique:
                index = 1;
                break;
            case R.id.layout_category:
                index = 2;
                break;
            case R.id.layout_cart:
                if(FuLiCenterApplication.getInstance().getUser() != null) {
                    index = 3;
                }else{
                    gotoLogin("cart");
                }
                break;
            case R.id.layout_personal_center:
                if (FuLiCenterApplication.getInstance().getUser() != null) {
                    index = 4;
                } else {
                    gotoLogin("personal");
                }
                break;
        }
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(mFragments[currentTabIndex]);
            if (!mFragments[index].isAdded()) {
                trx.add(R.id.fragment_container, mFragments[index]);
            }
            trx.show(mFragments[index]).commit();
            setRadioChecked(index);
            currentTabIndex = index;
        }
    }

    private void gotoLogin(String action) {
        Intent intent = new Intent(FuliCenterMainActivity.this,LoginActivity.class).putExtra("action",action);
        startActivity(intent);
    }

    private void setRadioChecked(int index) {
        for (int i = 0; i < mRadios.length; i++) {
            if (i == index) {
                mRadios[i].setChecked(true);
            } else {
                mRadios[i].setChecked(false);
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        String action = getIntent().getStringExtra("action");
        Log.e(TAG, "onNewIntent,action=" +action);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "currentTabIndex="+currentTabIndex+",Index="+index);
        Log.e(TAG, "user=" + FuLiCenterApplication.getInstance().getUser());
        String action = getIntent().getStringExtra("action");
        Log.e(TAG, "action=" +action);
        if (action != null && FuLiCenterApplication.getInstance().getUser() != null) {
            if (action.equals("personal")) {
                index = 4;
            }
            if(action.equals("cart")){
                index = 3;
            }
        } else {
            setRadioChecked(index);
        }
        if (currentTabIndex == 4 && FuLiCenterApplication.getInstance().getUser() == null) {
            index =0;
            Log.e(TAG, "退出index="+index);
        }
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(mFragments[currentTabIndex]);
            if (!mFragments[index].isAdded()) {
                trx.add(R.id.fragment_container, mFragments[index]);
            }
            trx.show(mFragments[index]).commit();
            setRadioChecked(index);
            currentTabIndex = index;
        }
    }


    CartChangedReceiver mCartChangedReceiver;

    class CartChangedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int count= Utils.sumCartCount();
            Log.e(TAG,"CartChangedReceiver,count = "+count);
            if(count>0){
                //显示购物车中的商品件数
                mtvCartHint.setText(""+count);
                mtvCartHint.setVisibility(View.VISIBLE);
            } else {
                mtvCartHint.setVisibility(View.GONE);
            }
        }
    }
    private void registerCartChangedReceiver() {
        mCartChangedReceiver=new CartChangedReceiver();
        IntentFilter filter=new IntentFilter("update_cart");
        filter.addAction("update_user");
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
