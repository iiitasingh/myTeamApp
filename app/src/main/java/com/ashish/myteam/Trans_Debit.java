package com.ashish.myteam;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import static com.ashish.myteam.SplashScreen.DbtTransList;



public class Trans_Debit extends Fragment {

    ListView transacListView;
    public Trans_Debit() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_trans_debit, container, false);
        transacListView = (ListView)v.findViewById(R.id.transDebitList);
        if (DbtTransList.isEmpty()) {
            transacListView.setAdapter(new Adapter_Transaction_List(getActivity(), R.layout.list_transaction, DbtTransList));
            Toast.makeText(getActivity(), "No Debit Transactions", Toast.LENGTH_LONG).show();
        } else {
            transacListView.setAdapter(new Adapter_Transaction_List(getActivity(), R.layout.list_transaction, DbtTransList));
        }
        return v;
    }

}
