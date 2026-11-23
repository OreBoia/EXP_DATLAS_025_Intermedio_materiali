package com.shopflow.ordini.interno;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

// MG4 Base: misura il tempo dei servizi del modulo ordini, senza toccarne il codice
@Aspect
@Component
public class TempiAspect {

    private static final Logger log = LoggerFactory.getLogger(TempiAspect.class);

    // Pointcut: i metodi pubblici delle classi *Service del modulo ordini
    @Around("execution(public * com.shopflow.ordini.interno..*Service.*(..))")
    public Object misura(ProceedingJoinPoint chiamata) throws Throwable {
        long inizio = System.nanoTime();
        try {
            return chiamata.proceed();    // esegue il metodo vero; senza questa riga non viene eseguito
        } finally {
            long ms = (System.nanoTime() - inizio) / 1_000_000;
            log.info("{} eseguito in {} ms", chiamata.getSignature().toShortString(), ms);
        }
    }
}
