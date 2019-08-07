package com.example.qrcode.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.qrcode.Constant;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import java.lang.ref.WeakReference;
import java.util.EnumMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by yangyu on 2017/11/27.
 */

public class DecodeUtils {
    private static final String TAG = "DecodeUtils";

    public static class DecodeAsyncTask extends AsyncTask<Bitmap, Integer, Result> {

        private WeakReference<Context> mContext;
        private Result result;

        public DecodeAsyncTask(Context mContext) {
            this.mContext = new WeakReference<Context>(mContext);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Result doInBackground(Bitmap... bitmaps) {
            result = QRCodeUtil.decodeFromPicture(bitmaps[0]);
            return result;
        }

        @Override
        protected void onPostExecute(Result result) {
            super.onPostExecute(result);
            if (result != null) {
                String text = result.getText();
                if (!TextUtils.isEmpty(text)) {
                    Intent data = new Intent();
                    data.putExtra(Constant.EXTRA_RESULT_CODE_TYPE, "QR_CODE");
                    data.putExtra(Constant.EXTRA_RESULT_CONTENT, result.getText());

                    if (mContext.get() instanceof Activity){
                        ((Activity) mContext.get()).setResult(RESULT_OK, data);
                        ((Activity) mContext.get()).finish();
                    }
                }
            } else {
                Toast.makeText(mContext.get(), "解码失败", Toast.LENGTH_SHORT).show();
            }

        }
    }
}


