package funfun.gsuaki.api.routers;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@Component
public class HealthRouter {
    public RouterFunction<ServerResponse> createHealthRoute() {
        return RouterFunctions.route(GET("/ping"),
                r -> ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).syncBody("pong"));
    }
}
