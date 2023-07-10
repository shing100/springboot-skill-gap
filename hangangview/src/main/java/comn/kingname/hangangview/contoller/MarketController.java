package comn.kingname.hangangview.contoller;

import comn.kingname.hangangview.domain.MarketCode;
import comn.kingname.hangangview.dto.MinuteCandles;
import comn.kingname.hangangview.service.MarketService;
import comn.kingname.hangangview.service.TradeService;
import comn.kingname.hangangview.util.TelegramSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static comn.kingname.hangangview.contants.Constants.MARKET;
import static comn.kingname.hangangview.contants.Constants.UNIT_COUNT;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MarketController {

    private final MarketService marketService;
    private final TradeService tradeService;
    private final TelegramSender telegramSender;

    @GetMapping("/market/all")
    public List<MarketCode> getMarketAll() {
        log.info("GET /market/all");
        return marketService.getMarketAll();
    }

    @GetMapping("/market/coin")
    public List<MinuteCandles.Response> getMinuteCandles(MinuteCandles.Request request) {
        log.info("GET /market/coin");
        return marketService.getMinuteCandles(request);
    }
}
