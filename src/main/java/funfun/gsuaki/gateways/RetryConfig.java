package funfun.gsuaki.gateways;

import lombok.Value;

import java.time.Duration;

@Value
public class RetryConfig {
    private Duration backoffDelay;
    private Integer times;
}
