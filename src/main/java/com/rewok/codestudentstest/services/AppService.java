package com.rewok.codestudentstest.services;

import com.rewok.codestudentstest.models.MyUser;
import com.rewok.codestudentstest.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class AppService {
    private UserRepository repository;
    private PasswordEncoder encoder;



    public void addUser(MyUser user){
        user.setPassword(encoder.encode(user.getPassword()));
        repository.save(user);
    }

    public void changePassword(String username, String currentPassword, String newPassword) {
        // Найдите пользователя по имени
        MyUser user = repository.findByName(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Проверьте, что текущий пароль пользователя совпадает с введенным текущим паролем
        if (!encoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Incorrect current password");
        }

        // Закодируйте новый пароль и установите его для пользователя
        String encodedNewPassword = encoder.encode(newPassword);
        user.setPassword(encodedNewPassword);

        // Сохраните обновленного пользователя в базе данных
        repository.save(user);
    }
}
