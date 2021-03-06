package com.eomsbd.cutprice.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.eomsbd.cutprice.Constant;
import com.eomsbd.cutprice.adapters.CheckRecyclerViewAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.eomsbd.cutprice.helpers.MySharedPreference;
import com.eomsbd.cutprice.helpers.SimpleDividerItemDecoration;
import com.eomsbd.cutprice.model.products_model.Datum;
import com.eomsbd.cutprice.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    private static final String TAG = CheckoutActivity.class.getSimpleName();


    private RecyclerView checkRecyclerView;

    private TextView subTotal;

    private double mSubTotal = 0;
    List<Datum> products;
    CheckRecyclerViewAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle("Over Cart");
        products = new ArrayList<>();
        subTotal = findViewById(R.id.sub_total);

        checkRecyclerView = findViewById(R.id.checkout_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CheckoutActivity.this);
        checkRecyclerView.setLayoutManager(linearLayoutManager);
        checkRecyclerView.setHasFixedSize(true);
        checkRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(CheckoutActivity.this));

        // get content of cart
        MySharedPreference mShared = new MySharedPreference(CheckoutActivity.this);

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        Datum[] addCartProducts = gson.fromJson(mShared.retrieveProductFromCart(), Datum[].class);
        List<Datum> productList = convertObjectArrayToListObject(addCartProducts);

        mAdapter = new CheckRecyclerViewAdapter(CheckoutActivity.this, productList);
        checkRecyclerView.setAdapter(mAdapter);

        mSubTotal = getTotalPrice(productList);
        subTotal.setText("Subtotal excluding tax and shipping: " + mSubTotal + " bdt");

        mAdapter.notifyDataSetChanged();
        Button shoppingButton = findViewById(R.id.shopping);
        assert shoppingButton != null;
        shoppingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shoppingIntent = new Intent(CheckoutActivity.this, ShoppingActivity.class);
                startActivity(shoppingIntent);
            }
        });

        Button checkButton = findViewById(R.id.checkout);
        assert checkButton != null;
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent paymentIntent = new Intent(CheckoutActivity.this, PayPalCheckoutActivity.class);
                paymentIntent.putExtra("TOTAL_PRICE", mSubTotal);
                startActivity(paymentIntent);
            }
        });
    }

    private List<Datum> convertObjectArrayToListObject(Datum[] allProducts) {
        List<Datum> mProduct = new ArrayList<Datum>();
        Collections.addAll(mProduct, allProducts);
        return mProduct;
    }

    private int returnQuantityByProductName(String productName, List<Datum> mProducts) {
        int quantityCount = 0;
        for (int i = 0; i < mProducts.size(); i++) {
            Datum pObject = mProducts.get(i);
            if (pObject.getProductName().trim().equals(productName.trim())) {
                quantityCount++;
            }
        }
        return quantityCount;
    }

    public double getTotalPrice(List<Datum> mProducts) {
        double totalCost = 0;
        for (int i = 0; i < mProducts.size(); i++) {
            Datum pObject = mProducts.get(i);
            double productPrice = Double.parseDouble(pObject.getProductSellingPrice().trim());
            totalCost = totalCost + productPrice;
        }
        return totalCost;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(Constant.DELETE))
            deleteItem(item.getItemId());
        return true;
    }

    public void deleteItem(int position) {
        products.remove(position);

        checkRecyclerView.clearFocus();
        for (Datum data : products) {
            getTotalPrice(products);
        }
        mAdapter.notifyDataSetChanged();

    }

}