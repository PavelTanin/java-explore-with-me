package ru.practicum.repository.user;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.user.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByIdInOrderByIdAsc(List<Long> ids, Pageable pageable);
}
