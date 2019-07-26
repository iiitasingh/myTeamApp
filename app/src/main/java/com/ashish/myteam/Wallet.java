package com.ashish.myteam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import static com.ashish.myteam.SplashScreen.cardList;
import static com.ashish.myteam.SplashScreen.profileUser;

public class Wallet extends AppCompatActivity {

    TextView remainingAmount;
    ListView pendingContri;
    ArrayList<Class_Event> pending;
    public static ArrayList<Class_Event> ContriEvents;
    int remSum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_wallet);
        remainingAmount = (TextView) findViewById(R.id.remainingAmount);
        pendingContri = (ListView) findViewById(R.id.pendingContri);
        pending = new ArrayList<>();
        ContriEvents = new ArrayList<>();

        if (cardList.size() > 0) {
            for (int i = 0; i < cardList.size(); i++) {
                if (cardList.get(i).eContiBool.equals("true")) {
                    remSum += cardList.get(i).eContriRem;
                    String[] creditMems = cardList.get(i).eCdtMembs.split(",", 0);
                    if (Arrays.asList(creditMems).contains(String.valueOf(profileUser.id))) {
                    } else {
                        pending.add(cardList.get(i));
                    }
                    ContriEvents.add(cardList.get(i));
                }
            }
            remainingAmount.setText("" + remSum + ".00");
            pendingContri.setAdapter(new Adapter_Pending_Wallet(Wallet.this, R.layout.list_pending_contri, pending, profileUser.team));
        }else {
            pendingContri.setAdapter(new Adapter_Pending_Wallet(Wallet.this, R.layout.list_pending_contri, pending, profileUser.team));
            Toast.makeText(Wallet.this, "No events", Toast.LENGTH_LONG).show();
        }

        remainingAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contri = new Intent(Wallet.this,Remaining_Contri.class);
                startActivity(contri);
            }
        });
    }
}
