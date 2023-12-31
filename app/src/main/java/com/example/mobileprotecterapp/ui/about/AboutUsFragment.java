package com.example.mobileprotecterapp.ui.about;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mobileprotecterapp.databinding.FragmentAboutBinding;

public class AboutUsFragment extends Fragment {

    private FragmentAboutBinding binding;

    @SuppressLint("SetJavaScriptEnabled")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AboutUsViewModel dashboardViewModel =
                new ViewModelProvider(this).get(AboutUsViewModel.class);

        binding = FragmentAboutBinding.inflate(inflater, container, false);

        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.loadUrl("file:///android_asset/aboutUs.html");

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}