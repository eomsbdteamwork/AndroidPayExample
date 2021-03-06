package com.eomsbd.cutprice.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eomsbd.cutprice.OnBackPressed;
import com.eomsbd.cutprice.R;

import com.eomsbd.cutprice.activity.ShoppingActivity;
import com.eomsbd.cutprice.adapters.CategoryRecyclerViewAdapter;
import com.eomsbd.cutprice.helpers.SpacesItemDecoration;
import com.eomsbd.cutprice.model.category_model.Category;
import com.eomsbd.cutprice.web_api.IClientServer;
import com.eomsbd.cutprice.web_api.RetrofitService;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment implements OnBackPressed {

    RecyclerView recyclerView;
    IClientServer iClientServer;
    CategoryRecyclerViewAdapter categoryRecyclerViewAdapter;
    AlertDialog.Builder alertDialogBuilder;

    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        iClientServer = RetrofitService.getRetrofitInstance().create(IClientServer.class);
        getCategoryData(view);
        return view;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getActivity(), ShoppingActivity.class));
    }


    public void getCategoryData(final View view) {

        recyclerView = view.findViewById(R.id.category_list);

        Call<Category> categoryCall = iClientServer.getSubmenu();
        categoryCall.enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                Category category = response.body();
                if (response.isSuccessful() && category != null) {
                    categoryRecyclerViewAdapter = new CategoryRecyclerViewAdapter(getContext(), category.getData());

                    //Use a LinearLayoutManager with default vertical orientation//
                    GridLayoutManager mGrid = new GridLayoutManager(getContext(), 2);
                    recyclerView.setLayoutManager(mGrid);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.addItemDecoration(new SpacesItemDecoration(2, 12, false));


                    //Set the Adapter to the RecyclerView//
                    recyclerView.setAdapter(categoryRecyclerViewAdapter);


                }

            }


            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                final android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(getActivity());
                alertDialog.setIcon(R.drawable.wifi);
                alertDialog.setMessage(R.string.message_text2)
                        .setCancelable(true).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //your custom toast
                    }
                }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).create().show();


            }

        });
    }
}

