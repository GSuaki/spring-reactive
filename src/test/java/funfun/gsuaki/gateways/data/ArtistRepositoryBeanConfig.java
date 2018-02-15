package funfun.gsuaki.gateways.data;

import org.mockito.AdditionalAnswers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import static org.mockito.Mockito.mock;

@Configuration
@Profile("test")
public class ArtistRepositoryBeanConfig {

  @Primary
  @Bean(name = "artistRepositoryMock")
  ArtistRepository artistRepository(final ArtistRepository repository) {
    return mock(ArtistRepository.class, AdditionalAnswers.delegatesTo(repository));
  }
}
