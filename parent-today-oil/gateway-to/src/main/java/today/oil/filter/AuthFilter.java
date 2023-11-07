package today.oil.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import today.oil.common.Result;
import today.oil.config.WhiteListConfig;
import today.oil.constant.ServiceConstants;
import today.oil.utils.JWTUtil;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @create: 2023/10/31
 * @Description:
 * @FileName: AuthFilter
 */
@Slf4j
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private WhiteListConfig whiteListConfig;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        final String path = request.getURI().getPath();
        for (String item : whiteListConfig.getWhiteList()) {
            if (path.contains(item)) {
                log.info("白名单：{}", path);
                return chain.filter(exchange);
            }
        }

        log.info("尝试获取Token：path({})", path);
        String token = getToken(request);
        log.info("Token: {}", token);
        try {
            if (hasToken(token)) {
                log.info("存在Token，设置uid");
                Long uid = JWTUtil.getUserFrom(token).getId();
                request.mutate().header("uid", "" + uid);
                resetToken(token);
                return chain.filter(exchange);
            }
        } catch (Exception ex) {
            log.error("Token校验错误", ex);
        }

        log.info("Token无效，设置错误响应");
        final ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        Result result = Result.error(HttpStatus.UNAUTHORIZED.value(), "请登录后访问");
        DataBuffer buffer = response.bufferFactory()
                .wrap(objectMapper.writeValueAsString(result).getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return 0;
    }

    /** 获取Token
     * @Description:
     * @date: 2023/11/1
     * @param request
     * @return: java.lang.String
     */
    private static String getToken(ServerHttpRequest request) {
        log.info("从请求头获取Token");
        String token = request.getHeaders().getFirst(ServiceConstants.TOKEN);
        if (StringUtils.isEmpty(token)) {
            log.info("请求头无Token，从查询参数获取");
            token = request.getQueryParams().getFirst(ServiceConstants.TOKEN);
        }

        if (StringUtils.isEmpty(token)) {
            log.info("查询参数无Token，从Cookie获取");
            HttpCookie cookie = request.getCookies().getFirst(ServiceConstants.TOKEN);
            if (Objects.nonNull(cookie)) {
                token = cookie.getValue();
            }
        }
        return token;
    }

    /** 重置Token缓存时间
     * @Description:
     * @date: 2023/11/1
     * @param token
     * @return: void
     */
    private void resetToken(String token) {
        log.info("重置Token缓存有效时间");
        redisTemplate.opsForValue().set(token, "", 30, TimeUnit.MINUTES);
    }

    /** 缓存是否存在Token
     * @Description:
     * @date: 2023/11/1
     * @param token
     * @return: boolean
     */
    private boolean hasToken(String token) {
        return StringUtils.hasText(token) &&
                Objects.equals(redisTemplate.hasKey(token), Boolean.TRUE);
    }
}
