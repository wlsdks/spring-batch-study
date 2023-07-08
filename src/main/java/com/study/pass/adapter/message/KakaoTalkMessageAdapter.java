package com.study.pass.adapter.message;

import com.study.pass.config.KakaoTalkMessageConfig;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class KakaoTalkMessageAdapter {

    private final WebClient webClient; //gradle에 webflux 의존성 주입 필요

    public KakaoTalkMessageAdapter(KakaoTalkMessageConfig config) {
        webClient = WebClient.builder()
                .baseUrl(config.getHost())
                .defaultHeaders(h -> {
                    h.setBearerAuth(config.getToken());
                    h.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                }).build();

    }

    public boolean sendKakaoTalkMessage(final String uuid, final String text) {
        KakaoTalkMessageResponse response = webClient.post().uri("/v1/api/talk/friends/message/default/send")
                .body(BodyInserters.fromValue(new KakaoTalkMessageRequest(uuid, text)))
                .retrieve()
                .bodyToMono(KakaoTalkMessageResponse.class)
                .block();

        // response가 성공이면 type을 바꿔준다. 성공: true, 실패: false
        if (response == null || response.getSuccessfulReceiverUuids() == null) {
            return false;

        }
        return response.getSuccessfulReceiverUuids().size() > 0;

    }

}
