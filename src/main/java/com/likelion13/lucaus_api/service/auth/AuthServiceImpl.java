package com.likelion13.lucaus_api.service.auth;

import com.likelion13.lucaus_api.common.exception.ErrorCode;
import com.likelion13.lucaus_api.common.exception.GeneralHandler;
import com.likelion13.lucaus_api.domain.entity.stamp.*;
import com.likelion13.lucaus_api.domain.repository.stamp.*;
import com.likelion13.lucaus_api.domain.repository.user.UserRepository;
import com.likelion13.lucaus_api.dto.request.auth.LoginRequestDto;
import com.likelion13.lucaus_api.dto.request.auth.SignUpRequestDto;
import com.likelion13.lucaus_api.security.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    private final StampBoardRepository stampBoardRepository;
    private final StampBoothRepository stampBoothRepository;
    private final StampBoardBoothMappingRepository stampBoardBoothMappingRepository;

    @Transactional
    public String signup(SignUpRequestDto request) {

        // 아이디 정책 4자리 이상
        if(request.getId().length() < 4) {
            throw new GeneralHandler(ErrorCode.INVALID_ID_LENGTH);
        }

        // 중복 id 생성 불가
        if (userRepository.existsById(request.getId())){
           throw new GeneralHandler(ErrorCode.DUPLICATED_ID);
        }

        // pw 정책 4자리 이상
        if(request.getPw().length() < 4) {
            throw new GeneralHandler(ErrorCode.INVALID_PW_LENGTH);
        }

        // 이름 공백 불가
        if(request.getName().isEmpty()) {
            throw new GeneralHandler(ErrorCode.INVALID_NAME_LENGTH);
        }

        String studentId = request.getStudentId();
        // 학번 정책
        if(studentId.length() < 8) {
            throw new GeneralHandler(ErrorCode.INVALID_STUDENTNUM_LENGTH);
        }
        if (!studentId.matches("\\d+")) {
            throw new GeneralHandler(ErrorCode.INVALID_STUDENTNUM_FORMAT);
        }

        // 유저 등록
        User user = User.builder()
                .id(request.getId())
                .pw(passwordEncoder.encode(request.getPw()))
                .name(request.getName())
                .studentId(request.getStudentId())
                .build();
        userRepository.save(user);

        for (int type : List.of(1, 2)) {
            // type별 boothId 필터링
            List<StampBooth> filteredBooths = stampBoothRepository.findAll().stream()
                    .filter(booth -> {
                        Long boothId = booth.getId();
                        if (type == 1) return boothId >= 1 && boothId <= 9;
                        if (type == 2) return boothId >= 11 && boothId <= 19;
                        return false;
                    })
                    .toList();

            StampBoard stampBoard = stampBoardRepository.save(
                    StampBoard.builder()
                            .type(type)
                            .firstReward(false)
                            .secondReward(false)
                            .thirdReward(false)
                            .user(user)
                            .build()
            );

            List<StampBoardBoothMapping> mappings = filteredBooths.stream()
                    .map(booth -> StampBoardBoothMapping.builder()
                            .stampBoard(stampBoard)
                            .stampBooth(booth)
                            .isClear(false)
                            .build())
                    .toList();

            stampBoardBoothMappingRepository.saveAll(mappings);
        }

        return "success";
    }

    public String login(LoginRequestDto request) {
        User user = userRepository.findById(request.getId())
                .orElseThrow(() -> new GeneralHandler(ErrorCode.NOT_FOUND_USER));
        if (!passwordEncoder.matches(request.getPw(), user.getPw())) {
            throw new GeneralHandler(ErrorCode.WRONG_PW);
        }
        return jwtTokenProvider.createToken(user.getId());

    }
}
