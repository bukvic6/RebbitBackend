package rebbit.com.example.rebbit.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("Admin")
public class Admin extends User{
    public Admin() {

    }
    @Override
    public GrantedAuthority getRole(){
        return new SimpleGrantedAuthority("ADMIN");
    }
}
