package undouse_hotel.model;

import java.util.*;
import undouse_hotel.model.AdminModel.RoomUnit;

public class RoomModel {
    private Map<String, String> roomNameMap;
    private Map<String, Integer> roomCapacities;
    private Map<Integer, RoomSelection> selectedRooms;
    private Map<String, RoomData> roomDatabase;
    private List<UpdateListener> listeners;
    
    public interface UpdateListener {
        void onRoomDataUpdated();
    }
    
    public RoomModel() {
        this.roomNameMap = new HashMap<>();
        this.roomCapacities = new HashMap<>();
        this.selectedRooms = new HashMap<>();
        this.roomDatabase = new HashMap<>();
        this.listeners = new ArrayList<>();
        initializeRoomData();
    }
    
    private void initializeRoomData() {
        roomNameMap.put("Family Suite", "Family Suite");
        roomNameMap.put("Premier Deluxe Room", "Premier Deluxe Room");
        roomNameMap.put("Classic Room", "Classic Room");
        
        roomCapacities.put("Classic Room", 2);
        roomCapacities.put("Standard Deluxe Room", 2);
        roomCapacities.put("Premier Deluxe Room", 4);
        roomCapacities.put("Executive Suite", 3);
        roomCapacities.put("Family Suite", 5);
        roomCapacities.put("Presidential Suite", 6);
        
        roomDatabase.put("Classic Room", new RoomData("Classic Room", 5000, "Perfect for 2 guests with garden view.", 10));
        roomDatabase.put("Standard Deluxe Room", new RoomData("Standard Deluxe Room", 6500, "Ideal for couples, includes balcony.", 12));
        roomDatabase.put("Premier Deluxe Room", new RoomData("Premier Deluxe Room", 8000, "Spacious room with pool view.", 15));
        roomDatabase.put("Executive Suite", new RoomData("Executive Suite", 10000, "Executive desk and lounge access.", 18));
        roomDatabase.put("Family Suite", new RoomData("Family Suite", 12000, "Fits 4 people, kids stay free.", 20));
        roomDatabase.put("Presidential Suite", new RoomData("Presidential Suite", 20000, "Luxury amenities and top floor view.", 25));
    }
    
    public void addUpdateListener(UpdateListener listener) {
        listeners.add(listener);
    }
    
    public void removeUpdateListener(UpdateListener listener) {
        listeners.remove(listener);
    }
    
    private void notifyListeners() {
        for (UpdateListener listener : listeners) {
            listener.onRoomDataUpdated();
        }
    }
    
    /**
     * Update room data from admin inventory - SINGLE SOURCE OF TRUTH
     * This method synchronizes RoomsPanelView with AdminModel changes
     */
    public void updateFromAdminInventory(List<RoomUnit> inventory) {
        if (inventory == null || inventory.isEmpty()) {
            System.err.println("Warning: Admin inventory is empty or null");
            return;
        }
        
        Map<String, RoomData> updatedData = new HashMap<>();
        Map<String, Integer> roomCounts = new HashMap<>();
        
        for (RoomUnit unit : inventory) {
            String roomType = unit.getRoomType();
            
            if (!updatedData.containsKey(roomType)) {
                updatedData.put(roomType, new RoomData(
                    roomType,
                    unit.getBasePrice(),
                    unit.getDescription(),
                    1
                ));
                roomCounts.put(roomType, 1);
            } else {
                roomCounts.put(roomType, roomCounts.get(roomType) + 1);
            }
        }
        
        for (Map.Entry<String, Integer> entry : roomCounts.entrySet()) {
            RoomData data = updatedData.get(entry.getKey());
            if (data != null) {
                data.available = entry.getValue();
            }
        }
        
        roomDatabase.clear();
        roomDatabase.putAll(updatedData);
        
        notifyListeners();
    }
    
    public String[][] getAllRooms() {
        List<String[]> rooms = new ArrayList<>();
        for (RoomData data : roomDatabase.values()) {
            rooms.add(new String[]{
                data.roomType,
                "₱" + String.format("%,d", data.basePrice),
                String.valueOf(data.available),
                data.description
            });
        }
        return rooms.toArray(new String[0][]);
    }
    
    public String[][] getRoomsPanelData() {
        List<String[]> rooms = new ArrayList<>();
        for (RoomData data : roomDatabase.values()) {
            rooms.add(new String[]{
                data.roomType,
                "₱" + String.format("%,d", data.basePrice),
                data.description
            });
        }
        return rooms.toArray(new String[0][]);
    }
    
    public int getRoomCapacity(String roomType) {
        return roomCapacities.getOrDefault(roomType, 0);
    }
    
    public String getRoomName(String key) {
        return roomNameMap.get(key);
    }
    
    public String getRoomDescription(String roomType) {
        RoomData data = roomDatabase.get(roomType);
        return data != null ? data.description : "";
    }
    
    public int getRoomBasePrice(String roomType) {
        RoomData data = roomDatabase.get(roomType);
        return data != null ? data.basePrice : 0;
    }
    
    public void addSelectedRoom(int index, RoomSelection room) {
        selectedRooms.put(index, room);
    }
    
    public void removeSelectedRoom(int index) {
        selectedRooms.remove(index);
    }
    
    public RoomSelection getSelectedRoom(int index) {
        return selectedRooms.get(index);
    }
    
    public Map<Integer, RoomSelection> getAllSelectedRooms() {
        return selectedRooms;
    }
    
    public void clearSelectedRooms() {
        selectedRooms.clear();
    }
    
    public boolean hasSelectedRoom(int index) {
        return selectedRooms.containsKey(index);
    }
    
    public boolean hasAnySelectedRooms() {
        return !selectedRooms.isEmpty();
    }
    
    private static class RoomData {
        String roomType;
        int basePrice;
        String description;
        int available;
        
        RoomData(String roomType, int basePrice, String description, int available) {
            this.roomType = roomType;
            this.basePrice = basePrice;
            this.description = description;
            this.available = available;
        }
    }
    
    public static class RoomSelection {
        public String title;
        public String description;
        public int basePrice;
        public double taxes;
        
        public RoomSelection(String title, String description, int basePrice, double taxes) {
            this.title = title;
            this.description = description;
            this.basePrice = basePrice;
            this.taxes = taxes;
        }
    }
}