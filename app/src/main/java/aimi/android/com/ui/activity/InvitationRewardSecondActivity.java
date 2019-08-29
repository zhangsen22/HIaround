package aimi.android.com.ui.activity;

import android.content.Intent;
import android.view.View;
import com.growalong.util.util.ActivityUtils;
import aimi.android.com.BaseActivity;
import aimi.android.com.R;
import aimi.android.com.presenter.InvitationPresenter;
import aimi.android.com.presenter.modle.InvitationModle;
import aimi.android.com.ui.fragment.InvitationRewardSecondFragment;

public class InvitationRewardSecondActivity extends BaseActivity {
    private static final String TAG = InvitationRewardSecondActivity.class.getSimpleName();

    public static void startThis(BaseActivity activity, long userId) {
        Intent intent = new Intent(activity, InvitationRewardSecondActivity.class);
        intent.putExtra("userId",userId);
        activity.startActivity(intent);
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_content;
    }

    @Override
    protected void initView(View mRootView) {

    }

    @Override
    protected void initData() {
        long userId = getIntent().getLongExtra("userId", 0);
        InvitationRewardSecondFragment invitationRewardSecondFragment = (InvitationRewardSecondFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if (invitationRewardSecondFragment == null) {
            invitationRewardSecondFragment = InvitationRewardSecondFragment.newInstance(userId);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    invitationRewardSecondFragment, R.id.contentFrame);
        }
        //初始化presenter
        new InvitationPresenter(invitationRewardSecondFragment, new InvitationModle());
    }
}
