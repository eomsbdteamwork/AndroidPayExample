package com.eomsbd.cutprice.activity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.eomsbd.cutprice.model.product_order_model.ProductOrder;
import com.eomsbd.cutprice.model.product_order_model.UserOrder;
import com.eomsbd.cutprice.R;
import com.eomsbd.cutprice.web_api.IClientServer;
import com.eomsbd.cutprice.web_api.RetrofitService;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.eomsbd.cutprice.activity.RegistrationActivity.MyPREFERENCES;

public class OrderNowActivity extends AppCompatActivity{

    IClientServer iClientServer;
    private RelativeLayout rlayout;
    private Animation animation;
    private EditText fullname, email, phone, quantity, password, address, note;
    private Button orderNow;
    SharedPreferences sharedPreferences;
    private AlertDialog.Builder alertDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_now);



        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        fullname = findViewById(R.id.fullname_editTextId);
        email = findViewById(R.id.email_editTextId);
        phone = findViewById(R.id.phone_editTextId);
        password = findViewById(R.id.password_editTextId);
        quantity = findViewById(R.id.product_Quantity_editTextId);
        address = findViewById(R.id.address_editTextId);
        note = findViewById(R.id.note_editTextId);
        orderNow = findViewById(R.id.submit_btn1);
        //data from OrderNowActivity sharedprefrence
        String clientName1 = sharedPreferences.getString( "client_name", "" );
        String clientemail1 = sharedPreferences.getString( "client_email", "" );
        String clientpassword1 = sharedPreferences.getString( "client_password", "" );

       //data from login activity
        String clientName = getIntent().getExtras().getString("client_name");
        String clientemail = getIntent().getExtras().getString("client_email");
        String clientpassword = getIntent().getExtras().getString("client_password");

        if (clientemail1==null && clientName1==null && clientpassword1==null ){
            fullname.setText(clientName);
            email.setText(clientemail);
            password.setText(clientpassword);
        }else{
            fullname.setText(clientName1);
            email.setText(clientemail1);
            password.setText(clientpassword1);
        }



        iClientServer = RetrofitService.getRetrofitInstance().create(IClientServer.class);
        orderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidity()) {
                } else {
                    dialogBox();
                    getProductFromApi();
                }


            }
        });


    }
    public boolean isEmpty(EditText text){
        CharSequence t=text.getText().toString();
        return TextUtils.isEmpty( t );
    }


    public boolean checkValidity() {
        View focusView = null;
        boolean cancel = false;
        if (isEmpty(fullname)) {
            // focusView=userName;
            cancel = true;
            fullname.setError("Enter a valid name");
        }
        if (isEmpty(email)) {
            // focusView = pass;
            cancel = true;
            email.setError("Enter a valid email address");
        }
        if (isEmpty(phone)) {
            // focusView = pass;
            cancel = true;
            phone.setError("Enter a valid Phone Number");
        }
        if (isEmpty(quantity)) {
            // focusView = pass;
            cancel = true;
            quantity.setError("Enter a product Quantity");
        }
        if (isEmpty(password)) {
            // focusView = pass;
            cancel = true;
            password.setError("Enter a valid password");
        }
        if (isEmpty(address)) {
            // focusView = pass;
            cancel = true;
            address.setError("Enter a valid address");
        }
        if (isEmpty(note)) {
            // focusView = pass;
            cancel = true;
            note.setError("Enter a valid Note");
        }
        return cancel;
    }

    public void getProductFromApi() {
        String api_key = "Cutprice@987";
        String productId = getIntent().getExtras().getString("productId");
        String productSellingPrice = getIntent().getExtras().getString("sellingPrice");
        double quanti = Double.parseDouble(quantity.getText().toString());
        String name = fullname.getText().toString();
        String emaill = email.getText().toString();
        String addres = address.getText().toString();
        String number = phone.getText().toString();
        String pass = password.getText().toString();
        String comment = note.getText().toString();
        double product=Double.parseDouble(productSellingPrice);
        String quanSell=String.valueOf(quanti* product);

        ProductOrder paramProductOrder = new ProductOrder();
        paramProductOrder.setApiKey(api_key);
        paramProductOrder.setProductId(productId);
        paramProductOrder.setQuantity(String.valueOf(quanti));
        paramProductOrder.setName(name);
        paramProductOrder.setEmail(emaill);
        paramProductOrder.setAddress(addres);
        paramProductOrder.setPassword(pass);
        paramProductOrder.setMobile(number);
        paramProductOrder.setComment(comment);
        paramProductOrder.setProductSellingPrice(quanSell);

        Call<UserOrder> paramProductOrderCall = iClientServer.orderProduct(paramProductOrder);
        paramProductOrderCall.enqueue(new Callback<UserOrder>() {
            @Override
            public void onResponse(Call<UserOrder> call, Response<UserOrder> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserOrder order = response.body();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("client_email",  email.getText().toString());
                    editor.putString("client_name", fullname.getText().toString());
                    editor.putString("client_password",password.getText().toString());
                    editor.apply();
                    editor.commit();
                    Toast.makeText(OrderNowActivity.this, "Success : " + order.getSuccess(), Toast.LENGTH_LONG).show();
                } else {

                    Toast.makeText(OrderNowActivity.this, "Not Success !", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserOrder> call, Throwable t) {
                Toast.makeText(OrderNowActivity.this, "message !" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


  public void dialogBox(){
      alertDialogBuilder = new AlertDialog.Builder(OrderNowActivity.this);

      //For Setting Title

      alertDialogBuilder.setTitle(R.string.title_text);

      //for setting message
      alertDialogBuilder.setMessage(R.string.message_text);
      //fot setting Icon
      alertDialogBuilder.setIcon(R.mipmap.success);

      alertDialogBuilder.setPositiveButton(" হ্যাঁ", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {

              //exit
              finish();
          }
      });

      alertDialogBuilder.setNeutralButton(" হোম", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {


              Intent login = new Intent(OrderNowActivity.this , ShoppingActivity.class);
              login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
              startActivity(login);


          }
      });

      alertDialogBuilder.setNegativeButton("না", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {

              dialog.cancel();
          }
      });

      AlertDialog alertDialog = alertDialogBuilder.create();
      alertDialog.show();
  }




}

