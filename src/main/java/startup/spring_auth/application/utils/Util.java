package startup.spring_auth.application.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import startup.spring_auth.application.entities.User;
import startup.spring_auth.application.repository.UserRepository;

import java.util.Optional;
import java.util.Random;

/**
 * Barcha (class,service,repository,security ...) uchun ba'zida kerak bo'ladigan method yoki constantalar saqlash uchun maxsus util class.
 * <br><br>Created by Ibrohim on 20/05/2025.
 */

@Component
@RequiredArgsConstructor
public class Util {
    private final UserRepository userRepository;
    Random random = new Random();

    //   6 xonali son generatsiya qiladi.
    public Integer generateRandomNumber() {
        return random.nextInt(100000, 999999);
    }

    //    Hozirgi Muvofaqqiyatli Login qilgan Userni qaytarish metodi
    public Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        // Anonymous foydalanuvchi emasligini tekshiramiz
        if (authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();
        User user = userRepository.findByPhoneNumberFetchOtp(principal.toString()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return Optional.of(user);
    }

    //     numberni 6 xonalikka tekshiradi agar 6 xona bo'lsa true aks xolda false
    public boolean isSixDigit(Integer number) {
        return number != null && number >= 100000 && number <= 999999;
    }

}
