package tr.com.bracket.aiku360.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentAdapterHelper extends FragmentPagerAdapter {

    private List<Fragment> _fragmentList=new ArrayList<>();
    private List<String> _fragmentTags=new ArrayList<>();

    public FragmentAdapterHelper( FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public int getCount() {
        return _fragmentList.size();
    }

    @Override
    public Fragment getItem(int position) {
       return _fragmentList.get(position);
    }

    public String getTag(int position){
        return _fragmentTags.get(position);
    }

    public void addFragment(Fragment fragment,String tag){
        _fragmentList.add(fragment);
        _fragmentTags.add(tag);
    }
}
