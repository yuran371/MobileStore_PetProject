package utlis;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.experimental.UtilityClass;
import utlis.jdbc.PropertiesUtil;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

@UtilityClass
public class TokenHandler {

    private static final String PBKDF_2_WITH_HMAC_SHA_256 = "PBKDF2WithHmacSHA256";
    private static final SecretKey KEY;

    static {
        try {
            KEY = getKeyFromPassword(PropertiesUtil.getProperty("token.password"), PropertiesUtil.getProperty("token" +
                                                                                                              ".salt"));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }

    private static SecretKey getKeyFromPassword(String password, String salt) throws InvalidKeySpecException,
            NoSuchAlgorithmException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(PBKDF_2_WITH_HMAC_SHA_256);
        KeySpec keyspec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65530, 256);
        return new SecretKeySpec(factory.generateSecret(keyspec).getEncoded(), "HmacSHA256");
    }

    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .signWith(KEY)
                .compact();
    }

    public boolean checkTokenAuthenticity(String token) {
        try {
            Jwts.parser()
                    .verifyWith(KEY)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
