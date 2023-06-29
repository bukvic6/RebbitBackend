package rebbit.com.example.rebbit.service;

import rebbit.com.example.rebbit.model.User;

public interface UserService {

    User register (User user);
    User findByUsername(String username);

}
