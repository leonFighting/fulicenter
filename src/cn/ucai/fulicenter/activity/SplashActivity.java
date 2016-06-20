package cn.ucai.fulicenter.activity;

import android.content.Intent;
import android.os.Bundle;

import cn.ucai.fulicenter.DemoHXSDKHelper;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.db.UserDao;

/**
 * 开屏页
 *
 */
public class SplashActivity extends BaseActivity {
	private SplashActivity mContext;

	private static final int sleepTime = 2000;

	@Override
	protected void onCreate(Bundle arg0) {
		setContentView(R.layout.activity_splash);
		super.onCreate(arg0);
		mContext = this;
	}

	@Override
	protected void onStart() {
		super.onStart();
		final String userName = FuLiCenterApplication.getInstance().getUserName();
		if(DemoHXSDKHelper.getInstance().isLogined()){

		}
		new Thread(new Runnable() {
			public void run() {
				if (DemoHXSDKHelper.getInstance().isLogined()) {

					UserDao dao = new UserDao(mContext);
					User user = dao.findUserByUserName(userName);
					FuLiCenterApplication.getInstance().setUser(user);
				}else {
					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
					}
				}
				startActivity(new Intent(SplashActivity.this, FuliCenterMainActivity.class));
				finish();
			}
		}).start();

	}
}
