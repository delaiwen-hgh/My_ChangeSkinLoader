package solid.ren.skinlibrary.loader;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListenerV1;
import com.thin.downloadmanager.ThinDownloadManager;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import solid.ren.skinlibrary.FontLoaderListener;
import solid.ren.skinlibrary.SkinConfig;
import solid.ren.skinlibrary.SkinLoaderListener;
import solid.ren.skinlibrary.ISkinUpdate;
import solid.ren.skinlibrary.utils.ResourcesCompat;
import solid.ren.skinlibrary.utils.SkinFileUtils;
import solid.ren.skinlibrary.utils.SkinL;
import solid.ren.skinlibrary.utils.TypefaceUtils;


/**
 * Created by _SOLID
 * Date:2016/4/13
 * Time:21:07
 */
public class SkinManager implements ISkinLoader {

    private List<ISkinUpdate> mSkinObservers;
    private static volatile SkinManager mInstance;
    private Context context;
    private Resources mResources;
    private boolean isDefaultSkin = false;
    /**
     * skin package name
     */
    private String skinPackageName;
    /**
     * skin path
     */
    private String skinPath;

    /**
     * 换肤资源后缀
     */
    private String mSuffix = "";


    private SkinManager() {

    }

    public void init(Context ctx) {
        context = ctx.getApplicationContext();
        TypefaceUtils.CURRENT_TYPEFACE = TypefaceUtils.getTypeface(context);
    }

    public Context getContext() {
        return context;
    }

    public int getColorPrimaryDark()
    {    //状态栏颜色

        String resName = "colorPrimaryDark";
        resName = appendSuffix(resName);
        if (mResources != null)
        {
            int identify = mResources.getIdentifier(resName, "color", skinPackageName);
            if (!(identify <= 0))
            {
                return mResources.getColor(identify);
            }
        }
        return -1;
    }

    public boolean isExternalSkin() {
        return !isDefaultSkin && mResources != null;
    }

    public String getCurSkinPath() {
        return skinPath;
    }

    public String getCurSkinPackageName() {
        return skinPackageName;
    }

    public Resources getResources() {
        return mResources;
    }

    /**
     * 恢复到默认主题
     */
    public void restoreDefaultTheme() {
        SkinConfig.saveSkinPath(context, SkinConfig.DEFAULT_SKIN);
        SkinConfig.putPluginSuffix(context, SkinConfig.DEFAULT_SUFFIX);
        mSuffix = "";
        isDefaultSkin = true;
        mResources = context.getResources();
        skinPackageName = context.getPackageName();
        notifySkinUpdate();
    }

    public static SkinManager getInstance() {
        if (mInstance == null) {
            synchronized (SkinManager.class) {
                if (mInstance == null) {
                    mInstance = new SkinManager();
                }
            }
        }
        return mInstance;
    }

    @Override
    public void attach(ISkinUpdate observer) {
        if (mSkinObservers == null) {
            mSkinObservers = new ArrayList<>();
        }
        if (!mSkinObservers.contains(observer)) {
            mSkinObservers.add(observer);
        }
    }

    @Override
    public void detach(ISkinUpdate observer) {
        if (mSkinObservers == null) return;
        if (mSkinObservers.contains(observer)) {
            mSkinObservers.remove(observer);
        }
    }

    @Override
    public void notifySkinUpdate() {
        if (mSkinObservers == null) return;

        for (ISkinUpdate observer : mSkinObservers) {
            observer.onThemeUpdate();
        }
    }

    public void loadSkin() {
        String skin = SkinConfig.getCustomSkinPath(context);
        mSuffix = SkinConfig.getSuffix(context);

        loadSkin(skin, null);
    }

    public void loadSkin(SkinLoaderListener callback) {
        String skin = SkinConfig.getCustomSkinPath(context);
        if (SkinConfig.isDefaultSkin(context)) {
            return;
        }
        loadSkin(skin, callback);
    }

