package hiaround.android.com.ui.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.growalong.util.util.BitmapUtils;
import com.growalong.util.util.DateUtil;
import com.growalong.util.util.GALogger;
import com.growalong.util.util.ImageUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultDisposable;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.util.ConvertUtils;
import hiaround.android.com.BaseFragment;
import hiaround.android.com.MyApplication;
import hiaround.android.com.R;
import hiaround.android.com.app.AccountManager;
import hiaround.android.com.modle.BaseBean;
import hiaround.android.com.presenter.IdentityPresenter;
import hiaround.android.com.presenter.contract.IdentityContract;
import hiaround.android.com.ui.activity.IdentityActivity;
import hiaround.android.com.util.FileUtils;
import hiaround.android.com.util.ToastUtil;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class IdentityFragment extends BaseFragment implements IdentityContract.View, CompoundButton.OnCheckedChangeListener {
    private static final String TAG = IdentityFragment.class.getSimpleName();
    @BindView(R.id.fl_title_comtent)
    FrameLayout flTitleComtent;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_identity_username)
    EditText etIdentityUsername;
    @BindView(R.id.identity_cb_man)
    CheckBox identityCbMan;
    @BindView(R.id.identity_cb_woman)
    CheckBox identityCbWoman;
    @BindView(R.id.et_identity_date)
    TextView etIdentityDate;
    @BindView(R.id.et_identity_idcard)
    EditText etIdentityIdcard;
    @BindView(R.id.iv_identity_idcard_front)
    ImageView ivIdentityIdcardFront;
    @BindView(R.id.iv_identity_idcard_fan)
    ImageView ivIdentityIdcardFan;
    @BindView(R.id.iv_identity_idcard_sc)
    ImageView ivIdentityIdcardSc;
    @BindView(R.id.cb_identity_agree)
    CheckBox cbIdentityAgree;
    @BindView(R.id.tv_identity_submit)
    TextView tvIdentitySubmit;
    private IdentityActivity identityActivity;
    private IdentityPresenter presenter;
    private String sIdcardFront;
    private String sIdcardFan;
    private String sIdcardSc;
    private int sex = 0;//性别 0为男 1为女

    public static IdentityFragment newInstance(@Nullable String taskId) {
        Bundle arguments = new Bundle();
        IdentityFragment fragment = new IdentityFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        identityActivity = (IdentityActivity) getActivity();
    }

    @Override
    protected int getRootView() {
        return R.layout.identity_fragment;
    }

    @Override
    protected void initView(View root) {
        setRootViewPaddingTop(flTitleComtent);
        tvTitle.setText("身份认证");
        identityCbMan.setOnCheckedChangeListener(this);
        identityCbWoman.setOnCheckedChangeListener(this);
    }

    @Override
    public void lazyLoadData() {
        super.lazyLoadData();
    }

    @OnClick({R.id.iv_back, R.id.iv_identity_idcard_front, R.id.iv_identity_idcard_fan, R.id.iv_identity_idcard_sc, R.id.tv_identity_submit,R.id.et_identity_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                identityActivity.finish();
                break;
            case R.id.iv_identity_idcard_front:
                //自定义方法的单选
                RxGalleryFinal
                        .with(identityActivity)
                        .image()//图片
                        .radio()//单选
                        .imageLoader(ImageLoaderType.GLIDE)
                        .subscribe(new RxBusResultDisposable<ImageRadioResultEvent>() {
                            @Override
                            protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
                                //图片选择结果
                                String path = imageRadioResultEvent.getResult().getOriginalPath();
                                GALogger.d(TAG, "path === " + path);
                                if (!TextUtils.isEmpty(path)) {
                                    initLuBan(path,1);
                                }
                            }
                        })
                        .openGallery();
                break;
            case R.id.iv_identity_idcard_fan:
                //自定义方法的单选
                RxGalleryFinal
                        .with(identityActivity)
                        .image()//图片
                        .radio()//单选
                        .imageLoader(ImageLoaderType.GLIDE)
                        .subscribe(new RxBusResultDisposable<ImageRadioResultEvent>() {
                            @Override
                            protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
                                //图片选择结果
                                String path = imageRadioResultEvent.getResult().getOriginalPath();
                                GALogger.d(TAG, "path === " + path);
                                if (!TextUtils.isEmpty(path)) {
                                    initLuBan(path,2);
                                }
                            }
                        })
                        .openGallery();
                break;
            case R.id.iv_identity_idcard_sc:
                //自定义方法的单选
                RxGalleryFinal
                        .with(identityActivity)
                        .image()//图片
                        .radio()//单选
                        .imageLoader(ImageLoaderType.GLIDE)
                        .subscribe(new RxBusResultDisposable<ImageRadioResultEvent>() {
                            @Override
                            protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
                                //图片选择结果
                                String path = imageRadioResultEvent.getResult().getOriginalPath();
                                GALogger.d(TAG, "path === " + path);
                                if (!TextUtils.isEmpty(path)) {
                                    initLuBan(path,3);
                                }
                            }
                        })
                        .openGallery();
                break;
            case R.id.tv_identity_submit:
                String username = etIdentityUsername.getText().toString().trim();
                if(TextUtils.isEmpty(username)){
                    ToastUtil.shortShow("请输入真实姓名");
                    return;
                }
                String yearDate = etIdentityDate.getText().toString().trim();
                if(TextUtils.isEmpty(yearDate)){
                    ToastUtil.shortShow("请输入出生年月");
                    return;
                }

                String idcard = etIdentityIdcard.getText().toString().trim();
                if(TextUtils.isEmpty(idcard)){
                    ToastUtil.shortShow("请输入证件号码");
                    return;
                }
                if(TextUtils.isEmpty(sIdcardFront)){
                    ToastUtil.shortShow("请上传身份证正面");
                    return;
                }
                if(TextUtils.isEmpty(sIdcardFan)){
                    ToastUtil.shortShow("请上传身份证反面");
                    return;
                }
                if(TextUtils.isEmpty(sIdcardSc)){
                    ToastUtil.shortShow("请上传手持证件照");
                    return;
                }
                if(!cbIdentityAgree.isChecked()){
                    ToastUtil.shortShow("阅读并同意本人承诺所有提交审核的材料");
                    return;
                }
                presenter.idCheck(1,username,sex,yearDate,idcard,sIdcardFront,sIdcardFan,sIdcardSc);
                break;
            case R.id.et_identity_date:
                onYearMonthDayPicker(etIdentityDate);
                break;
        }
    }

    private void initLuBan(String path, final int type) {
        Luban.with(MyApplication.appContext)
                .load(path)
                .ignoreBy(1 * 1024)//1.0M 以下不压缩
                .setTargetDir(FileUtils.getTempPath(MyApplication.appContext))
                .filter(new CompressionPredicate() {
                    @Override
                    public boolean apply(String path) {
                        return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                    }
                })
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                        showLoadingDialog();
                    }

                    @Override
                    public void onSuccess(File file) {
                        // TODO 压缩成功后调用，返回压缩后的图片文件
                        hideLoadingDialog();
                        try {
                            if(type == 1){
                                Bitmap bitmap = ImageUtil.fileToBitmap(file);
                                ivIdentityIdcardFront.setImageBitmap(bitmap);
                                ivIdentityIdcardFront.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                sIdcardFront = BitmapUtils.bitmapToBase64(bitmap);
                            }else if(type == 2){
                                Bitmap bitmap = ImageUtil.fileToBitmap(file);
                                ivIdentityIdcardFan.setImageBitmap(bitmap);
                                ivIdentityIdcardFan.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                sIdcardFan = BitmapUtils.bitmapToBase64(bitmap);
                            }else if(type == 3){
                                Bitmap bitmap = ImageUtil.fileToBitmap(file);
                                ivIdentityIdcardSc.setImageBitmap(bitmap);
                                ivIdentityIdcardSc.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                sIdcardSc = BitmapUtils.bitmapToBase64(bitmap);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                    }
                }).launch();
    }

    @Override
    public void idCheckSuccess(BaseBean baseBean) {
        AccountManager.getInstance().setIDstatus(1);
        identityActivity.setResult(Activity.RESULT_OK);
        identityActivity.finish();
    }

    @Override
    public void setPresenter(IdentityContract.Presenter presenter) {
            this.presenter = (IdentityPresenter) presenter;
    }

    @Override
    public void showLoading() {
            showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        hideLoadingDialog();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.identity_cb_man:
                if(isChecked){
                    identityCbWoman.setChecked(false);
                    sex = 0;
                }else {
                    identityCbWoman.setChecked(true);
                    sex = 1;
                }
                break;
            case R.id.identity_cb_woman:
                if(isChecked){
                    identityCbMan.setChecked(false);
                    sex = 1;
                }else {
                    identityCbMan.setChecked(true);
                    sex = 0;
                }
                break;
        }
    }

    public void onYearMonthDayPicker(final TextView view) {
        final DatePicker picker = new DatePicker(identityActivity);
        picker.setCanceledOnTouchOutside(true);
        picker.setUseWeight(true);
        picker.setTopPadding(ConvertUtils.toPx(MyApplication.appContext, 10));
        picker.setRangeEnd(DateUtil.getCurrentYear(), DateUtil.getCurrentMonth(), DateUtil.getCurrentDay());
        picker.setRangeStart(1960, 1, 1);
        picker.setSelectedItem(DateUtil.getCurrentYear(), DateUtil.getCurrentMonth(), DateUtil.getCurrentDay());
        picker.setResetWhileWheel(false);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                view.setText(year + "-" + month + "-" + day);
            }
        });
        picker.show();
    }
}
