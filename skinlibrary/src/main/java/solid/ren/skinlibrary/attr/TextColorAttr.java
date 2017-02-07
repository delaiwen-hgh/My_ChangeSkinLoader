package solid.ren.skinlibrary.attr;

import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import solid.ren.skinlibrary.attr.base.SkinAttr;
import solid.ren.skinlibrary.loader.SkinManager;


/**
 * Created by _SOLID
 * Date:2016/4/13
 * Time:22:53
 */
public class TextColorAttr extends SkinAttr {

    private Handler mHandler = new Handler();


    @Override
    public void apply(View view) {
        if (view instanceof TextView) {
            final TextView tv = (TextView) view;
            if (RES_TYPE_NAME_COLOR.equals(attrValueTypeName))
            {
                mHandler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        tv.setTextColor(SkinManager.getInstance()
                                                   .getColorStateList(attrValueRefId));
                    }
                });
            }
        }
    }
}
