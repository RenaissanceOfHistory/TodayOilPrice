package today.oil.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import today.oil.model.User;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

/** 用户登录工具（JWT--Java Web Token）
 * @create: 2023/10/30
 * @Description:
 * @FileName: JWTUtil
 */
public class JWTUtil {
    /** 密钥 */
    private static final String SECRET = UUID.randomUUID().toString();

    private static final String CLAIM_USER_ID = "user_id";
    private static final String CLAIM_USERNAME = "username";
    private static final String CLAIM_USER_PASSWORD = "user_password";

    private JWTUtil() {}

    /** 将登录用户转换为Token
     * @Description:
     * @date: 2023/10/30
     * @param user 登录用户
     * @return: java.lang.String
     */
    public static String token(User user) {
        return JWT.create()
                .withClaim(CLAIM_USER_ID, user.getId())
                .withClaim(CLAIM_USERNAME, user.getUsername())
                .withClaim(CLAIM_USER_PASSWORD, user.getPassword())
                .withIssuedAt(new Date())   // 现在发布，30分钟后过期
                .withExpiresAt(Date.from(LocalDateTime.now().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant()))
                .sign(Algorithm.HMAC256(SECRET));
    }

    /** 校验Token
     * @Description:
     * @date: 2023/10/30
     * @param token 用户登录时token
     * @return: com.auth0.jwt.interfaces.DecodedJWT
     */
    public static DecodedJWT verify(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
    }

    /** 从token解析出用户
     * @Description:
     * @date: 2023/10/30
     * @param token
     * @return: today.oil.modal.User
     */
    public static User getUserFrom(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return User.builder()
                .id(jwt.getClaim(CLAIM_USER_ID).asLong())
                .username(jwt.getClaim(CLAIM_USERNAME).asString())
                .password(jwt.getClaim(CLAIM_USER_PASSWORD).asString())
                .build();
    }
}
