package com.likelion13.lucaus_api.service.user;

import com.likelion13.lucaus_api.domain.entity.stemp.User;
import com.likelion13.lucaus_api.domain.repository.user.UserRepository;
import com.likelion13.lucaus_api.dto.response.user.UserInfoResponseDto;
import com.likelion13.lucaus_api.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServcieImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final HttpServletRequest request;

    public UserInfoResponseDto getCurrentUserInfo(String userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new IllegalStateException("User not found"));

        return new UserInfoResponseDto(
                user.getName(),
                user.getStudentId()
        );
    }
}
