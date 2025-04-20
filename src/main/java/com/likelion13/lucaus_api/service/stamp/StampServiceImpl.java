package com.likelion13.lucaus_api.service.stamp;

import com.likelion13.lucaus_api.domain.entity.stamp.StampBoard;
import com.likelion13.lucaus_api.domain.repository.stamp.StampBoardRepository;
import com.likelion13.lucaus_api.dto.response.stamp.StampBoardInfoResponseDto;
import com.likelion13.lucaus_api.dto.response.stamp.StampBoothClearResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StampServiceImpl implements StampService {
    private final StampBoardRepository stampBoardRepository;

    public List<StampBoardInfoResponseDto> getStampBoards(String userId) {
        List<StampBoard> stampBoards = stampBoardRepository.findWithBoothMappingsByUserId(userId);

        return stampBoards.stream().map(sb -> new StampBoardInfoResponseDto(
                sb.getId(),
                sb.getType(),
                sb.getFirstReward(),
                sb.getSecondReward(),
                sb.getThirdReward(),
                sb.getStampBoardBoothMappings().stream()
                        .map(mapping -> new StampBoothClearResponseDto(
                                mapping.getStampBooth().getId(),
                                mapping.getIsClear()
                        )).toList()
        )).toList();
    }
}
