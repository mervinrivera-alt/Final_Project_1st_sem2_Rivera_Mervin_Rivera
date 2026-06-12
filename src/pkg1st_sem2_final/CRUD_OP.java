/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pkg1st_sem2_final;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author river
 */
public class CRUD_OP {
    public ArrayList<info> getAllItems() {
        ArrayList<info> itemList = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM 1st_sem2_final.items";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                info itemData = new info();
                
                itemData.setItemId(rs.getString("items_id"));
                itemData.setItemName(rs.getString("name"));
                itemData.setPrice(rs.getDouble("price"));
                itemData.setQuantity(rs.getInt("quantity"));
                
                itemList.add(itemData);
            }
            
            rs.close();
            stmt.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return itemList;
    }
    public info getItemDetailsByName(String itemName) {
    info item = null;
    String sql = "SELECT items_id, quantity FROM 1st_sem2_final.items WHERE name = ?";
    
    try (java.sql.Connection conn = DBConnection.getConnection();
         java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setString(1, itemName);
        try (java.sql.ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                item = new info();
                item.setItemId(rs.getString("items_id"));
                item.setQuantity(rs.getInt("quantity"));
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return item;
}
    public static boolean saveTransaction(String employeeId, double totalAmount, DefaultTableModel cart) {
        Connection conn = null;
        PreparedStatement pstSales = null;
        PreparedStatement pstLog = null;
        ResultSet rs = null;
        
        String receiptId = "REC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        String salesSql = "INSERT INTO 1st_sem2_final.sales (items_items_id, total_price) VALUES (?, ?)";
        String logSql = "INSERT INTO 1st_sem2_final.sales_log (receipt_id, employees_employee_id, sales_sales_id, sales_date, total_amount) VALUES (?, ?, ?, NOW(), ?)";
        
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            
            pstSales = conn.prepareStatement(salesSql, Statement.RETURN_GENERATED_KEYS);
            pstLog = conn.prepareStatement(logSql);
            
            int rowCount = cart.getRowCount();
            
            for (int i = 0; i < rowCount; i++) {
                String itemId = cart.getValueAt(i, 0).toString();
                double totalPrice = Double.parseDouble(cart.getValueAt(i, 2).toString());
                
                pstSales.setString(1, itemId);
                pstSales.setDouble(2, totalPrice);
                pstSales.executeUpdate();
                
                rs = pstSales.getGeneratedKeys();
                if (rs.next()) {
                    int salesId = rs.getInt(1);
                    
                    pstLog.setString(1, receiptId);
                    pstLog.setString(2, employeeId);
                    pstLog.setInt(3, salesId);
                    pstLog.setDouble(4, totalAmount);
                    pstLog.executeUpdate();
                }
            }
            
            conn.commit();
            return true;
            
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) {}
            try { if (pstLog != null) pstLog.close(); } catch (SQLException e) {}
            try { if (pstSales != null) pstSales.close(); } catch (SQLException e) {}
            try { if (conn != null) conn.close(); } catch (SQLException e) {}
        }
    }
    public info authenticateUser(String firstName, String password) {
    info sessionInfo = null;
    
    // We now select both employee_id AND role. We check against first_name.
    String sql = "SELECT employee_id, role FROM 1st_sem2_final.employees WHERE first_name = ? AND password = ?";
    
    try (java.sql.Connection conn = DBConnection.getConnection();
         java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setString(1, firstName);
        stmt.setString(2, password);
        
        try (java.sql.ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                sessionInfo = new info();
                
                sessionInfo.setEmployeeId(rs.getString("employee_id")); 
                sessionInfo.setRole(rs.getString("role"));
                
                sessionInfo.setfname(firstName);
                sessionInfo.setPassword(password);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    
    return sessionInfo; 
}
    public static boolean addCategory(String categoryName) {
    Connection conn = null;
    PreparedStatement pst = null;
    
    String sql = "INSERT INTO 1st_sem2_final.category (category_name) VALUES (?)";
    
    try {
        conn = DBConnection.getConnection();
        pst = conn.prepareStatement(sql);
        pst.setString(1, categoryName);
        
        int rowsAffected = pst.executeUpdate();
        return rowsAffected > 0; 
        
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    } finally {
        try { if (pst != null) pst.close(); } catch (SQLException e) {}
        try { if (conn != null) conn.close(); } catch (SQLException e) {}
    }
}
    public static void readCategories(javax.swing.JTable table) {
    javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) table.getModel();
    model.setRowCount(0); 
    
    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    try {
        conn = DBConnection.getConnection();
        pst = conn.prepareStatement("SELECT category_id, category_name FROM 1st_sem2_final.category");
        rs = pst.executeQuery();
        
        while (rs.next()) {
            int id = rs.getInt("category_id");
            String name = rs.getString("category_name");
            model.addRow(new Object[]{id, name});
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try { if (rs != null) rs.close(); } catch (Exception e) {}
        try { if (pst != null) pst.close(); } catch (Exception e) {}
        try { if (conn != null) conn.close(); } catch (Exception e) {} 
    }
    }
    public static boolean updateCategory(int categoryId, String newCategoryName) {
    Connection conn = null;
    PreparedStatement pst = null;
    
    String sql = "UPDATE 1st_sem2_final.category SET category_name = ? WHERE category_id = ?";
    
    try {
        conn = DBConnection.getConnection();
        pst = conn.prepareStatement(sql);
        
        pst.setString(1, newCategoryName);
        pst.setInt(2, categoryId);
        
        int rowsAffected = pst.executeUpdate();
        return rowsAffected > 0;
        
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    } finally {
        try { if (pst != null) pst.close(); } catch (SQLException e) {}
        try { if (conn != null) conn.close(); } catch (SQLException e) {}
    }
}
    public static boolean deleteCategory(int categoryId) {
    Connection conn = null;
    PreparedStatement pst = null;
    
    String sql = "DELETE FROM 1st_sem2_final.category WHERE category_id = ?";
    
    try {
        conn = DBConnection.getConnection();
        pst = conn.prepareStatement(sql);
        
        pst.setInt(1, categoryId);
        
        int rowsAffected = pst.executeUpdate();
        return rowsAffected > 0;
        
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    } finally {
        try { if (pst != null) pst.close(); } catch (SQLException e) {}
        try { if (conn != null) conn.close(); } catch (SQLException e) {}
    }
}
    public static String getCategoryName(int categoryId) {
    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    String name = null;
    
    try {
        conn = DBConnection.getConnection();
        pst = conn.prepareStatement("SELECT category_name FROM 1st_sem2_final.category WHERE category_id = ?");
        pst.setInt(1, categoryId);
        rs = pst.executeQuery();
        
        if (rs.next()) {
            name = rs.getString("category_name");
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try { if (rs != null) rs.close(); } catch (Exception e) {}
        try { if (pst != null) pst.close(); } catch (Exception e) {}
        try { if (conn != null) conn.close(); } catch (Exception e) {}
    }
    return name;
}
    public boolean addDelivery(int batchId, int crateId, int quantity) {
    String sql = "INSERT INTO 1st_sem2_final.delivery_arrive (Batch_id, crate_id, Arrival_date, Quantities) VALUES (?, ?, ?, ?)";
    
    try (Connection conn = DBConnection.getConnection(); 
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        // Automatically get today's date in YYYY-MM-DD format
        String autoDate = java.time.LocalDate.now().toString(); 
        
        pstmt.setInt(1, batchId);
        pstmt.setInt(2, crateId);
        pstmt.setString(3, autoDate); 
        pstmt.setInt(4, quantity);
        
        int rowsAffected = pstmt.executeUpdate();
        return rowsAffected > 0; 
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "MYSQL ERROR: " + e.getMessage());
    return false;
    }
}
    public int[] getDeliveryInfo(int batchId) {
    String sql = "SELECT crate_id, Quantities FROM 1st_sem2_final.delivery_arrive WHERE Batch_id = ?";
    
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setInt(1, batchId);
        ResultSet rs = pstmt.executeQuery();
        
        // If the record exists, return the data
        if (rs.next()) {
            int foundCrateId = rs.getInt("crate_id");
            int foundQuantity = rs.getInt("Quantities");
            return new int[] { foundCrateId, foundQuantity };
        }
        
    } catch (SQLException e) {
        System.out.println("Search error: " + e.getMessage());
    }
    
    // Returns null if the Batch ID doesn't exist
    return null; 
}
    public boolean updateDelivery(int batchId, int crateId, int quantity) {
    // We only update the crate ID and quantity. We use the Batch ID to find the right row.
    String sql = "UPDATE 1st_sem2_final.delivery_arrive SET crate_id = ?, Quantities = ? WHERE Batch_id = ?";
    
    try (Connection conn = DBConnection.getConnection(); 
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        // 1. Set the new Crate ID
        pstmt.setInt(1, crateId);
        
        // 2. Set the new Quantity
        pstmt.setInt(2, quantity);
        
        // 3. Identify WHICH row to update (The WHERE clause)
        pstmt.setInt(3, batchId); 
        
        // Execute the update
        int rowsAffected = pstmt.executeUpdate();
        return rowsAffected > 0; // Returns true if the database successfully updated the row
        
    } catch (SQLException e) {
        System.out.println("Error updating delivery: " + e.getMessage());
        return false;
    }
}
    public boolean deleteDelivery(int batchId) {
    String sql = "DELETE FROM 1st_sem2_final.delivery_arrive WHERE Batch_id = ?";
    
    try (Connection conn = DBConnection.getConnection(); 
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setInt(1, batchId); 
        
        int rowsAffected = pstmt.executeUpdate();
        return rowsAffected > 0; 
        
    } catch (SQLException e) {
        System.out.println("Error deleting delivery: " + e.getMessage());
        return false;
    }
}
    public java.sql.ResultSet getAllDeliveries() {
    String sql = "SELECT Batch_id, crate_id, Arrival_date, Quantities FROM 1st_sem2_final.delivery_arrive";
    try {
        // We do NOT close the connection here yet, because the JFrame needs to read the ResultSet first
        java.sql.Connection conn = DBConnection.getConnection(); 
        java.sql.PreparedStatement pstmt = conn.prepareStatement(sql);
        return pstmt.executeQuery(); 
        
    } catch (java.sql.SQLException e) {
        System.out.println("Error fetching deliveries: " + e.getMessage());
        return null;
    }
}
    // 1. Get Batch IDs that have NOT been sorted yet
public java.util.ArrayList<Integer> getUnsortedBatchIds() {
    java.util.ArrayList<Integer> list = new java.util.ArrayList<>();
    String sql = "SELECT Batch_id FROM 1st_sem2_final.delivery_arrive WHERE Batch_id NOT IN (SELECT delivery_arrive_Batch_id FROM 1st_sem2_final.sorted_batch)";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
            list.add(rs.getInt("Batch_id"));
        }
    } catch (SQLException e) {
        System.out.println("Error fetching unsorted batches: " + e.getMessage());
    }
    return list;
}