    /**
     * load skin form local
     * <p>
     * eg:theme.skin
     * </p>
     *
     * @param skinName the name of skin(in assets/skin)
     * @param callback load Callback
     */
    public void loadSkin(String skinName, final SkinLoaderListener callback) {

        new AsyncTask<String, Void, Resources>() {

            protected void onPreExecute() {
                if (callback != null) {
                    callback.onStart();
                }
            }

            @Override
            protected Resources doInBackground(String... params) {
                try {
                    if (params.length == 1) {
                        String skinPkgPath = SkinFileUtils.getSkinDir(context) + File.separator + params[0];
            //            SkinL.i("skinPkgPath", skinPkgPath);

                        File file = new File(skinPkgPath);
                        if (!file.exists()) {
                            return null;
                        }
                        PackageManager mPm = context.getPackageManager();
                        PackageInfo mInfo = mPm.getPackageArchiveInfo(skinPkgPath,
                                                                      PackageManager.GET_ACTIVITIES);
                        skinPackageName = mInfo.packageName;
                        AssetManager assetManager = AssetManager.class.newInstance();
                        Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
                        addAssetPath.invoke(assetManager, skinPkgPath);


                        Resources superRes = context.getResources();
                        Resources skinResource = ResourcesCompat.getResources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
                        SkinConfig.saveSkinPath(context, params[0]);//保存皮肤路径
                        SkinConfig.putPluginSuffix(context,  SkinConfig.DEFAULT_SUFFIX);


                        skinPath = skinPkgPath;
                        isDefaultSkin = false;
                        mSuffix = "";
                        return skinResource;
                    }
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            protected void onPostExecute(Resources result) {
                mResources = result;

                if (mResources != null) {
                    if (callback != null) callback.onSuccess();
                    notifySkinUpdate();
                } else
                {
                    if (!mSuffix.equals(SkinConfig.DEFAULT_SUFFIX)) //初始化加载后缀
                    {
                        mResources = context.getResources();
                        skinPackageName = context.getPackageName();
                        isDefaultSkin = false;
                        notifySkinUpdate();
                    }else  //初始化加载默认
                    {
                        isDefaultSkin = true; //第一次运行，未选择皮肤，不进行notifySkinUpdate()的操作,使用宿主资源
                    }

                    if (callback != null)
                    {
                        callback.onFailed("没有获取到资源");
                    }
                }
            }

        }.execute(skinName);
    }


    /**
     * load skin form internet
     * <p>
     *
     * </p>
     *
     * @param skinUrl  the url of skin
     * @param callback load Callback
     */
    public void loadSkinFromUrl(String skinUrl, final SkinLoaderListener callback) {
        String skinPath = SkinFileUtils.getSkinDir(context);
        final String skinName = skinUrl.substring(skinUrl.lastIndexOf("/") + 1);
        String skinFullName = skinPath + File.separator + skinName;
        final File skinFile = new File(skinFullName);
        if (skinFile.exists()) {
            loadSkin(skinName, callback);
            return;
        }

        Uri downloadUri = Uri.parse(skinUrl);
        Uri destinationUri = Uri.parse(skinFullName);

        DownloadRequest downloadRequest = new DownloadRequest(downloadUri)
                .setRetryPolicy(new DefaultRetryPolicy())
                .setDestinationURI(destinationUri)
                .setPriority(DownloadRequest.Priority.HIGH);
        callback.onStart();
        downloadRequest.setStatusListener(new DownloadStatusListenerV1() {
            @Override
            public void onDownloadComplete(DownloadRequest downloadRequest) {
                callback.onStart();
                loadSkin(skinName, callback);
            }

            @Override
            public void onDownloadFailed(DownloadRequest downloadRequest, int errorCode, String errorMessage)
            {

                callback.onFailed(errorMessage);
            }

            @Override
            public void onProgress(DownloadRequest downloadRequest, long totalBytes, long downloadedBytes, int progress) {
                callback.onProgress(progress);
            }
        });

        ThinDownloadManager manager = new ThinDownloadManager();
        manager.add(downloadRequest);


    }

    public void loadFont(String fontName,final FontLoaderListener callback) {
        callback.onStart();
        Typeface tf = TypefaceUtils.createTypeface(context, fontName,callback);
        TextViewRepository.applyFont(tf);
    }


    /**
     * load font form internet
     * <p>
     *
     * </p>
     *
     * @param fontUrl  the url of font
     * @param callback load Callback
     */
    public void loadFontFromUrl(String fontUrl, final FontLoaderListener callback) {
        String skinPath = SkinFileUtils.getSkinDir(context);              //下载到皮肤更目录下
        final String fontName = fontUrl.substring(fontUrl.lastIndexOf("/") + 1);
        String fontFullName = skinPath + File.separator + fontName;
       final File fontFile = new File(fontFullName);
        if (fontFile.exists()) {
            loadFont(fontName, callback);
            return;
        }

        Uri downloadUri = Uri.parse(fontUrl);
        Uri destinationUri = Uri.parse(fontFullName);

        DownloadRequest downloadRequest = new DownloadRequest(downloadUri)
                .setRetryPolicy(new DefaultRetryPolicy())
                .setDestinationURI(destinationUri)
                .setPriority(DownloadRequest.Priority.HIGH);
        callback.onStart();
        downloadRequest.setStatusListener(new DownloadStatusListenerV1()
        {
            @Override
            public void onDownloadComplete(DownloadRequest downloadRequest)
            {
                loadFont(fontName, callback);
            }

            @Override
            public void onDownloadFailed(DownloadRequest downloadRequest, int errorCode,
                                         String errorMessage)
            {
                callback.onFailed(errorMessage);
            }

            @Override
            public void onProgress(DownloadRequest downloadRequest, long totalBytes,
                                   long downloadedBytes, int progress)
            {
                callback.onProgress(progress);
            }
        });

        ThinDownloadManager manager = new ThinDownloadManager();
        manager.add(downloadRequest);

    }








    public int getColor(int resId) {
        int originColor = ContextCompat.getColor(context, resId);
        if (mResources == null || isDefaultSkin) {
            return originColor;
        }
        String resName = context.getResources().getResourceEntryName(resId);

        resName = appendSuffix(resName);

        int trueResId = mResources.getIdentifier(resName, "color", skinPackageName);
        int trueColor;

        try {
            trueColor = mResources.getColor(trueResId);
        } catch (Resources.NotFoundException e) {
      //      e.printStackTrace();
            trueColor = originColor;
        }

        return trueColor;
    }



    public Drawable getDrawable(int resId) {
        Drawable originDrawable = ContextCompat.getDrawable(context, resId);
        if (mResources == null || isDefaultSkin) {
            return originDrawable;
        }
        String resName = context.getResources().getResourceEntryName(resId);

        resName = appendSuffix(resName);

        String resType=context.getResources().getResourceTypeName(resId);
        int trueResId=-1;
        if (resType.equals("drawable"))
        {
             trueResId = mResources.getIdentifier(resName, "drawable", skinPackageName);
        }else if (resType.equals("mipmap"))
        {
             trueResId = mResources.getIdentifier(resName, "mipmap", skinPackageName);
        }
        Drawable trueDrawable;
        try {
      //      SkinL.i("SkinManager getDrawable", "SDK_INT = " + android.os.Build.VERSION.SDK_INT);
            if (android.os.Build.VERSION.SDK_INT < 22) {
                trueDrawable = mResources.getDrawable(trueResId);
            } else {
                trueDrawable = mResources.getDrawable(trueResId, null);
            }
        } catch (Resources.NotFoundException e) {
      //      e.printStackTrace();
            trueDrawable = originDrawable;
        }

        return trueDrawable;
    }

    /**
     * 加载指定资源颜色drawable,转化为ColorStateList，保证selector类型的Color也能被转换。
     * 无皮肤包资源返回默认主题颜色
     *
     * @param resId resources id
     * @return ColorStateList
     * @author pinotao
     */
    public ColorStateList getColorStateList(int resId) {
        boolean isExternalSkin = true;
        if (mResources == null || isDefaultSkin) {
            isExternalSkin = false;
        }
        String resName = context.getResources().getResourceEntryName(resId);

        resName = appendSuffix(resName);

        if (isExternalSkin) {
            int trueResId = mResources.getIdentifier(resName, "color", skinPackageName);
            ColorStateList trueColorList;
            if (trueResId == 0) { // 如果皮肤包没有复写该资源，但是需要判断是否是ColorStateList
                try {
                    ColorStateList originColorList = context.getResources().getColorStateList(resId);
                    return originColorList;
                } catch (Resources.NotFoundException e) {
       //             e.printStackTrace();
        //            SkinL.e("resName = " + resName + " NotFoundException : " + e.getMessage());
                }
            } else {
                try {
                    trueColorList = mResources.getColorStateList(trueResId);
                    return trueColorList;
                } catch (Resources.NotFoundException e) {
       //             e.printStackTrace();
       //             SkinL.e("resName = " + resName + " NotFoundException :" + e.getMessage());
                }
            }
        } else {
            try {
                ColorStateList originColorList = context.getResources().getColorStateList(resId);
                return originColorList;
            } catch (Resources.NotFoundException e) {
        //        e.printStackTrace();
        //        SkinL.e("resName = " + resName + " NotFoundException :" + e.getMessage());
            }

        }

        int[][] states = new int[1][1];
        return new ColorStateList(states, new int[]{context.getResources().getColor(resId)});
    }



    /**
     * 应用内换肤，传入资源区别的后缀
     *
     * @param suffix
     */
    public void changeSkin(String suffix)
    {
        SkinConfig.saveSkinPath(context, SkinConfig.DEFAULT_SKIN);
        SkinConfig.putPluginSuffix(context, suffix);
        mSuffix = suffix;
        isDefaultSkin = false;
        mResources = context.getResources();
        skinPackageName = context.getPackageName();
        notifySkinUpdate();
    }
    private String appendSuffix(String name)
    {
        if (!TextUtils.isEmpty(mSuffix))
            return name += "_" + mSuffix;
        return name ;
    }


}
