/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pkg1st_sem2_final_fixed;

/**
 *
 * @author river
 */
public class ItemsInfo {
    private int itemsId;
    private String code;
    private int sortedBatchId;
    private double price;
    private String status;

    public ItemsInfo(int itemsId, String code, int sortedBatchId, double price, String status) {
        this.itemsId = itemsId;
        this.code = code;
        this.sortedBatchId = sortedBatchId;
        this.price = price;
        this.status = status;
    }

    public int getItemsId() { return itemsId; }
    public void setItemsId(int itemsId) { this.itemsId = itemsId; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public int getSortedBatchId() { return sortedBatchId; }
    public void setSortedBatchId(int sortedBatchId) { this.sortedBatchId = sortedBatchId; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
