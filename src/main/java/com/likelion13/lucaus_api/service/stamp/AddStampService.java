package com.likelion13.lucaus_api.service.stamp;

import com.likelion13.lucaus_api.domain.entity.stamp.StampBoard;

public interface AddStampService {
    String addStamp(String userId, Integer type, Integer stampBoothId, String pw);
}
