package com.mszablak.eval.controller;

import com.mszablak.eval.model.Foo;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@WebFluxTest
public class FooControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void givenRequestTakeThree_whenGetFoo_thenReturnThreeElements() {

        val body = webTestClient
            .get()
            .uri(FooController.PATH)
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectHeader().contentType(MediaType.APPLICATION_NDJSON)
            .returnResult(Foo.class)
            .getResponseBody()
            .take(3)
            .collectList()
            .block();

        val randomUUID = UUID.randomUUID().toString();
        assertThat(body)
            .hasSize(3)
            .allSatisfy(foo -> assertThat(foo.getId()).hasSameSizeAs(randomUUID))
            .extracting(Foo::getName)
            .containsExactly("dummy:0", "dummy:1", "dummy:2");
    }
}
