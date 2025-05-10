package com.likelion13.lucaus_api.service.stamp;

import com.likelion13.lucaus_api.common.exception.ErrorCode;
import com.likelion13.lucaus_api.common.exception.GeneralHandler;
import com.likelion13.lucaus_api.domain.entity.stamp.RewardPw;
import com.likelion13.lucaus_api.domain.entity.stamp.StampBoard;
import com.likelion13.lucaus_api.domain.entity.stamp.StampBoardBoothMapping;
import com.likelion13.lucaus_api.domain.repository.stamp.RewardPwRepository;
import com.likelion13.lucaus_api.domain.repository.stamp.StampBoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class GetRewardServiceImpl implements GetRewardService {
    private final StampBoardRepository stampBoardRepository;

    private final RewardPwRepository rewardPwRepository;

    public String getReward(String userId, Integer type, Integer degree, String pw){
        StampBoard stampBoard = stampBoardRepository.findStampBoardByUserIdAndType(userId, type);

        long clearCount = stampBoard.getStampBoardBoothMappings()
                .stream().filter(StampBoardBoothMapping::getIsClear).count();

        // 도장 개수 불충분
        if(type == 1){
            if ((degree == 1 && clearCount < 3) || (degree == 2 && clearCount < 5) || (degree == 3 && clearCount < 7)) {
                throw new GeneralHandler(ErrorCode.NOT_ENOUGH_STAMP);
            }
        } else if (type == 2){
            if ((degree == 1 && clearCount < 2) || (degree == 2 && clearCount < 4) || (degree == 3 && clearCount < 6)) {
                throw new GeneralHandler(ErrorCode.NOT_ENOUGH_STAMP);
            }
        }



        // 잘못된 수령 차수
        RewardPw rewardPw = rewardPwRepository.findByTypeAndDegree(type,degree);
        if (rewardPw == null) {
            throw new GeneralHandler(ErrorCode.INVALID_DEGREE);
        }
        String rightPw = rewardPw.getPw();

        if (!rightPw.equals(pw)) {
            throw new GeneralHandler(ErrorCode.WRONG_PW);
        }

        switch (degree) {
            case 1:
                if (stampBoard.getFirstReward()) throw new GeneralHandler(ErrorCode.DUPLICATED_REWARD);
                break;
            case 2:
                if (stampBoard.getSecondReward()) throw new GeneralHandler(ErrorCode.DUPLICATED_REWARD);
                break;
            case 3:
                if (stampBoard.getThirdReward()) throw new GeneralHandler(ErrorCode.DUPLICATED_REWARD);
                break;
            default:
                throw new GeneralHandler(ErrorCode.INVALID_DEGREE);
        }
        stampBoard.getReward(degree);
        return "success";
    }
}
