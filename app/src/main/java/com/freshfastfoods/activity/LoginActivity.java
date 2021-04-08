package com.freshfastfoods.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.freshfastfoods.R;
import com.freshfastfoods.model.LoginUser;
import com.freshfastfoods.retrofit.APIClient;
import com.freshfastfoods.retrofit.GetResult;
import com.freshfastfoods.utils.CustPrograssbar;
import com.freshfastfoods.utils.SessionManager;
import com.freshfastfoods.utils.Utiles;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

import static com.freshfastfoods.utils.SessionManager.login;

public class LoginActivity extends AppCompatActivity implements GetResult.MyListener {

    @BindView(R.id.ed_username)
    EditText edUsername;
    @BindView(R.id.ed_password)
    EditText edPassword;
    SessionManager sessionManager;
    CustPrograssbar custPrograssbar;

    //pre-defined logins
    String[] emails = {
            "sagar@bharatmart.com", "shivam@bharatmart.com", "mihir@bharatmart.com", "harshil@bharatmart.com",
            "kartik@bharatmart.com", "jinesh@bharatmart.com", "shubh@bharatmart.com", "pruthvi@bharatmart.com",
            "divyesh@bharatmart.com", "dhruvil@bharatmart.com"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(LoginActivity.this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_singlechoice, emails);
        AutoCompleteTextView acTextView = (AutoCompleteTextView) findViewById(R.id.ed_username);
        acTextView.setAdapter(adapter);
        acTextView.setThreshold(1);

        //Set Default Password
        edPassword.setText("admin");

    }
    @OnClick({R.id.btn_login, R.id.btn_sign, R.id.txt_forgotpassword})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                if (validation()) {
                    loginUser();
                }
                break;
            case R.id.btn_sign:
                startActivity(new Intent(LoginActivity.this, SingActivity.class));
                finish();
                break;
            case R.id.txt_forgotpassword:
                startActivity(new Intent(LoginActivity.this, ForgotActivity.class));
                break;
            default:
                break;
        }
    }
    private void loginUser() {
        custPrograssbar.prograssCreate(LoginActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", edUsername.getText().toString());
            jsonObject.put("password", edPassword.getText().toString());
            jsonObject.put("imei", Utiles.getIMEI(LoginActivity.this));
            JsonParser jsonParser = new JsonParser();

            Call<JsonObject> call = APIClient.getInterface().getLogin((JsonObject) jsonParser.parse(jsonObject.toString()));
            GetResult getResult = new GetResult();
            Log.e("errorrr", String.valueOf(getResult));
            getResult.setMyListener(this);
            getResult.callForLogin(call, "1");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public boolean validation() {
        if (edUsername.getText().toString().isEmpty()) {
            edUsername.setError("Enter Mobile No");
            return false;
        }
        if (edPassword.getText().toString().isEmpty()) {
            edPassword.setError("Enter Password");
            return false;
        }
        return true;
    }
    @Override
    public void callback(JsonObject result, String callNo) {
        try {

            custPrograssbar.closePrograssBar();
            Gson gson = new Gson();
            LoginUser response = gson.fromJson(result.toString(), LoginUser.class);

            Toast.makeText(LoginActivity.this, "" + response.getResponseMsg(), Toast.LENGTH_LONG).show();
            if (response.getResult().equals("true")) {
                sessionManager.setUserDetails("", response.getUser());
                Log.e("errors"," --> "+response.getUser().toString());
                sessionManager.setBooleanData(login,true);
                OneSignal.sendTag("userId", response.getUser().getId());
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
            }
        }
        catch (Exception e) {
            Log.e("error"," --> "+e.toString());
        }
    }
}
