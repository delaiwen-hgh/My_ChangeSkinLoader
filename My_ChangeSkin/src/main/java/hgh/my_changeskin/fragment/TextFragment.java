package hgh.my_changeskin.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import hgh.my_changeskin.R;
import solid.ren.skinlibrary.base.SkinBaseFragment;



public class TextFragment  extends SkinBaseFragment
{
    private LinearLayout mLinearLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_text, container, false);
        initView(view);
        return view;
    }

    private void initView(View view)
    {
        mLinearLayout = (LinearLayout) view.findViewById(R.id.fg_tx_ll);

        for (int i = 0; i < 20; i++)
        {
            TextView textView = new TextView(getActivity());
            textView.setText("动态创建TextView--" + i);
            textView.setTextSize(20);
            textView.setTextColor(getResources().getColor(R.color.item_fg2_tv_color));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(20, 20, 20, 20);
            textView.setLayoutParams(params);

            dynamicAddView(textView,"textColor",R.color.item_fg2_tv_color); //添加动态创建的View，及其对应的属性(因为初始化的时候未添加)

            mLinearLayout.addView(textView);

            dynamicAddFontView(textView); //添加对字体的支持
        }
    }


}
