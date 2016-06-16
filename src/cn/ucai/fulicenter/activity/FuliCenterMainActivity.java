package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.fragment.NewGoodFragment;

public class FuliCenterMainActivity extends BaseActivity {
    Fragment[] mFragments = new Fragment[2];
    NewGoodFragment mNewGoodFragment;
    BoutiqueFragment mBoutiqueFragment;

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
                .add(R.id.fragment_container,mBoutiqueFragment)
                .hide(mBoutiqueFragment)
//                .add(R.id.fragment_container, contactListFragment)
//                .hide(contactListFragment)
                .show(mNewGoodFragment)
                .commit();
    }

    private void initFragment() {
        mNewGoodFragment = new NewGoodFragment();
        mBoutiqueFragment = new BoutiqueFragment();
        mFragments[0] = mNewGoodFragment;
        mFragments[1] = mBoutiqueFragment;

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
                index = 3;
                break;
            case R.id.layout_personal_center:
                index = 4;
                break;
        }
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(mFragments[currentTabIndex]);
            if (!mFragments[index].isAdded()) {
                trx.add(R.id.fragment_container, mFragments[index]);
            }
            trx.show(mFragments[index]).commit();
        }
        setRadioChecked(index);
        currentTabIndex = index;
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
}
