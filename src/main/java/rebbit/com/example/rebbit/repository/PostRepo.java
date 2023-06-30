package rebbit.com.example.rebbit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rebbit.com.example.rebbit.model.Post;

import java.util.List;

public interface PostRepo extends JpaRepository<Post,Long> {
    @Query("select c from Post c where c.id =?1")
    Post findOneById(Long id);


    @Query("select c from Post c where c.community.id =?1")
    List<Post> findAllByCommunity(Long id);
}
