package com.cometchat.javasampleapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cometchat.javasampleapp.fragments.calls.CallLogsFragment;
import com.cometchat.javasampleapp.fragments.conversations.ConversationsFragment;
import com.cometchat.javasampleapp.fragments.groups.GroupsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;

import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.shared.resources.theme.CometChatTheme;
import com.cometchat.chatuikit.shared.resources.theme.Palette;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.chat.core.CometChat;
import com.cometchat.chat.exceptions.CometChatException;
import com.cometchat.javasampleapp.AppUtils;
import com.cometchat.javasampleapp.R;
import com.cometchat.javasampleapp.constants.StringConstants;

public class HomeActivity extends AppCompatActivity {

    private ImageView darkMode, lightMode, logout;
    private View parentView;
    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        darkMode = findViewById(R.id.dark_mode);
        lightMode = findViewById(R.id.light_mode);
        parentView = findViewById(R.id.parent_view);
        logout = findViewById(R.id.logout);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fab = findViewById(R.id.fab);

        setUpUI();

        fab.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, ComponentLaunchActivity.class);
            intent.putExtra("component", R.id.contacts);
            startActivity(intent);
        });

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_chats) {
                loadFragment(new ConversationsFragment());
                fab.setImageResource(R.drawable.compose);
                fab.setOnClickListener(v -> {
                    Intent intent = new Intent(HomeActivity.this, ComponentLaunchActivity.class);
                    intent.putExtra("component", R.id.contacts);
                    startActivity(intent);
                });
                return true;
            } else if (id == R.id.nav_groups) {
                loadFragment(new GroupsFragment());
                fab.setImageResource(R.drawable.create_group);
                fab.setOnClickListener(v -> {
                    Intent intent = new Intent(HomeActivity.this, ComponentLaunchActivity.class);
                    intent.putExtra("component", R.id.create_group);
                    startActivity(intent);
                });
                return true;
            } else if (id == R.id.nav_calls) {
                loadFragment(new CallLogsFragment());
                fab.setImageResource(R.drawable.call_log);
                fab.setOnClickListener(v -> {
                    // Action for new call, maybe open contacts list
                    Intent intent = new Intent(HomeActivity.this, ComponentLaunchActivity.class);
                    intent.putExtra("component", R.id.contacts);
                    startActivity(intent);
                });
                return true;
            }
            return false;
        });

        // Load default fragment
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_chats);
        }

        logout.setOnClickListener(view -> CometChatUIKit.logout(new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String s) {
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
                finishAffinity();
            }

            @Override
            public void onError(CometChatException e) {

            }
        }));

        darkMode.setOnClickListener(view -> toggleDarkMode());

        lightMode.setOnClickListener(view -> toggleDarkMode());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_search) {
            // Handle search
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpUI() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (AppUtils.isNightMode(this)) {
            AppUtils.changeIconTintToWhite(this, darkMode);
            AppUtils.changeIconTintToWhite(this, lightMode);
            AppUtils.changeIconTintToWhite(this, logout);
            Utils.setStatusBarColor(this, ContextCompat.getColor(this, R.color.app_background_dark));
            toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.app_background_dark));
            bottomNavigationView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.app_background_dark)));
            darkMode.setVisibility(View.GONE);
            lightMode.setVisibility(View.VISIBLE);
        } else {
            AppUtils.changeIconTintToWhite(this, darkMode);
            AppUtils.changeIconTintToWhite(this, lightMode);
            AppUtils.changeIconTintToWhite(this, logout);
            Utils.setStatusBarColor(this, ContextCompat.getColor(this, R.color.whatsapp_green));
            toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.whatsapp_green));
            bottomNavigationView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white)));
            darkMode.setVisibility(View.VISIBLE);
            lightMode.setVisibility(View.GONE);
        }
    }

    private void toggleDarkMode() {
        if (AppUtils.isNightMode(this)) {
            Palette.getInstance(this).mode(CometChatTheme.MODE.LIGHT);
            AppUtils.switchLightMode();
        } else {
            Palette.getInstance(this).mode(CometChatTheme.MODE.DARK);
            AppUtils.switchDarkMode();
        }
    }

    private void loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    private void handleIntent(String module) {
        Intent intent = new Intent(this, ComponentListActivity.class);
        intent.putExtra(StringConstants.MODULE, module);
        startActivity(intent);
    }
}