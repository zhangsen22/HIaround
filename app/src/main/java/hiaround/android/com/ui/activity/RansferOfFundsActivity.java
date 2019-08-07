package hiaround.android.com.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.growalong.util.util.ActivityUtils;
import hiaround.android.com.BaseActivity;
import hiaround.android.com.R;
import hiaround.android.com.presenter.RansferOfFundsPresenter;
import hiaround.android.com.presenter.modle.RansferOfFundsModle;
import hiaround.android.com.ui.fragment.RansferOfFundsFragment;

public class RansferOfFundsActivity extends BaseActivity {

    public static void startThis(BaseActivity context, int formType,int requestCode) {
        Intent intent = new Intent(context, RansferOfFundsActivity.class);
        intent.putExtra("formType",formType);
        context.startActivityForResult(intent,requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        int formType = getIntent().getIntExtra("formType", 1);
        RansferOfFundsFragment ransferOfFundsFragment = (RansferOfFundsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if (ransferOfFundsFragment == null) {
            ransferOfFundsFragment = RansferOfFundsFragment.newInstance(formType);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    ransferOfFundsFragment, R.id.contentFrame);
        }
        //初始化presenter
        new RansferOfFundsPresenter(ransferOfFundsFragment, new RansferOfFundsModle());
    }
}
