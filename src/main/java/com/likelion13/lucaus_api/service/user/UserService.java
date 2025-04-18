package com.likelion13.lucaus_api.service.user;

import com.likelion13.lucaus_api.domain.entity.stemp.User;
import com.likelion13.lucaus_api.dto.response.user.UserInfoResponseDto;

public interface UserService {
    UserInfoResponseDto getCurrentUserInfo(String userId);

}
