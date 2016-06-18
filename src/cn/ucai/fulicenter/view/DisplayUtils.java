package cn.ucai.fulicenter.view;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.ucai.fulicenter.R;

/**
 * Created by leon on 2016/6/16.
 */
public class DisplayUtils {
    public static void initBack(final Activity activity) {
        LinearLayout clickBack = (LinearLayout) activity.findViewById(R.id.layout_back_Title);
        clickBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
    }

    public static void initBackWithTitle(Activity activity, String title) {
        TextView tvTitle = (TextView) activity.findViewById(R.id.tv_head_title);
        tvTitle.setText(title);
        initBack(activity);
    }
}
