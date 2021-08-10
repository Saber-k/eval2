package com.mszablak.eval.controller;

import com.mszablak.eval.model.Foo;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.UUID;

import static com.mszablak.eval.controller.FooController.PATH;

@RestController
@RequestMapping(PATH)
public class FooController {
    public static final String PATH = "/foo";
    public static final Duration DELAY = Duration.ofSeconds(1);

    @GetMapping(produces = MediaType.APPLICATION_NDJSON_VALUE)
    Flux<Foo> getFoo() {
        return Flux.interval(DELAY)
            .map(FooController::fooSupplier);
    }

    private static Foo fooSupplier(Long longVal) {
        return Foo.builder()
            .id(UUID.randomUUID().toString())
            .name("dummy:" + longVal)
            .build();
    }
}
