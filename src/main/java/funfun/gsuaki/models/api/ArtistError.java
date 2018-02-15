package funfun.gsuaki.models.api;

import funfun.gsuaki.domains.Artist;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArtistError {

  public static class ArtistAlreadyCreatedException extends RuntimeException {

    @Getter
    private final Artist artist;

    public ArtistAlreadyCreatedException(final Artist artist) {
      super("Artist with name " + artist.getName() + " has already been created.");
      this.artist = artist;
    }
  }
}
