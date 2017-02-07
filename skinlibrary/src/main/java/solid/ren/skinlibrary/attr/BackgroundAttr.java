package solid.ren.skinlibrary.attr;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;



import solid.ren.skinlibrary.attr.base.SkinAttr;
import solid.ren.skinlibrary.loader.SkinManager;

/**
 * Created by _SOLID
 * Date:2016/4/13
 * Time:21:46
 */
public class BackgroundAttr extends SkinAttr {

    private Handler mHander = new Handler();
    @Override
    public void apply( final View view) {

        if (RES_TYPE_NAME_COLOR.equals(attrValueTypeName)) {
            final int color = SkinManager.getInstance().getColor(attrValueRefId);
            if (view instanceof CardView) {//这里对CardView特殊处理下
                final CardView cardView = (CardView) view;
                //给CardView设置背景色应该使用cardBackgroundColor，直接使用background没有圆角效果
                mHander.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        cardView.setCardBackgroundColor(color);
                    }
                });

            } else if (view instanceof Toolbar)
            {

              final   Toolbar toolbar = (Toolbar) view;
                    mHander.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            toolbar.setBackgroundColor(color);
                        }
                    });
            }

            else
            {

                mHander.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        view.setBackgroundColor(color);
                    }
                });
            }



        } else if (RES_TYPE_NAME_DRAWABLE.equals(attrValueTypeName)||RES_TYPE_NAME_MIPMAP.equals(attrValueTypeName)) {
             final Drawable bg = SkinManager.getInstance().getDrawable(attrValueRefId);

            mHander.post(new Runnable()
            {
                @Override
                public void run()
                {
                    view.setBackgroundDrawable(bg);
                }
            });
        }



    }
}
