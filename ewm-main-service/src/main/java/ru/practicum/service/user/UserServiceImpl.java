package ru.practicum.service.user;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.dto.user.NewUserDto;
import ru.practicum.dto.user.UserDto;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.mapper.user.UserMapper;
import ru.practicum.model.user.User;
import ru.practicum.repository.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final UserRepository userRepository;

    public UserDto addUser(NewUserDto newUserDto) {
        log.info("Добавляется новый пользователь: {}", newUserDto.toString());
        User user = UserMapper.toUser(newUserDto);
        log.info("Пользователь добавлен");
        return UserMapper.toUserDto(userRepository.save(user));
    }

    public String deleteUser(Long userId) {
        log.info("Попытка ужалить пользователя с id: {}", userId);
        if (!userRepository.existsById(userId)) {
            log.info("Попытка удалить несуществующего пользователя");
            throw new ObjectNotFoundException("Пользователя с такими идентификатором не существует");
        }
        userRepository.deleteById(userId);
        return "Пользователь удален";
    }

    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        Pageable pageRequest = PageRequest.of(from, size);
        return userRepository.findAllByIdInOrderByIdAsc(ids, pageRequest).stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }
}
