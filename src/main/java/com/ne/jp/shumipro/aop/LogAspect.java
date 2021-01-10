package com.ne.jp.shumipro.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LogAspect {
    @Before("execution(* *..*Controller.*(..))")
    public void startLog(JoinPoint joinPoint){
    	Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
    	logger.info("Start:" + joinPoint.getSignature().getName());
    }
    @After("execution(* *..*Controller.*(..))")
    public void endLog(JoinPoint joinPoint){
    	Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
    	logger.info("End:" + joinPoint.getSignature().getName());
    }
}
