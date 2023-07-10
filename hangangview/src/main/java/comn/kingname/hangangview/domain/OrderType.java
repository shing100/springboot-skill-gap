package comn.kingname.hangangview.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @ToString @AllArgsConstructor
public enum OrderType {
    BID("bid"), ASK("ask");

    private String type;
}
