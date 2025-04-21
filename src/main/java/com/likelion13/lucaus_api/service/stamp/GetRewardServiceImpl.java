package com.likelion13.lucaus_api.service.stamp;

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

        if ((degree == 1 && clearCount < 3) || (degree == 2 && clearCount < 5) || (degree == 3 && clearCount < 9)) {
            throw new IllegalStateException("도장이 충분하지 않습니다.");
        }

        RewardPw rewardPw = rewardPwRepository.findByDegree(degree);
        if (rewardPw == null) {
            throw new IllegalArgumentException("유효하지 않은 상품 차수입니다.");
        }
        String rightPw = rewardPw.getPw();

        if (!rightPw.equals(pw)) {
            return "비밀번호가 틀렸습니다.";
        }

        switch (degree) {
            case 1:
                if (stampBoard.getFirstReward()) throw new IllegalStateException("이미 1차 상품 수령했습니다.");
                break;
            case 2:
                if (stampBoard.getSecondReward()) throw new IllegalStateException("이미 2차 상품 수령했습니다.");
                break;
            case 3:
                if (stampBoard.getThirdReward()) throw new IllegalStateException("이미 3차 상품 수령했습니다.");
                break;
            default:
                throw new IllegalArgumentException("존재하지 않는 수령 차수입니다.");
        }
        stampBoard.getReward(degree);
        return "success";
    }
}
