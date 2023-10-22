package my.app.imshop.repository;

import my.app.imshop.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByTitleIgnoreCase(@NonNull String title);
    boolean existsByIdOrTitleIgnoreCase(@NonNull Long id, @NonNull String title);
}
