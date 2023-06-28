package rebbit.com.example.rebbit.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Moderator")
public class Moderator extends User{

    @Override
    public GrantedAuthority getRole(){
        return new SimpleGrantedAuthority("MODERATOR");
    }
}
