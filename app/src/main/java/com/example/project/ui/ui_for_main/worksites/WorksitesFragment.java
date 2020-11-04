package com.example.project.ui.ui_for_main.worksites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.project.R;

public class WorksitesFragment extends Fragment {

    private WorksitesViewModel worksitesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        worksitesViewModel =
                new ViewModelProvider(this).get(WorksitesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_worksites, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        worksitesViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}