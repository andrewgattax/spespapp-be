package it.trinex.spespappbe.exception;

import org.springframework.http.HttpStatus;

/**
 * Classe di eccezione base per tutte le eccezioni specifiche dell'applicazione.
 * Fornisce funzionalità comuni per tutte le eccezioni nell'applicazione.
 */
public abstract class BaseException extends RuntimeException {

    private final HttpStatus status;

    /**
     * Costruisce una nuova eccezione con il messaggio di dettaglio e lo stato HTTP specificati.
     *
     * @param messaggio il messaggio di dettaglio
     * @param status lo stato HTTP da restituire al client
     */
    public BaseException(String messaggio, HttpStatus status) {
        super(messaggio);
        this.status = status;
    }

    /**
     * Costruisce una nuova eccezione con il messaggio di dettaglio, la causa e lo stato HTTP specificati.
     *
     * @param messaggio il messaggio di dettaglio
     * @param cause la causa dell'eccezione
     * @param status lo stato HTTP da restituire al client
     */
    public BaseException(String messaggio, Throwable cause, HttpStatus status) {
        super(messaggio, cause);
        this.status = status;
    }

    /**
     * Restituisce lo stato HTTP associato a questa eccezione.
     *
     * @return lo stato HTTP
     */
    public HttpStatus getStatus() {
        return status;
    }
}
