package undouse_hotel.model;

public class BookingModel {
    private int numRooms;
    private int numAdults;
    private int numChildren;
    private String checkInDate;
    private String checkOutDate;
    private String checkInDateFormatted;
    private String checkOutDateFormatted;
    private int currentRoomIndex;
    
    public BookingModel() {
        this.numRooms = 1;
        this.numAdults = 1;
        this.numChildren = 0;
        this.checkInDate = "";
        this.checkOutDate = "";
        this.checkInDateFormatted = "";
        this.checkOutDateFormatted = "";
        this.currentRoomIndex = 1;
    }
    
    public int getNumRooms() { return numRooms; }
    public int getNumAdults() { return numAdults; }
    public int getNumChildren() { return numChildren; }
    public String getCheckInDate() { return checkInDate; }
    public String getCheckOutDate() { return checkOutDate; }
    public String getCheckInDateFormatted() { return checkInDateFormatted; }
    public String getCheckOutDateFormatted() { return checkOutDateFormatted; }
    public int getCurrentRoomIndex() { return currentRoomIndex; }
    public int getTotalGuests() { return numAdults + numChildren; }
    
    public void setNumRooms(int numRooms) { this.numRooms = numRooms; }
    public void setNumAdults(int numAdults) { this.numAdults = numAdults; }
    public void setNumChildren(int numChildren) { this.numChildren = numChildren; }
    public void setCheckInDate(String date) { this.checkInDate = date; }
    public void setCheckOutDate(String date) { this.checkOutDate = date; }
    public void setCheckInDateFormatted(String date) { this.checkInDateFormatted = date; }
    public void setCheckOutDateFormatted(String date) { this.checkOutDateFormatted = date; }
    public void setCurrentRoomIndex(int index) { this.currentRoomIndex = index; }
    
    public void incrementRoomIndex() { this.currentRoomIndex++; }
    public void decrementRoomIndex() { if (currentRoomIndex > 1) this.currentRoomIndex--; }
    
    public void resetBooking() {
        this.currentRoomIndex = 1;
    }
    
    public boolean isValidBooking() {
        return !checkInDate.isEmpty() && !checkOutDate.isEmpty() && 
               getTotalGuests() > 0 && numRooms > 0;
    }
}