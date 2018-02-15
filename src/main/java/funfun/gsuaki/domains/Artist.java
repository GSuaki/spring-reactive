package funfun.gsuaki.domains;

import lombok.*;
import lombok.experimental.Wither;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Wither
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "artist", indexes = {
    @Index(name = "artist_index", columnList = "id,name")
})
public class Artist {

  @Id
  @Min(1)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Column(unique = true)
  private String name;

}
