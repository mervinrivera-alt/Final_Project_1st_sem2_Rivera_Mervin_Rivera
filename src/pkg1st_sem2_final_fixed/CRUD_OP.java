/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pkg1st_sem2_final_fixed;

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

// 4. Create Sorted Batch
// 1. FIXES YOUR CURRENT ERROR: Overloaded addSortedBatch that takes a String date and automatically finds crateId
    public boolean addSortedBatch(int catId, String itemName, int qty, int batchId, String arrivalDate) {
        int crateId = 0;
        String lookupSql = "SELECT crate_id FROM 1st_sem2_final.delivery_arrive WHERE Batch_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement lookupStmt = conn.prepareStatement(lookupSql)) {
            lookupStmt.setInt(1, batchId);
            try (ResultSet rs = lookupStmt.executeQuery()) {
                if (rs.next()) { crateId = rs.getInt("crate_id"); }
            }
        } catch (SQLException e) { e.printStackTrace(); }

        String sql = "INSERT INTO 1st_sem2_final.sorted_batch (category_category_id, Item_name, Quantities, delivery_arrive_Batch_id, delivery_arrive_crate_id, delivery_arrive_Arrival_date) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, catId);
            pstmt.setString(2, itemName);
            pstmt.setInt(3, qty);
            pstmt.setInt(4, batchId);
            pstmt.setInt(5, crateId);
            pstmt.setDate(6, java.sql.Date.valueOf(arrivalDate)); // Safely convert String to java.sql.Date
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    // 2. FIXES UPDATE MODE ERROR: Overloaded updateSortedBatch that takes a String date and automatically finds crateId
    public boolean updateSortedBatch(int sortedId, int catId, String itemName, int qty, int batchId, String arrivalDate) {
        int crateId = 0;
        String lookupSql = "SELECT crate_id FROM 1st_sem2_final.delivery_arrive WHERE Batch_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement lookupStmt = conn.prepareStatement(lookupSql)) {
            lookupStmt.setInt(1, batchId);
            try (ResultSet rs = lookupStmt.executeQuery()) {
                if (rs.next()) { crateId = rs.getInt("crate_id"); }
            }
        } catch (SQLException e) { e.printStackTrace(); }

        String sql = "UPDATE 1st_sem2_final.sorted_batch SET category_category_id = ?, Item_name = ?, Quantities = ?, delivery_arrive_Batch_id = ?, delivery_arrive_crate_id = ?, delivery_arrive_Arrival_date = ? WHERE sorted_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, catId);
            pstmt.setString(2, itemName);
            pstmt.setInt(3, qty);
            pstmt.setInt(4, batchId);
            pstmt.setInt(5, crateId);
            pstmt.setDate(6, java.sql.Date.valueOf(arrivalDate));
            pstmt.setInt(7, sortedId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    // 3. Adds missing getBatchDetails helper expected by SortingWindow line 700
    public String[] getBatchDetails(int batchId) {
        String sql = "SELECT Arrival_date, Quantities FROM 1st_sem2_final.delivery_arrive WHERE Batch_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, batchId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new String[] {
                        rs.getDate("Arrival_date").toString(),
                        String.valueOf(rs.getInt("Quantities"))
                    };
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // 4. Adds missing getUnsortedBatchIds expected by SortingWindow line 483
    public ArrayList<Integer> getUnsortedBatchIds() {
        ArrayList<Integer> list = new ArrayList<>();
        String sql = "SELECT Batch_id FROM 1st_sem2_final.delivery_arrive WHERE Batch_id NOT IN (SELECT delivery_arrive_Batch_id FROM 1st_sem2_final.sorted_batch)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) { list.add(rs.getInt("Batch_id")); }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // 5. Adds missing 1-argument getDeliveryInfo method expected by SortingWindow line 636
    public int[] getDeliveryInfo(int batchId) {
        String sql = "SELECT crate_id, Quantities FROM 1st_sem2_final.delivery_arrive WHERE Batch_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, batchId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) { return new int[] { rs.getInt("crate_id"), rs.getInt("Quantities") }; }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // 6. Adds missing 3-argument updateDelivery method expected by SortingWindow line 617
    public boolean updateDelivery(int batchId, int crateId, int quantity) {
        String sql = "UPDATE 1st_sem2_final.delivery_arrive SET crate_id = ?, Quantities = ? WHERE Batch_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, crateId);
            pstmt.setInt(2, quantity);
            pstmt.setInt(3, batchId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    // 7. Adds missing 1-argument deleteDelivery method expected by SortingWindow line 662
    public boolean deleteDelivery(int batchId) {
        String sql = "DELETE FROM 1st_sem2_final.delivery_arrive WHERE Batch_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, batchId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
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
public boolean deleteBatchUsingItemInfo(ItemsInfo itemData) {
        // We extract the ID directly from your ItemsInfo object
        int targetId = itemData.getSortedBatchId(); 
        
        String sql = "DELETE FROM 1st_sem2_final.items WHERE items_id = ?";
        
        try (java.sql.Connection conn = DBConnection.getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, targetId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // Returns true if it successfully deleted
            
        } catch (java.sql.SQLIntegrityConstraintViolationException ex) {
            javax.swing.JOptionPane.showMessageDialog(null, 
                "Action Denied: You cannot delete this batch because it already has a price in the Items table.\n" +
                "Please delete or update the item price first.");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
   public java.sql.ResultSet searchInventory(String searchTerm) {
        // We added "OR i.items_id LIKE ?" to the WHERE clause!
        String sql = "SELECT i.items_id, i.code, sb.Item_name, i.price, i.Status "
                   + "FROM 1st_sem2_final.items i "
                   + "JOIN 1st_sem2_final.sorted_batch sb ON i.Sorted_batch_sorted_id = sb.sorted_id "
                   + "WHERE sb.Item_name LIKE ? OR i.code LIKE ? OR i.items_id LIKE ?";
        
        try {
            java.sql.Connection conn = DBConnection.getConnection();
            java.sql.PreparedStatement pstmt = conn.prepareStatement(sql);
            
            // The % wildcard allows partial matches (e.g., typing "12" will find ID 12, 120, 212, etc.)
            String searchPattern = "%" + searchTerm + "%"; 
            
            pstmt.setString(1, searchPattern); // Matches sb.Item_name
            pstmt.setString(2, searchPattern); // Matches i.code
            pstmt.setString(3, searchPattern); // Matches i.items_id
            
            return pstmt.executeQuery();
            
        } catch (Exception e) {
            System.out.println("Inventory Search Error: " + e.getMessage());
            return null;
        }
    }
   public java.sql.ResultSet getUnpricedSortedBatches() {
        String sql = "SELECT sb.sorted_id, sb.category_category_id, sb.Item_name, "
                   + "sb.Quantities, sb.delivery_arrive_Batch_id, sb.delivery_arrive_Arrival_date "
                   + "FROM 1st_sem2_final.sorted_batch sb "
                   + "WHERE NOT EXISTS ("
                   + "    SELECT 1 FROM 1st_sem2_final.items i "
                   + "    WHERE i.Sorted_batch_sorted_id = sb.sorted_id"
                   + ")";
        
        try {
            java.sql.Connection conn = DBConnection.getConnection();
            java.sql.PreparedStatement pstmt = conn.prepareStatement(sql);
            return pstmt.executeQuery();
        } catch (java.sql.SQLException e) {
            System.out.println("Table fetch error: " + e.getMessage());
            return null;
        }
    }

    // === PASTE THIS NEW METHOD HERE ===
    public java.util.ArrayList<DeliveryInfo> getUnsortedDeliveries() {
    java.util.ArrayList<DeliveryInfo> list = new java.util.ArrayList<>();
    String sql = "SELECT Batch_id, crate_id, Arrival_date, Quantities "
               + "FROM 1st_sem2_final.delivery_arrive "
               + "WHERE Batch_id NOT IN (SELECT delivery_arrive_Batch_id FROM 1st_sem2_final.sorted_batch)";
    try (java.sql.Connection conn = DBConnection.getConnection();
         java.sql.PreparedStatement pstmt = conn.prepareStatement(sql);
         java.sql.ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
            list.add(new DeliveryInfo(
                rs.getInt("Batch_id"),
                rs.getInt("crate_id"),
                rs.getString("Arrival_date"),
                rs.getInt("Quantities")
            ));
        }
    } catch (java.sql.SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
    return list;
}
    public int getExactDeliveryQuantity(int batchId, int crateId, String arrivalDate) {
        String sql = "SELECT Quantities FROM 1st_sem2_final.delivery_arrive WHERE Batch_id = ? AND crate_id = ? AND Arrival_date = ?";
        try (java.sql.Connection conn = DBConnection.getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, batchId);
            pstmt.setInt(2, crateId);
            pstmt.setDate(3, java.sql.Date.valueOf(arrivalDate));
            
            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("Quantities"); // Returns the exact quantity!
                }
            }
        } catch (java.sql.SQLException e) {
            System.out.println("Error fetching exact quantity: " + e.getMessage());
        }
        return 0; // Returns 0 if it can't find it
    }
    public boolean deleteSortedBatch(int sortedId) {
        String sql = "DELETE FROM 1st_sem2_final.sorted_batch WHERE sorted_id = ?";
        try {
            java.sql.Connection conn = DBConnection.getConnection();
            java.sql.PreparedStatement pstmt = conn.prepareStatement(sql);
            
            pstmt.setInt(1, sortedId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // Returns true if it successfully deleted
            
        } catch (java.sql.SQLException e) {
            System.out.println("Error deleting sorted batch: " + e.getMessage());
            return false;
        }
    }
}
