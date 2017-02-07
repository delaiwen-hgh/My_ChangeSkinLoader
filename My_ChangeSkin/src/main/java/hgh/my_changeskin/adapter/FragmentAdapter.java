package hgh.my_changeskin.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import hgh.my_changeskin.fragment.SetFragment;
import hgh.my_changeskin.fragment.SkinFragment;
import hgh.my_changeskin.fragment.TextFragment;
import solid.ren.skinlibrary.base.SkinBaseFragment;

/**
 * Created by 3046 on 2017/1/19.
 */
public class FragmentAdapter extends FragmentPagerAdapter
{
    private static final String TAG = "FragmentAdapter";
    private String[] title;
    public FragmentAdapter(FragmentManager fm, String[] title)
    {
        super(fm);
        this.title = title;
    }

    @Override
    public Fragment getItem(int position)
    {
        SkinBaseFragment fragment = new SkinFragment();
        switch (position)
        {
            case 0:
                fragment = new SkinFragment();
                break;
            case 1:
                fragment= new TextFragment();
                break;
            case 2:
                fragment = new SetFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount()
    {
        return title.length;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return title[position];
    }
}
