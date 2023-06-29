package rebbit.com.example.rebbit.dto;


import lombok.Data;

import java.io.Serializable;

@Data
public class UserDTO implements Serializable {
    private final String username;
    private final String password;
    private final String email;
    private final String displayName;
}
