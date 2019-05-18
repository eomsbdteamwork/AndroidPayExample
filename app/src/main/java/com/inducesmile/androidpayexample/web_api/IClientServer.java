package com.inducesmile.androidpayexample.web_api;

import com.inducesmile.androidpayexample.model.product_order_model.ProductOrder;
import com.inducesmile.androidpayexample.model.product_order_model.UserOrder;
import com.inducesmile.androidpayexample.model.products_model.Products;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IClientServer {
    @GET("products/getAllProducts")
    Call<Products> getALlProducts(@Query("api_key") String id);

    /*
        @POST("products/buy_now")
        @FormUrlEncoded
        Call<UserOrder> orderProduct(@Body ParamProductOrder paramProductOrder);
        */
    @POST("products/buy_now")
    Call<UserOrder> orderProduct(@Body ProductOrder productOrder);

}
