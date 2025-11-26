package undouse_hotel.model;

import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

public class GuestModel {
    private static final String GUESTS_FILE = "guests_data.dat";
    private static final String PAYMENTS_FILE = "payments_data.dat";
    
    private AdminModel adminModel; 
    private List<GuestRecord> guestDatabase;
    private List<BookingPaymentRecord> paymentDatabase;
    
    public GuestModel(AdminModel adminModel) {  
        this.adminModel = adminModel;
        this.guestDatabase = new ArrayList<>();
        this.paymentDatabase = new ArrayList<>();
        
        loadGuestData();
        loadPaymentData();
    }
    
    private void loadGuestData() {
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

    private void saveGuestData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(GUESTS_FILE))) {
            oos.writeObject(guestDatabase);
            oos.flush();
        } catch (IOException e) {
            System.err.println("Error saving guests: " + e.getMessage());
        }
    }
    
    private void loadPaymentData() {
        File file = new File(PAYMENTS_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                paymentDatabase = (List<BookingPaymentRecord>) ois.readObject();
                System.out.println("Loaded " + paymentDatabase.size() + " payment records");
            } catch (Exception e) {
                System.err.println("Error loading payments: " + e.getMessage());
                paymentDatabase = new ArrayList<>();
            }
        }
    }
    
    private void savePaymentData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PAYMENTS_FILE))) {
            oos.writeObject(paymentDatabase);
            oos.flush();
        } catch (IOException e) {
            System.err.println("Error saving payments: " + e.getMessage());
        }
    }
    
    
    public void saveGuestData(GuestRecord guest) {

        GuestRecord existingGuest = findGuestByEmail(guest.getEmail());
        if (existingGuest != null) {

            updateGuestInfo(existingGuest, guest);
        } else {
   
            guestDatabase.add(guest);
        }
        saveGuestData();
        
      
        if (adminModel != null) {
            AdminModel.GuestRecord adminGuest = new AdminModel.GuestRecord(
                guest.getPrefix(),
                guest.getFirstName(),
                guest.getLastName(),
                guest.getEmail(),
                guest.getPhone(),
                guest.getMobile(),
                guest.getAddress1(),
                guest.getAddress2(),
                guest.getCity(),
                guest.getZipCode(),
                guest.getCountry()
            );
            adminModel.addGuest(adminGuest);
        }
        
        System.out.println("Guest saved: " + guest.getFirstName() + " " + guest.getLastName());
    }
    
    private void updateGuestInfo(GuestRecord existing, GuestRecord updated) {
        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        existing.setPhone(updated.getPhone());
        existing.setMobile(updated.getMobile());
        existing.setAddress1(updated.getAddress1());
        existing.setAddress2(updated.getAddress2());
        existing.setCity(updated.getCity());
        existing.setZipCode(updated.getZipCode());
        existing.setCountry(updated.getCountry());
    }
    
    public void saveBookingPayment(BookingPaymentRecord payment) {
        paymentDatabase.add(payment);
        savePaymentData();
        System.out.println("Payment recorded: " + payment.getReceiptNumber());
    }
    
    public List<GuestRecord> getAllGuests() {
        return new ArrayList<>(guestDatabase);
    }
    
    public List<BookingPaymentRecord> getAllPayments() {
        return new ArrayList<>(paymentDatabase);
    }
    
    public GuestRecord findGuestByEmail(String email) {
        for (GuestRecord guest : guestDatabase) {
            if (guest.getEmail().equalsIgnoreCase(email)) {
                return guest;
            }
        }
        return null;
    }
    
    public GuestRecord findGuestByPhone(String phone) {
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
                guest.getMobile().contains(searchQuery) ||
                guest.getCity().toLowerCase().contains(searchQuery)) {
                results.add(guest);
            }
        }
        return results;
    }
    
    public int getTotalGuests() {
        return guestDatabase.size();
    }
    
    public Map<String, Integer> getGuestsByCity() {
        Map<String, Integer> cityStats = new HashMap<>();
        for (GuestRecord guest : guestDatabase) {
            String city = guest.getCity();
            cityStats.put(city, cityStats.getOrDefault(city, 0) + 1);
        }
        return cityStats;
    }
    
    public Map<String, Integer> getGuestsByCountry() {
        Map<String, Integer> countryStats = new HashMap<>();
        for (GuestRecord guest : guestDatabase) {
            String country = guest.getCountry();
            countryStats.put(country, countryStats.getOrDefault(country, 0) + 1);
        }
        return countryStats;
    }
    
    public BookingPaymentRecord getBookingByReceipt(String receiptNumber) {
        for (BookingPaymentRecord payment : paymentDatabase) {
            if (payment.getReceiptNumber().equals(receiptNumber)) {
                return payment;
            }
        }
        return null;
    }
    
    public List<BookingPaymentRecord> getGuestBookings(String email) {
        List<BookingPaymentRecord> bookings = new ArrayList<>();
        for (BookingPaymentRecord payment : paymentDatabase) {
            if (payment.getGuestEmail().equalsIgnoreCase(email)) {
                bookings.add(payment);
            }
        }
        return bookings;
    }
    
    public List<BookingPaymentRecord> getRecentBookings(int count) {
        List<BookingPaymentRecord> recent = new ArrayList<>();
        int start = Math.max(0, paymentDatabase.size() - count);
        for (int i = start; i < paymentDatabase.size(); i++) {
            recent.add(paymentDatabase.get(i));
        }
        return recent;
    }
    
    public double getTotalRevenue() {
        double total = 0;
        for (BookingPaymentRecord payment : paymentDatabase) {
            total += payment.getAmountPaid();
        }
        return total;
    }
    
    public int getTotalBookings() {
        return paymentDatabase.size();
    }
    
    public Map<String, Integer> getBookingsByRoomType() {
        Map<String, Integer> roomTypeStats = new HashMap<>();
        for (BookingPaymentRecord payment : paymentDatabase) {
            String roomType = payment.getRoomType();
            roomTypeStats.put(roomType, roomTypeStats.getOrDefault(roomType, 0) + 1);
        }
        return roomTypeStats;
    }
    
    public boolean updateBookingStatus(String receiptNumber, String newStatus) {
        BookingPaymentRecord booking = getBookingByReceipt(receiptNumber);
        if (booking != null) {
            booking.setBookingStatus(newStatus);
            savePaymentData();
            return true;
        }
        return false;
    }
    
    public boolean cancelBooking(String receiptNumber) {
        for (int i = 0; i < paymentDatabase.size(); i++) {
            BookingPaymentRecord payment = paymentDatabase.get(i);
            if (payment.getReceiptNumber().equals(receiptNumber)) {
                paymentDatabase.remove(i);
                savePaymentData();
                return true;
            }
        }
        return false;
    }
    public boolean canCancelBooking(String receiptNumber) {
        BookingPaymentRecord booking = getBookingByReceipt(receiptNumber);
        if (booking == null) {
            return false;
        }
        
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy");
            Date checkInDate = sdf.parse(booking.getCheckInDate());
            Date today = new Date();
            
            long diffInMillis = checkInDate.getTime() - today.getTime();
            long diffInDays = diffInMillis / (1000 * 60 * 60 * 24);
            
            return diffInDays >= 3;
        } catch (Exception e) {
            System.err.println("Error checking cancellation eligibility: " + e.getMessage());
            return false;
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
        
        public void setFirstName(String firstName) { this.firstName = firstName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        public void setPhone(String phone) { this.phone = phone; }
        public void setMobile(String mobile) { this.mobile = mobile; }
        public void setAddress1(String address1) { this.address1 = address1; }
        public void setAddress2(String address2) { this.address2 = address2; }
        public void setCity(String city) { this.city = city; }
        public void setZipCode(String zipCode) { this.zipCode = zipCode; }
        public void setCountry(String country) { this.country = country; }
        
        public String getFullName() {
            return prefix + " " + firstName + " " + lastName;
        }
        
        public String getFullAddress() {
            return address1 + ", " + address2 + ", " + city + " " + zipCode + ", " + country;
        }
    }
    
    public static class BookingPaymentRecord implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private String receiptNumber;
        private String guestEmail;
        private String guestName;
        private String roomType;
        private int numberOfRooms;
        private int numberOfGuests;
        private String checkInDate;
        private String checkOutDate;
        private String paymentMethod;
        private String accountName;
        private String referenceNumber;
        private double amountPaid;
        private Date paymentDate;
        private String bookingStatus;
        
        public BookingPaymentRecord(String receiptNumber, String guestEmail, String guestName,
                                   String roomType, int numberOfRooms, int numberOfGuests,
                                   String checkInDate, String checkOutDate,
                                   String paymentMethod, String accountName, String referenceNumber,
                                   double amountPaid) {
            this.receiptNumber = receiptNumber;
            this.guestEmail = guestEmail;
            this.guestName = guestName;
            this.roomType = roomType;
            this.numberOfRooms = numberOfRooms;
            this.numberOfGuests = numberOfGuests;
            this.checkInDate = checkInDate;
            this.checkOutDate = checkOutDate;
            this.paymentMethod = paymentMethod;
            this.accountName = accountName;
            this.referenceNumber = referenceNumber;
            this.amountPaid = amountPaid;
            this.paymentDate = new Date();
            this.bookingStatus = "CONFIRMED";
        }
       
        public String getReceiptNumber() { return receiptNumber; }
        public String getGuestEmail() { return guestEmail; }
        public String getGuestName() { return guestName; }
        public String getRoomType() { return roomType; }
        public int getNumberOfRooms() { return numberOfRooms; }
        public int getNumberOfGuests() { return numberOfGuests; }
        public String getCheckInDate() { return checkInDate; }
        public String getCheckOutDate() { return checkOutDate; }
        public String getPaymentMethod() { return paymentMethod; }
        public String getAccountName() { return accountName; }
        public String getReferenceNumber() { return referenceNumber; }
        public double getAmountPaid() { return amountPaid; }
        public Date getPaymentDate() { return paymentDate; }
        public String getBookingStatus() { return bookingStatus; }
        
        public void setBookingStatus(String status) { this.bookingStatus = status; }
    }
}