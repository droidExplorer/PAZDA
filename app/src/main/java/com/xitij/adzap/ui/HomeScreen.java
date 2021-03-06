package com.xitij.adzap.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.gson.GsonBuilder;
import com.xitij.adzap.R;
import com.xitij.adzap.adapters.OfferListAdapter;
import com.xitij.adzap.helpers.AppConstants;
import com.xitij.adzap.helpers.CallWebService;
import com.xitij.adzap.helpers.ComplexPreferences;
import com.xitij.adzap.helpers.PrefUtils;
import com.xitij.adzap.model.CheckBalance;
import com.xitij.adzap.model.Offers;
import com.xitij.adzap.model.User;
import com.xitij.adzap.receiver.AppLocationService;
import com.xitij.adzap.widget.CircleDialog;

import org.json.JSONObject;



public class HomeScreen extends ActionBarActivity implements View.OnClickListener{
    AppLocationService appLocationService;
    private ViewGroup menuEarnCoins,menuRewards,menuFriends,menuHistory;
    private  ImageView settings,imgProfile;
    private TextView txtCoin,txtINR;
    private CircleDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        appLocationService = new AppLocationService(HomeScreen.this);

        imgProfile = (ImageView)findViewById(R.id.imgProfile);
        setupUI();

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(HomeScreen.this, "user_pref", 0);
        User currentUser1 = complexPreferences.getObject("current_user", User.class);
        Glide.with(HomeScreen.this).load(AppConstants.BASE_URL_PROFILE_IMAGE + currentUser1.Image).thumbnail(0.1f).into(imgProfile);


        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeScreen.this,ProfileScreen.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupBalance();


    }






    private void setupBalance(){
      /*  dialog = new CircleDialog(HomeScreen.this, 0);
        dialog.setCancelable(false);
        dialog.show();*/

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(HomeScreen.this, "user_pref", 0);
        User currentUser = complexPreferences.getObject("current_user", User.class);



        new CallWebService(AppConstants.CHECK_BALANCE + currentUser.UserId, CallWebService.TYPE_JSONOBJECT) {

            @Override
            public void response(String response) {
             //   dialog.dismiss();
                Log.e("response", response.toString());

                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString("Response").equalsIgnoreCase("0")) {
                        CheckBalance chkBalance = new GsonBuilder().create().fromJson(response, CheckBalance.class);


                        PrefUtils.setcityID(HomeScreen.this, String.valueOf(chkBalance.CityId));

                        // 1 Rs = 15 Coins
                        if(chkBalance.ClosingBal == null){
                            txtCoin.setText("0");
                            txtINR.setText("₹ 0");
                        }else{

                            txtCoin.setText(chkBalance.ClosingBal);

                            Float coins = Float.valueOf(chkBalance.ClosingBal);
                            Float temp_rupees = coins /AppConstants.coinRate;
                            String final_rate = String.format("%.2f", temp_rupees);
                            txtINR.setText("₹ "+final_rate);


                        }


                    } else {
                     //   Toast.makeText(HomeScreen.this, "Error - " + obj.getString("ResponseMsg").toString(), Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {

                }



            }

            @Override
            public void error(VolleyError error) {
                Toast.makeText(HomeScreen.this, "Network Error, Please Try again.", Toast.LENGTH_LONG).show();
             //   dialog.dismiss();
            }
        }.start();
    }

    private void setupUI() {


        txtCoin = (TextView)findViewById(R.id.txtCoin);
        txtINR = (TextView)findViewById(R.id.txtINR);

        settings = (ImageView)findViewById(R.id.settings);
        menuHistory = (ViewGroup)findViewById(R.id.menuFour);
        menuEarnCoins = (ViewGroup)findViewById(R.id.menuOne);
        menuFriends = (ViewGroup)findViewById(R.id.menuThree);
        menuRewards = (ViewGroup)findViewById(R.id.menuTwo);

        menuHistory.setOnClickListener(this);
        menuEarnCoins.setOnClickListener(this);
        menuFriends.setOnClickListener(this);
        menuRewards.setOnClickListener(this);

        setRewards();
        setEarnCoins();
        setFriends();
        setHistory();


        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent set = new Intent(HomeScreen.this,SettingsScreen.class);
                startActivity(set);
            }
        });



    }

    private void setRewards() {

        ImageView img = (ImageView)menuRewards.findViewById(R.id.itemHomeImage);
        TextView txt = (TextView)menuRewards.findViewById(R.id.itemHomeText);
        img.setImageResource(R.drawable.rewards);
        txt.setText("REWARDS");


    }

    private void setEarnCoins() {

        ImageView img = (ImageView)menuEarnCoins.findViewById(R.id.itemHomeImage);
        TextView txt = (TextView)menuEarnCoins.findViewById(R.id.itemHomeText);
        img.setImageResource(R.drawable.earn_coins);
        txt.setText("EARN COINS");

    }


    private void setFriends() {

        ImageView img = (ImageView)menuFriends.findViewById(R.id.itemHomeImage);
        TextView txt = (TextView)menuFriends.findViewById(R.id.itemHomeText);
        img.setImageResource(R.drawable.friends);
        txt.setText("FRIENDS");
    }

    private void setHistory() {

        ImageView img = (ImageView)menuHistory.findViewById(R.id.itemHomeImage);
        TextView txt = (TextView)menuHistory.findViewById(R.id.itemHomeText);
        img.setImageResource(R.drawable.history);
        txt.setText("HISTORY");

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.menuOne:
                Intent offer = new Intent(HomeScreen.this,GetOffersScreen.class);
                startActivity(offer);
                break;
            case R.id.menuTwo:
                Intent reward = new Intent(HomeScreen.this,RewardsScreen.class);
                startActivity(reward);
                break;
            case R.id.menuThree:
                Intent frnd = new Intent(HomeScreen.this,FriendsScreen.class);
                startActivity(frnd);
                break;
            case R.id.menuFour:
                Intent history = new Intent(HomeScreen.this,HistoryScreen.class);
                startActivity(history);
                break;
        }


    }
}
