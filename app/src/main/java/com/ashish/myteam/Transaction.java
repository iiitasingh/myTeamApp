package com.ashish.myteam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class Transaction extends AppCompatActivity {

    TabLayout TransTabLayout;
    TabItem debit,credit;
    ViewPager TransViewPager;
    Adapter_Transaction_Tab transactionPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_transaction);

        TransTabLayout = findViewById(R.id.TransactionTablayout);
        debit = findViewById(R.id.debits);
        credit = findViewById(R.id.credits);
        TransViewPager = findViewById(R.id.TransactionViewPager);

        transactionPageAdapter = new Adapter_Transaction_Tab(getSupportFragmentManager(),TransTabLayout.getTabCount());
        TransViewPager.setAdapter(transactionPageAdapter);
        TransViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(TransTabLayout));

        TransTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TransViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
