package edu.stanford.irt.laneweb;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import edu.stanford.irt.cocoon.cache.CachedResponse;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* javax.cache.Cache.get(..))")
    public Object logExecutionTime(final ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;
        log.info("get {} executed in {}ms", joinPoint.getArgs()[0], executionTime);
        if (proceed == null) {
            log.info("value=null");
        } else {
            CachedResponse cachedResponse = (CachedResponse) proceed;
            log.info("isValid={}", cachedResponse.getValidity().isValid());
        }
        return proceed;
    }
}
