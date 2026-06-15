/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pkg1st_sem2_final_fixed;

/**
 *
 * @author river
 */
public class Sorted_Batch {
    private int sortedId;
    private int categoryId;
    private String itemName;
    private int quantities;
    private int batchId;
    private String arrivalDate;

    public Sorted_Batch(int sortedId, int categoryId, String itemName, int quantities, int batchId, String arrivalDate) {
        this.sortedId = sortedId;
        this.categoryId = categoryId;
        this.itemName = itemName;
        this.quantities = quantities;
        this.batchId = batchId;
        this.arrivalDate = arrivalDate;
    }

    public int getSortedId() { return sortedId; }
    public void setSortedId(int sortedId) { this.sortedId = sortedId; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public int getQuantities() { return quantities; }
    public void setQuantities(int quantities) { this.quantities = quantities; }

    public int getBatchId() { return batchId; }
    public void setBatchId(int batchId) { this.batchId = batchId; }

    public String getArrivalDate() { return arrivalDate; }
    public void setArrivalDate(String arrivalDate) { this.arrivalDate = arrivalDate; }
}

