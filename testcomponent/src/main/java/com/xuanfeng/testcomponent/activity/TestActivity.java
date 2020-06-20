package com.xuanfeng.testcomponent.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.transition.Explode;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.xuanfeng.testcomponent.R;
import com.xuanfeng.testcomponent.databinding.ActivityTestBinding;
import com.xuanfeng.testcomponent.hotfix.ISay;
import com.xuanfeng.testcomponent.hotfix.SayException;
import com.xuanfeng.xflibrary.component.ComponentUtil;
import com.xuanfeng.xflibrary.mvp.BaseActivity;
import com.xuanfeng.xflibrary.mvp.BasePresenter;
import com.xuanfeng.xflibrary.utils.AppUtil;
import com.xuanfeng.xflibrary.utils.FileUtil;
import com.xuanfeng.xflibrary.utils.ImageUtil;
import com.xuanfeng.xflibrary.utils.SoftKeyBoardUtil;
import com.xuanfeng.xflibrary.utils.StringUtils;
import com.xuanfeng.xflibrary.utils.ToastUtil;
import com.xuanfeng.xflibrary.widget.popupmenu.PopupMenu;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.DexClassLoader;


public class TestActivity extends BaseActivity<BasePresenter, ViewModel, ActivityTestBinding> implements View.OnClickListener {


    private PopupMenu mPopupMenu;
    private SoftKeyBoardUtil mSoftKeyBoardUtil;

    int GALLERY_CODE = 666;
    int CROP_CODE = 777;
    int TAKE_CODE = 888;


