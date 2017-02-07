package hgh.my_changeskin.fragment;


import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import hgh.my_changeskin.R;
import solid.ren.skinlibrary.FontLoaderListener;
import solid.ren.skinlibrary.SkinLoaderListener;
import solid.ren.skinlibrary.base.SkinBaseFragment;
import solid.ren.skinlibrary.loader.SkinManager;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.HashMap;


public class SetFragment extends SkinBaseFragment implements View.OnClickListener
{

    public static final String SKINURL = "https://raw.githubusercontent.com/delaiwen-hgh/MyTry/master/My_ChangeSkin/theme_wukong.skin";
    public static final String FONTURL = "https://raw.githubusercontent.com/delaiwen-hgh/MyTry/master/My_ChangeSkin/hanyi.ttf";
    private HashMap<String, String> map = new HashMap<>();
    private MaterialDialog mSkindialog;
    private MaterialDialog mFontdialog;
    private Button mChangeSkin_btn;
    private Button mChangeFont_btn;
    private Button mAddInternetSkin_btn;
    private Button mAddInternetFont_btn;
    private Button mChangeInner_btn;
    private Button mNight_btn;

    private boolean mIsFromInternetSkin; //为了显示过渡动画

    private SkinLoaderListener mSkinLoaderListener=new SkinLoaderListener()
    {
        @Override
        public void onStart() //开始执行加载之前
        {
            Log.i("SkinLoaderListener", "正在切换中");
            mSkindialog.show();
        }

        @Override
        public void onSuccess()
        {      Log.i("SkinLoaderListener", "切换成功");
            mSkindialog.dismiss();
            if (mIsFromInternetSkin)  //在线皮肤过渡
            {
                    showAnimation();
                mIsFromInternetSkin = false;
            }
        }

        @Override
        public void onFailed(String errMsg)
        {

            Log.i("SkinLoaderListener", "切换失败:" + errMsg);
            mSkindialog.setContent("换肤失败:" + errMsg);
            mIsFromInternetSkin = false;


        }

        @Override
        public void onProgress(int progress)
        {
            Log.i("SkinLoaderListener", "皮肤文件下载中:" + progress);
            mSkindialog.setProgress(progress);
        }
    };



