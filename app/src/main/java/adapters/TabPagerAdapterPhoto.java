package adapters;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import Fragments.PGallery;
import constants.Values;
import dataInLists.DataInPersonsProfile;

public class TabPagerAdapterPhoto extends FragmentStatePagerAdapter {
    private ArrayList<String> dataObj;

    public TabPagerAdapterPhoto(FragmentManager fragmentManager, ArrayList<String> obj) {

        super(fragmentManager);
        this.dataObj = obj;
        // TODO Auto-generated constructor stub
    }

    @Override
    public Fragment getItem(int i) {
        return new PGallery(Values.Link_Image + dataObj.get(i));

    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return dataObj.size(); // No of Tabs
    }

}