package undouse_hotel.model;

import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AdminModel {
    private static final String CREDENTIALS_FILE = "admin_credentials.dat";
    private static final String ROOMS_FILE = "room_inventory.dat";
    private static final String BOOKINGS_FILE = "bookings_data.dat";
    private static final String GUESTS_FILE = "admin_data.dat";
    
    private String adminUsername;
    private String adminPasswordHash;
    private List<BookingRecord> bookingHistory;
    private Map<String, RoomStatistics> roomStats;
    private List<RoomUnit> roomInventory;
    private List<GuestRecord> guestDatabase;
    private RoomModel roomModel;
    
    public AdminModel() {
        this.bookingHistory = new ArrayList<>();
        this.roomStats = new HashMap<>();
        this.roomInventory = new ArrayList<>();
        this.guestDatabase = new ArrayList<>();
        
        loadCredentials();
        initializeRoomStats();
        loadRoomInventory();
        loadBookings();
        loadGuests();
    }
    
    public void setRoomModel(RoomModel roomModel) {
        this.roomModel = roomModel;
    }
    
    
    private void loadCredentials() {
        File credFile = new File(CREDENTIALS_FILE);
        if (credFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(credFile))) {
                adminUsername = reader.readLine();
                adminPasswordHash = reader.readLine();
                if (adminUsername == null || adminPasswordHash == null) {
                    setDefaultCredentials();
                }
            } catch (IOException e) {
                setDefaultCredentials();
            }
        } else {
            setDefaultCredentials();
        }
    }
    
    private void setDefaultCredentials() {
        adminUsername = "admin@sereneoasis.com";
        adminPasswordHash = hashPassword("admin123");
        saveCredentials();
    }
    
    private void saveCredentials() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CREDENTIALS_FILE))) {
            writer.println(adminUsername);
            writer.println(adminPasswordHash);
            writer.flush();
        } catch (IOException e) {
            System.err.println("Failed to save credentials: " + e.getMessage());
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
    
    public boolean authenticate(String username, String password) {
        return adminUsername.equals(username) && 
               adminPasswordHash.equals(hashPassword(password));
    }
    
    public boolean changePassword(String currentPassword, String newPassword) {
        if (!authenticate(adminUsername, currentPassword)) {
            return false;
        }
        adminPasswordHash = hashPassword(newPassword);
        saveCredentials();
        return true;
    }
    
    public boolean changeUsername(String newUsername) {
        if (newUsername == null || newUsername.trim().isEmpty()) {
            return false;
        }
        adminUsername = newUsername.trim();
        saveCredentials();
        return true;
    }
    
    public String getAdminUsername() {
        return adminUsername;
    }
    
    private void initializeRoomStats() {
        String[] roomTypes = {
            "Classic Room", "Standard Deluxe Room", "Premier Deluxe Room",
            "Executive Suite", "Family Suite", "Presidential Suite"
        };
        
        for (String roomType : roomTypes) {
            roomStats.put(roomType, new RoomStatistics(roomType));
        }
    }
    
    private void loadBookings() {
        File file = new File(BOOKINGS_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                bookingHistory = (List<BookingRecord>) ois.readObject();
                
                for (BookingRecord booking : bookingHistory) {
                    String roomType = booking.getRoomType();
                    if (roomStats.containsKey(roomType)) {
                        roomStats.get(roomType).incrementBookings();
                        roomStats.get(roomType).addRevenue(booking.getTotalAmount());
                    }
                }
                
                System.out.println("Loaded " + bookingHistory.size() + " bookings from file");
            } catch (Exception e) {
                System.err.println("Error loading bookings: " + e.getMessage());
                bookingHistory = new ArrayList<>();
            }
        } else {
            System.out.println("No previous bookings found. Starting fresh.");
        }
    }
    
    private void saveBookings() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BOOKINGS_FILE))) {
            oos.writeObject(bookingHistory);
            oos.flush();
        } catch (IOException e) {
            System.err.println("Error saving bookings: " + e.getMessage());
        }
    }
    
    private void loadGuests() {
        File file = new File(GUESTS_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                guestDatabase = (List<GuestRecord>) ois.readObject();
                System.out.println("Loaded " + guestDatabase.size() + " guest records");
            } catch (Exception e) {
                System.err.println("Error loading guests: " + e.getMessage());
                guestDatabase = new ArrayList<>();
            }
        }
    }
    
    private void saveGuests() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(GUESTS_FILE))) {
            oos.writeObject(guestDatabase);
            oos.flush();
        } catch (IOException e) {
            System.err.println("Error saving guests: " + e.getMessage());
        }
    }
    
    private void loadRoomInventory() {
        File roomFile = new File(ROOMS_FILE);
        if (roomFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(roomFile))) {
                roomInventory = (List<RoomUnit>) ois.readObject();
                System.out.println("Loaded " + roomInventory.size() + " rooms from inventory");
            } catch (InvalidClassException e) {
                // This happens when serialVersionUID changes - reset to default
                System.out.println("Room inventory format changed. Resetting to default inventory...");
                initializeDefaultRoomInventory();
            } catch (Exception e) {
                System.err.println("Failed to load room inventory, initializing default: " + e.getMessage());
                initializeDefaultRoomInventory();
            }
        } else {
            initializeDefaultRoomInventory();
        }
    }
    
    private void saveRoomInventory() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ROOMS_FILE))) {
            oos.writeObject(roomInventory);
            oos.flush();
        } catch (IOException e) {
            System.err.println("Failed to save room inventory: " + e.getMessage());
        }
    }
    
    private void initializeDefaultRoomInventory() {
        String[][] roomData = {
            {"Classic Room", "2", "5000", "Available", "Perfect for 2 guests with garden view."},
            {"Standard Deluxe Room", "2-4", "6500", "Available", "Ideal for couples, includes balcony."},
            {"Premier Deluxe Room", "4", "8000", "Available", "Spacious room with pool view."},
            {"Executive Suite", "5", "10000", "Available", "Executive desk and lounge access."},
            {"Family Suite", "8-10", "12000", "Available", "Fits 4 people, kids stay free."},
            {"Presidential Suite", "10-15", "20000", "Available", "Luxury amenities and top floor view."}
        };
        
        for (String[] room : roomData) {
            for (int i = 1; i <= 3; i++) {
                RoomUnit unit = new RoomUnit(
                    generateRoomId(room[0], i),
                    room[0],
                    room[1],
                    Integer.parseInt(room[2]),
                    room[3],
                    "Floor " + ((i % 3) + 1),
                    room[4]
                );
                roomInventory.add(unit);
            }
        }
        saveRoomInventory();
    }
    
    private String generateRoomId(String roomType, int number) {
        String prefix = roomType.substring(0, 3).toUpperCase();
        return prefix + "-" + String.format("%03d", number);
    }
    
    public void addBooking(BookingRecord booking) {
        bookingHistory.add(booking);
        
        String roomType = booking.getRoomType();
        if (roomStats.containsKey(roomType)) {
            roomStats.get(roomType).incrementBookings();
            roomStats.get(roomType).addRevenue(booking.getTotalAmount());
        }
        
        saveBookings();
        System.out.println("Booking saved: " + booking.getReceiptNumber() + " - " + booking.getGuestName());
    }
    
    public List<GuestRecord> getAllGuests() {
        return new ArrayList<>(guestDatabase);
    }
    
    public GuestRecord getGuestByEmail(String email) {
        for (GuestRecord guest : guestDatabase) {
            if (guest.getEmail().equalsIgnoreCase(email)) {
                return guest;
            }
        }
        return null;
    }
    
    public GuestRecord getGuestByPhone(String phone) {
        for (GuestRecord guest : guestDatabase) {
            if (guest.getPhone().equals(phone)) {
                return guest;
            }
        }
        return null;
    }
    
    public List<GuestRecord> searchGuests(String query) {
        List<GuestRecord> results = new ArrayList<>();
        String searchQuery = query.toLowerCase();
        
        for (GuestRecord guest : guestDatabase) {
            if (guest.getFirstName().toLowerCase().contains(searchQuery) ||
                guest.getLastName().toLowerCase().contains(searchQuery) ||
                guest.getEmail().toLowerCase().contains(searchQuery) ||
                guest.getPhone().contains(searchQuery) ||
                guest.getCity().toLowerCase().contains(searchQuery)) {
                results.add(guest);
            }
        }
        return results;
    }
    
    public boolean addGuest(GuestRecord guest) {
        if (getGuestByEmail(guest.getEmail()) != null) {
            return false;
        }
        guestDatabase.add(guest);
        saveGuests();
        return true;
    }
    
    public boolean updateGuest(String email, GuestRecord updatedGuest) {
        for (int i = 0; i < guestDatabase.size(); i++) {
            if (guestDatabase.get(i).getEmail().equalsIgnoreCase(email)) {
                guestDatabase.set(i, updatedGuest);
                saveGuests();
                return true;
            }
        }
        return false;
    }
    
    public boolean deleteGuest(String email) {
        boolean removed = guestDatabase.removeIf(guest -> guest.getEmail().equalsIgnoreCase(email));
        if (removed) {
            saveGuests();
        }
        return removed;
    }
    
    public int getTotalGuests() {
        return guestDatabase.size();
    }
    
    public Map<String, Integer> getGuestsByCity() {
        Map<String, Integer> cityCount = new HashMap<>();
        for (GuestRecord guest : guestDatabase) {
            String city = guest.getCity();
            cityCount.put(city, cityCount.getOrDefault(city, 0) + 1);
        }
        return cityCount;
    }
    
    public List<RoomUnit> getAllRoomUnits() {
        return new ArrayList<>(roomInventory);
    }
    
    public List<RoomUnit> getRoomUnitsByType(String roomType) {
        List<RoomUnit> units = new ArrayList<>();
        for (RoomUnit unit : roomInventory) {
            if (unit.getRoomType().equals(roomType)) {
                units.add(unit);
            }
        }
        return units;
    }
    
    public RoomUnit getRoomUnitById(String roomId) {
        for (RoomUnit unit : roomInventory) {
            if (unit.getRoomId().equals(roomId)) {
                return unit;
            }
        }
        return null;
    }
    
    public boolean addRoomUnit(RoomUnit unit) {
        if (getRoomUnitById(unit.getRoomId()) != null) {
            return false;
        }
        roomInventory.add(unit);
        saveRoomInventory();
        syncWithRoomModel();
        return true;
    }
    
    public boolean updateRoomUnit(String roomId, RoomUnit updatedUnit) {
        for (int i = 0; i < roomInventory.size(); i++) {
            if (roomInventory.get(i).getRoomId().equals(roomId)) {
                roomInventory.set(i, updatedUnit);
                saveRoomInventory();
                syncWithRoomModel();
                return true;
            }
        }
        return false;
    }
    
    public boolean deleteRoomUnit(String roomId) {
        boolean removed = roomInventory.removeIf(unit -> unit.getRoomId().equals(roomId));
        if (removed) {
            saveRoomInventory();
            syncWithRoomModel();
        }
        return removed;
    }
    
    public boolean updateRoomStatus(String roomId, String newStatus) {
        RoomUnit unit = getRoomUnitById(roomId);
        if (unit != null) {
            unit.setStatus(newStatus);
            saveRoomInventory();
            syncWithRoomModel();
            return true;
        }
        return false;
    }
    
    private void syncWithRoomModel() {
        if (roomModel != null) {
            roomModel.updateFromAdminInventory(roomInventory);
        }
    }
    
    public int getAvailableRoomCount(String roomType) {
        int count = 0;
        for (RoomUnit unit : roomInventory) {
            if (unit.getRoomType().equals(roomType) && 
                unit.getStatus().equals("Available")) {
                count++;
            }
        }
        return count;
    }
    
    public Map<String, Integer> getRoomCountByStatus() {
        Map<String, Integer> statusCount = new HashMap<>();
        for (RoomUnit unit : roomInventory) {
            String status = unit.getStatus();
            statusCount.put(status, statusCount.getOrDefault(status, 0) + 1);
        }
        return statusCount;
    }
    
    public List<BookingRecord> getAllBookings() {
        return new ArrayList<>(bookingHistory);
    }
    
    public List<BookingRecord> getBookingsByDateRange(Date startDate, Date endDate) {
        List<BookingRecord> filtered = new ArrayList<>();
        for (BookingRecord booking : bookingHistory) {
            if (booking.getBookingDate().compareTo(startDate) >= 0 &&
                booking.getBookingDate().compareTo(endDate) <= 0) {
                filtered.add(booking);
            }
        }
        return filtered;
    }
    
    public List<BookingRecord> getRecentBookings(int count) {
        List<BookingRecord> recent = new ArrayList<>();
        int start = Math.max(0, bookingHistory.size() - count);
        for (int i = start; i < bookingHistory.size(); i++) {
            recent.add(bookingHistory.get(i));
        }
        return recent;
    }
    
    public Map<String, RoomStatistics> getRoomStatistics() {
        return new HashMap<>(roomStats);
    }
    
    public Map<String, RoomAvailabilityStats> getRoomAvailabilityStatistics() {
        Map<String, RoomAvailabilityStats> availStats = new HashMap<>();
        
        Set<String> roomTypes = new HashSet<>();
        for (RoomUnit unit : roomInventory) {
            roomTypes.add(unit.getRoomType());
        }
        
        for (String roomType : roomTypes) {
            int total = 0;
            int available = 0;
            int occupied = 0;
            int maintenance = 0;
            int outOfService = 0;
            
            for (RoomUnit unit : roomInventory) {
                if (unit.getRoomType().equals(roomType)) {
                    total++;
                    String status = unit.getStatus();
                    if ("Available".equals(status)) {
                        available++;
                    } else if ("Occupied".equals(status)) {
                        occupied++;
                    } else if ("Maintenance".equals(status)) {
                        maintenance++;
                    } else if ("Out of Service".equals(status)) {
                        outOfService++;
                    }
                }
            }
            
            availStats.put(roomType, new RoomAvailabilityStats(
                roomType, total, available, occupied, maintenance, outOfService
            ));
        }
        
        return availStats;
    }
    
    public double getTotalRevenue() {
        double total = 0;
        for (BookingRecord booking : bookingHistory) {
            total += booking.getTotalAmount();
        }
        return total;
    }
    
    public int getTotalBookings() {
        return bookingHistory.size();
    }
    
    public boolean updateBookingStatus(String receiptNumber, String newStatus) {
        for (BookingRecord booking : bookingHistory) {
            if (booking.getReceiptNumber().equals(receiptNumber)) {
                booking.setStatus(newStatus);
                saveBookings();
                return true;
            }
        }
        return false;
    }
    
    public BookingRecord getBookingByReceipt(String receiptNumber) {
        for (BookingRecord booking : bookingHistory) {
            if (booking.getReceiptNumber().equals(receiptNumber)) {
                return booking;
            }
        }
        return null;
    }
    
    public String getMostPopularRoom() {
        String mostPopular = "";
        int maxBookings = 0;
        
        for (Map.Entry<String, RoomStatistics> entry : roomStats.entrySet()) {
            if (entry.getValue().getBookingCount() > maxBookings) {
                maxBookings = entry.getValue().getBookingCount();
                mostPopular = entry.getKey();
            }
        }
        
        return mostPopular.isEmpty() ? "N/A" : mostPopular;
    }
    
    public double getAverageBookingValue() {
        if (bookingHistory.isEmpty()) return 0;
        return getTotalRevenue() / bookingHistory.size();
    }
    
    public int getBookingsToday() {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        
        int count = 0;
        for (BookingRecord booking : bookingHistory) {
            if (booking.getBookingDate().after(today.getTime())) {
                count++;
            }
        }
        return count;
    }
    
    public void clearAllBookings() {
        bookingHistory.clear();
        for (RoomStatistics stat : roomStats.values()) {
            stat.reset();
        }
        saveBookings();
    }
    
    public static class BookingRecord implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private String receiptNumber;
        private String guestName;
        private String email;
        private String roomType;
        private String checkInDate;
        private String checkOutDate;
        private double totalAmount;
        private Date bookingDate;
        private String paymentMethod;
        private String status = "Pending";
        
        public BookingRecord(String receiptNumber, String guestName, String email, String roomType,
                           String checkInDate, String checkOutDate, double totalAmount,
                           Date bookingDate, String paymentMethod) {
            this.receiptNumber = receiptNumber;
            this.guestName = guestName;
            this.email = email;
            this.roomType = roomType;
            this.checkInDate = checkInDate;
            this.checkOutDate = checkOutDate;
            this.totalAmount = totalAmount;
            this.bookingDate = bookingDate;
            this.paymentMethod = paymentMethod;
            this.status = "Pending";
        }
        
        public String getReceiptNumber() { return receiptNumber; }
        public String getGuestName() { return guestName; }
        public String getEmail() { return email; }
        public String getRoomType() { return roomType; }
        public String getCheckInDate() { return checkInDate; }
        public String getCheckOutDate() { return checkOutDate; }
        public double getTotalAmount() { return totalAmount; }
        public Date getBookingDate() { return bookingDate; }
        public String getPaymentMethod() { return paymentMethod; }
        
        public String getStatus() {
            return status != null ? status : "Pending";
        }
        
        public void setStatus(String status) {
            this.status = status;
        }
    }
    
    public static class RoomStatistics {
        private String roomType;
        private int bookingCount;
        private double totalRevenue;
        
        public RoomStatistics(String roomType) {
            this.roomType = roomType;
            this.bookingCount = 0;
            this.totalRevenue = 0;
        }
        
        public void incrementBookings() {
            this.bookingCount++;
        }
        
        public void addRevenue(double amount) {
            this.totalRevenue += amount;
        }
        
        public void reset() {
            this.bookingCount = 0;
            this.totalRevenue = 0;
        }
        
        public String getRoomType() { return roomType; }
        public int getBookingCount() { return bookingCount; }
        public double getTotalRevenue() { return totalRevenue; }
    }
    
 // ✅ ENHANCED RoomUnit class with multiple image support
    public static class RoomUnit implements Serializable {
        private static final long serialVersionUID = 3L; // USE 3 to match existing data
        
        private String roomId;
        private String roomType;
        private String capacity;
        private int basePrice;
        private String status;
        private String location;
        private String description;
        private Date lastUpdated;
        private String imagePath;
        private java.util.List<String> imagePaths = new ArrayList<>(); // NEW: Multiple images
        
        public RoomUnit(String roomId, String roomType, String capacity, 
                       int basePrice, String status, String location, String description) {
            this.roomId = roomId;
            this.roomType = roomType;
            this.capacity = capacity;
            this.basePrice = basePrice;
            this.status = status;
            this.location = location;
            this.description = description;
            this.lastUpdated = new Date();
            this.imagePath = null;
            this.imagePaths = new ArrayList<>();
        }
        
        // Getters
        public String getRoomId() { return roomId; }
        public String getRoomType() { return roomType; }
        public String getCapacity() { return capacity; }
        public int getBasePrice() { return basePrice; }
        public String getStatus() { return status; }
        public String getLocation() { return location; }
        public String getDescription() { return description; }
        public Date getLastUpdated() { return lastUpdated; }
        public String getImagePath() { return imagePath; }
        
        // NEW: Multiple images getter
        public java.util.List<String> getImagePaths() { 
            if (imagePaths == null) {
                imagePaths = new ArrayList<>(); // Initialize if null (for old serialized objects)
            }
            return new ArrayList<>(imagePaths); // Return copy to prevent external modification
        }
        
        // Setters
        public void setRoomId(String roomId) { 
            this.roomId = roomId;
            this.lastUpdated = new Date();
        }
        
        public void setRoomType(String roomType) { 
            this.roomType = roomType;
            this.lastUpdated = new Date();
        }
        
        public void setCapacity(String capacity) { 
            this.capacity = capacity;
            this.lastUpdated = new Date();
        }
        
        public void setBasePrice(int basePrice) { 
            this.basePrice = basePrice;
            this.lastUpdated = new Date();
        }
        
        public void setStatus(String status) { 
            this.status = status;
            this.lastUpdated = new Date();
        }
        
        public void setLocation(String location) { 
            this.location = location;
            this.lastUpdated = new Date();
        }
        
        public void setDescription(String description) {
            this.description = description;
            this.lastUpdated = new Date();
        }
        
        public void setImagePath(String imagePath) {
            this.imagePath = imagePath;
            this.lastUpdated = new Date();
            // Also add to image paths for consistency
            if (imagePath != null && !imagePath.isEmpty()) {
                if (this.imagePaths == null) {
                    this.imagePaths = new ArrayList<>();
                }
                if (!this.imagePaths.contains(imagePath)) {
                    this.imagePaths.add(imagePath);
                }
            }
        }
        
        // NEW: Multiple images setter
        public void setImagePaths(java.util.List<String> imagePaths) {
            if (imagePaths != null) {
                this.imagePaths = new ArrayList<>(imagePaths); // Store copy
                // Update single image path for backward compatibility
                if (!imagePaths.isEmpty()) {
                    this.imagePath = imagePaths.get(0);
                } else {
                    this.imagePath = null;
                }
            } else {
                this.imagePaths = new ArrayList<>();
                this.imagePath = null;
            }
            this.lastUpdated = new Date();
        }
        
        // NEW: Helper method to add single image
        public void addImagePath(String imagePath) {
            if (imagePath != null && !imagePath.isEmpty()) {
                if (this.imagePaths == null) {
                    this.imagePaths = new ArrayList<>();
                }
                if (!this.imagePaths.contains(imagePath)) {
                    this.imagePaths.add(imagePath);
                    // Update single image path if it's the first one
                    if (this.imagePath == null) {
                        this.imagePath = imagePath;
                    }
                }
                this.lastUpdated = new Date();
            }
        }
        
        // NEW: Helper method to remove image
        public boolean removeImagePath(String imagePath) {
            if (this.imagePaths != null && this.imagePaths.remove(imagePath)) {
                // Update single image path if we removed the current one
                if (imagePath.equals(this.imagePath)) {
                    this.imagePath = this.imagePaths.isEmpty() ? null : this.imagePaths.get(0);
                }
                this.lastUpdated = new Date();
                return true;
            }
            return false;
        }
        
        // NEW: Helper method to get image count
        public int getImageCount() {
            return imagePaths != null ? imagePaths.size() : 0;
        }
        
        // NEW: Helper method to check if room has images
        public boolean hasImages() {
            return imagePaths != null && !imagePaths.isEmpty();
        }
        
        // Handle old serialized objects that don't have imagePaths field
        private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
            ois.defaultReadObject();
            // Initialize imagePaths if it's null (for objects serialized before this field was added)
            if (imagePaths == null) {
                imagePaths = new ArrayList<>();
                // If there's an imagePath, add it to imagePaths for consistency
                if (imagePath != null && !imagePath.isEmpty()) {
                    imagePaths.add(imagePath);
                }
            }
        }
    }
    
    public static class GuestRecord implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private String prefix;
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private String mobile;
        private String address1;
        private String address2;
        private String city;
        private String zipCode;
        private String country;
        private Date registrationDate;
        
        public GuestRecord(String prefix, String firstName, String lastName, String email,
                          String phone, String mobile, String address1, String address2,
                          String city, String zipCode, String country) {
            this.prefix = prefix;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.phone = phone;
            this.mobile = mobile;
            this.address1 = address1;
            this.address2 = address2;
            this.city = city;
            this.zipCode = zipCode;
            this.country = country;
            this.registrationDate = new Date();
        }
        
        public String getPrefix() { return prefix; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getEmail() { return email; }
        public String getPhone() { return phone; }
        public String getMobile() { return mobile; }
        public String getAddress1() { return address1; }
        public String getAddress2() { return address2; }
        public String getCity() { return city; }
        public String getZipCode() { return zipCode; }
        public String getCountry() { return country; }
        public Date getRegistrationDate() { return registrationDate; }
        
        public String getFullName() {
            return prefix + " " + firstName + " " + lastName;
        }
        
        public String getFullAddress() {
            return address1 + ", " + address2 + ", " + city + " " + zipCode + ", " + country;
        }
    }
    
    public static class RoomAvailabilityStats {
        private String roomType;
        private int totalRooms;
        private int availableRooms;
        private int occupiedRooms;
        private int maintenanceRooms;
        private int outOfServiceRooms;
        
        public RoomAvailabilityStats(String roomType, int totalRooms, int availableRooms, 
                                     int occupiedRooms, int maintenanceRooms, int outOfServiceRooms) {
            this.roomType = roomType;
            this.totalRooms = totalRooms;
            this.availableRooms = availableRooms;
            this.occupiedRooms = occupiedRooms;
            this.maintenanceRooms = maintenanceRooms;
            this.outOfServiceRooms = outOfServiceRooms;
        }
        
        public String getRoomType() { return roomType; }
        public int getTotalRooms() { return totalRooms; }
        public int getAvailableRooms() { return availableRooms; }
        public int getOccupiedRooms() { return occupiedRooms; }
        public int getMaintenanceRooms() { return maintenanceRooms; }
        public int getOutOfServiceRooms() { return outOfServiceRooms; }
        
        public double getOccupancyRate() {
            if (totalRooms == 0) return 0.0;
            return (occupiedRooms * 100.0) / totalRooms;
        }
        
        public double getAvailabilityRate() {
            if (totalRooms == 0) return 0.0;
            return (availableRooms * 100.0) / totalRooms;
        }
    }
}