package com.likelion13.lucaus_api.service.visitor;

import com.likelion13.lucaus_api.common.exception.ErrorCode;
import com.likelion13.lucaus_api.common.exception.GeneralHandler;
import com.likelion13.lucaus_api.domain.entity.visitor.VisitorsNum;
import com.likelion13.lucaus_api.domain.repository.visitor.VisitorNumRepository;
import com.likelion13.lucaus_api.dto.request.VisitorNumRequestDto;
import com.likelion13.lucaus_api.dto.response.visitor.VisitorNumResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VisitorNumServiceImpl implements VisitorNumService {
    private final VisitorNumRepository visitorNumRepository;

    public VisitorNumResponseDto getNewestVisitorNum() {
        VisitorsNum latest = visitorNumRepository.findLatestVisitorNum();
        if (latest == null) {
            return new VisitorNumResponseDto(null, 0); // 기본값
        }
        return new VisitorNumResponseDto(latest.getUpdatedAt(), latest.getVisitorsNum());
    }

    public String postNewestVisitorNum(VisitorNumRequestDto requestDto) {

        if(requestDto.getVisitorNum() == null) {
            throw new GeneralHandler(ErrorCode.INVALID_VISITOR_NUM);
        }

        VisitorsNum newEntry = VisitorsNum.builder()
                .updatedAt(LocalDateTime.now())
                .visitorsNum(requestDto.getVisitorNum())
                .build();

        visitorNumRepository.save(newEntry);
        return "success";
    }
}
