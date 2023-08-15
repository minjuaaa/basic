package com.plete.basic.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SiteUser create(String email, String password) {
        SiteUser user = new SiteUser();
        user.setEmail(email);
        //비밀번호는 암호화 저장
        //BCryptPasswordEncoder 객체를 직접 생성하여 사용하지 않고
        // 빈으로 등록한 PasswordEncoder 객체를 주입받아 사용
        user.setPassword(passwordEncoder.encode(password));
        this.userRepository.save(user);
        return user;
    }
}