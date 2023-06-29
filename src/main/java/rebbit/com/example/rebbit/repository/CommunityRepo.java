package rebbit.com.example.rebbit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rebbit.com.example.rebbit.model.Community;

public interface CommunityRepo extends JpaRepository<Community, Long> {
}