    public void onClick(View view) {
        int i = view.getId();//测试自定义EditText
        if (i == R.id.tv_test_for_edittext) {
            Intent intent = new Intent(this, TestForEditTextActivity.class);
            startActivity(intent);
        } else if (i == R.id.tv_test_for_popupmenu) {
            if (mPopupMenu == null) {
                initPopupMenu();
            }
            if (!mPopupMenu.isShowing()) {
                mPopupMenu.showAsDropDown(mBinding.tvTestForPopupmenu);
            }
        } else if (i == R.id.tv_test_for_touch_dispatch) {
            Intent intent;
            intent = new Intent(this, TestTouchEventActivity.class);
            startActivity(intent);
        } else if (i == R.id.tv_test_for_flag) {
            addFlags((int) Math.pow(2, abs++));
            mBinding.tvTestForFlag.setText(Integer.toBinaryString(mFlags) + "");
        } else if (i == R.id.tv_test_for_download_https_image) {//下载https图片
            String url = "http://imgservice.suning.cn/uimg1/snsawp/snsawp_common/d59b9eb8-c2de-4885-a826-ce0679d2bb3c.png";
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                    Bitmap bitmap = ImageUtil.getBitmap(url);
                    ImageUtil.saveBitmapToSD(bitmap, path, "ceshi.jpg");

                }
            }).start();

        } else if (i == R.id.tv_test_for_gallery) {
            Intent intent;
            intent = new Intent(this, TestForGalleryActivity.class);
            startActivity(intent);
        } else if (i == R.id.tv_aidl) {
//            ImageUtil.selectFromGallery(this, GALLERY_CODE);

            Uri  outUri = Uri.fromFile(new File(AppUtil.getAppTempPath(this) + "/" + "take.jpg"));

            ImageUtil.takePhoto(this,outUri, TAKE_CODE);

        } else if (i == R.id.ll_test_share_anim) {
            Intent intent;
            intent = new Intent(this, TestShareAnimActivity.class);
            Pair pair = Pair.create(mBinding.tvShareAnim, "fab");
            Pair pair1 = Pair.create(mBinding.ivShareAnim, "pic");
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(TestActivity.this, pair, pair1);
            startActivity(intent, activityOptionsCompat.toBundle());
        } else if (i == R.id.tv_contact) {
            ComponentUtil.toRouterPage(TestActivity.this, "667");
        } else if (i == R.id.iv_left) {
            finishAfterTransition();
        } else if (i == R.id.tv_hot_fix) {
            hotFix();
        }
    }

    private void hotFix() {

        ISay say;


        // 获取hotfix的jar包文件
        final File jarFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "outfix.dex");

        if (!jarFile.exists()) {
            say = new SayException();
            Toast.makeText(this, say.saySomething(), Toast.LENGTH_SHORT).show();
        } else {
            // 只要有读写权限的路径均可
            DexClassLoader dexClassLoader = new DexClassLoader(jarFile.getAbsolutePath(),
                    getExternalCacheDir().getAbsolutePath(), null, getClassLoader());
            try {
                // 加载 SayHotFix 类
                Class clazz = dexClassLoader.loadClass("com.xuanfeng.testcomponent.hotfix.SayHotFix");
                // 强转成 ISay, 注意 ISay 的包名需要和hotfix jar包中的一致
                ISay iSay = (ISay) clazz.newInstance();
                Toast.makeText(this, iSay.saySomething(), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void initPopupMenu() {
        final List<String> list = new ArrayList<>();
        list.add("周杰伦");
        list.add("王力宏");
        list.add("陶喆");
        list.add("周传雄");
        list.add("胡彦斌");
        list.add("刘德华");
        list.add("简弘亦");
        list.add("张学友");
        list.add("杨坤");
        mPopupMenu = new PopupMenu(this, mBinding.tvTestForPopupmenu.getWidth(), 500, list);
        mPopupMenu.setOnItemClickListener((view, position) -> {
            mPopupMenu.dismiss();
            Toast.makeText(TestActivity.this, list.get(position), Toast.LENGTH_SHORT).show();
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mSoftKeyBoardUtil.removeListener();
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    public BasePresenter initPresenter() {
        mBinding.setListener(this);
        return null;
    }


    @Override
    public void initData(Bundle bundle) {
        mBinding.tvTittle.setText("测试界面");
        mBinding.setListener(this);
        mSoftKeyBoardUtil = SoftKeyBoardUtil.setListener(this, mKeyBoardListener);
        Glide.with(this).load("https://111.229.132.124/image/20200615/35274f02bf37454b86cac85059eaa73f.jpg").

                apply(RequestOptions.bitmapTransform(new CircleCrop())).into(mBinding.ivShareAnim);

    }

    SoftKeyBoardUtil.KeyBoardListener mKeyBoardListener = new SoftKeyBoardUtil.KeyBoardListener() {
        @Override
        public void keyBoardShow(int height) {
            //do nothing
        }

        @Override
        public void keyBoardHide(int height) {
            //do nothing
        }
    };

    @Override
    public int getStatusBarColorResId() {
        return R.color.baseThemeColor;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setEnterTransition(new Explode());
        super.onCreate(savedInstanceState);
    }

    private int mFlags = 0b000;
    int abs = 0;

    //mFlags是否true
    public boolean isFlags(int mFlags) {
        return (this.mFlags & mFlags) == mFlags;
    }

    //设置mFlags 为true
    public void addFlags(int mFlags) {
        this.mFlags |= mFlags;
    }

    //设置mFlags为false
    public void removeFlags(int mFlags) {
        this.mFlags &= ~mFlags;
    }

    //设置mFlags属性value
    public void setFlags(int mFlags, boolean value) {
        if (value) {
            addFlags(mFlags);
        } else {
            removeFlags(mFlags);
        }
    }

    Uri outUri;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        outUri = Uri.fromFile(new File(AppUtil.getAppTempPath(this) + "/" + "small.jpg"));


        if (GALLERY_CODE == requestCode) {
            if (resultCode == RESULT_OK) {
                try {

                    String path = ImageUtil.getPathFromUri(this, data.getData());
                    Glide.with(this).load(path).into(mBinding.ivShareAnim);
                    FileUtil.deleteFile(AppUtil.getAppTempPath(this) + "/" + "small.jpg");
                    ImageUtil.cropFromGallery(this, CROP_CODE, data.getData(), outUri, 150, 150, 1, 1);
                } catch (Exception e) {
                    // TODO Auto-generatedcatch block
                    e.printStackTrace();
                }

            }
            return;
        }

        if (CROP_CODE == requestCode) {
            if (resultCode == RESULT_OK) {
                String path = ImageUtil.getPathFromUri(this, outUri);
                Glide.with(this).load(path).
                        skipMemoryCache(true).
                        diskCacheStrategy(DiskCacheStrategy.NONE).
                        into(mBinding.ivShareAnim);
            }
        }

        if (TAKE_CODE == requestCode) {
            if (resultCode == RESULT_OK) {
                Uri uu = Uri.fromFile(new File(AppUtil.getAppTempPath(this) + File.separator + "take.jpg"));
                ImageUtil.cropFromGallery(this, CROP_CODE, uu, outUri, 150, 150, 1, 1);

            }
        }
    }
}
