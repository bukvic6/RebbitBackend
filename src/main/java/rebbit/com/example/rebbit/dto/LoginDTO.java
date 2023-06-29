package rebbit.com.example.rebbit.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginDTO implements Serializable {
    private final String username;
    private final String password;
}
