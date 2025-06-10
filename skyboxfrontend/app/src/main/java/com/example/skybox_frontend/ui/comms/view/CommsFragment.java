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

    private TextView tabChat, tabVoice, tabAda;
    private View tabIndicator;
    private LinearLayout tabRow;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comms, container, false);

        tabChat = view.findViewById(R.id.tab_chat);
        tabVoice = view.findViewById(R.id.tab_voice);
        tabAda = view.findViewById(R.id.tab_ada);
        tabIndicator = view.findViewById(R.id.tab_indicator);
        tabRow = view.findViewById(R.id.tab_row);

        setupTabClicks();

        // Default selection
        selectTab(tabChat, new ChatFragment(), false);

        return view;
    }

    private void setupTabClicks() {
        tabChat.setOnClickListener(v -> selectTab(tabChat, new ChatFragment(), true));
        tabVoice.setOnClickListener(v -> selectTab(tabVoice, new VoiceFragment(), true));
        tabAda.setOnClickListener(v -> selectTab(tabAda, new ChatBotFragment(), true));
    }

    private void selectTab(@NonNull TextView selectedTab, Fragment fragmentToShow, boolean animateUnderline) {
        updateTabTextColors(selectedTab);
        animateUnderline(selectedTab, animateUnderline);

        getChildFragmentManager().beginTransaction()
                .replace(R.id.comms_fragment_container, fragmentToShow)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    private void updateTabTextColors(@NonNull TextView activeTab) {
        // Reset all colors
        tabChat.setTextColor(getResources().getColor(R.color.secondary, requireContext().getTheme()));
        tabVoice.setTextColor(getResources().getColor(R.color.secondary, requireContext().getTheme()));
        tabAda.setTextColor(getResources().getColor(R.color.secondary, requireContext().getTheme()));

        // Highlight selected
        activeTab.setTextColor(getResources().getColor(R.color.primary_text, requireContext().getTheme()));
    }

    private void animateUnderline(TextView selectedTab, boolean shouldAnimateUnderline) {
        ViewTreeObserver.OnPreDrawListener listener = new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                selectedTab.getViewTreeObserver().removeOnPreDrawListener(this);

                int[] rowLocation = new int[2];
                tabRow.getLocationOnScreen(rowLocation);

                int[] tabLocation = new int[2];
                selectedTab.getLocationOnScreen(tabLocation);

                int startMargin = tabLocation[0] - rowLocation[0];
                int width = selectedTab.getWidth();

                ConstraintLayout rootLayout = requireView().findViewById(R.id.comms_root);
                ConstraintSet set = new ConstraintSet();
                set.clone(rootLayout);

                if (shouldAnimateUnderline) {
                    Transition transition = new ChangeBounds();
                    transition.setDuration(150);
                    TransitionManager.beginDelayedTransition(rootLayout, transition);
                }

                set.constrainWidth(tabIndicator.getId(), width);
                set.connect(tabIndicator.getId(), ConstraintSet.START, rootLayout.getId(), ConstraintSet.START, startMargin);
                set.applyTo(rootLayout);

                return true;
            }
        };

        selectedTab.getViewTreeObserver().addOnPreDrawListener(listener);
    }
}
