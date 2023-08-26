package com.example.varandev.Mail.Received_requests;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.varandev.Mail.Mail_Tab_Adapter;
import com.example.varandev.R;
import com.google.android.material.tabs.TabLayout;


public class Main_received_Fragment extends Fragment {

    TabLayout tablayout;
    ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =   inflater.inflate(R.layout.fragment_mail_p2, container, false);
        tablayout = view.findViewById(R.id.xml_mail_p2_tablayout);
        viewPager = view.findViewById(R.id.xml_mail_p2_viewpgr);

        //must use getChildFragmentManager instead get getfragment manager
        Mail_Tab_Adapter mail_tab_adapter = new Mail_Tab_Adapter(getChildFragmentManager());
        mail_tab_adapter.add_mail_parent_tab(new Pending_requests_Fragment(),"Pending");
        mail_tab_adapter.add_mail_parent_tab(new Accepted_requests_Fragment(),"Accepted");
        mail_tab_adapter.add_mail_parent_tab(new Rejected_requests_Fragment(),"Rejected");
        viewPager.setAdapter(mail_tab_adapter);
        tablayout.setupWithViewPager(viewPager);

        return view;
    }
}