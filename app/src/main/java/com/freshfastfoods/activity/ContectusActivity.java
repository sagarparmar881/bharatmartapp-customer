package com.freshfastfoods.activity;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.freshfastfoods.MyApplication;
import com.freshfastfoods.R;
import com.freshfastfoods.utils.SessionManager;



import butterknife.BindView;
import butterknife.ButterKnife;
import io.customerly.Customerly;


import static com.freshfastfoods.utils.SessionManager.contactUs;

public class ContectusActivity extends BaseActivity {
    @BindView(R.id.txt_contac)
    TextView txtContac;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contectus);

        ButterKnife.bind(this);
        sessionManager = new SessionManager(this);


        Customerly.openSupport(ContectusActivity.this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txtContac.setText(Html.fromHtml(sessionManager.getStringData(contactUs), Html.FROM_HTML_MODE_COMPACT));
        } else {
            txtContac.setText(Html.fromHtml(sessionManager.getStringData(contactUs)));
        }
    }

 
}
