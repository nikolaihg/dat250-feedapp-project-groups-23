package dat250.FeedApp.repository;

import dat250.FeedApp.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
}
