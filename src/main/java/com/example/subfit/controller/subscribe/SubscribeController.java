package com.example.subfit.controller.subscribe;

import com.example.subfit.dto.subscribe.SubscribeRequestDto;
import com.example.subfit.dto.subscribe.SubscribeResponseDto;
import com.example.subfit.service.subscribe.SubscribeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/subscribe")
public class SubscribeController {

    private final SubscribeService subscribeService;

    @Autowired
    public SubscribeController(SubscribeService subscribeService) {
        this.subscribeService = subscribeService;
    }

    @PostMapping("/add")
    public SubscribeResponseDto addSubscription(@ModelAttribute SubscribeRequestDto requestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        return subscribeService.addSubscription(requestDto, userId);
    }

    @GetMapping("/list")
    public List<SubscribeResponseDto> getSubscriptions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        return subscribeService.getSubscriptions(userId);
    }
    @GetMapping("/list/{subscriptionId}")
    public SubscribeResponseDto getSubscriptionDetail(@PathVariable Long subscriptionId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        return subscribeService.getSubscriptionDetail(subscriptionId, userId);
    }
    @GetMapping("/summary")
    public Map<String, Object> getSubscriptionSummary() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        return subscribeService.getSubscriptionSummary(userId);
    }

    @PutMapping("/update/{subscriptionId}")
    public SubscribeResponseDto updateSubscription(@PathVariable Long subscriptionId, @ModelAttribute SubscribeRequestDto requestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        return subscribeService.updateSubscription(subscriptionId, requestDto, userId);
    }

    @DeleteMapping("/delete/{subscriptionId}")
    public void deleteSubscription(@PathVariable Long subscriptionId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        subscribeService.deleteSubscription(subscriptionId, userId);
    }
}
