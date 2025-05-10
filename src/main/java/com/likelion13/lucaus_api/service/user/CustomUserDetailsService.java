package com.likelion13.lucaus_api.service.user;

import com.likelion13.lucaus_api.domain.entity.stamp.User;
import com.likelion13.lucaus_api.domain.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

// 스프링시큐리티의 userDetail의 구현체
// jwt 토큰에서 userId 가져옴
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(
                user.getId(), user.getPw(), List.of() // 권한 없으면 빈 리스트
        );
    }
}
