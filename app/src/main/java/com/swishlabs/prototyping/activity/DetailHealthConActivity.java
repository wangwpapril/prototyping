package com.swishlabs.prototyping.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.swishlabs.prototyping.MyApplication;
import com.swishlabs.prototyping.R;

public class DetailHealthConActivity extends ActionBarActivity {

    private String mName, mDescription, mSymptoms, mPrevention;

    private TextView mTitleTv, mDesTv, mSymTv, mPreTv;
    private ImageView mBackIv;

    public DetailHealthConActivity instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_detail_health_con);
        instance = this;
        mName = getIntent().getStringExtra("name");
        mDescription = getIntent().getStringExtra("description");
        mSymptoms = getIntent().getStringExtra("symptoms");
        mPrevention = getIntent().getStringExtra("prevention");

        initialView();

    }


    protected void initialView(){
        mTitleTv = (TextView) findViewById(R.id.toolbar_title);
        mTitleTv.setText(mName);

        mDesTv = (TextView)findViewById(R.id.destination_content);
        mDesTv.setText(mDescription);

        mSymTv = (TextView)findViewById(R.id.destination_content2);
        mSymTv.setText(mSymptoms);

        mPreTv = (TextView)findViewById(R.id.destination_content3);
        mPreTv.setText(mPrevention);

        mBackIv = (ImageView)findViewById(R.id.title_back);
        mBackIv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

}
