package com.likelion13.lucaus_api.service.stamp;

import com.likelion13.lucaus_api.domain.entity.stamp.StampBoard;
import com.likelion13.lucaus_api.dto.response.stamp.StampBoardInfoResponseDto;

import java.util.List;

public interface StampService {
   List<StampBoardInfoResponseDto> getStampBoards(String userId);
}
