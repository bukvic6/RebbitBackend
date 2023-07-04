package rebbit.com.example.rebbit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rebbit.com.example.rebbit.dto.CoundAndKarma;
import rebbit.com.example.rebbit.model.Post;

import java.util.List;

public interface PostRepo extends JpaRepository<Post,Long> {
    @Query("select c from Post c where c.id =?1")
    Post findOneById(Long id);


    @Query("select c from Post c where c.community.id =?1")
    List<Post> findAllByCommunity(Long id);


    @Query("select COUNT(*) as count from Post p where p.community.id = ?1")
    Long findCount(Long id);

    @Query("select SUM(karma) as karmaCount from Post p where p.community.id = ?1")
    Long findKarma(Long id);
}
