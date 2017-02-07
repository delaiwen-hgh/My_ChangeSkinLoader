package hgh.my_changeskin;

import hgh.my_changeskin.skinattr.TabIndicatorAttr;
import hgh.my_changeskin.skinattr.TabSelectedTextAttr;
import hgh.my_changeskin.skinattr.ToolbarTitleAttr;
import solid.ren.skinlibrary.SkinConfig;
import solid.ren.skinlibrary.base.SkinBaseApplication;

/**
 * Created by 3046 on 2017/1/19.
 */
public class APP extends SkinBaseApplication
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        SkinConfig.setCanChangeStatusColor(true);
        SkinConfig.setCanChangeFont(true);
        SkinConfig.addSupportAttr("tabLayoutIndicator", new TabIndicatorAttr()); //对tabLayoutIndicator 属性进行支持
        SkinConfig.addSupportAttr("tabSelectedTextColor", new TabSelectedTextAttr()); //tabSelectedTextColor 属性进行支持
        SkinConfig.addSupportAttr("titleTextColor", new ToolbarTitleAttr()); //对titleTextColor 属性进行支持

    }
}
