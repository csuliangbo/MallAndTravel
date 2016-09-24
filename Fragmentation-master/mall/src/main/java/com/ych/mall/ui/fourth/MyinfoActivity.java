package com.ych.mall.ui.fourth;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.ych.mall.R;
import com.ych.mall.bean.ParentBean;
import com.ych.mall.bean.UserInfoBean;
import com.ych.mall.model.Http;
import com.ych.mall.model.UserInfoModel;
import com.ych.mall.ui.AddressActivity_;
import com.ych.mall.ui.base.BaseActivity;
import com.ych.mall.utils.KV;
import com.ych.mall.utils.UserCenter;
import com.ych.mall.widget.CircleImageView;
import com.ych.mall.widget.ProgressButton;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;

/**
 * Created by ych on 2016/9/9.
 */
@EActivity(R.layout.activity_info)
public class MyinfoActivity extends BaseActivity {

    @Click
    void onHead() {
        //showDialog();
    }

    @Click
    void onName() {
        startActivityForResult(new Intent(this, ChangeNameActivity_.class), NAME_REQUEST_CODE);
    }

    @Click
    void onIDNum() {
        startActivityForResult(new Intent(this, ChangeNameActivity_.class).putExtra(KV.TYPE, -1), ID_REQUEST_CODE);
    }

    @Click
    void onGender() {

        new AlertDialog.Builder(this)
                .setTitle("选择性别")
                .setItems(new String[]{"男", "女"}, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which) {
                            case 0:
                                gender = "男";

                                break;
                            case 1:
                                gender = "女";
                                break;
                        }
                        if (!getT(mGender).equals(gender)) {
                            UserInfoModel.changeGender(gender, genderCallBack);
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }


    @Click
    void onBirthday() {
        Calendar calendar = Calendar.getInstance();
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH);
        int d = calendar.get(Calendar.DATE);
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                day=year + "-" + monthOfYear + "-" + dayOfMonth;
                UserInfoModel.changeBirthday(day,birthdayCallBack);
            }
        }, y, m, d).show();
    }

    @Click
    void onPhone() {
        startActivity(new Intent(this, ChangePhoneActivity_.class));
    }

    @Click
    void onPwd() {
        startActivityForResult(new Intent(this, ChangeNameActivity_.class).putExtra(KV.TYPE, 1), NAME_REQUEST_CODE);
    }

    @Click
    void onAddress() {
        startActivity(new Intent(this, AddressActivity_.class).putExtra(KV.DATA, (ArrayList) mDatas));
    }

    @Click
    void onBalance() {
        startActivity(new Intent(this, BalanceActivity_.class));
    }

    @Click
    void onBack() {
        finish();
    }

    @ViewById
    TextView tiTitle;
    @ViewById
    TextView mBalance;
    @ViewById
    CircleImageView mHead;
    @ViewById
    TextView mName;
    @ViewById
    TextView mIDNum;
    @ViewById
    TextView mGender;
    @ViewById
    TextView mBirthday;
    @ViewById
    TextView mPhone;
    @ViewById
    TextView mPwd;
    @ViewById
    TextView mAddress;
    @ViewById
    ProgressButton onOut;
    String gender = null;
    String day;
    List<UserInfoBean.UserInfoData> mDatas;

    @AfterViews
    void initViews() {
        tiTitle.setText("个人信息");
        onOut.setText("退出登录", "加载中...");
        onOut.startLoading();
        UserInfoModel.getUserInfo(UserCenter.getInstance().getCurrentUserId(), infoCallBack);
    }

    private void setUserInfo(UserInfoBean.UserInfoData data) {
        sT(mBalance, data.getAccount_balance());
        sT(mName, data.getRealname());
        sT(mIDNum, data.getIdcard());
        sT(mGender, data.getSex());
        sT(mPhone, data.getMobile());
        UserCenter.getInstance().setCurrentUserPhone(data.getMobile());
        sT(mBirthday, data.getBirthday());


    }

    private String[] items = new String[]{"选择本地图片", "拍照"};


    /*头像名称*/
    private static final String IMAGE_FILE_NAME = "HeadTemple.jpg";

    /* 请求码*/
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;
    private static final int NAME_REQUEST_CODE = 3;
    private static final int ID_REQUEST_CODE = 4;

    private void showDialog() {

        new AlertDialog.Builder(this)
                .setTitle("设置头像")
                .setItems(items, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intentFromGallery = new Intent();
                                intentFromGallery.setType("image/*"); // 设置文件类型
                                intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
                                break;
                            case 1:
                                Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                // 判断存储卡是否可以用，可用进行存储
                                if (hasSDCard()) {
                                    intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT,
                                            Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
                                }
                                startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
                                break;
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

    }

    public boolean hasSDCard() {
        return Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 结果码不等于取消时候
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    break;
                case CAMERA_REQUEST_CODE:
                    if (hasSDCard()) {
                        File tempFile = new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME);
                        startPhotoZoom(Uri.fromFile(tempFile));
                    } else {
                        Toast.makeText(MyinfoActivity.this, "没有SDCARD", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case RESULT_REQUEST_CODE:
                    if (data != null) {
                        setImageToView(data);
                    }
                    break;
                case NAME_REQUEST_CODE:
                    mName.setText(data.getStringExtra(KV.DATA));
                    break;
                case ID_REQUEST_CODE:
                    mIDNum.setText(data.getStringExtra(KV.DATA));
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 2);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param data
     */
    private void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            saveBitmap(photo);
            mHead.setImageBitmap(photo);
        }
    }

    public void saveBitmap(Bitmap bm) {
        File f = new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    //修改性别
    StringCallback genderCallBack = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            TOT("网络链接失败");
        }

        @Override
        public void onResponse(String response, int id) {
            ParentBean bean= Http.model(ParentBean.class,response);
            TOT(bean.getMessage());
            if (bean.getCode().equals("200"))
            {
                mGender.setText(gender);
            }
        }
    };
    //获取用户信息
    StringCallback infoCallBack = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            onOut.stop();
            TOT("加载失败，请检查你的网络");
        }

        @Override
        public void onResponse(String response, int id) {
            onOut.stop();
            UserInfoBean bean = Http.model(UserInfoBean.class, response);
            if (bean.getCode().equals("200")) {
                mDatas = bean.getData();
                setUserInfo(bean.getData().get(0));
            } else
                TOT(bean.getMessage());
        }
    };
    //修改生日
    StringCallback birthdayCallBack=new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            TOT("网络连接失败");
        }

        @Override
        public void onResponse(String response, int id) {
ParentBean bean= Http.model(ParentBean.class,response);
            if (bean.getCode().equals("200")){
                mBirthday.setText(day);
            }
            TOT(bean.getMessage());
        }
    };
}


