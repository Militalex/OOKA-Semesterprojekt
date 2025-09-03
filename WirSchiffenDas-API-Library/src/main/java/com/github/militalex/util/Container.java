package com.github.militalex.util;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Ein veränderlicher (mutable) Container-Objekt, das einen einzelnen Wert
 * enthalten kann oder leer sein kann. Diese Klasse ist eine Alternative zu
 * {@link java.util.Optional}, wenn die Flexibilität zur nachträglichen
 * Änderung des enthaltenen Wertes erforderlich ist.
 *
 * <p>Im Gegensatz zu {@code Optional} ist diese Klasse nicht thread-sicher.
 *
 * @author Google Gemini-AI
 * @param <T> der Typ des Wertes
 * @since 1.0
 */
public final class Container<T> {

    private T value;

    /**
     * Erstellt eine leere {@code Container}-Instanz.
     *
     * @param <T> der Typ des Wertes
     * @return eine leere {@code Container}-Instanz
     */
    public static <T> Container<T> empty() {
        return new Container<>();
    }

    /**
     * Erstellt eine {@code Container}-Instanz mit dem gegebenen nicht-null Wert.
     *
     * @param value der nicht-null Wert
     * @param <T>   der Typ des Wertes
     * @return eine {@code Container}-Instanz mit dem Wert
     * @throws NullPointerException wenn der Wert {@code null} ist
     */
    public static <T> Container<T> of(T value) {
        return new Container<>(Objects.requireNonNull(value));
    }

    /**
     * Erstellt eine {@code Container}-Instanz mit dem gegebenen Wert.
     * Wenn der Wert {@code null} ist, wird eine leere Instanz zurückgegeben.
     *
     * @param value der Wert
     * @param <T>   der Typ des Wertes
     * @return eine {@code Container}-Instanz mit dem Wert oder eine leere Instanz
     */
    public static <T> Container<T> ofNullable(T value) {
        return new Container<>(value);
    }

    private Container() {
        this.value = null;
    }

    private Container(T value) {
        this.value = value;
    }

    /**
     * Setzt den Wert dieses Containers. Wenn der Wert {@code null} ist, wird der
     * Container geleert.
     *
     * @param value der neue Wert
     */
    public void set(T value) {
        this.value = value;
    }

    /**
     * Gibt den Wert zurück, falls er vorhanden ist, andernfalls wird eine
     * {@code NoSuchElementException} ausgelöst.
     *
     * @return der nicht-null Wert
     * @throws NoSuchElementException wenn der Wert nicht vorhanden ist
     */
    public T get() {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }

    /**
     * Prüft, ob ein Wert vorhanden ist.
     *
     * @return {@code true} wenn ein Wert vorhanden ist, sonst {@code false}
     */
    public boolean isPresent() {
        return value != null;
    }

    /**
     * Prüft, ob der Container leer ist.
     *
     * @return {@code true} wenn kein Wert vorhanden ist, sonst {@code false}
     */
    public boolean isEmpty() {
        return value == null;
    }

    /**
     * Wenn ein Wert vorhanden ist, wird die gegebene {@code Consumer}-Aktion
     * mit dem Wert ausgeführt.
     *
     * @param action die Aktion, die ausgeführt werden soll
     * @throws NullPointerException wenn die Aktion {@code null} ist
     */
    public void ifPresent(Consumer<? super T> action) {
        if (isPresent()) {
            action.accept(value);
        }
    }

    /**
     * Gibt den Wert zurück, falls er vorhanden ist, andernfalls den
     * übergebenen Standardwert.
     *
     * @param other der zurückzugebende Wert, falls kein Wert vorhanden ist.
     * @return der Wert, falls vorhanden, sonst der Standardwert.
     */
    public T orElse(T other) {
        return isPresent() ? value : other;
    }
}