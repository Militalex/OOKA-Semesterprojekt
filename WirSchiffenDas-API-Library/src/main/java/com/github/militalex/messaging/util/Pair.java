package com.github.militalex.messaging.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * Eine einfache, unveränderliche generische Klasse, die ein Paar von Objekten
 * speichert. Dies ist nützlich, um eine Methode zu erstellen, die zwei
 * Werte zurückgibt, oder um Schlüssel-Wert-Paare in Sammlungen zu speichern.
 *
 * <p>Die Klasse ist {@code final}, um die Vererbung zu verhindern und die
 * Unveränderlichkeit zu gewährleisten.
 *
 * @param <F> der Typ des ersten Elements (Schlüssel) im Paar
 * @param <S> der Typ des zweiten Elements (Wert) im Paar
 * @author Gemini AI
 * @version 1.0
 * @since 1.0
 */
public final class Pair<F, S> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Das erste Element des Paares.
     */
    private final F first;

    /**
     * Das zweite Element des Paares.
     */
    private final S second;

    /**
     * Erstellt ein neues unveränderliches Pair-Objekt mit den angegebenen
     * Elementen.
     *
     * @param first   das erste Element
     * @param second das zweite Element
     */
    @JsonCreator
    public Pair(@JsonProperty("first") F first, @JsonProperty("second") S second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Erzeugt eine Instanz eines Pair-Objekts. Dies ist eine bequeme statische
     * Factory-Methode.
     *
     * @param key   das erste Element
     * @param value das zweite Element
     * @param <K>   der Typ des ersten Elements
     * @param <V>   der Typ des zweiten Elements
     * @return eine neue Instanz von {@code Pair}
     */
    public static <K, V> Pair<K, V> of(K key, V value) {
        return new Pair<>(key, value);
    }

    /**
     * Gibt das erste Element des Paares zurück.
     *
     * @return das erste Element
     */
    public F getFirst() {
        return first;
    }

    /**
     * Gibt das zweite Element des Paares zurück.
     *
     * @return das zweite Element
     */
    public S getSecond() {
        return second;
    }

    /**
     * Gibt eine String-Repräsentation des Paares zurück.
     *
     * @return eine String-Repräsentation im Format {@code (key, value)}
     */
    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }

    /**
     * Überprüft, ob das angegebene Objekt diesem Paar entspricht.
     * Die Paare werden als gleich betrachtet, wenn ihre Schlüssel und Werte
     * gleich sind.
     *
     * @param o das Objekt, mit dem verglichen werden soll
     * @return {@code true}, wenn das angegebene Objekt gleich diesem ist,
     * andernfalls {@code false}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(first, pair.first) &&
                Objects.equals(second, pair.second);
    }

    /**
     * Gibt einen Hashcode für dieses Paar zurück.
     *
     * @return ein Hashcode-Wert für dieses Objekt
     */
    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}