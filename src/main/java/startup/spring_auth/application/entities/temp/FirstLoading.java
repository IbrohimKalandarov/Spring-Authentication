package startup.spring_auth.application.entities.temp;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import startup.spring_auth.application.entities.Address;
import startup.spring_auth.application.entities.User;
import startup.spring_auth.application.entities.enums.Neighborhood;
import startup.spring_auth.application.entities.enums.Role;
import startup.spring_auth.application.entities.enums.Street;
import startup.spring_auth.application.repository.AddressRepository;
import startup.spring_auth.application.repository.UserRepository;

@Component
@RequiredArgsConstructor
@Transactional
public class
FirstLoading implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(FirstLoading.class);
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddl;

    /**
     * Test uchun 3 ta foydalanuvchi qo'shiladi
     * 1-Super Admin
     * 2-Admin
     * 3-User
     */
    @Override
    public void run(String... args) {
        if (ddl.equals("create-drop") || ddl.equals("create")) {
            logger.info("SUPER ADMIN CREATED SUCCESSFULLY User tel number: {}", userRepository.save(User.builder().firstName("sa").lastName("sa").fatherName("sa").password(passwordEncoder.encode("sa123")).phoneNumber("998900000000").otpCode(null).role(Role.SUPER_ADMIN).enabled(true)
                    .address(addressRepository.save(Address.builder().homeNumber("01A").neighborhood(Neighborhood.TINCHLIK).street(Street.MEVAZOR).build())).build()).getUsername()
            );

            logger.info("ADMIN CREATED SUCCESSFULLY User tel number: {}", userRepository.save(User.builder().firstName("a").lastName("a").fatherName("a").password(passwordEncoder.encode("a123")).phoneNumber("998901111111").otpCode(null).role(Role.ADMIN).enabled(true)
                    .address(addressRepository.save(Address.builder().homeNumber("02A").neighborhood(Neighborhood.TINCHLIK).street(Street.MEVAZOR).build())).build()).getUsername()
            );

            logger.info("USER CREATED SUCCESSFULLY User tel number: {}", userRepository.save(User.builder().firstName("u").lastName("u").fatherName("u").password(passwordEncoder.encode("u123")).phoneNumber("998902222222").otpCode(null).role(Role.USER).enabled(true)
                    .address(addressRepository.save(Address.builder().homeNumber("03A").neighborhood(Neighborhood.TINCHLIK).street(Street.MEVAZOR).build())).build()).getUsername()
            );
        }
    }
}
