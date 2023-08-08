package org.macbeth.ecommarketapp.limiter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;

@Component
@Aspect
public class RateLimitAspect {

    private static final Logger LOG = LogManager.getLogger(RateLimitAspect.class);

    private final HashMap<String, ConnectionRegistrar> connectionMap = new HashMap<>();

    @Value("${rate-limit-connection}")
    String connectionLimit;

    @Value("${rate-limit-period-min}")
    String period;


    @Before("@annotation(rateLimit)")
    public synchronized void checkConnection(RateLimit rateLimit) {

        String address = getRemoteAddr();
        ConnectionRegistrar reg = connectionMap.get(address);
        if (reg == null) {
            reg = new ConnectionRegistrar(connectionLimit, period);
            connectionMap.put(address, reg);
        } else {
            reg.registerConnection();
        }

        if (!reg.isConnectionAllowed()) {
            LOG.info("Connection denied. Address: {}", address);
            throw new RateLimitException();
        }
        LOG.info("Connection allowed. Address: {}", address);

    }

    private String getRemoteAddr() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getRemoteAddr();
    }
}
