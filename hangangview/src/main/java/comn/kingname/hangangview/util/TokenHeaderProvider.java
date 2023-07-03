package comn.kingname.hangangview.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import comn.kingname.hangangview.properties.UpbitProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TokenHeaderProvider {
    private final UpbitProperties properties;

    public String createToken() {
        Algorithm algorithm = Algorithm.HMAC256(properties.getSecretKey());
        return JWT.create()
                .withClaim("access_key", properties.getAccessKey())
                .withClaim("nonce", UUID.randomUUID().toString())
                .sign(algorithm);
    }

    public HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Authorization", creatAuthenticationToken());
        return headers;
    }

    private String creatAuthenticationToken() {
        return "Bearer " + createToken();
    }
}
