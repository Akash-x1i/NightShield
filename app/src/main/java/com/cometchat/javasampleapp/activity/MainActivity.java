package com.cometchat.javasampleapp.activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import com.bumptech.glide.Glide;
import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.shared.cometchatuikit.UIKitSettings;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.chat.core.CometChat;
import com.cometchat.chat.exceptions.CometChatException;
import com.cometchat.javasampleapp.AppConstants;
import com.cometchat.javasampleapp.AppUtils;
import com.cometchat.javasampleapp.BuildConfig;
import com.cometchat.javasampleapp.R;
import com.cometchat.chat.models.User;
import com.google.android.material.card.MaterialCardView;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
public class MainActivity extends AppCompatActivity {
    private AppCompatImageView ivLogo;
    private AppCompatTextView tvCometChat;
    private LinearLayout parentView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parentView = findViewById(R.id.parent_view);
        Utils.setStatusBarColor(this, getResources().getColor(android.R.color.white));
        UIKitSettings uiKitSettings = new UIKitSettings.UIKitSettingsBuilder()
                .setRegion(AppConstants.REGION)
                .setAppId(AppConstants.APP_ID)
                .setAuthKey(AppConstants.AUTH_KEY)
                .subscribePresenceForAllUsers()
                .build();
        CometChatUIKit.init(this, uiKitSettings, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String s) {
                CometChat.setDemoMetaInfo(getAppMetadata());
                if (CometChatUIKit.getLoggedInUser() != null) {
                    AppUtils.fetchDefaultObjects();
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    finish();
                } else {
                    AppUtils.fetchSampleUsers(new CometChat.CallbackListener<List<User>>() {
                        @Override
                        public void onSuccess(List<User> users) {
                        }
                        @Override
                        public void onError(CometChatException e) {
                        }
                    });
                }
            }
            @Override
            public void onError(CometChatException e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.login).setOnClickListener(view -> startActivity(new Intent(MainActivity.this, LoginActivity.class)));
        setUpUI();
    }
    private void login(String uid) {
        CometChatUIKit.login(uid, new CometChat.CallbackListener<User>() {
            @Override
            public void onSuccess(User user) {
                AppUtils.fetchDefaultObjects();
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                finish();
            }
            @Override
            public void onError(CometChatException e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setUpUI() {
        if (AppUtils.isNightMode(this)) {
            Utils.setStatusBarColor(this, ContextCompat.getColor(this, R.color.app_background_dark));
            parentView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.app_background_dark)));
        } else {
            Utils.setStatusBarColor(this, getResources().getColor(R.color.app_background));
            parentView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.app_background)));
        }
    }
    private JSONObject getAppMetadata() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", getResources().getString(R.string.app_name));
            jsonObject.put("type", "sample");
            jsonObject.put("version", BuildConfig.VERSION_NAME);
            jsonObject.put("bundle", BuildConfig.APPLICATION_ID);
            jsonObject.put("platform", "android");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
    public void createUser(View view) {
        startActivity(new Intent(this, CreateUserActivity.class));
    }
}
