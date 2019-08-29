package aimi.android.com.ui.activity;

import android.content.Intent;
import android.view.View;
import com.growalong.util.util.ActivityUtils;
import aimi.android.com.BaseActivity;
import aimi.android.com.R;
import aimi.android.com.presenter.InvitationPresenter;
import aimi.android.com.presenter.modle.InvitationModle;
import aimi.android.com.ui.fragment.InvitationRewardFirstFragment;

public class InvitationRewardFirstActivity extends BaseActivity {
    private static final String TAG = InvitationRewardFirstActivity.class.getSimpleName();

    public static void startThis(BaseActivity activity) {
        activity.startActivity(new Intent(activity, InvitationRewardFirstActivity.class));
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
        InvitationRewardFirstFragment invitationRewardFirstFragment = (InvitationRewardFirstFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if (invitationRewardFirstFragment == null) {
            invitationRewardFirstFragment = InvitationRewardFirstFragment.newInstance("");
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    invitationRewardFirstFragment, R.id.contentFrame);
        }
        //初始化presenter
        new InvitationPresenter(invitationRewardFirstFragment, new InvitationModle());
    }
}