    private FontLoaderListener mFontLoaderListener=new FontLoaderListener()
    {
        @Override
        public void onStart()
        {
            Log.i("mFontLoaderListener", "正在切换中");
            mFontdialog.show();
        }

        @Override
        public void onSuccess()
        {
            Log.i("mFontLoaderListener", "切换成功");
            mFontdialog.dismiss();
        }

        @Override
        public void onFailed(String errMsg)
        {
            Log.i("mFontLoaderListener", "切换失败:" + errMsg);
            mFontdialog.setContent(errMsg);
        }

        @Override
        public void onProgress(int progress)
        {
            Log.i("mFontLoaderListener", "字体文件下载中:" + progress);
            mFontdialog.setProgress(progress);
        }
    };








    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_set, container, false);
          initProgressDialog();
        initBtn(view);
        return view;
    }

    private void initBtn(View view)
    {
        mChangeSkin_btn = (Button) view.findViewById(R.id.changeSkin_btn);
        mChangeFont_btn = (Button) view.findViewById(R.id.changeFont_btn);
        mAddInternetSkin_btn = (Button) view.findViewById(R.id.addInternetSkin_btn);
        mAddInternetFont_btn= (Button) view.findViewById(R.id.addInternetFont_btn);
        mChangeInner_btn = (Button) view.findViewById(R.id.changeInner_btn);
        mNight_btn = (Button) view.findViewById(R.id.night_btn);


        mChangeSkin_btn.setOnClickListener(this);
        mChangeFont_btn.setOnClickListener(this);
        mAddInternetSkin_btn.setOnClickListener(this);
        mAddInternetFont_btn.setOnClickListener(this);
        mChangeInner_btn.setOnClickListener(this);
        mNight_btn.setOnClickListener(this);
    }

    private void initProgressDialog()
    {
        mSkindialog = new MaterialDialog.Builder(getContext()).title("换肤中")
                                                         .content("请耐心等待")
                                                         .canceledOnTouchOutside(false)
                                                         .progress(false, 100, true).build();

        mFontdialog = new MaterialDialog.Builder(getContext()).title("切换字体中")
                                                         .content("请耐心等待")
                                                         .canceledOnTouchOutside(false)
                                                         .progress(false, 100, true).build();

    }


    public void changeSkin()
    {

        map.clear();
        initProgressDialog();
        map.put("机器猫", "theme_jiqimao.skin");
        map.put("路飞", "theme_lufei.skin");
        map.put("默认", "restoreTheme");
        new MaterialDialog.Builder(getContext()).title("选择皮肤")
                                                .items(map.keySet())
                                                .itemsCallbackSingleChoice(1,
                                                                           new MaterialDialog.ListCallbackSingleChoice()
                                                                           {
                                                                               @Override
                                                                               public boolean onSelection(
                                                                                       MaterialDialog dialog,
                                                                                       View itemView,
                                                                                       int which,
                                                                                       CharSequence text)
                                                                               {
                                                                                   showAnimation(); //本地皮肤过渡
                                                                                   if (text.equals("默认"))
                                                                                   {
                                                                                       restoreTheme();
                                                                                       return true;
                                                                                   }
                                                                                   SkinManager.getInstance().loadSkin(map.get(text),mSkinLoaderListener);
                                                                                   return true;
                                                                               }
                                                                           })
                                                .positiveText("确定")
                                                .show();
    }

    public void changeFont()
    {
        map.clear();
        map.put("默认", null);
        map.put("时尚细黑", "SSXHZT.ttf");
        map.put("大梁体", "DLTZT.ttf");
        map.put("微软雅黑", "WRYHZT.ttf");
        new MaterialDialog.Builder(getContext()).title("选择字体")
                                                .items(map.keySet())
                                                .itemsCallbackSingleChoice(1,
                                                                           new MaterialDialog.ListCallbackSingleChoice()
                                                                           {
                                                                               @Override
                                                                               public boolean onSelection(
                                                                                       MaterialDialog dialog,
                                                                                       View view,
                                                                                       int which,
                                                                                       CharSequence text)
                                                                               {
                                                                                   SkinManager.getInstance()
                                                                                              .loadFont(
                                                                                                      map.get(text),
                                                                                                      mFontLoaderListener);
                                                                                   return true;
                                                                               }
                                                                           })
                                                .positiveText("确定")
                                                .show();
    }


    public void restoreTheme()
    {
        SkinManager.getInstance().restoreDefaultTheme();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.changeSkin_btn:
                changeSkin();
                break;
            case R.id.changeFont_btn:
                changeFont();
                break;
            case R.id.addInternetSkin_btn:
                addInternetSkin();
                break;
            case R.id.addInternetFont_btn:
                addInternetFont();
                break;
            case R.id.changeInner_btn:
                showAnimation();
                changeInnerSkin();
                break;
            case R.id.night_btn:
                showAnimation();
                changeNightSkin();
                break;

        }


    }

    private void changeNightSkin()
    {
        SkinManager.getInstance().changeSkin("night");
    }

    private void changeInnerSkin()
    {
        SkinManager.getInstance().changeSkin("red");
    }

    private void addInternetFont()
    {

        mFontdialog.setContent("正在从网络下载字体文件");
        SkinManager.getInstance()
                   .loadFontFromUrl(FONTURL, mFontLoaderListener);

    }

    private void addInternetSkin()
    {
        mIsFromInternetSkin = true;
        mSkindialog.setContent("正在从网络下载皮肤文件");
        SkinManager.getInstance()
                   .loadSkinFromUrl(SKINURL, mSkinLoaderListener);

    }


    /**
     * 展示一个切换动画
     */
    private void showAnimation() {
        final View decorView = getActivity().getWindow().getDecorView();
        Bitmap cacheBitmap = getCacheBitmapFromView(decorView);
        if (decorView instanceof ViewGroup && cacheBitmap != null) {
            final View view = new View(getActivity());
            view.setBackgroundDrawable(new BitmapDrawable(getResources(), cacheBitmap));
            ViewGroup.LayoutParams layoutParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                                            ViewGroup.LayoutParams.MATCH_PARENT);
            ((ViewGroup) decorView).addView(view, layoutParam);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
            objectAnimator.setDuration(700);
            objectAnimator.addListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd(Animator animation)
                {
                    super.onAnimationEnd(animation);
                    ((ViewGroup) decorView).removeView(view);
                }
            });
            objectAnimator.start();

        }
    }

    /**
     * 获取一个 View 的缓存视图
     *
     * @param view
     * @return
     */
    private Bitmap getCacheBitmapFromView(View view) {
        final boolean drawingCacheEnabled = true;
        view.setDrawingCacheEnabled(drawingCacheEnabled);
        view.buildDrawingCache(drawingCacheEnabled);
        final Bitmap drawingCache = view.getDrawingCache();
        Bitmap bitmap;
        if (drawingCache != null) {
            bitmap = Bitmap.createBitmap(drawingCache);
            view.setDrawingCacheEnabled(false);
        } else {
            bitmap = null;
        }
        return bitmap;
    }

}
