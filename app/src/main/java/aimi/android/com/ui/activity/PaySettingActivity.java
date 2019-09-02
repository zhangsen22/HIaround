package aimi.android.com.ui.activity;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import com.growalong.util.util.ActivityUtils;

import aimi.android.com.BaseActivity;
import aimi.android.com.R;
import aimi.android.com.app.Constants;
import aimi.android.com.modle.LaCaraPayeeItemModelPayee;
import aimi.android.com.modle.WeChatPayeeItemModelPayee;
import aimi.android.com.modle.YunShanFuPayeeItemModelPayee;
import aimi.android.com.presenter.AliPayEditPresenter;
import aimi.android.com.presenter.IdCastPresenter;
import aimi.android.com.presenter.LaCaraEditPresenter;
import aimi.android.com.presenter.WebChatEditPresenter;
import aimi.android.com.presenter.YunShanFuEditPresenter;
import aimi.android.com.presenter.modle.PaySettingModle;
import aimi.android.com.ui.fragment.AliPayEditFragment;
import aimi.android.com.ui.fragment.IdCastPayEditFragment;
import aimi.android.com.ui.fragment.LaCaraEditFragment;
import aimi.android.com.ui.fragment.WebChatEditFragment;
import aimi.android.com.ui.fragment.YunShanFunEditFragment;

public class PaySettingActivity extends BaseActivity {
    private static final String TAG = PaySettingActivity.class.getSimpleName();
    private WebChatEditFragment webChatEditFragment;
    private AliPayEditFragment aliPayEditFragment;
    private YunShanFunEditFragment yunShanFunEditFragment;
    private LaCaraEditFragment laCaraEditFragment;
    private int type;

    public static void startThis(BaseActivity activity,int type,int requestCode) {
        Intent intent = new Intent(activity, PaySettingActivity.class);
        intent.putExtra("type",type);
        activity.startActivityForResult(intent,requestCode);
    }

    public static void startThis(BaseActivity activity, int type, WeChatPayeeItemModelPayee weChatPayeeItemModelPayee, int requestCode) {
        Intent intent = new Intent(activity, PaySettingActivity.class);
        intent.putExtra("type",type);
        intent.putExtra("weChatPayeeItemModelPayee",weChatPayeeItemModelPayee);
        activity.startActivityForResult(intent,requestCode);
    }

    public static void startThisYunShanFu(BaseActivity activity, int type, YunShanFuPayeeItemModelPayee yunShanFuPayeeItemModelPayee, int requestCode) {
        Intent intent = new Intent(activity, PaySettingActivity.class);
        intent.putExtra("type",type);
        intent.putExtra("yunShanFuPayeeItemModelPayee",yunShanFuPayeeItemModelPayee);
        activity.startActivityForResult(intent,requestCode);
    }

    public static void startThisLaCara(BaseActivity activity, int type, LaCaraPayeeItemModelPayee laCaraPayeeItemModelPayee, int requestCode) {
        Intent intent = new Intent(activity, PaySettingActivity.class);
        intent.putExtra("type",type);
        intent.putExtra("laCaraPayeeItemModelPayee",laCaraPayeeItemModelPayee);
        activity.startActivityForResult(intent,requestCode);
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
        type = getIntent().getIntExtra("type", 1);
        if(type == 1){//1为支付宝，2为微信，3为银行账户
            aliPayEditFragment = (AliPayEditFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.contentFrame);
            if (aliPayEditFragment == null) {
                aliPayEditFragment = AliPayEditFragment.newInstance("");
                ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                        aliPayEditFragment, R.id.contentFrame);
            }
            //初始化presenter
            new AliPayEditPresenter(aliPayEditFragment, new PaySettingModle());
        }else if(type == 2){
            WeChatPayeeItemModelPayee weChatPayeeItemModelPayee = getIntent().getParcelableExtra("weChatPayeeItemModelPayee");
            webChatEditFragment = (WebChatEditFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.contentFrame);
            if (webChatEditFragment == null) {
                webChatEditFragment = WebChatEditFragment.newInstance(weChatPayeeItemModelPayee);
                ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                        webChatEditFragment, R.id.contentFrame);
            }
            //初始化presenter
            new WebChatEditPresenter(webChatEditFragment, new PaySettingModle());
        }else if(type == 3){
            IdCastPayEditFragment idCastPayEditFragment = (IdCastPayEditFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.contentFrame);
            if (idCastPayEditFragment == null) {
                idCastPayEditFragment = IdCastPayEditFragment.newInstance("");
                ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                        idCastPayEditFragment, R.id.contentFrame);
            }
            //初始化presenter
            new IdCastPresenter(idCastPayEditFragment, new PaySettingModle());
        }else if(type == 4){
            YunShanFuPayeeItemModelPayee yunShanFuPayeeItemModelPayee = getIntent().getParcelableExtra("yunShanFuPayeeItemModelPayee");
            yunShanFunEditFragment = (YunShanFunEditFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.contentFrame);
            if (yunShanFunEditFragment == null) {
                yunShanFunEditFragment = YunShanFunEditFragment.newInstance(yunShanFuPayeeItemModelPayee);
                ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                        yunShanFunEditFragment, R.id.contentFrame);
            }
            new YunShanFuEditPresenter(yunShanFunEditFragment, new PaySettingModle());
        }else if(type == 5){
            LaCaraPayeeItemModelPayee laCaraPayeeItemModelPayee = getIntent().getParcelableExtra("laCaraPayeeItemModelPayee");
            laCaraEditFragment = (LaCaraEditFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.contentFrame);
            if (laCaraEditFragment == null) {
                laCaraEditFragment = LaCaraEditFragment.newInstance(laCaraPayeeItemModelPayee);
                ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                        laCaraEditFragment, R.id.contentFrame);
            }
            new LaCaraEditPresenter(laCaraEditFragment, new PaySettingModle());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 100:
                    if (data == null) return;
                    webChatEditFragment.onActivityResultF(requestCode,resultCode,data);
                    break;
                case 101:
                    if (data == null) return;
                    aliPayEditFragment.onActivityResultF(requestCode,resultCode,data);
                    break;
                case 102:
                    if (data == null) return;
                    yunShanFunEditFragment.onActivityResultF(requestCode,resultCode,data);
                    break;
                case 103:
                    if (data == null) return;
                    laCaraEditFragment.onActivityResultF(requestCode,resultCode,data);
                    break;
                case Constants.REQUESTCODE_20:
                    if (data == null) return;
                    yunShanFunEditFragment.onActivityBack(requestCode,resultCode,data);
                    break;
                default:
                    break;

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if(type == 4){
                if(yunShanFunEditFragment != null){
                    yunShanFunEditFragment.onkeyDown();
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
