package solid.ren.skinlibrary.attr;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import solid.ren.skinlibrary.attr.base.SkinAttr;
import solid.ren.skinlibrary.loader.SkinManager;

/**
 * Created by 3046 on 2017/1/23.
 */
public class SrcAttr extends SkinAttr
{
    private Handler mHandler = new Handler();
    @Override
    public void apply(View view)
    {
        if (RES_TYPE_NAME_DRAWABLE.equals(attrValueTypeName)||RES_TYPE_NAME_MIPMAP.equals(attrValueTypeName)) {
            final Drawable bg = SkinManager.getInstance().getDrawable(attrValueRefId);
            if (view instanceof ImageView)
            {
                final ImageView imageView = (ImageView) view;

                mHandler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        imageView.setImageDrawable(bg);
                    }
                });

            }
        }
    }
}
