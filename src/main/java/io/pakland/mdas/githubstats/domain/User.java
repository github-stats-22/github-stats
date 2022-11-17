package io.pakland.mdas.githubstats.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * <b>Class</b>: User <br/>
 * <b>Copyright</b>: 2022 La Salle - mdas <br/>.
 *
 * @author 2022  MDAS <br/>
 * <u>Service Provider</u>: La Salle <br/>
 * <u>Developed by</u>: mdas <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     Septiembre 13, 2022 Creación de Clase.
 *   </li>
 * </ul>
 */

@Data
@NoArgsConstructor
@ToString
@Entity
@Table(name = "user")
public class User {

  @Id
  @Column(name = "id", updatable = false, nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "login")
  private String login;

  @Column(name = "organization_url")
  private String organizationUrl;

}
