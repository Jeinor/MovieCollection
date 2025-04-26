package com.example.moviecollection;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.moviecollection.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        // Получаем логин из SharedPreferences или любой сессии
        String username = SessionManager.getInstance(requireContext()).getUsername();
        binding.textUserLogin.setText("Логин: " + username);

        // Обработка кнопки выхода
        binding.buttonLogout.setOnClickListener(v -> {
            SessionManager.getInstance(requireContext()).logout();
            requireActivity().finish();
            startActivity(new Intent(requireContext(), LoginActivity.class));
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}