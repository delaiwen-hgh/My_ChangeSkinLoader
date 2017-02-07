package solid.ren.skinlibrary;

/**
 * Created by 3046 on 2017/1/20.
 */
public interface FontLoaderListener
{
    void onStart();

    void onSuccess();

    void onFailed(String errMsg);

    /**
     * called when from network load skin
     *
     * @param progress download progress
     */
    void onProgress(int progress);
}
