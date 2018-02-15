package funfun.gsuaki.testdata;

import funfun.gsuaki.domains.Artist;

public class Models {

  public static Artist dummyArtist(final Long id, final String name) {
    return new Artist(id, name);
  }
}
