package solid.ren.skinlibrary.base;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import solid.ren.skinlibrary.SkinConfig;
import solid.ren.skinlibrary.loader.SkinManager;
import solid.ren.skinlibrary.utils.SkinFileUtils;


/**
 * Created by _SOLID
 * Date:2016/4/14
 * Time:10:54
 * Desc:
 */
public class SkinBaseApplication extends Application {

    public void onCreate() {

        super.onCreate();
        initSkinLoader();
    }

    /**
     * Must call init first
     */
    private void initSkinLoader()
    {

      /*  setUpSkinFile();
        setUpFontFile();
        SkinManager.getInstance()
                   .init(SkinBaseApplication.this); //初始化字体
        SkinManager.getInstance()
                   .loadSkin(); //初始化皮肤*/
        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected Void doInBackground(Void... params)
            {
                setUpSkinFile();
                setUpFontFile();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid)
            {
                super.onPostExecute(aVoid);
            }
        }.execute();

        SkinManager.getInstance()
                   .init(SkinBaseApplication.this); //初始化字体
        SkinManager.getInstance()
                   .loadSkin(); //初始化皮肤
    }

    private void setUpSkinFile() {
        try {
            String[] skinFiles = getAssets().list(SkinConfig.SKIN_DIR_NAME);
            for (String fileName : skinFiles) {
                File file = new File(SkinFileUtils.getSkinDir(this), fileName);
                if (!file.exists())
                    SkinFileUtils.copySkinAssetsToDir(this, fileName, SkinFileUtils.getSkinDir(this));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setUpFontFile() //保存字体文件到皮肤文件的根目录下
    {
        try {
            String[] skinFiles = getAssets().list(SkinConfig.FONT_DIR_NAME);
            for (String fileName : skinFiles) {
                File file = new File(SkinFileUtils.getSkinDir(this), fileName);
                if (!file.exists())
                    SkinFileUtils.copyFontAssetsToDir(this, fileName, SkinFileUtils.getSkinDir(this));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }




    }


}
