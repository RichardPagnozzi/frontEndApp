package com.example.skybox_frontend.ui.comms.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.transition.ChangeBounds;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.example.skybox_frontend.R;

public class CommsFragment extends Fragment {

    // Tab elements for Chat, Voice, and ADA bot
    private TextView tabChat, tabVoice, tabAda;

    // Visual indicator below the selected tab
    private View tabIndicator;

    // Parent layout for the tab row
    private LinearLayout tabRow;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comms, container, false);

        // Initialize UI elements
        tabChat = view.findViewById(R.id.tab_chat);
        tabVoice = view.findViewById(R.id.tab_voice);
        tabAda = view.findViewById(R.id.tab_ada);
        tabIndicator = view.findViewById(R.id.tab_indicator);
        tabRow = view.findViewById(R.id.tab_row);

        // Set up click listeners for tab switching
        setupTabClicks();

        // Set default selection to Chat tab on launch
        selectTab(tabChat, new ChatFragment(), false);

        return view;
    }

    // Assign click listeners to each tab
    private void setupTabClicks() {
        tabChat.setOnClickListener(v -> selectTab(tabChat, new ChatFragment(), true));
        tabVoice.setOnClickListener(v -> selectTab(tabVoice, new VoiceFragment(), true));
        tabAda.setOnClickListener(v -> selectTab(tabAda, new ChatBotFragment(), true));
    }

    // Switch fragment and update tab UI state
    private void selectTab(@NonNull TextView selectedTab, Fragment fragmentToShow, boolean animateUnderline) {
        updateTabTextColors(selectedTab);              // Highlight the selected tab
        animateUnderline(selectedTab, animateUnderline); // Animate tab indicator to the new position

        // Replace inner fragment with the selected one
        getChildFragmentManager().beginTransaction()
                .replace(R.id.comms_fragment_container, fragmentToShow)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    // Update text color to show which tab is active
    private void updateTabTextColors(@NonNull TextView activeTab) {
        // Reset all tabs to inactive color
        tabChat.setTextColor(getResources().getColor(R.color.secondary, requireContext().getTheme()));
        tabVoice.setTextColor(getResources().getColor(R.color.secondary, requireContext().getTheme()));
        tabAda.setTextColor(getResources().getColor(R.color.secondary, requireContext().getTheme()));

        // Set active tab text color
        activeTab.setTextColor(getResources().getColor(R.color.primary_text, requireContext().getTheme()));
    }

    // Animate the underline indicator under the selected tab
    private void animateUnderline(TextView selectedTab, boolean shouldAnimateUnderline) {
        ViewTreeObserver.OnPreDrawListener listener = new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                // Remove listener to prevent repeated calls
                selectedTab.getViewTreeObserver().removeOnPreDrawListener(this);

                // Get screen position of tab row and selected tab
                int[] rowLocation = new int[2];
                tabRow.getLocationOnScreen(rowLocation);

                int[] tabLocation = new int[2];
                selectedTab.getLocationOnScreen(tabLocation);

                // Calculate start margin and width for indicator alignment
                int startMargin = tabLocation[0] - rowLocation[0];
                int width = selectedTab.getWidth();

                ConstraintLayout rootLayout = requireView().findViewById(R.id.comms_root);
                ConstraintSet set = new ConstraintSet();
                set.clone(rootLayout);

                // Optionally animate the change in bounds
                if (shouldAnimateUnderline) {
                    Transition transition = new ChangeBounds();
                    transition.setDuration(150); // animation duration in ms
                    TransitionManager.beginDelayedTransition(rootLayout, transition);
                }

                // Set new width and position of the indicator
                set.constrainWidth(tabIndicator.getId(), width);
                set.connect(tabIndicator.getId(), ConstraintSet.START, rootLayout.getId(), ConstraintSet.START, startMargin);
                set.applyTo(rootLayout);

                return true;
            }
        };

        // Register the pre-draw listener
        selectedTab.getViewTreeObserver().addOnPreDrawListener(listener);
    }
}
