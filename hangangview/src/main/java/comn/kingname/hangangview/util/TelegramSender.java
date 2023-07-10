package comn.kingname.hangangview.util;

import comn.kingname.hangangview.domain.Message;
import comn.kingname.hangangview.properties.TelegramProperties;
import comn.kingname.hangangview.web.WebClientInvoker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramSender {

    private final TelegramProperties telegramProperties;
    private final WebClientInvoker webClientInvoker;

    public void sendMessage(String msg) {
        send(telegramProperties.getBotToken(), telegramProperties.getChatId(), "[LG] ".concat(msg));
    }

    public void send(String botToken, String chatId, String msg) {
        log.debug("{},{},{}", botToken, chatId, msg);
        String sendUrl = telegramProperties.getSendUrl().replace("{BOT_TOKEN}", botToken);
        Message message = new Message(chatId, msg);

        webClientInvoker.post(sendUrl, message);
    }
}
