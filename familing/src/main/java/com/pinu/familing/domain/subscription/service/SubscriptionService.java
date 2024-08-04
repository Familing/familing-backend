package com.pinu.familing.domain.subscription.service;

import com.pinu.familing.domain.subscription.dto.SubscriptionResponse;
import com.pinu.familing.domain.subscription.entity.Subscription;
import com.pinu.familing.domain.subscription.repository.SubscriptionRepository;
import com.pinu.familing.global.error.CustomException;
import com.pinu.familing.global.error.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    //준형님은 바로 return 하시는 걸 더 좋아하시나요?
    public List<SubscriptionResponse> sendSubscriptions() {
        List<Subscription> subscriptions = subscriptionRepository.findAll();

        //저장된 구독목록의 개수가 3이하일 경우 에러발생 (이부분 필요없나요?
        if (subscriptions.size() < 3) {
            throw new CustomException(ExceptionCode.ERROR_SUBSCRIPTION_LIST);
        }

        return subscriptions.stream()
                .map(subscription -> new SubscriptionResponse(subscription))
                .collect(Collectors.toList());

    }
}
