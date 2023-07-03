package comn.kingname.hangangview.web;

import comn.kingname.hangangview.domain.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebClientInvoker {
    private final WebClient webClient;

    public void post(String url, Message messageVo) {
        webClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(messageVo)
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(ret -> {log.info(ret);})
        ;

    }
}
