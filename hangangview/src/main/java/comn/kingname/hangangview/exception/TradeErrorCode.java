package comn.kingname.hangangview.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public enum TradeErrorCode {
    INTERNAL_SERVER_ERROR("서버에 오류가 발생했습니다."),
    UPBIT_SERVER_ERROR("업비트 서버에 오류가 발생했습니다."),
    TELEGRAM_SERVER_ERROR("텔레그램 서버에 오류가 발생했습니다."),
    INVALID_REQUEST("잘못된 요청입니다.");

    private final String message;
}
