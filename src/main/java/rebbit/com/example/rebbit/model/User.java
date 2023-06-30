package rebbit.com.example.rebbit.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "Role")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column()
    private String avatar;

    @Column(nullable = false)
    private LocalDate registrationDate;

    @Column()
    private String description;

    @Column(nullable = false)
    private String displayName;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Post> posts;

    public User(String username, String password, String email, LocalDate now, String displayName) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.registrationDate = now;
        this.displayName = displayName;
    }

    public GrantedAuthority getRole(){
        return new SimpleGrantedAuthority("USER_ROLE");
    }


}
