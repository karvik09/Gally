package com.vikas.gally.activity;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.vikas.gally.constant.AppConstant;

public class BaseActivity extends AppCompatActivity implements AppConstant {

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
