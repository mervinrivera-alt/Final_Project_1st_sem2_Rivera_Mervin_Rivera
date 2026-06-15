/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pkg1st_sem2_final_fixed;

/**
 *
 * @author river
 */
public class DeliveryInfo {
    private int batchId;
    private int crateId;
    private String arrivalDate;
    private int quantity;

    public DeliveryInfo(int batchId, int crateId, String arrivalDate, int quantity) {
        this.batchId = batchId;
        this.crateId = crateId;
        this.arrivalDate = arrivalDate;
        this.quantity = quantity;
    }

    public int getBatchId() { return batchId; }
    public void setBatchId(int batchId) { this.batchId = batchId; }

    public int getCrateId() { return crateId; }
    public void setCrateId(int crateId) { this.crateId = crateId; }

    public String getArrivalDate() { return arrivalDate; }
    public void setArrivalDate(String arrivalDate) { this.arrivalDate = arrivalDate; }

    public int getQuantities() { return quantity; }
    public void setQuantities(int quantities) { this.quantity = quantities; }
}
