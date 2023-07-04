package rebbit.com.example.rebbit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import rebbit.com.example.rebbit.model.Reaction;

public interface ReactionRepo extends JpaRepository<Reaction,Long> {

    @Query("select r from Reaction r where r.post.id=?1 and r.user.id=?2")
    Reaction findByUserId(Long postId, Long useId);

    @Modifying
    @Transactional
    @Query("delete from Reaction r where r.post.id=?1")
    void deleteByPostId(Long id);
}
