package com.github.militalex.messaging;

import com.github.militalex.messaging.util.Pair;
import lombok.Getter;
import org.apache.kafka.common.errors.TimeoutException;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

public class KafkaCircuitBreaker<K, V> {

    /**
     * Default timeout for the circuit breaker in milliseconds.
     */
    public static final int DEFAULT_TIMEOUT = 5000;

    /**
     * Timeout for attempting to reset the circuit breaker in milliseconds.
     */
    public static final int ATTEMPT_RESET_TIMEOUT = 10000;

    /**
     * Threshold for the circuit breaker to trip.
     */
    public static final int THRESHOLD = 3;

    @Getter
    private State state = State.CLOSED;
    private final Consumer<Pair<K, V>> sendMethod;

    private int counter = 0;

    public KafkaCircuitBreaker(Function<Pair<K, V>, CompletableFuture<SendResult<K, V>>> sendMethod) {
        this.sendMethod = pair -> {
            CompletableFuture<SendResult<K, V>> future = sendMethod.apply(pair);
            future.orTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS);
            future.whenComplete((irSendResult, throwable) -> {
                if (throwable != null) {
                    fail();
                    throw new RuntimeException(throwable);
                }
                success();
            });
        };
    }

    public void call(K key, V value) {
        if (state == State.OPEN){
            throw new TimeoutException("Circuit breaker is open, cannot send message. Please wait for reset.");
        }
        sendMethod.accept(new Pair<>(key, value));
    }

    private void success(){
        counter = 0;
        state = State.CLOSED;
    }

    private void attemptReset(){
        state = State.HALF_OPEN;
    }

    private void fail(){
        if (state == State.HALF_OPEN){
            trip();
            return;
        }

        counter++;
        if (counter >= THRESHOLD) trip();
    }

    private void trip(){
        state = State.OPEN;
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(ATTEMPT_RESET_TIMEOUT);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            finally {
                attemptReset();
            }
        });
    }

    public enum State {
        CLOSED, OPEN, HALF_OPEN
    }
}
