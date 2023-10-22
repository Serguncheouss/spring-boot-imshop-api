package my.app.imshop.repository;

import my.app.imshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginIgnoreCase(@NonNull String login);
    boolean existsByIdOrLoginIgnoreCase(@NonNull Long id, @NonNull String login);
}
