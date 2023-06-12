package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.DuplicateException;
import ru.practicum.shareit.exceptions.NotFoundException;

import java.util.HashMap;
import java.util.List;

import static ru.practicum.shareit.user.UserMapper.toUser;
import static ru.practicum.shareit.user.UserMapper.toUserDto;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final HashMap<Long, User> usersMap = new HashMap<>();
    private Long id = 1L;

    @Override
    public List<User> getUsers() {
        return List.copyOf(usersMap.values());
    }

    @Override
    public UserDto getUserById(Long userId) {
        if (!usersMap.containsKey(userId)) {
            throw new NotFoundException("User not found!");
        }
        return toUserDto(usersMap.get(userId));
    }

    @Override
    public UserDto addUser(UserDto user) throws DuplicateException {
        if (usersMap.values().stream().anyMatch(user1 -> user1.getEmail().equals(user.getEmail()))) {
            throw new DuplicateException("Email duplicates");
        }
        user.setId(id++);
        usersMap.put(user.getId(), toUser(user));
        return user;
    }

    @Override
    public UserDto updateUser(Long userId, User user) throws DuplicateException {
        User updUser = usersMap.get(userId);
        if (usersMap.values().stream()
                .filter(anotherUser -> !anotherUser.getId().equals(userId))
                .anyMatch(anotherUser -> anotherUser.getEmail().equals(user.getEmail()))) {
            throw new DuplicateException("Email duplicates");
        }
        if (user.getName() != null) {
            updUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            updUser.setEmail(user.getEmail());
        }
        usersMap.put(userId, updUser);

        return getUserById(userId);
    }

    @Override
    public Boolean deleteUser(Long userId) {
        if (!usersMap.containsKey(userId)) {
            return false;
        }
        usersMap.remove(userId);
        return true;
    }
}
