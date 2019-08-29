package aimi.android.com.ui.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.growalong.util.util.DateUtil;
import com.growalong.util.util.GsonUtil;
import com.lxj.xpopup.XPopup;
import butterknife.BindView;
import butterknife.OnClick;
import aimi.android.com.BaseFragment;
import aimi.android.com.MyApplication;
import aimi.android.com.R;
import aimi.android.com.modle.AliPayee;
import aimi.android.com.modle.BankPayee;
import aimi.android.com.modle.MySellOrBuyinfoItem;
import aimi.android.com.modle.WechatPayee;
import aimi.android.com.modle.YunShanFuPayee;
import aimi.android.com.ui.activity.OrderDetailsActivity;
import aimi.android.com.ui.widget.CenterErWeiMaPopupView;
import aimi.android.com.util.ToastUtil;

public class OrderDetailsFragment extends BaseFragment {
    private static final String TAG = OrderDetailsFragment.class.getSimpleName();
    @BindView(R.id.fl_title_comtent)
    FrameLayout flTitleComtent;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_order_details_code)
    TextView tvOrderDetailsCode;
    @BindView(R.id.tv_order_details_status)
    TextView tvOrderDetailsStatus;
    @BindView(R.id.tv_order_details_price)
    TextView tvOrderDetailsPrice;
    @BindView(R.id.tv_order_details_sell_name)
    TextView tvOrderDetailsSellName;
    @BindView(R.id.tv_order_details_singleprice)
    TextView tvOrderDetailsSingleprice;
    @BindView(R.id.tv_order_details_num)
    TextView tvOrderDetailsNum;
    @BindView(R.id.tv_order_details_time)
    TextView tvOrderDetailsTime;
    @BindView(R.id.tv_order_details_cankaoma)
    TextView tvOrderDetailsCankaoma;
    @BindView(R.id.tv_shoukuai_order_details_name)
    TextView tvShoukuaiOrderDetailsName;
    @BindView(R.id.tv_order_details_shoukuan_type)
    TextView tvOrderDetailsShoukuanType;
    @BindView(R.id.iv_order_details_code_image)
    ImageView ivOrderDetailsCodeImage;
    @BindView(R.id.tv_order_details_account)
    TextView tvOrderDetailsAccount;
    @BindView(R.id.tv_order_details_copy)
    TextView tvOrderDetailsCopy;
    @BindView(R.id.tv_order_details_allprice)
    TextView tvOrderDetailsAllprice;
    private OrderDetailsActivity orderDetailsActivity;
    private MySellOrBuyinfoItem orderDetailsModle;

    public static OrderDetailsFragment newInstance(@Nullable MySellOrBuyinfoItem orderDetailsModle) {
        Bundle arguments = new Bundle();
        arguments.putParcelable("orderDetailsModle", orderDetailsModle);
        OrderDetailsFragment fragment = new OrderDetailsFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderDetailsActivity = (OrderDetailsActivity) getActivity();
        orderDetailsModle = getArguments().getParcelable("orderDetailsModle");
    }

    @Override
    protected int getRootView() {
        return R.layout.order_details_fragment;
    }

    @Override
    protected void initView(View root) {
        setRootViewPaddingTop(flTitleComtent);
        tvTitle.setText("订单详情");
    }

    @Override
    public void lazyLoadData() {
        super.lazyLoadData();
        if(orderDetailsModle != null){
            tvOrderDetailsCode.setText(orderDetailsModle.getTradeid());
            int status = orderDetailsModle.getStatus();
            if(status == 1){
                tvOrderDetailsStatus.setText("等待付款");
            }else if(status == 2){
                tvOrderDetailsStatus.setText("等待放币");
            }else if(status == 10){
                tvOrderDetailsStatus.setText("完成");
            }else if(status == 20){
                tvOrderDetailsStatus.setText("申诉中");
            }else if(status == 30){
                tvOrderDetailsStatus.setText("超时取消");
            }else if(status == 40){
                tvOrderDetailsStatus.setText("已关闭");
            }
            tvOrderDetailsPrice.setText(MyApplication.appContext.getResources().getString(R.string.rmb)+orderDetailsModle.getUsdtTotalMoneyFmt());
            tvOrderDetailsSingleprice.setText(MyApplication.appContext.getResources().getString(R.string.rmb)+orderDetailsModle.getUsdtPriceFmt());
            tvOrderDetailsNum.setText(orderDetailsModle.getUsdtNumFmt());
            tvOrderDetailsTime.setText(DateUtil.getCurrentDateString3(orderDetailsModle.getCreateTime()));
            tvOrderDetailsCankaoma.setText(orderDetailsModle.getPayCode()+"");
            int payType = orderDetailsModle.getPayType();
            String payee = GsonUtil.getInstance().objTojson(orderDetailsModle.getPayee());
            if(!TextUtils.isEmpty(payee)) {
                if (payType == 1) {
                    tvOrderDetailsShoukuanType.setText("支付宝");
                    AliPayee aliPayee =  GsonUtil.getInstance().getServerBean(payee,AliPayee.class);
                    if(aliPayee != null){
                        tvOrderDetailsSellName.setText(aliPayee.getName());
                        tvShoukuaiOrderDetailsName.setText(aliPayee.getName());
                        tvOrderDetailsAccount.setText(aliPayee.getAccount());
                        ivOrderDetailsCodeImage.setVisibility(View.VISIBLE);
                    }
                } else if (payType == 2) {
                    tvOrderDetailsShoukuanType.setText("微信");
                    WechatPayee wechatPayee =  GsonUtil.getInstance().getServerBean(payee,WechatPayee.class);
                    if(wechatPayee != null){
                        tvOrderDetailsSellName.setText(wechatPayee.getName());
                        tvShoukuaiOrderDetailsName.setText(wechatPayee.getName());
                        tvOrderDetailsAccount.setText(wechatPayee.getAccount());
                        ivOrderDetailsCodeImage.setVisibility(View.VISIBLE);
                    }
                } else if (payType == 3) {
                    tvOrderDetailsShoukuanType.setText("银行账户");
                    BankPayee bankPayee =  GsonUtil.getInstance().getServerBean(payee,BankPayee.class);
                    if(bankPayee != null){
                        tvOrderDetailsSellName.setText(bankPayee.getName());
                        tvShoukuaiOrderDetailsName.setText(bankPayee.getName());
                        tvOrderDetailsAccount.setText(bankPayee.getAccount());
                        ivOrderDetailsCodeImage.setVisibility(View.GONE);
                    }
                }else if (payType == 4) {
                    tvOrderDetailsShoukuanType.setText("云闪付");
                    YunShanFuPayee yunShanFuPayee =  GsonUtil.getInstance().getServerBean(payee,YunShanFuPayee.class);
                    if(yunShanFuPayee != null){
                        tvOrderDetailsSellName.setText(yunShanFuPayee.getName());
                        tvShoukuaiOrderDetailsName.setText(yunShanFuPayee.getName());
                        tvOrderDetailsAccount.setText(yunShanFuPayee.getAccount());
                        ivOrderDetailsCodeImage.setVisibility(View.VISIBLE);
                    }
                }
            }
            tvOrderDetailsAllprice.setText(MyApplication.appContext.getResources().getString(R.string.rmb)+orderDetailsModle.getUsdtTotalMoneyFmt());
        }
    }

    @OnClick({R.id.iv_back, R.id.iv_order_details_code_image, R.id.tv_order_details_copy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                orderDetailsActivity.finish();
                break;
            case R.id.iv_order_details_code_image:
                    new XPopup.Builder(getContext())
                            .hasStatusBarShadow(true) //启用状态栏阴影
                            .asCustom(new CenterErWeiMaPopupView(getContext(),orderDetailsModle.getPayType(),GsonUtil.getInstance().objTojson(orderDetailsModle.getPayee()),orderDetailsModle.getUsdtTotalMoneyFmt()))
                            .show();
                break;
            case R.id.tv_order_details_copy:
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) orderDetailsActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", tvOrderDetailsAccount.getText().toString().trim());
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                ToastUtil.shortShow("已复制到剪贴板");
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
