package my.app.imshop.service;

import my.app.imshop.model.Role;
import my.app.imshop.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public boolean create(Role role) {
        if (!roleRepository.existsByIdOrTitleIgnoreCase(role.getId(), role.getTitle())) {
            roleRepository.save(role);

            return true;
        }

        return false;
    }

    @Nullable
    public Role get(long id) {
        return roleRepository.findById(id).orElse(null);
    }

    public Optional<Role> getByTitle(@NonNull String title) {
        return roleRepository.findByTitleIgnoreCase(title);
    }

    public List<Role> getAll() {
        return roleRepository.findAll();
    }

    public boolean update(long id, Role newRole) {
        if (roleRepository.existsById(id)) {
            newRole.setId(id);
            roleRepository.save(newRole);

            return true;
        }

        return false;
    }

    public boolean delete(long id) {
        if (roleRepository.existsById(id)) {
            roleRepository.deleteById(id);

            return true;
        }

        return false;
    }

    public static Role convertFromMap(Map<?, ?> roleMap) {
        return new Role((long) (int) roleMap.get("id"), (String) roleMap.get("title"));
    }
}
