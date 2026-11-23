package com.shopflow.comune.audit;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

// MG4 Intermedio: registra le azioni dei metodi annotati con @Audited
@Aspect
@Component
class AuditAspect {

    private final RegistroAudit registro;

    AuditAspect(RegistroAudit registro) {
        this.registro = registro;
    }

    // "audited" è il parametro del metodo: dà accesso ai valori dell'annotazione
    @AfterReturning(pointcut = "@annotation(audited)", returning = "esito")
    void registra(JoinPoint punto, Audited audited, Object esito) {
        registro.scrivi(audited.azione(), punto.getSignature().getName(), esito);
    }
}
