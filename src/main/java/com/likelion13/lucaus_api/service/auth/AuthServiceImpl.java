package com.likelion13.lucaus_api.service.auth;

import com.likelion13.lucaus_api.domain.entity.stemp.StampBoard;
import com.likelion13.lucaus_api.domain.entity.stemp.User;
import com.likelion13.lucaus_api.domain.repository.stemp.UserRepository;
import com.likelion13.lucaus_api.dto.request.auth.LoginRequestDto;
import com.likelion13.lucaus_api.dto.request.auth.SignUpRequestDto;
import com.likelion13.lucaus_api.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public String signup(SignUpRequestDto request) {
        if (userRepository.existsById(request.getId())){
           throw new IllegalArgumentException("User already exists"); // 나중에 에러처리 바꾸기
        }
        User user = User.builder()
                .id(request.getId()) // 아이디
                .pw(passwordEncoder.encode(request.getPw())) // 비밀번호 해싱
                .name(request.getName()) // 이름
                .studentId(request.getStudentId()) // 학번
                .build();
        userRepository.save(user);

        return "success";
    }

    public String login(LoginRequestDto request) {
        User user = userRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!passwordEncoder.matches(request.getPw(), user.getPw())) {
            throw new IllegalArgumentException("Wrong password");// 나중에 에러처리 바꾸기
        }
        return jwtTokenProvider.createToken(user.getId());

    }
}
