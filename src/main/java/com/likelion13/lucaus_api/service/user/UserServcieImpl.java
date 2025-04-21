package com.likelion13.lucaus_api.service.user;

import com.likelion13.lucaus_api.common.exception.ErrorCode;
import com.likelion13.lucaus_api.common.exception.GeneralHandler;
import com.likelion13.lucaus_api.domain.entity.stamp.User;
import com.likelion13.lucaus_api.domain.repository.user.UserRepository;
import com.likelion13.lucaus_api.dto.response.user.UserInfoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServcieImpl implements UserService {
    private final UserRepository userRepository;

    public UserInfoResponseDto getCurrentUserInfo(String userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new GeneralHandler(ErrorCode.NOT_FOUND_USER));

        return new UserInfoResponseDto(
                user.getName(),
                user.getStudentId()
        );
    }
}
