package com.example.moviecollection;

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

import com.example.moviecollection.databinding.FragmentRegistrationBinding;

public class RegistrationFragment extends Fragment {

    private FragmentRegistrationBinding binding; // ViewBinding для доступа к UI
    private DatabaseHelper dbHelper;             // SQLite helper для пользователей

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Привязываем макет к binding и возвращаем корневой View
        binding = FragmentRegistrationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new DatabaseHelper(requireContext()); // Инициализация помощника базы данных

        // Обработка кнопки "Зарегистрироваться"
        binding.buttonRegister.setOnClickListener(v -> {
            String username = binding.editTextNewLogin.getText().toString().trim();
            String password = binding.editTextNewPassword.getText().toString().trim();
            String confirmPassword = binding.editTextConfirmPassword.getText().toString().trim();

            // Проверка на пустые поля
            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                Toast.makeText(getContext(), "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            // Проверка совпадения паролей
            if (!password.equals(confirmPassword)) {
                Toast.makeText(getContext(), "Пароли не совпадают", Toast.LENGTH_SHORT).show();
                return;
            }

            // Регистрация пользователя
            if (dbHelper.registerUser(username, password)) {
                Toast.makeText(getContext(), "Регистрация прошла успешно", Toast.LENGTH_SHORT).show();

                // Возврат к экрану входа
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_registrationFragment_to_loginFragment);
            } else {
                Toast.makeText(getContext(), "Пользователь с таким логином уже существует", Toast.LENGTH_SHORT).show();
            }
        });

        // Обработка ссылки "Уже есть аккаунт? Войти"
        binding.textGoToLogin.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_registrationFragment_to_loginFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}