package comn.kingname.hangangview.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import comn.kingname.hangangview.properties.UpbitProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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

    public HttpHeaders getJwtTokenHeaders(HashMap<String, String> params) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        ArrayList<String> queryElements = new ArrayList<>();
        for(Map.Entry<String, String> entity : params.entrySet()) {
            queryElements.add(entity.getKey() + "=" + entity.getValue());
        }

        String queryString = String.join("&", queryElements.toArray(new String[0]));

        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(queryString.getBytes("UTF-8"));
        String queryHash = String.format("%0128x", new java.math.BigInteger(1, md.digest()));

        Algorithm algorithm = Algorithm.HMAC256(properties.getSecretKey());
        String jwtToken = JWT.create()
                .withClaim("access_key", properties.getAccessKey())
                .withClaim("nonce", UUID.randomUUID().toString())
                .withClaim("query_hash", queryHash)
                .withClaim("query_hash_alg", "SHA512")
                .sign(algorithm);

        String authenticationToken = "Bearer " + jwtToken;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", authenticationToken);
        return headers;
    }

    private String creatAuthenticationToken() {
        return "Bearer " + createToken();
    }
}
