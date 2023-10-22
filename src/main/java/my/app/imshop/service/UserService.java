package my.app.imshop.service;

import lombok.NonNull;
import my.app.imshop.model.User;
import my.app.imshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public boolean create(User user) {
        if (!userRepository.existsByIdOrLoginIgnoreCase(user.getId(), user.getLogin())) {
            userRepository.save(user);

            return true;
        }

        return false;
    }

    @Nullable
    public User get(long id) {
        return userRepository.findById(id).orElse(null);
    }

    public Optional<User> getByLogin(@NonNull String login) {
        return userRepository.findByLoginIgnoreCase(login);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public boolean update(long id, User user) {
        if (userRepository.existsById(id)) {
            user.setId(id);
            userRepository.save(user);

            return true;
        }

        return false;
    }

    public boolean delete(long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);

            return true;
        }

        return false;
    }

    public long count() {
        return userRepository.count();
    }
}
