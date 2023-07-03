package comn.kingname.hangangview.exception;

import lombok.Getter;

@Getter
public class TradeException extends RuntimeException {

    private final TradeErrorCode errorCode;
    private final String message;

    public TradeException(String message, TradeErrorCode errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }

    public TradeException(TradeErrorCode errorCode) {
        this.message = errorCode.getMessage();
        this.errorCode = errorCode;
    }
}
