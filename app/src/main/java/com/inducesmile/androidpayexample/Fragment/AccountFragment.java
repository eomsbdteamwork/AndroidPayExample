package com.inducesmile.androidpayexample.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inducesmile.androidpayexample.OnBackPressed;
import com.inducesmile.androidpayexample.R;
import com.inducesmile.androidpayexample.ShoppingActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment implements OnBackPressed {


    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getActivity(), ShoppingActivity.class));
    }
}
