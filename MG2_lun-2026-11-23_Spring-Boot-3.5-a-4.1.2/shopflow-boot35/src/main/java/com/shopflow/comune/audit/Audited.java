package com.shopflow.comune.audit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// MG4 Intermedio: l'aspect scatta solo sui metodi con questa annotazione
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)             // senza RUNTIME l'aspect non vede l'annotazione
public @interface Audited {
    String azione();
}
