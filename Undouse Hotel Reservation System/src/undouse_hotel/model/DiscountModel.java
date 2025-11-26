package undouse_hotel.model;

import undouse_hotel.model.DiscountModel.DiscountCalculation;
import undouse_hotel.model.DiscountModel.DiscountType;

public class DiscountModel {
    
    public enum DiscountType {
        NONE,
        SENIOR_CITIZEN,  
        PWD,             
        CHILD            
    }
    
    public static class DiscountCalculation {
        private double originalPrice;
        private double discountAmount;
        private double vatAmount;
        private double finalPrice;
        private DiscountType discountType;
        
        public DiscountCalculation(double originalPrice, double discountAmount, 
                                 double vatAmount, double finalPrice, DiscountType discountType) {
            this.originalPrice = originalPrice;
            this.discountAmount = discountAmount;
            this.vatAmount = vatAmount;
            this.finalPrice = finalPrice;
            this.discountType = discountType;
        }
        
        // Getters
        public double getOriginalPrice() { return originalPrice; }
        public double getDiscountAmount() { return discountAmount; }
        public double getVatAmount() { return vatAmount; }
        public double getFinalPrice() { return finalPrice; }
        public DiscountType getDiscountType() { return discountType; }
        
        public String getFormattedBreakdown() {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("Original Price: ₱%,.2f\n", originalPrice));
            
            if (discountAmount > 0) {
                sb.append(String.format("Discount (%s): -₱%,.2f\n", 
                    getDiscountTypeName(), discountAmount));
            }
            
            if (vatAmount > 0) {
                sb.append(String.format("VAT (12%%): +₱%,.2f\n", vatAmount));
            } else if (discountType == DiscountType.SENIOR_CITIZEN || discountType == DiscountType.PWD) {
                sb.append("VAT: Exempt\n");
            }
            
            sb.append(String.format("Final Price: ₱%,.2f", finalPrice));
            
            return sb.toString();
        }
        
        private String getDiscountTypeName() {
            switch (discountType) {
                case SENIOR_CITIZEN: return "10% Senior Citizen";
                case PWD: return "10% PWD";
                case CHILD: return "5% Child";
                default: return "Discount";
            }
        }
    }
    
    public static boolean validateDiscountEligibility(DiscountType discountType, String idNumber, int age) {
        switch (discountType) {
            case SENIOR_CITIZEN:
            case PWD:
                return idNumber != null && !idNumber.trim().isEmpty();
            case CHILD:
                return age > 0 && age < 12;
            case NONE:
                return true;
            default:
                return false;
        }
    }
    
    public static DiscountCalculation calculateDiscount(double originalPrice, DiscountType discountType) {
        double discountRate = 0.0;
        boolean vatExempt = false;
        
        switch (discountType) {
            case SENIOR_CITIZEN:
                discountRate = 0.10; // 10% discount
                vatExempt = true;
                break;
            case PWD:
                discountRate = 0.10; // 10% discount
                vatExempt = true;
                break;
            case CHILD:
                discountRate = 0.05; // 5% discount
                vatExempt = true;
                break;
            case NONE:
            default:
                discountRate = 0.0;
                vatExempt = false;
        }
        
        // Simply apply discount to original price
        double discountAmount = originalPrice * discountRate;
        double finalPrice = originalPrice - discountAmount;
        
        // VAT is 0 for all discounts
        double vatAmount = 0.0;
        
        return new DiscountCalculation(originalPrice, discountAmount, vatAmount, finalPrice, discountType);
    }
}