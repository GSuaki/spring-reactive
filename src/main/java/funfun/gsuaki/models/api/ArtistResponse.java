package funfun.gsuaki.models.api;

import funfun.gsuaki.domains.Artist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtistResponse {

  private Long id;

  private String name;

  public ArtistResponse(final Artist artist) {
    this.id = artist.getId();
    this.name = artist.getName();
  }
}
