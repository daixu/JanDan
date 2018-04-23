package com.daixu.jandan.view.about;

import android.os.Bundle;
import android.view.View;

import com.daixu.jandan.R;
import com.daixu.jandan.base.BaseActivity;

public class AboutActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        findViewById(R.id.button).setOnClickListener(this);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                break;
            default:
                break;
        }
    }
}
