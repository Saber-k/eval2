package com.mszablak.eval.controller;

import com.mszablak.eval.model.Foo;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class FooControllerUnitTest {

    private final FooController fooController = new FooController();

    @Test
    void whenGetFoo_thenStreamDataIndefinitely() {
        StepVerifier
            .withVirtualTime(fooController::getFoo)
            .expectSubscription()
            .thenAwait(FooController.DELAY.multipliedBy(3))
            .expectNextMatches(foo -> assertFoo(foo, 0))
            .expectNextMatches(foo -> assertFoo(foo, 1))
            .expectNextMatches(foo -> assertFoo(foo, 2))
            .thenCancel()
            .verify();
    }

    @Test
    void givenTakeThree_whenGetFoo_thenStreamDataEverySecondAndComplete() {
        StepVerifier.withVirtualTime(() -> fooController.getFoo().elapsed().take(3))
            .expectSubscription()
            .expectNoEvent(FooController.DELAY)
            .expectNextMatches(tuple -> assertFoo(tuple.getT2(), 0) && assertLatency(tuple.getT1()))
            .thenAwait(FooController.DELAY.multipliedBy(2))
            .expectNextMatches(tuple -> assertFoo(tuple.getT2(), 1) && assertLatency(tuple.getT1()))
            .expectNextMatches(tuple -> assertFoo(tuple.getT2(), 2) && assertLatency(tuple.getT1()))
            .expectComplete()
            .verify();
    }

    private static boolean assertLatency(Long elapsed) {
        return elapsed == 1000;
    }

    private static boolean assertFoo(Foo foo, long idx) {
        return foo != null && foo.getId() != null && foo.getName().contains("dummy:");
    }
}