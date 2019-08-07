package hiaround.android.com.ui.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import com.growalong.util.util.ActivityUtils;
import hiaround.android.com.BaseActivity;
import hiaround.android.com.R;
import hiaround.android.com.app.Constants;
import hiaround.android.com.presenter.WebChatListPresenter;
import hiaround.android.com.presenter.modle.WebChatListModle;
import hiaround.android.com.ui.fragment.WebChatListFragment;

public class WebChatListActivity extends BaseActivity {
    private static final String TAG = WebChatListActivity.class.getSimpleName();
    private WebChatListFragment webChatListFragment;

    public static void startThis(BaseActivity activity) {
        activity.startActivity(new Intent(activity, WebChatListActivity.class));
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
        webChatListFragment = (WebChatListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if (webChatListFragment == null) {
            webChatListFragment = WebChatListFragment.newInstance("");
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    webChatListFragment, R.id.contentFrame);
        }
        //初始化presenter
        new WebChatListPresenter(webChatListFragment, new WebChatListModle());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == Constants.REQUESTCODE_17){
                webChatListFragment.onActivityResultF();
            }
        }
    }
}
