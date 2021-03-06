package com.birdcopy.BirdCopyApp.IM;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.birdcopy.BirdCopyApp.ShareDefine;
import com.birdcopy.BirdCopyApp.DataManager.FlyingDataManager;
import com.birdcopy.BirdCopyApp.Download.FlyingFileManager;
import com.birdcopy.BirdCopyApp.Http.FlyingHttpTool;
import com.birdcopy.BirdCopyApp.Content.FlyingWebViewActivity;
import com.birdcopy.BirdCopyApp.MainHome.MainActivity;
import com.birdcopy.BirdCopyApp.R;
import com.birdcopy.BirdCopyApp.Scan.BitmapToText;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;


/**
 * Created by birdcopy on 6/25/15.
 */
public class ShowPhotoActivity extends Activity {


    private ImageView mShowPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showphoto);

        initView();
    }

    /**
     * 初始化layout控件
     */
    private void initView()
    {

        Uri uri = getIntent().getParcelableExtra("photo");

        mShowPhoto = (ImageView) findViewById(R.id.theShowPhoto);

        Picasso.with(this)
                .load(uri)
                .placeholder(R.drawable.placeholder_image)
                .into(mShowPhoto);

        mShowPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowPhotoActivity.this.finish();
            }
        });

        mShowPhoto.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                int itmesRes= R.array.dealWtihPicWaysQR;

                new MaterialDialog.Builder(ShowPhotoActivity.this)
                        .items(itmesRes)
                        .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                /**
                                 * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                                 * returning false here won't allow the newly selected radio button to actually be selected.
                                 **/
                                switch (which) {
                                    case 0:

                                    {
                                        Bitmap bitmap = mShowPhoto.getDrawingCache();

                                        FlyingFileManager.savePhotoToLocal(bitmap, null);
                                        Toast.makeText(ShowPhotoActivity.this, "已经成功保存图片", Toast.LENGTH_SHORT).show();

                                        break;
                                    }

                                    case 1:

                                        break;

                                    case 2:
                                    {
                                        mShowPhoto.buildDrawingCache();
                                        Bitmap bitmap = mShowPhoto.getDrawingCache();

                                        String barcode = new BitmapToText(bitmap).getText();

                                        if (barcode != null) {
                                            dealWithScanString(barcode);
                                        }
                                        break;
                                    }
                                }

                                return true;
                            }
                        })
                        .show();

                return true;
            }
        });
    }

    public  void dealWithScanString(String scanStr)
    {
        String type = ShareDefine.judgeScanType(scanStr);

        if (type.equals(ShareDefine.KQRTyepeWebURL))
        {
            String lessonID = ShareDefine.getLessonIDFromOfficalURL(scanStr);
            if (lessonID != null)
            {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("lessonID", lessonID);
                startActivity(intent);
            }
            else
            {
                Intent webAdvertisingActivityIntent = new Intent(this, FlyingWebViewActivity.class);
                webAdvertisingActivityIntent.putExtra("url", scanStr);
                startActivity(webAdvertisingActivityIntent);
            }
        }
        if (type.equals(ShareDefine.KQRTyepeChargeCard))
        {
            if (scanStr != null)
            {
                FlyingHttpTool.chargingCrad(scanStr,
                        FlyingDataManager.getCurrentPassport(),
                        FlyingDataManager.getBirdcopyAppID(),
                        new FlyingHttpTool.ChargingCradListener() {
                            @Override
                            public void completion(String resultStr) {

                                Toast.makeText(ShowPhotoActivity.this, resultStr, Toast.LENGTH_LONG).show();
                            }
                        }
                );
            }
        }

        if (type.equals(ShareDefine.KQRTypeLogin))
        {
            if (scanStr != null)
            {

                String loginID = ShareDefine.getLoginIDFromQR(scanStr);

                if (loginID!=null) {

                    FlyingHttpTool.loginWithQR(loginID,
                            FlyingDataManager.getCurrentPassport(),
                            FlyingDataManager.getBirdcopyAppID(),
                            new FlyingHttpTool.LoginWithQRListener() {
                        @Override
                        public void completion(boolean isOK) {

                            if (isOK)
                            {
                                Toast.makeText(ShowPhotoActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(ShowPhotoActivity.this, "登录失败", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        }

        if (type.equals(ShareDefine.KQRTyepeCode))
        {
            if (scanStr != null)
            {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("lessonID", scanStr);
                startActivity(intent);
            }
        }
    }

}
