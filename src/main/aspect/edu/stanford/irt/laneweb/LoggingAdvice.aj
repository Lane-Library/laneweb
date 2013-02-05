package edu.stanford.irt.laneweb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public aspect LoggingAdvice {

    private Logger log = LoggerFactory.getLogger(LoggingAdvice.class);
    
    pointcut loggableMethod() : call(public * edu.stanford.irt.laneweb.bookmarks.*.*(..));
    
    Object around() : loggableMethod() {
        this.log.info("begin " + thisJoinPoint.getSignature());
        Object value = proceed();
        this.log.info("end " + thisJoinPoint.getSignature());
        return value;
    }
}
