package aimi.android.com.net.retrofit;

import android.content.Intent;
import android.os.Looper;
import com.growalong.util.util.GALogger;
import com.growalong.util.util.Md5Utils;

import aimi.android.com.MyApplication;
import aimi.android.com.app.AccountInfo;
import aimi.android.com.app.AccountManager;
import aimi.android.com.app.AppManager;
import aimi.android.com.modle.BaseBean;
import aimi.android.com.net.retrofit.exception.ModelException;
import aimi.android.com.net.retrofit.exception.ModelExceptionBuilder;
import aimi.android.com.net.retrofit.exception.ModelExceptionMap;
import aimi.android.com.net.retrofit.exception.ServerExceptionMap;
import aimi.android.com.net.retrofit.service.ApiServices;
import aimi.android.com.ui.activity.LoginActivity;
import aimi.android.com.util.ToastUtil;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 封装 Observer 统一返回方法
 *
 * @param <T>
 */
public abstract class ModelResultObserver<T> implements Observer<T> {
    private static final String TAG = ModelResultObserver.class.getSimpleName();
    protected Disposable mDisposable = null;

    protected ModelResultObserver() {
    }


    @Override
    public void onSubscribe(Disposable d) {

        mDisposable = d;
    }

    @Override
    public void onNext(T t) {
        GALogger.d(TAG,t.toString());
        onSuccess(t);
        if (t instanceof BaseBean) {
            BaseBean baseBean = (BaseBean) t;
        }
        //解除注册
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        //handle fail
        if (e instanceof ModelException) {
            onFailure((ModelException) e);
        } else {
            ModelException me = ModelExceptionBuilder.build(e);
            onFailure(me);
        }
        //解除注册
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    /**
     * 成功时候的处理
     *
     * @param result
     * @return 返回 false 会继续处理；返回 true 不会继续处理
     */
    public abstract void onSuccess(T result);

    /**
     * 失败时候的处理
     */
    public void onFailure(ModelException ex) {
        GALogger.d(TAG, "onFailure() into, " + ex.toString() + "   mMessage   " + ex.mMessage + "   mCode   " + ex.mCode);
        if (ex.mCode == 1 && Looper.myLooper() == Looper.getMainLooper()) {//"账号在别处已经登录"
            /**
             * 掉登录接口
             */
            long currentTime = System.currentTimeMillis();
            String phoneNumber = AccountManager.getInstance().getPhoneNumber();
            String passWord = AccountManager.getInstance().getPassWord();
            BaseRetrofitClient.getInstance().create(ApiServices.class)
                    .login("86"+phoneNumber, Md5Utils.getMD5(passWord+currentTime),currentTime)
                    .subscribeOn(Schedulers.io())
                    .map(new ServerExceptionMap<AccountInfo>())
                    .onErrorResumeNext(new ModelExceptionMap<AccountInfo>()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ModelResultObserver<AccountInfo>() {
                        @Override
                        public void onSuccess(AccountInfo accountInfo) {
                            accountInfo.setPhoneNumber(phoneNumber);
                            accountInfo.setPassword(passWord);
                            AccountManager.getInstance().saveAccountInfoFormModel(accountInfo);
                            ToastUtil.longShow("请重复上次操作");
                        }

                        @Override
                        public void onFailure(ModelException ex) {
                            super.onFailure(ex);
                            Intent intent = new Intent("android.intent.action.LoginActivity");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            MyApplication.appContext.startActivity(intent);
                            AppManager.getInstance().finishAllActivity(LoginActivity.class);
                        }
                    });
        }else {
            ToastUtil.longShow(ex.mMessage);
        }
    }
}
