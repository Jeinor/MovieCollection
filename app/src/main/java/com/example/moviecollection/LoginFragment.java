package com.example.moviecollection;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.moviecollection.databinding.FragmentLoginBinding;
import com.example.moviecollection.databinding.FragmentMovieListBinding;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding; // ViewBinding для доступа к элементам разметки
    private DatabaseHelper dbHelper;      // Помощник для работы с SQLite

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Привязываем макет к binding и возвращаем корневой View
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new DatabaseHelper(requireContext()); // Инициализация помощника

        // Обработка кнопки "Войти"
        binding.buttonLogin.setOnClickListener(v -> {
            String username = binding.editTextLogin.getText().toString().trim();
            String password = binding.editTextPassword.getText().toString().trim();

            // Проверка на пустые поля
            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                Toast.makeText(getContext(), "Введите логин и пароль", Toast.LENGTH_SHORT).show();
                return;
            }

            // Проверка авторизации
            if (dbHelper.validateUser(username, password)) {
                Toast.makeText(getContext(), "Успешный вход", Toast.LENGTH_SHORT).show();

                // Запуск основной активности после успешного входа
                Intent intent = new Intent(requireContext(), MainActivity.class);
                startActivity(intent);
                requireActivity().finish(); // Закрываем LoginActivity, чтобы не вернуться назад

            } else {
                Toast.makeText(getContext(), "Неверный логин или пароль", Toast.LENGTH_SHORT).show();
            }
        });

        // Переход к фрагменту регистрации
        binding.textGoToRegister.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_loginFragment_to_registrationFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}