package aimi.android.com.ui.activity;

import android.content.Intent;
import android.view.View;
import com.growalong.util.util.ActivityUtils;
import aimi.android.com.BaseActivity;
import aimi.android.com.R;
import aimi.android.com.presenter.MessageCenterPresenter;
import aimi.android.com.presenter.modle.MessageCenterModle;
import aimi.android.com.ui.fragment.MessageCenterFragment;

public class MessageCenterActivity extends BaseActivity {
    private static final String TAG = MessageCenterActivity.class.getSimpleName();

    public static void startThis(BaseActivity activity) {
        activity.startActivity(new Intent(activity, MessageCenterActivity.class));
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_content;
    }

    @Override
    protected void initView(View mRootView) {
        setRootViewPaddingTop();
    }

    @Override
    protected void initData() {
        MessageCenterFragment messageCenterFragment = (MessageCenterFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if (messageCenterFragment == null) {
            messageCenterFragment = MessageCenterFragment.newInstance("");
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    messageCenterFragment, R.id.contentFrame);
        }
        //初始化presenter
        new MessageCenterPresenter(messageCenterFragment, new MessageCenterModle());
    }
}
