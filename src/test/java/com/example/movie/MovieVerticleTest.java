
package com.example.movie;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(VertxExtension.class)
public class MovieVerticleTest {

    @BeforeEach
    void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
        vertx.deployVerticle(new MovieVerticle(), testContext.succeeding(id -> testContext.completeNow()));
    }

    @Test
    void test_search_api(Vertx vertx, VertxTestContext testContext) {
        WebClient client = WebClient.create(vertx);
        client.get(8080, "localhost", "/api/movies/search")
              .send(ar -> {
                  if (ar.succeeded()) {
                      assertEquals(200, ar.result().statusCode());
                      assertTrue(ar.result().bodyAsString().contains("placeholder"));
                      testContext.completeNow();
                  } else {
                      testContext.failNow(ar.cause());
                  }
              });
    }
}
