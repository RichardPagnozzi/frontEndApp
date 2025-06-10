package com.example.skybox_frontend;

import android.os.Bundle;
import android.util.TypedValue;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import com.example.skybox_frontend.databinding.ActivityMainBinding;
import com.example.skybox_frontend.ui.comms.view.CommsFragment;
import com.example.skybox_frontend.ui.login.LoginFragment;
import com.example.skybox_frontend.ui.map.MapFragment;
import com.example.skybox_frontend.ui.operations.OperationsFragment;
import com.example.skybox_frontend.ui.trivia.TriviaFragment;
import com.example.skybox_frontend.utilities.SidebarController;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    // <editor-fold desc="Constants">
    private static final int SIDEBAR_MAX_WIDTH_DP = 200;
    private static final int ANIMATION_DURATION_MS = 250;
    // </editor-fold>

    // <editor-fold desc="Members">
    private ActivityMainBinding binding;
    private SidebarController sidebarController;
    private MaterialButton selectedButton = null;
    // </editor-fold>

    // <editor-fold desc="Lifecycle">
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sidebarController = new SidebarController(binding.sideMenu, binding.fragmentContainerView, dpToPx(SIDEBAR_MAX_WIDTH_DP), ANIMATION_DURATION_MS);
        sidebarController.attachTouchGesture(binding.rootLayout);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(binding.fragmentContainerView.getId(), new LoginFragment()).commit();
        }
    }
    // </editor-fold>

    // <editor-fold desc="Login">
    public void onLoginSuccess()
    {
        setupDrawerListeners();

        // Open the drawer
        sidebarController.animateOpen();

        // Set Comms as the selected button
        updateSelectedButton(binding.navComms); // ✅ assumes you've bound this via ViewBinding

        // Load the Comms fragment
        getSupportFragmentManager().beginTransaction()
                .replace(binding.fragmentContainerView.getId(), new CommsFragment())
                .commit();
    }
    // </editor-fold>

    // <editor-fold desc="Drawer Navigation">
    private void setupDrawerListeners() {
        binding.navComms.setOnClickListener(v -> {
            updateSelectedButton(binding.navComms);
            switchFragment(new CommsFragment());
        });
        binding.navMap.setOnClickListener(v -> {
            updateSelectedButton(binding.navMap);
            switchFragment(new MapFragment());
        });
        binding.navOperations.setOnClickListener(v -> {
            updateSelectedButton(binding.navOperations);
            switchFragment(new OperationsFragment());
        });
        binding.navTrivia.setOnClickListener(v -> {
            updateSelectedButton(binding.navTrivia);
            switchFragment(new TriviaFragment());
        });
    }

    private void switchFragment(androidx.fragment.app.Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(binding.fragmentContainerView.getId(), fragment).commit();
    }

    private void updateSelectedButton(MaterialButton newSelected) {
        if (selectedButton != null) {
            // Reset old button style
            selectedButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.surface));
            selectedButton.setTextColor(ContextCompat.getColor(this, R.color.primary_text));
            selectedButton.setIconTint(ContextCompat.getColorStateList(this, R.color.primary_text));
        }

        // Apply accent to new selection
        newSelected.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.accent));
        newSelected.setTextColor(ContextCompat.getColor(this, R.color.text_on_accent));
        newSelected.setIconTint(ContextCompat.getColorStateList(this, R.color.text_on_accent));

        selectedButton = newSelected;
    }

    // </editor-fold>

    // <editor-fold desc="Utility">
    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
    // </editor-fold>
}
