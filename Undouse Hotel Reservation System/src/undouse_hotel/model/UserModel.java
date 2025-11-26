package undouse_hotel.model;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

public class UserModel {
    private static final String USERS_FILE = "users_data.dat";
    private static UserModel instance;
    private List<User> users;
    private User currentLoggedInUser;
    
    private UserModel() {
        users = new ArrayList<>();
        loadUsers();
    }
    
    public static UserModel getInstance() {
        if (instance == null) {
            instance = new UserModel();
        }
        return instance;
    }
    
    private void loadUsers() {
        File file = new File(USERS_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                users = (List<User>) ois.readObject();
            } catch (Exception e) {
                System.err.println("Error loading users: " + e.getMessage());
                users = new ArrayList<>();
            }
        }
    }
    
    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
            oos.writeObject(users);
            oos.flush();
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }
    
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }
    
    public String generatePasswordResetToken(String email) {
        User user = getUserByEmail(email);
        if (user != null) {
            String token = UUID.randomUUID().toString().substring(0, 8);
            
            return token;
        }
        return null;
    }

    public boolean resetPassword(String email, String token, String newPassword) {
        User user = getUserByEmail(email);
        if (user != null) {
           
            user.setPasswordHash(hashPassword(newPassword));
            saveUsers();
            return true;
        }
        return false;
    }

    public boolean updatePassword(String email, String currentPassword, String newPassword) {
        if (!authenticateUser(email, currentPassword)) {
            return false;
        }
        
        if (!isPasswordValid(newPassword)) {
            return false;
        }
        
        User user = getUserByEmail(email);
        user.setPasswordHash(hashPassword(newPassword));
        saveUsers();
        return true;
    }
    
    public boolean isPasswordValid(String password) {
        if (password == null || password.length() < 6) {
            return false;
        }
        
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else if (!Character.isWhitespace(c)) hasSpecial = true;
        }
        
        return hasUpper && hasLower && hasDigit && hasSpecial;
    }

    
    public boolean registerUser(String email, String password, String firstName, String lastName, 
                               String phone, String address, String city, String country) {
     
        if (getUserByEmail(email) != null) {
            return false;
        }
        
        String passwordHash = hashPassword(password);
        User newUser = new User(email, passwordHash, firstName, lastName, phone, address, city, country);
        users.add(newUser);
        saveUsers();
        return true;
    }
    
    public boolean authenticateUser(String email, String password) {
        User user = getUserByEmail(email);
        if (user != null && user.getPasswordHash().equals(hashPassword(password))) {
            currentLoggedInUser = user;
            user.updateLastLogin();
            saveUsers();
            return true;
        }
        return false;
    }
    
    public User getUserByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }
        return null;
    }
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
    
    public User getCurrentUser() {
        return currentLoggedInUser;
    }
    
    public void logout() {
        currentLoggedInUser = null;
    }
    public boolean isUserLoggedIn() {
        return currentLoggedInUser != null;
    }
    
    public boolean updateUser(String email, String firstName, String lastName, 
                             String phone, String address, String city, String country) {
        User user = getUserByEmail(email);
        if (user != null) {
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPhone(phone);
            user.setAddress(address);
            user.setCity(city);
            user.setCountry(country);
            saveUsers();
            return true;
        }
        return false;
    }
    
    public void saveCurrentUser() {
        if (currentLoggedInUser != null) {
       
            saveUsers();
        }
    }
    public static class User implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private String email;
        private String passwordHash;
        private String firstName;
        private String lastName;
        private String phone;
        private String address;
        private String city;
        private String country;
        private Date registrationDate;
        private Date lastLoginDate;
        private int totalBookings;
        
        public User(String email, String passwordHash, String firstName, String lastName,
                   String phone, String address, String city, String country) {
            this.email = email;
            this.passwordHash = passwordHash;
            this.firstName = firstName;
            this.lastName = lastName;
            this.phone = phone;
            this.address = address;
            this.city = city;
            this.country = country;
            this.registrationDate = new Date();
            this.lastLoginDate = new Date();
            this.totalBookings = 0;
        }
        
        public String getEmail() { return email; }
        public String getPasswordHash() { return passwordHash; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getFullName() { return firstName + " " + lastName; }
        public String getPhone() { return phone; }
        public String getAddress() { return address; }
        public String getCity() { return city; }
        public String getCountry() { return country; }
        public Date getRegistrationDate() { return registrationDate; }
        public Date getLastLoginDate() { return lastLoginDate; }
        public int getTotalBookings() { return totalBookings; }
        
        public void setFirstName(String firstName) { this.firstName = firstName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        public void setPhone(String phone) { this.phone = phone; }
        public void setAddress(String address) { this.address = address; }
        public void setCity(String city) { this.city = city; }
        public void setCountry(String country) { this.country = country; }
        public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
        
        public void updateLastLogin() {
            this.lastLoginDate = new Date();
        }
        
        public void incrementBookings() {
            this.totalBookings++;
        }
        
        public String getFormattedRegistrationDate() {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
            return sdf.format(registrationDate);
        }
        
        public String getFormattedLastLogin() {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm");
            return sdf.format(lastLoginDate);
        }
    }
}