package cn.ucai.fulicenter.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.HashMap;

import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.fragment.SettingsActivity;
import cn.ucai.fulicenter.task.DownloadCollectCountTask;
import cn.ucai.fulicenter.utils.UserUtils;

public class PersonalCenterFragment extends Fragment {
    public static final String TAG = PersonalCenterFragment.class.getName();
    Context mContext;

    NetworkImageView mnivUserAvatar;
    TextView mtvUserName;
    TextView mtvCollectCount;
    TextView mtvSettings;
    ImageView mivMessage;
    LinearLayout mLayoutCenterCollect;
    RelativeLayout mLyaoutCenterUserInfo;
    MyClickListener myClickListener;
    int mCollectCount = 0;
    User mUser;
    CollectCountChangedReceiver mReceiver;
    UpdateUserChangerReceiver mUpdateUserReceiver;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        View layout = inflater.inflate(R.layout.fragment_personal_center, container, false);
        initView(layout);
        initData();
        setListener();
        return layout;
    }

    private void setListener() {
        registerCollectCountReceiver();
        registerUpdateUserChangedReceiver();
        myClickListener = new MyClickListener();
        mtvSettings.setOnClickListener(myClickListener);
        mLyaoutCenterUserInfo.setOnClickListener(myClickListener);
        mLayoutCenterCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext,CollectActivity.class));
            }
        });
    }

    class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_center_settings:
                case R.id.center_user_info:
                    Log.e(TAG, "MyClickListener");
                    startActivity(new Intent(mContext, SettingsActivity.class));
                    break;
            }
        }
    }


    private void initData() {
        mUser = FuLiCenterApplication.getInstance().getUser();
        Log.e(TAG, "initData,mUser=" + mUser);
        mCollectCount = FuLiCenterApplication.getInstance().getCollectCount();
        mtvCollectCount.setText("" + mCollectCount);
        if (mUser != null) {
            UserUtils.setCurrentUserAvatar(mnivUserAvatar);
            UserUtils.setCurrentUserBeanNick(mtvUserName);
        }
    }

    private void initView(View layout) {
        mnivUserAvatar = (NetworkImageView) layout.findViewById(R.id.iv_user_avatar);
        mtvUserName = (TextView) layout.findViewById(R.id.tv_user_name);
        mLayoutCenterCollect = (LinearLayout) layout.findViewById(R.id.layout_center_collect);
        mtvCollectCount = (TextView) layout.findViewById(R.id.tv_collect_count);
        mtvSettings = (TextView) layout.findViewById(R.id.tv_center_settings);
        mivMessage = (ImageView) layout.findViewById(R.id.iv_persional_center_msg);
        mLyaoutCenterUserInfo = (RelativeLayout) layout.findViewById(R.id.center_user_info);
        initOrderList(layout);
    }

    private void initOrderList(View layout) {
        GridView mOrderList = (GridView) layout.findViewById(R.id.center_user_order_list);
        ArrayList<HashMap<String, Object>> imageList = new ArrayList<HashMap<String, Object>>();


        HashMap<String, Object> map1 = new HashMap<String, Object>();
        map1.put("image", R.drawable.order_list1);
        imageList.add(map1);
        HashMap<String, Object> map2 = new HashMap<String, Object>();
        map2.put("image", R.drawable.order_list2);
        imageList.add(map2);
        HashMap<String, Object> map3 = new HashMap<String, Object>();
        map3.put("image", R.drawable.order_list3);
        imageList.add(map3);
        HashMap<String, Object> map4 = new HashMap<String, Object>();
        map4.put("image", R.drawable.order_list4);
        imageList.add(map4);
        HashMap<String, Object> map5 = new HashMap<String, Object>();
        map5.put("image", R.drawable.order_list5);
        imageList.add(map5);

        SimpleAdapter simpleAdapter = new SimpleAdapter(mContext, imageList, R.layout.simple_grid_item, new String[]{"image"}, new int[]{R.id.image});
        mOrderList.setAdapter(simpleAdapter);
    }

    class UpdateUserChangerReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG,"UpdateUserChangerReceiver,user="+FuLiCenterApplication.getInstance().getUser());
            new DownloadCollectCountTask(mContext).execute();
            initData();
        }
    }
    private void registerUpdateUserChangedReceiver(){
        mUpdateUserReceiver = new UpdateUserChangerReceiver();
        IntentFilter filter = new IntentFilter("update_user");
        mContext.registerReceiver(mUpdateUserReceiver,filter);
    }

    class CollectCountChangedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mCollectCount = FuLiCenterApplication.getInstance().getCollectCount();
            Log.e(TAG,"CollectCountChangedReceiver,mCollectCount="+mCollectCount);
            initData();
        }
    }

    private void registerCollectCountReceiver(){
        mReceiver = new CollectCountChangedReceiver();
        IntentFilter filter = new IntentFilter("update_collect_count");
        mContext.registerReceiver(mReceiver,filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mReceiver!=null){
            mContext.unregisterReceiver(mReceiver);
        }
        if(mUpdateUserReceiver!=null){
            mContext.unregisterReceiver(mUpdateUserReceiver);
        }
    }
}
