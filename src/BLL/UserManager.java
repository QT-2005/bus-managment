package BLL;

import DAL.UserDAO;
import DTO.User;

import java.util.List;

public class UserManager {
    private UserDAO userDAO;

    public UserManager() {
        userDAO = new UserDAO();
    }

    public User authenticate(String username, String password) {
        return userDAO.authenticate(username, password);
    }

    public boolean addUser(User user) {
        // Validate user data
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            return false;
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            return false;
        }
        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            return false;
        }

        return userDAO.addUser(user);
    }
    public boolean isUsernameExists(String username) {
        return userDAO.isUsernameExists(username);
    }


    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public User getUserById(int id) {
        return userDAO.getUserById(id);
    }

    public boolean updateUser(User user) {
        // Validate user data
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            return false;
        }
        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            return false;
        }

        return userDAO.updateUser(user);
    }

    public boolean deleteUser(int id) {
        return userDAO.deleteUser(id);
    }
}