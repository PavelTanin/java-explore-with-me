package ru.practicum.service.user;

import ru.practicum.dto.user.NewUserDto;
import ru.practicum.dto.user.UserDto;

import java.util.List;

public interface UserService {

    UserDto addUser(NewUserDto newUserDto);

    String deleteUser(Long userId);

    List<UserDto> getUsers(List<Long> ids, Integer from, Integer size);
}
