package com.example.varandev.Mail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.varandev.HomeActivity;
import com.example.varandev.Mail.Sent_requests.Main_sent_Fragment;
import com.example.varandev.Mail.Received_requests.Main_received_Fragment;
import com.example.varandev.Match.MatchActivity;
import com.example.varandev.R;
import com.example.varandev.Search.SearchActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;

public class MailActivity extends AppCompatActivity {

    TabLayout tablayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);

        tablayout = findViewById(R.id.xml_mail_tablayout);
        viewPager = findViewById(R.id.xml_mail_viewpgr);

        Mail_Tab_Adapter mail_tab_adapter = new Mail_Tab_Adapter(getSupportFragmentManager());
        mail_tab_adapter.add_mail_parent_tab(new Main_sent_Fragment(),getResources().getString(R.string.sent));
        mail_tab_adapter.add_mail_parent_tab(new Main_received_Fragment(),getResources().getString(R.string.received));
        viewPager.setAdapter(mail_tab_adapter);
        tablayout.setupWithViewPager(viewPager);

        /*----Start of bottom nav*/
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_btm);


        bottomNavigationView.setSelectedItemId(R.id.Mail);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.Home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0,0);
                        break;

                    case R.id.Profile:
                        startActivity(new Intent(getApplicationContext(), MatchActivity.class));
                        overridePendingTransition(0,0);
                        break;

                    case R.id.Mail:
                        break;

                    case R.id.Search:
                        startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                        overridePendingTransition(0,0);
                        break;
                }
                return true;
            }
        });
        /*----End of bottom nav*/
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("Mail Lifecycle", "onStart");

    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d("Mail Lifecycle", "onStop");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Mail Lifecycle", "onResume");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Mail Lifecycle", "onDestroy");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent Mail_intent = new Intent(MailActivity.this,HomeActivity.class);
        startActivity(Mail_intent);
        overridePendingTransition(0,0);
    }
}