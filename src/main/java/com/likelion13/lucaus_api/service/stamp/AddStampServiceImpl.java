package com.likelion13.lucaus_api.service.stamp;

import com.likelion13.lucaus_api.common.exception.ErrorCode;
import com.likelion13.lucaus_api.common.exception.GeneralHandler;
import com.likelion13.lucaus_api.domain.entity.stamp.StampBoard;
import com.likelion13.lucaus_api.domain.entity.stamp.StampBoardBoothMapping;
import com.likelion13.lucaus_api.domain.repository.stamp.StampBoardBoothMappingRepository;
import com.likelion13.lucaus_api.domain.repository.stamp.StampBoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AddStampServiceImpl implements AddStampService {
    private final StampBoardRepository stampBoardRepository;

    private final StampBoardBoothMappingRepository stampBoardBoothMappingRepository;

    public String addStamp(String userId, Integer type, Integer stampBoothId, String pw){
        StampBoard stampBoard = stampBoardRepository.findStampBoardByUserIdAndType(userId, type);

        StampBoardBoothMapping mapping = stampBoardBoothMappingRepository
                .findByStampBoardAndStampBoothId(stampBoard, Long.valueOf(stampBoothId));
        if (mapping == null) {
            throw new GeneralHandler(ErrorCode.NOT_FOUND_BOOTH);
        }
        // 비밀번호 비교
        if (!mapping.getStampBooth().getPw().equals(pw)) {
            throw new GeneralHandler(ErrorCode.WRONG_PW);
        }

        // 중복 도장
        if (mapping.getIsClear()) {
            throw new GeneralHandler(ErrorCode.DUPLICATED_STAMP);
        }

        mapping.addStamp(); // 도장 찍기
        return "도장이 성공적으로 찍혔습니다.";
    }
}
