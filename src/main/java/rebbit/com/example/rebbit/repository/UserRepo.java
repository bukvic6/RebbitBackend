package rebbit.com.example.rebbit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rebbit.com.example.rebbit.model.User;

public interface UserRepo extends JpaRepository<User,Long> {
    User findUserByUsername(String username);}
