package hgh.my_changeskin.skinattr;

import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.view.View;

import solid.ren.skinlibrary.attr.base.SkinAttr;
import solid.ren.skinlibrary.utils.SkinResourcesUtils;

/**
 * Created by 3046 on 2017/1/19.
 */
public class TabIndicatorAttr extends SkinAttr
{
    private Handler mHandler = new Handler();
    @Override
    public void apply(View view)
    {
        if (view instanceof TabLayout)
        {
            final TabLayout tableLayout = (TabLayout) view;
            if (RES_TYPE_NAME_COLOR.equals(attrValueTypeName))//add.. 方法设置的是color 属性
            {
                final int color = SkinResourcesUtils.getColor(attrValueRefId);

                mHandler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        tableLayout.setSelectedTabIndicatorColor(color);
                    }
                });

            }

        }


    }
}
