package server.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import server.models.User;
import server.services.user.UserRepo;

@Service
public class UserSvc {

    private final UserRepo repo;

    public UserSvc(UserRepo repo) {
        this.repo = repo;
    }

    @Transactional
    public void createUser(User u) {
        repo.save(u);
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return repo.findAll();
    }
}
