package comn.kingname.hangangview.exception;

import comn.kingname.hangangview.util.TelegramSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class TradeExceptionHandler {

    private final TelegramSender telegramSender;

    @ExceptionHandler(Exception.class)
    public void handleException(Exception e) {
        log.error(getErrorStackTrace(e));
        telegramSender.sendMessage(getErrorStackTrace(e));
    }

    @ExceptionHandler(TradeException.class)
    public void handleTradeException(TradeException e) {
        telegramSender.sendMessage(e.getMessage());
        log.error(getErrorStackTrace(e));
    }

    public static String getErrorStackTrace(Exception e) {
        StringBuilder sb = new StringBuilder();
        sb.append(e.getMessage()).append(System.lineSeparator());
        StackTraceElement[] stackTrace = e.getStackTrace();

        for (int i = 0; i < Math.min(10, stackTrace.length); i++) {
            sb.append(String.format("%s %s.%s:%s", stackTrace[i].getFileName(), stackTrace[i].getClassName(),
                    stackTrace[i].getMethodName(), stackTrace[i].getLineNumber())).append(System.lineSeparator());
        }

        return sb.toString();
    }
}
