package hiaround.android.com.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import com.growalong.util.util.GALogger;
import com.growalong.util.util.StringUtils;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import hiaround.android.com.BaseFragment;
import hiaround.android.com.MyApplication;
import hiaround.android.com.R;
import hiaround.android.com.app.AccountInfo;
import hiaround.android.com.app.AccountManager;
import hiaround.android.com.app.Constants;
import hiaround.android.com.modle.DomainModel;
import hiaround.android.com.presenter.LoginPresenter;
import hiaround.android.com.presenter.contract.LoginContract;
import hiaround.android.com.ui.activity.LoginActivity;
import hiaround.android.com.ui.activity.MainActivity;
import hiaround.android.com.ui.activity.SplashActivity;
import hiaround.android.com.util.SharedPreferencesUtils;

public class SplashFragment extends BaseFragment implements LoginContract.View {
    private static final String TAG = SplashFragment.class.getSimpleName();
    private SplashActivity splashActivity;
    private LoginPresenter splashPresenter;
    private ExecutorService pool;
    private List<String> listDomain;
    private List<String> usableDomains;

    public static SplashFragment newInstance(@Nullable String taskId) {
        Bundle arguments = new Bundle();
        SplashFragment fragment = new SplashFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashActivity = (SplashActivity) getActivity();
    }

    @Override
    protected int getRootView() {
        return R.layout.splash_fragment;
    }

    @Override
    protected void initView(View root) {
    }

    @Override
    public void lazyLoadData() {
        super.lazyLoadData();
        splashPresenter.getDomainName();
    }

    @Override
    public void getDomainNameSuccess(DomainModel domainModel) {
        if(domainModel != null){
            GALogger.d(TAG,"domainModel     "+domainModel.toString());
            String version = domainModel.getVersion();
            boolean wxPayLock = domainModel.isWxPayLock();
            if(!TextUtils.isEmpty(version)){
                SharedPreferencesUtils.putString(Constants.VERSION, version.replaceAll("(?i)v",""));
            }
            SharedPreferencesUtils.putBoolean(Constants.WXPAYLOCK, wxPayLock);
            List<String> downLoad = domainModel.getDownLoad();
            if(downLoad  != null && downLoad.size() > 0){
                String s = downLoad.get(0);
                if(!TextUtils.isEmpty(s)){
                    MyApplication.setH5_down_Address(s);
                }
            }
            List<String> gateway = domainModel.getGateway();
            if(gateway != null && gateway.size() > 0){
                //        for (int i = 0; i < hostList.size(); i++) {
//            int finalI = i;
//            Thread thread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        synchronized (SplashFragment.this) {
//                            if (isStratThread) {
//                                InetAddress byName = null;
//                                int resCode = -1;
//                                try {
//                                    byName = InetAddress.getByName(hostList.get(finalI));
//                                    resCode = byName == null ? -1 : 0;
//                                } catch (UnknownHostException e) {
//                                    resCode = -1;
//                                    e.printStackTrace();
//                                }catch (Exception e) {
//                                    resCode = -1;
//                                    e.printStackTrace();
//                                } finally {
//                                    if (resCode == 0) {
//                                        isStratThread = false;
//                                        goApp();
//                                    }
//                                    GALogger.d(TAG, "resCode   " + resCode + "    i     " + finalI + "   isStratThread   " + isStratThread+"   host   "+hostList.get(finalI));
//                                }
//                            }
//                        }
//                    }
//                });
//            thread.setName(i+"");
//            thread.start();
//        }
                if(usableDomains == null){
                    usableDomains = new ArrayList<>();
                }else {
                    usableDomains.clear();
                }

                if(listDomain == null){
                    listDomain = new ArrayList<>();
                }else {
                    listDomain.clear();
                }

                for (int i = 0; i < gateway.size(); i++) {
                    String s = StringUtils.submitDomain(gateway.get(i));
                    if (s != null)
                        System.err.println(s);
                    listDomain.add(s);
                }

                if(pool == null) {
                    pool = Executors.newFixedThreadPool(gateway.size());
                }

                for (int i = 0; i < gateway.size(); i++) {
                    pool.execute(new MyThread(i,gateway,listDomain));
                }
                stopThread();
                if(usableDomains != null && usableDomains.size() > 0){
                    MyApplication.setHostAddress(usableDomains.get(0));
                    goApp();
                }
            }
        }
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        splashPresenter = (LoginPresenter) presenter;
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
    public void onDestroy() {
        stopThread();
        super.onDestroy();
    }

    private void stopThread() {
        if (pool != null && !pool.isShutdown()) {
            try {
                pool.shutdown();
                // (所有的任务都结束的时候，返回TRUE)
                if (!pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)) {
                    // 超时的时候向线程池中所有的线程发出中断(interrupted)。
                    pool.shutdownNow();
                }
            } catch (InterruptedException e) {
                // awaitTermination方法被中断的时候也中止线程池中全部的线程的执行。
                GALogger.d(TAG, "awaitTermination interrupted: " + e);
                pool.shutdownNow();
                e.printStackTrace();
            }
            GALogger.d(TAG, "end");
        }
    }


    class MyThread implements Runnable{
        private int position;
        private  List<String> gateway;
        private List<String> mListDomain;

        public MyThread(int i, List<String> gateway,List<String> listDomain) {
            this.position = i;
            this.gateway = gateway;
            this.mListDomain = listDomain;
        }

        @Override
        public void run() {
            InetAddress byName = null;
            int resCode = -1;
            try {
                byName = InetAddress.getByName(mListDomain.get(position));
                resCode = byName == null ? -1 : 0;
            } catch (UnknownHostException e) {
                resCode = -1;
                e.printStackTrace();
            }catch (Exception e) {
                resCode = -1;
                e.printStackTrace();
            } finally {
                if (resCode == 0) {
                    usableDomains.add(gateway.get(position)+"/");
                }
                GALogger.d(TAG, "resCode   " + resCode + "    i     " + position +"   host   "+gateway.get(position)+"/");
            }
        }
    }

    private void goApp(){
        String hostAddress = MyApplication.getHostAddress();
        GALogger.d(TAG, "hostAddress   " + hostAddress);
        if(AccountManager.getInstance().isLogin()){
            /**
             * 掉登录接口  成功了去首页
             */
            long currentTime = System.currentTimeMillis();
            String phoneNumber = AccountManager.getInstance().getPhoneNumber();
            String passWord = AccountManager.getInstance().getPassWord();
            splashPresenter.login(phoneNumber,passWord,currentTime,false);

        }else {
            MyApplication.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    LoginActivity.startThis(splashActivity);
                    splashActivity.finish();
                }
            },3000);
        }
    }

    @Override
    public void loginSuccess(AccountInfo accountInfo) {
        MyApplication.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                MainActivity.startThis(splashActivity);
                splashActivity.finish();
            }
        },3000);
    }

    @Override
    public void loginError() {
        MyApplication.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                LoginActivity.startThis(splashActivity);
                splashActivity.finish();
            }
        },3000);
    }
}
