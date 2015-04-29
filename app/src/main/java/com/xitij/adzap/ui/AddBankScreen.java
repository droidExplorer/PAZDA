package com.xitij.adzap.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.GsonBuilder;
import com.xitij.adzap.R;
import com.xitij.adzap.base.MyApplication;
import com.xitij.adzap.helpers.AppConstants;
import com.xitij.adzap.helpers.CallWebService;
import com.xitij.adzap.helpers.ComplexPreferences;
import com.xitij.adzap.helpers.PrefUtils;
import com.xitij.adzap.model.User;
import com.xitij.adzap.model.ViewInvoice;
import com.xitij.adzap.widget.CircleDialog;

import org.json.JSONObject;

public class AddBankScreen extends ActionBarActivity {

    private CircleDialog dialog;
    private ListView offerListView;
    private Toolbar toolbar;
    private ViewInvoice cuurentInvoice;
    private TextView txtSave;
    private View emptyView;
    private LinearLayout linearList,linearEmpty;
    private Spinner spAccountType;
    private static final String[] accType = new String[] {
            "Select", "Checking","Savings"
    };
    User currentUser;
    private EditText etBankName,etBankBranchName,etAccNo,etAccPersonName,etIFSCCode,etAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbank_screen);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbar_Title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        //toolbar.setNavigationIcon(R.drawable.icon_back_blue);
        toolbar_Title.setText("Add Bank Details");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initalizeViews();
        init();


    }

    private void initalizeViews(){
        spAccountType = (Spinner)findViewById(R.id.spAccountType);
        txtSave = (TextView)findViewById(R.id.txtSave);

        etBankName = (EditText)findViewById(R.id.etBankName);
        etBankBranchName = (EditText)findViewById(R.id.etBankBranchName);
        etAccNo = (EditText)findViewById(R.id.etAccNo);
        etAccPersonName = (EditText)findViewById(R.id.etAccPersonName);
        etIFSCCode = (EditText)findViewById(R.id.etIFSCCode);
        etAddress = (EditText)findViewById(R.id.etAddress);




        ArrayAdapter<String> spinadapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, accType);
        spAccountType.setAdapter(spinadapter);

        txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processSaveBank();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(AddBankScreen.this, "user_pref", 0);
        currentUser = complexPreferences.getObject("current_user", User.class);

    }

    private void processSaveBank(){


        try{

            int spPos = spAccountType.getSelectedItemPosition();

            JSONObject userobj = new JSONObject();
            userobj.put("ACNO",etAccNo.getText().toString().trim());
            userobj.put("AccountPersonName",etAccPersonName.getText().toString().trim());
            userobj.put("AccountType",accType[spPos]);
            userobj.put("Address",etAddress.getText().toString().trim());
            userobj.put("BankBranch",etBankBranchName.getText().toString().trim());
            userobj.put("BankId",0);
            userobj.put("BankName",etBankName.getText().toString().trim());
            userobj.put("CustId",currentUser.UserId);
            userobj.put("IFSCNo",etIFSCCode.getText().toString().trim());

            Log.e("Req bank", userobj.toString());

            dialog= new CircleDialog(AddBankScreen.this,0);
            dialog.setCancelable(false);
            dialog.show();

            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.SAVE_BANK_DETAILS, userobj, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jobj) {
                    dialog.dismiss();
                    String response = jobj.toString();
                    Log.e("Response bank: ", "" + response);

                    try{

                        JSONObject obj = new JSONObject(response);

                        if(obj.getString("Response").equalsIgnoreCase("0")){

                           /* User currentUser2 = new GsonBuilder().create().fromJson(response,User.class);
                            ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(AddBankScreen.this, "user_pref",0);
                            complexPreferences.putObject("current_user", currentUser2);
                            complexPreferences.commit();
*/

                            Toast.makeText(AddBankScreen.this,obj.getString("ResponseMsg").toString(),Toast.LENGTH_LONG).show();

                            Intent iCOnfirmSignUp = new Intent( AddBankScreen.this ,BankListScreen.class );
                            startActivity(iCOnfirmSignUp);
                            finish();
                        }

                        else {
                            Toast.makeText(AddBankScreen.this,"Error - "+obj.getString("ResponseMsg").toString(),Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {

                    }


                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    dialog.dismiss();
                    Log.e("error : ", error + "");
                    Toast.makeText(AddBankScreen.this,"Error - "+error.toString(),Toast.LENGTH_LONG).show();

                }
            });

            MyApplication.getInstance().addToRequestQueue(req);

        }catch(Exception e){

        }


    }








    private void init(){
            Typeface tf = PrefUtils.getTypeFaceCalibri(AddBankScreen.this);
          /*  etUname.setTypeface(tf);
            etPassword.setTypeface(tf);
            txtBtnLogin.setTypeface(tf);*/
    }




//end of main class
}