// 2. Fetch Category names and IDs
public java.util.ArrayList<info.CategoryItem> getCategories() {
    java.util.ArrayList<info.CategoryItem> list = new java.util.ArrayList<>();
    String sql = "SELECT category_id, category_name FROM 1st_sem2_final.category"; 
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
            // Notice we are calling info.CategoryItem now
            list.add(new info.CategoryItem(rs.getInt("category_id"), rs.getString("category_name")));
        }
    } catch (SQLException e) {
        System.out.println("Category fetch error: " + e.getMessage());
    }
    return list;
}

// 3. Get Quantity and Arrival Date for a specific batch
public String[] getBatchDetails(int batchId) {
    String sql = "SELECT Arrival_date, Quantities FROM 1st_sem2_final.delivery_arrive WHERE Batch_id = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, batchId);
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return new String[]{rs.getString("Arrival_date"), rs.getString("Quantities")};
            }
        }
    } catch (SQLException e) {
        System.out.println("Error fetching batch details: " + e.getMessage());
    }
    return null;
}

// 4. Create Sorted Batch
public boolean addSortedBatch(int catId, String itemName, int qty, int batchId, String arrivalDate) {
    String sql = "INSERT INTO 1st_sem2_final.sorted_batch (category_category_id, Item_name, Quantities, delivery_arrive_Batch_id, delivery_arrive_Arrival_date) VALUES (?, ?, ?, ?, ?)";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, catId);
        pstmt.setString(2, itemName);
        pstmt.setInt(3, qty);
        pstmt.setInt(4, batchId);
        pstmt.setString(5, arrivalDate);
        return pstmt.executeUpdate() > 0;
    } catch (SQLException e) {
        javax.swing.JOptionPane.showMessageDialog(null, "DB Insert Error: " + e.getMessage());
        return false;
    }
}

