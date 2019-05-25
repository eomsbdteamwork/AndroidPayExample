package com.eomsbd.cutprice.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eomsbd.cutprice.R;
import com.eomsbd.cutprice.model.category_model.Datum1;
import com.squareup.picasso.Picasso;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewHolder> {


    Context context;



    List<Datum1> data;
    private LayoutInflater inflater;

    public CategoryRecyclerViewAdapter(Context context, List<Datum1> data) {
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public CategoryRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_list, viewGroup, false);
        return new CategoryRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryRecyclerViewHolder holder, final int i) {
        final Datum1 singleProduct = data.get(i);
        String image = "https://www.rokomari.com/static/new/img/electronics/computer.png";
        Picasso.get().load(image).into(holder.imageView);
        holder.textView.setText(singleProduct.getCategoriesName());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toasty.success(v.getContext(),""+holder.textView.getText().toString(),Toasty.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
