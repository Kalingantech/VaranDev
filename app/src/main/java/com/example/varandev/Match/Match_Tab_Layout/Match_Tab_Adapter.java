package com.example.varandev.Match.Match_Tab_Layout;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Match_Tab_Adapter extends FragmentPagerAdapter {

    private final ArrayList<Fragment> Match_frag_Array_list = new ArrayList<>();
    private final ArrayList<String> Match_tab_title_list = new ArrayList<>();

    public void add_mail_parent_tab(Fragment fragment, String title) {
        Match_frag_Array_list.add(fragment);
        Match_tab_title_list.add(title);
    }

    public Match_Tab_Adapter(@NonNull @NotNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {
        return Match_frag_Array_list.get(position);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return Match_tab_title_list.get(position);
    }

    @Override
    public int getCount() {
        return Match_frag_Array_list.size();
    }


}
