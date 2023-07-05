package comn.kingname.hangangview.service;

import comn.kingname.hangangview.dto.MinuteCandles;
import comn.kingname.hangangview.util.Strategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TradeService {

    private final MarketService marketService;
    private final Strategy strategy;

    public void getTrade(MinuteCandles.Request request) {

    }
}
