package com.example.varandev.Match;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.varandev.HomeActivity;
import com.example.varandev.Mail.MailActivity;
import com.example.varandev.Match.Match_Tab_Layout.All_prof_listener_Fragment;
import com.example.varandev.Match.Match_Tab_Layout.All_prof_paging_Fragment;
import com.example.varandev.Match.Match_Tab_Layout.All_profiles_Fragment;
import com.example.varandev.Match.Match_Tab_Layout.All_prof_cache_Fragment;
import com.example.varandev.Match.Match_Tab_Layout.Match_Tab_Adapter;
import com.example.varandev.Match.Match_Tab_Layout.Shortlisted;
import com.example.varandev.Match.Match_Tab_Layout.Shortlisted_me;
import com.example.varandev.Search.SearchActivity;
import com.example.varandev.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;


public class MatchActivity extends AppCompatActivity {

    TabLayout tablayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);


        tablayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpgr);


        /*----Start of Tab layout */

        Match_Tab_Adapter match_tab_adapter = new Match_Tab_Adapter(getSupportFragmentManager());
        match_tab_adapter.add_mail_parent_tab(new All_profiles_Fragment(), "All_profiles");
        match_tab_adapter.add_mail_parent_tab(new All_prof_paging_Fragment(), "Paging");
        match_tab_adapter.add_mail_parent_tab(new All_prof_cache_Fragment(), "Cache");
        match_tab_adapter.add_mail_parent_tab(new All_prof_listener_Fragment(), "listener");
        match_tab_adapter.add_mail_parent_tab(new Shortlisted(), "Shortlisted");
        match_tab_adapter.add_mail_parent_tab(new Shortlisted_me(), "Shortlisted me");
        match_tab_adapter.add_mail_parent_tab(new All_prof_cache_Fragment(), "Fourth");
        viewPager.setAdapter(match_tab_adapter);
        tablayout.setupWithViewPager(viewPager);

        /*----end of Tab layout */


        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_btm);

        /*----Start of bottom nav*/
        bottomNavigationView.setSelectedItemId(R.id.Profile);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.Home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.Profile:
                        break;
                    case R.id.Mail:
                        startActivity(new Intent(getApplicationContext(), MailActivity.class));
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.Search:
                        startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                        overridePendingTransition(0, 0);
                        break;
                }
                return true;
            }
        });
        /*----End of bottom nav*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent Match_intent = new Intent(MatchActivity.this, HomeActivity.class);
        startActivity(Match_intent);
        overridePendingTransition(0, 0);
    }
}