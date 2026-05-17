package com.cometchat.javasampleapp.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chat.core.CometChat;
import com.cometchat.chat.exceptions.CometChatException;
import com.cometchat.javasampleapp.R;
import com.cometchat.javasampleapp.AppUtils;
import com.cometchat.chat.models.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout inputLayout;
    private ProgressBar progressBar;
    private TextInputEditText uid;
    private TextInputEditText etPassword;
    private RelativeLayout parentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        uid = findViewById(R.id.etUID);
        etPassword = findViewById(R.id.etPassword);
        progressBar = findViewById(R.id.loginProgress);
        inputLayout = findViewById(R.id.inputUID);
        parentView = findViewById(R.id.parent_view);
        uid.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE) {
                handleLogin();
            }
            return true;
        });

        etPassword.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE) {
                handleLogin();
            }
            return true;
        });


        findViewById(R.id.tvSignIn).setOnClickListener(view -> {
            handleLogin();
        });
        setUpUI();

    }

    private void handleLogin() {
        String uidText = uid.getText().toString().trim();
        String passwordText = etPassword.getText().toString().trim();

        if (uidText.isEmpty()) {
            uid.setError("Enter UID");
            return;
        }
        if (passwordText.isEmpty()) {
            etPassword.setError("Enter Password");
            return;
        }

        uid.setError(null);
        etPassword.setError(null);
        progressBar.setVisibility(View.VISIBLE);
        inputLayout.setEndIconVisible(false);
        login(uidText);
    }

    private void login(String uid) {
        CometChatUIKit.login(uid, new CometChat.CallbackListener<User>() {
            @Override
            public void onSuccess(User user) {
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                AppUtils.fetchDefaultObjects();
                finishAffinity();
            }

            @Override
            public void onError(CometChatException e) {
                inputLayout.setEndIconVisible(true);
                findViewById(R.id.loginProgress).setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void createUser(View view) {
        startActivity(new Intent(LoginActivity.this, CreateUserActivity.class));
    }

    private void setUpUI() {
        if (AppUtils.isNightMode(this)) {
            AppUtils.changeTextColorToWhite(this, findViewById(R.id.tvTitle));
            AppUtils.changeTextColorToWhite(this, findViewById(R.id.tvDes2));
            uid.setTextColor(getResources().getColor(R.color.white));
            etPassword.setTextColor(getResources().getColor(R.color.white));
            parentView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.app_background_dark)));
        } else {
            AppUtils.changeTextColorToBlack(this, findViewById(R.id.tvTitle));
            AppUtils.changeTextColorToBlack(this, findViewById(R.id.tvDes2));
            uid.setTextColor(getResources().getColor(R.color.black));
            etPassword.setTextColor(getResources().getColor(R.color.black));
            parentView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.app_background)));
        }
    }

}