// 5. Update Sorted Batch
public boolean updateSortedBatch(int sortedId, int catId, String itemName, int qty, int batchId, String arrivalDate) {
    String sql = "UPDATE 1st_sem2_final.sorted_batch SET category_category_id = ?, Item_name = ?, Quantities = ?, delivery_arrive_Batch_id = ?, delivery_arrive_Arrival_date = ? WHERE sorted_id = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, catId);
        pstmt.setString(2, itemName);
        pstmt.setInt(3, qty);
        pstmt.setInt(4, batchId);
        pstmt.setString(5, arrivalDate);
        pstmt.setInt(6, sortedId);
        return pstmt.executeUpdate() > 0;
    } catch (SQLException e) {
        javax.swing.JOptionPane.showMessageDialog(null, "DB Update Error: " + e.getMessage());
        return false;
    }
}

// 6. Search for an existing sorted record
public java.sql.ResultSet getSortedBatchRecord(int sortedId) {
    // ADDED the 1st_sem2_final. prefix to sorted_batch!
    String sql = "SELECT sorted_id, category_category_id, Item_name, Quantities, delivery_arrive_Batch_id, delivery_arrive_Arrival_date FROM 1st_sem2_final.sorted_batch";
    
    try {
        java.sql.Connection conn = DBConnection.getConnection();
        java.sql.PreparedStatement pstmt = conn.prepareStatement(sql);
        return pstmt.executeQuery();
    } catch (java.sql.SQLException e) {
        System.out.println("Table fetch error: " + e.getMessage());
        return null;
    }
}

// 7. Delete Sorted Batch
public boolean deleteSortedBatch(int sortedId) {
    String sql = "DELETE FROM 1st_sem2_final.sorted_batch WHERE sorted_id = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, sortedId);
        return pstmt.executeUpdate() > 0;
    } catch (SQLException e) {
        return false;
    }
}

// 8. Fetch all rows for jTable3
public java.sql.ResultSet getAllSortedBatches() {
    String sql = "SELECT 1st_sem2_final.sorted_id, category_category_id, Item_name, Quantities, delivery_arrive_Batch_id, delivery_arrive_Arrival_date FROM sorted_batch";
    try {
        Connection conn = DBConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        return pstmt.executeQuery();
    } catch (SQLException e) {
        return null;
    }
}
}
