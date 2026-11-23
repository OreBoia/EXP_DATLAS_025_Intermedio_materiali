package com.shopflow.comune;

// MG1 Avanzato: esito di un'operazione, o un valore o un errore PREVISTO (non un'eccezione)
public sealed interface Result<T, E> {
    record Ok<T, E>(T valore) implements Result<T, E> {}
    record Errore<T, E>(E errore) implements Result<T, E> {}
}
