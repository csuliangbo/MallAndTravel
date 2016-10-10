package com.ych.mall.ui.fourth.child;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.ych.mall.R;
import com.ych.mall.ui.base.BaseFragment;
import com.ych.mall.ui.fourth.WebViewActivity;
import com.ych.mall.ui.fourth.WebViewActivity_;
import com.ych.mall.utils.KV;
import com.ych.mall.utils.Tools;
import com.ych.mall.utils.UserCenter;
import com.ych.mall.zxingcode.activity.CaptureActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/21.
 */
@EFragment(R.layout.fragment_share)
public class ShareFragment extends BaseFragment {
    @ViewById(R.id.tv_user_id)
    TextView tvUserId;
    @ViewById(R.id.tv_dns)
    TextView tvDns;
    @ViewById(R.id.iv_code)
    ImageView ivCode;
    @ViewById(R.id.tiTitle)
    TextView tiTitle;
    String dns = "http://www.zzumall.com/index.php/Mobile/Myzzy/web/id/";
    String imgUrl = "http://www.zzumall.com/public/vipcard/10000285.png";
    String userId;
    public static int REQUEST_CODE = 123;

    public static ShareFragment newInstance() {
        Bundle bundle = new Bundle();
        ShareFragment fragment = new ShareFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Click
    void onBack() {
        back();
    }

    @Click
    void onShare() {
        umShare(dns);
    }

    @Click
    void onCopy() {
        // 得到剪贴板管理器
        Tools.copy(dns, getActivity());
        if (Tools.paste(getActivity()).equals(dns)) {
            TOT("复制成功");
        }
    }

    @AfterViews
    void init() {
        tiTitle.setText("我的分享");
        userId = UserCenter.getInstance().getCurrentUserId();
        dns = dns + userId;
        tvUserId.setText(userId);
        tvDns.setText(dns);
        ivCode.setImageBitmap(createQRCode(dns, 200, 200));
        ivCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgUrl = imgUrl.replace("10000285", userId);
                umShare(imgUrl);
            }
        });
    }

    UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            TOT("分享成功啦");
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            TOT("分享失败啦");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            TOT("分享取消了");
        }
    };

    private void umShare(String url) {
        UMImage image = new UMImage(getActivity(), R.drawable.icon_logo);//资源文件
        final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
                {
                        SHARE_MEDIA.WEIXIN,
                        SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN_CIRCLE
                };
        new ShareAction(getActivity()).setDisplayList(displaylist)
                .withText("我的主页")
                .withTitle("掌中游")
                .withMedia(image)
                .withTargetUrl(url)
                .setListenerList(umShareListener)
                .open();
    }

    /**
     * 点击扫一扫按钮，开启扫描二维码
     */
    public void startScan() {
        //跳转到扫一扫
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    /**
     * 创建二维码
     *
     * @param content   content
     * @param widthPix  widthPix
     * @param heightPix heightPix
     * @return 二维码
     */
    public static Bitmap createQRCode(String content, int widthPix, int heightPix) {
        try {
            if (content == null || "".equals(content)) {
                return null;
            }
            // 配置参数
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            // 容错级别
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, widthPix,
                    heightPix, hints);
            int[] pixels = new int[widthPix * heightPix];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < heightPix; y++) {
                for (int x = 0; x < widthPix; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * widthPix + x] = 0xff000000;
                    } else {
                        pixels[y * widthPix + x] = 0xffffffff;
                    }
                }
            }
            // 生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(widthPix, heightPix, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix);
            //必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，内存消耗巨大！
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

}
