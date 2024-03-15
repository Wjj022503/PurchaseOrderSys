/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package java_asg;

import java.awt.CardLayout;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
/**
 *
 * @author HappyMan20
 */

public class GUI extends javax.swing.JFrame{
    User currentUser = new User();
    String selectedID;
    /**
     * Creates new form GUI
     */
    CardLayout cardLayout1;
    CardLayout cardLayout2;
    public GUI() {
        initComponents();
        cardLayout1 = (CardLayout)(menuOptions.getLayout());
        cardLayout2 = (CardLayout)(functionalities.getLayout());
        
        item_table.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e){
                //restrict purchase manager
                if(currentUser.getRole().toString().equals("PURCHASEMANAGER")){
                    return;
                }              
                
                int row = e.getFirstRow();
                int column = e.getColumn();
                
                // Check if the row and column are valid
                if (row == -1 || column == -1) {
                    return;
                }

                    
                //get selected ID from listener
                TableModel model = (TableModel) e.getSource();
                selectedID = model.getValueAt(row, 0).toString();
                String data = model.getValueAt(row, column).toString();
                
                //check data is empty or blank
                if(data == null || data.isBlank()){
                    JOptionPane.showConfirmDialog(full_page, "Do not leave blank line.","Error", JOptionPane.PLAIN_MESSAGE);
                    DefaultTableModel itemModel = (DefaultTableModel)item_table.getModel();
                    while(itemModel.getRowCount() > 0) {
                        itemModel.removeRow(0);
                    }
                    for(Item item:Item.getItems()){
                        itemModel.addRow(new Object[]{item.getItemID(),item.getItemName(),item.getPrice(),item.getSupplier().getSupplierID(),item.getStock()});
                    }                    
                    return;
                }
                
                //get user option, if yes then edit
                int option = JOptionPane.showConfirmDialog(full_page, "Save changed?.","Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(option == JOptionPane.YES_OPTION){
                    boolean valid = new Item().edit(selectedID, data, column);
                    if(valid){
                        JOptionPane.showConfirmDialog(full_page, "Item detail has been changed.","Successful", JOptionPane.PLAIN_MESSAGE);
                    }
                    else{
                        JOptionPane.showConfirmDialog(full_page, "Invalid input.","Error", JOptionPane.PLAIN_MESSAGE);
                    }
                }
                //refresh table
                DefaultTableModel itemModel = (DefaultTableModel)item_table.getModel();
                while(itemModel.getRowCount() > 0) {
                    itemModel.removeRow(0);
                }
                for(Item item:Item.getItems()){
                    itemModel.addRow(new Object[]{item.getItemID(),item.getItemName(),item.getPrice(),item.getSupplier().getSupplierID(),item.getStock()});
                }
            }   
        });
        
        pr_table.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                //restrict purchase manager
                if(currentUser.getRole().toString().equals("PURCHASEMANAGER")){
                    return;
                }
                
                int row = e.getFirstRow();
                int column = e.getColumn();
                
                // Check if the row and column indices are valid
                if (row == -1 || column == -1) {
                    return; // Exit the method if they're not
                }

                TableModel model = (TableModel) e.getSource();
                selectedID = model.getValueAt(row, 0).toString();
                String data = model.getValueAt(row, column).toString();
                
                //check data is empty or blank
                if(data == null || data.isBlank()){
                    JOptionPane.showConfirmDialog(full_page, "Do not leave blank line.","Error", JOptionPane.PLAIN_MESSAGE);
                    DefaultTableModel itemModel = (DefaultTableModel)item_table.getModel();
                    while(itemModel.getRowCount() > 0) {
                        itemModel.removeRow(0);
                    }
                    for(Item item:Item.getItems()){
                        itemModel.addRow(new Object[]{item.getItemID(),item.getItemName(),item.getPrice(),item.getSupplier().getSupplierID(),item.getStock()});
                    }                    
                    return;
                }
                
                int option = JOptionPane.showConfirmDialog(full_page, "Save changed?.","Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(option == JOptionPane.YES_OPTION){
                    boolean valid = new PurchaseRequisition().edit(selectedID, data, column);
                    if(valid){
                        JOptionPane.showConfirmDialog(full_page, "Purchase Requisition detail has been changed.","Successful", JOptionPane.PLAIN_MESSAGE);
                    }
                    else{
                        JOptionPane.showConfirmDialog(full_page, "Invalid input.","Error", JOptionPane.PLAIN_MESSAGE);
                    }
                }
                //refresh table
                DefaultTableModel prModel = (DefaultTableModel)pr_table.getModel();
                while(prModel.getRowCount() > 0) {
                    prModel.removeRow(0);
                }
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                for(PurchaseRequisition pr: PurchaseRequisition.getPR()){
                    String requiredDateFormatted = sdf.format(pr.getRequiredDate());
                    prModel.addRow(new Object[]{pr.getPRID(), pr.getPRItem().getItemID(), pr.getPRItem().getItemName(), pr.getRequiredQuantity(), requiredDateFormatted, pr.getPRSMID(), pr.getTotalPrice(), pr.getStatus()});
                }     
            }   
        }); 
        
        po_table.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                //restrict sales manager
                if(currentUser.getRole().toString().equals("SALESMANAGER")){
                    return;
                }
                
                int row = e.getFirstRow();
                int column = e.getColumn();

                // Check if the row and column indices are valid
                if (row == -1 || column == -1) {
                    return; // Exit the method if they're not
                }

                TableModel model = (TableModel) e.getSource();
                selectedID = model.getValueAt(row, 0).toString();
                String data = model.getValueAt(row, column).toString();

                //check data is empty or blank
                if(data == null || data.isBlank()){
                    JOptionPane.showConfirmDialog(full_page, "Do not leave blank line.","Error", JOptionPane.PLAIN_MESSAGE);
                    DefaultTableModel itemModel = (DefaultTableModel)item_table.getModel();
                    while(itemModel.getRowCount() > 0) {
                        itemModel.removeRow(0);
                    }
                    for(Item item:Item.getItems()){
                        itemModel.addRow(new Object[]{item.getItemID(),item.getItemName(),item.getPrice(),item.getSupplier().getSupplierID(),item.getStock()});
                    }                    
                    return;
                }                
                
                int option = JOptionPane.showConfirmDialog(full_page, "Save changed?.","Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(option == JOptionPane.YES_OPTION){
                    boolean valid = new PurchaseOrder().edit(selectedID, data, column);
                    if(valid){
                        JOptionPane.showConfirmDialog(full_page, "Purchase order detail has been changed.","Successful", JOptionPane.PLAIN_MESSAGE);
                    }
                    else{
                        JOptionPane.showConfirmDialog(full_page, "Invalid input.","Error", JOptionPane.PLAIN_MESSAGE);
                    }
                }
                //refresh table
                DefaultTableModel poModel = (DefaultTableModel)po_table.getModel();
                while(poModel.getRowCount() > 0) {
                    poModel.removeRow(0);
                }
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                for(PurchaseOrder po: PurchaseOrder.getPO()){
                    String dateFormatted = sdf.format(po.getPOPR().getRequiredDate());
                    poModel.addRow(new Object[]{po.getPOID(), po.getPOPR().getPRID(),po.getPMID(),po.getPOPR().getPRItem().getItemID(),dateFormatted,po.getPOPR().getRequiredQuantity(),po.getPOPR().getTotalPrice()});
                }  
            }   
        });
        
        supplier_table.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                //restrict sales manager
                if(currentUser.getRole().toString().equals("PURCHASEMANAGER")){
                    return;
                }
                
                int row = e.getFirstRow();
                int column = e.getColumn();
                // Check if the row and column indices are valid
                if (row == -1 || column == -1) {
                    return; // Exit the method if they're not
                }

                TableModel model = (TableModel) e.getSource();
                selectedID = model.getValueAt(row, 0).toString();
                String data = model.getValueAt(row, column).toString();

                //check data is empty or blank
                if(data == null || data.isBlank()){
                    JOptionPane.showConfirmDialog(full_page, "Do not leave blank line.","Error", JOptionPane.PLAIN_MESSAGE);
                    DefaultTableModel itemModel = (DefaultTableModel)item_table.getModel();
                    while(itemModel.getRowCount() > 0) {
                        itemModel.removeRow(0);
                    }
                    for(Item item:Item.getItems()){
                        itemModel.addRow(new Object[]{item.getItemID(),item.getItemName(),item.getPrice(),item.getSupplier().getSupplierID(),item.getStock()});
                    }                    
                    return;
                }                
                
                int option = JOptionPane.showConfirmDialog(full_page, "Save changed?.","Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(option == JOptionPane.YES_OPTION){
                    boolean valid = new Supplier().edit(selectedID, data, column);
                    if(valid){
                        JOptionPane.showConfirmDialog(full_page, "Supplier detail has been changed.","Successful", JOptionPane.PLAIN_MESSAGE);
                    }
                    else{
                        JOptionPane.showConfirmDialog(full_page, "Invalid input.","Error", JOptionPane.PLAIN_MESSAGE);
                    }
                }
                //refresh table
                DefaultTableModel supModel = (DefaultTableModel)supplier_table.getModel();
                DefaultTableModel supItemModel = (DefaultTableModel)supplied_item_table.getModel();                
                while(supModel.getRowCount() > 0) {
                    supModel.removeRow(0);
                }

                while(supItemModel.getRowCount() > 0) {
                    supItemModel.removeRow(0);
                }

                for(Supplier sup : Supplier.getSupplier()){    
                    supModel.addRow(new Object[]{sup.getSupplierID(),sup.getSupplierName()});
                }   
            }   
        });
        
        se_table.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int column = e.getColumn();

                // Check if the row and column indices are valid
                if (row == -1 || column == -1) {
                    return; // Exit the method if they're not
                }

                TableModel model = (TableModel) e.getSource();
                selectedID = model.getValueAt(row, 0).toString();
                String data = model.getValueAt(row, column).toString();

                //check data is empty or blank
                if(data == null || data.isBlank()){
                    JOptionPane.showConfirmDialog(full_page, "Do not leave blank line.","Error", JOptionPane.PLAIN_MESSAGE);
                    DefaultTableModel itemModel = (DefaultTableModel)item_table.getModel();
                    while(itemModel.getRowCount() > 0) {
                        itemModel.removeRow(0);
                    }
                    for(Item item:Item.getItems()){
                        itemModel.addRow(new Object[]{item.getItemID(),item.getItemName(),item.getPrice(),item.getSupplier().getSupplierID(),item.getStock()});
                    }                    
                    return;
                }                
                
                int option = JOptionPane.showConfirmDialog(full_page, "Save changed?.","Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(option == JOptionPane.YES_OPTION){
                    boolean valid = new SalesEntry().edit(selectedID, data, column);
                    if(valid){
                        JOptionPane.showConfirmDialog(full_page, "Sales Entry detail has been changed.","Successful", JOptionPane.PLAIN_MESSAGE);
                    }
                    else{
                        JOptionPane.showConfirmDialog(full_page, "Invalid input.","Error", JOptionPane.PLAIN_MESSAGE);
                    }
                }
                //refresh table
                DefaultTableModel seModel = (DefaultTableModel)se_table.getModel();
                while(seModel.getRowCount() > 0) {
                    seModel.removeRow(0);
                }

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                for(SalesEntry se : SalesEntry.getSE()){
                    String salesDateFormatted = sdf.format(se.getSalesDate());
                    seModel.addRow(new Object[]{se.getSalesID(),se.getItem().getItemID(),se.getItem().getItemName(),salesDateFormatted,se.getQuantitySold(),se.getTotalAmount()});
                }  
            }   
        });

        user_table.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int column = e.getColumn();

                // Check if the row and column indices are valid
                if (row == -1 || column == -1) {
                    return; // Exit the method if they're not
                }

                TableModel model = (TableModel) e.getSource();
                selectedID = model.getValueAt(row, 0).toString();
                String data = model.getValueAt(row, column).toString();

                //check data is empty or blank
                if(data == null || data.isBlank()){
                    JOptionPane.showConfirmDialog(full_page, "Do not leave blank line.","Error", JOptionPane.PLAIN_MESSAGE);
                    DefaultTableModel itemModel = (DefaultTableModel)item_table.getModel();
                    while(itemModel.getRowCount() > 0) {
                        itemModel.removeRow(0);
                    }
                    for(Item item:Item.getItems()){
                        itemModel.addRow(new Object[]{item.getItemID(),item.getItemName(),item.getPrice(),item.getSupplier().getSupplierID(),item.getStock()});
                    }                    
                    return;
                }                
                
                int option = JOptionPane.showConfirmDialog(full_page, "Save changed?.","Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(option == JOptionPane.YES_OPTION){
                    boolean valid = currentUser.edit(selectedID, data, column);
                    if(valid){
                        JOptionPane.showConfirmDialog(full_page, "User detail has been changed.","Successful", JOptionPane.PLAIN_MESSAGE);
                    }
                    else{
                        JOptionPane.showConfirmDialog(full_page, "Invalid input.","Error", JOptionPane.PLAIN_MESSAGE);
                    }
                }
                //refresh table
                DefaultTableModel userModel = (DefaultTableModel)user_table.getModel();
                while(userModel.getRowCount() > 0) {
                    userModel.removeRow(0);
                }
                for(User user:User.getUser()){
                    userModel.addRow(new Object[]{user.getID(),user.getUsername(),user.getPassword(),user.getRole()});
                }  
            }   
        });        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        login_page = new javax.swing.JPanel();
        login_button = new javax.swing.JButton();
        company_title = new javax.swing.JLabel();
        system_title = new javax.swing.JLabel();
        username_field = new javax.swing.JTextField();
        password_field = new javax.swing.JPasswordField();
        username_label = new javax.swing.JLabel();
        password_label = new javax.swing.JLabel();
        full_page = new javax.swing.JSplitPane();
        menuOptions = new javax.swing.JPanel();
        sales_manager_options = new javax.swing.JPanel();
        item_entry = new javax.swing.JButton();
        supplier_entry = new javax.swing.JButton();
        daily_itemwise_sales_entry = new javax.swing.JButton();
        create_purchase_requisition = new javax.swing.JButton();
        view_purchase_order = new javax.swing.JButton();
        exit_sales_mng = new javax.swing.JButton();
        profile_button = new javax.swing.JButton();
        administrator_options = new javax.swing.JPanel();
        register_user_panel_button = new javax.swing.JButton();
        daily_itemwise_sales_entry_adm = new javax.swing.JButton();
        create_purchase_requisition_adm = new javax.swing.JButton();
        view_purchase_order_adm = new javax.swing.JButton();
        supplier_entry_adm = new javax.swing.JButton();
        item_entry_adm = new javax.swing.JButton();
        profile_button_adm = new javax.swing.JButton();
        logout_button_adm = new javax.swing.JButton();
        purchase_manager_options = new javax.swing.JPanel();
        view_items = new javax.swing.JButton();
        view_suppliers = new javax.swing.JButton();
        view_purchase_req_purchases = new javax.swing.JButton();
        exit_purchase_mng = new javax.swing.JButton();
        profile_button_pm = new javax.swing.JButton();
        view_po_button = new javax.swing.JButton();
        functionalities = new javax.swing.JPanel();
        registration = new javax.swing.JPanel();
        registration_title = new javax.swing.JLabel();
        new_username_label = new javax.swing.JLabel();
        new_username = new javax.swing.JTextField();
        new_password_label = new javax.swing.JLabel();
        new_password = new javax.swing.JPasswordField();
        confirm_password_label = new javax.swing.JLabel();
        confirm_password = new javax.swing.JPasswordField();
        role_label = new javax.swing.JLabel();
        role_cBox = new javax.swing.JComboBox<>();
        add_user_button = new javax.swing.JButton();
        list_user_button = new javax.swing.JButton();
        item_entry_panel = new javax.swing.JPanel();
        item_entry_page_title = new javax.swing.JLabel();
        item_id_label = new javax.swing.JLabel();
        item_id = new javax.swing.JTextField();
        item_name_label = new javax.swing.JLabel();
        item_name = new javax.swing.JTextField();
        item_price_label = new javax.swing.JLabel();
        supplier_id_label = new javax.swing.JLabel();
        item_supplier_id = new javax.swing.JTextField();
        list_item = new javax.swing.JButton();
        add_item = new javax.swing.JButton();
        stock_label = new javax.swing.JLabel();
        item_price = new javax.swing.JFormattedTextField();
        item_stock = new javax.swing.JFormattedTextField();
        supplier_management_panel = new javax.swing.JPanel();
        supp_management_page_title = new javax.swing.JLabel();
        add_item_supp = new javax.swing.JButton();
        list_supplier_supp = new javax.swing.JButton();
        supplier_id_label_supp = new javax.swing.JLabel();
        supplier_id_supp = new javax.swing.JTextField();
        supplier_name_label_supp = new javax.swing.JLabel();
        supplier_name_supp = new javax.swing.JTextField();
        create_purchase_requisition_panel = new javax.swing.JPanel();
        create_purchase_requisition_title = new javax.swing.JLabel();
        purchase_requisition_id_label_cpr = new javax.swing.JLabel();
        pr_id_cpr = new javax.swing.JTextField();
        item_id_label_cpr = new javax.swing.JLabel();
        item_id_cpr = new javax.swing.JTextField();
        required_quantity_label_cpr = new javax.swing.JLabel();
        total_price_label_cpr = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        total_price_cpr = new javax.swing.JTextPane();
        required_date_label_cpr = new javax.swing.JLabel();
        required_date_cpr = new javax.swing.JFormattedTextField();
        add_item_cpr = new javax.swing.JButton();
        list_pr_button = new javax.swing.JButton();
        required_quantity_cpr = new javax.swing.JFormattedTextField();
        daily_itemwise_sales_panel = new javax.swing.JPanel();
        daily_itemwise_sales_title = new javax.swing.JLabel();
        sales_entry_id_label_dis = new javax.swing.JLabel();
        sales_entry_id_dis = new javax.swing.JTextField();
        item_id_label_dis = new javax.swing.JLabel();
        item_id_dis = new javax.swing.JTextField();
        sold_quantity_label_dis = new javax.swing.JLabel();
        total_price_label_dis = new javax.swing.JLabel();
        add_item_dis = new javax.swing.JButton();
        list_daily_sales_dis = new javax.swing.JButton();
        sales_date_dis = new javax.swing.JFormattedTextField();
        sales_date_label = new javax.swing.JLabel();
        sold_quantity_dis = new javax.swing.JFormattedTextField();
        total_price_dis = new javax.swing.JTextField();
        generate_po_panel = new javax.swing.JPanel();
        create_purchase_requisition_title1 = new javax.swing.JLabel();
        purchase_requisition_id_label_cpr1 = new javax.swing.JLabel();
        po_id_field = new javax.swing.JTextField();
        item_id_label_cpr1 = new javax.swing.JLabel();
        po_pr_field = new javax.swing.JTextField();
        required_quantity_label_cpr1 = new javax.swing.JLabel();
        total_price_label_cpr1 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        po_amount_field = new javax.swing.JTextPane();
        required_date_label_cpr1 = new javax.swing.JLabel();
        po_date_field = new javax.swing.JFormattedTextField();
        add_po_button = new javax.swing.JButton();
        list_po_button = new javax.swing.JButton();
        po_qtt_field = new javax.swing.JFormattedTextField();
        po_item_field = new javax.swing.JTextField();
        item_id_label_cpr2 = new javax.swing.JLabel();
        list_user_panel = new javax.swing.JPanel();
        view_user_title = new javax.swing.JLabel();
        view_user_sPane = new javax.swing.JScrollPane();
        user_table = new javax.swing.JTable();
        search_user_label = new javax.swing.JLabel();
        search_user_field = new javax.swing.JTextField();
        delete_user_button = new javax.swing.JButton();
        list_item_panel = new javax.swing.JPanel();
        list_item_title = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        item_table = new javax.swing.JTable();
        search_item_label = new javax.swing.JLabel();
        search_item_field = new javax.swing.JTextField();
        delete_item_button = new javax.swing.JButton();
        list_supplier_panel = new javax.swing.JPanel();
        view_supplier_title = new javax.swing.JLabel();
        view_supplier_sPane = new javax.swing.JScrollPane();
        supplier_table = new javax.swing.JTable();
        search_supplier_label = new javax.swing.JLabel();
        search_supplier_field = new javax.swing.JTextField();
        view_supplier_sPane1 = new javax.swing.JScrollPane();
        supplied_item_table = new javax.swing.JTable();
        delete_sup_button = new javax.swing.JButton();
        view_pr_panel = new javax.swing.JPanel();
        view_purchase_requisition_title = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        pr_table = new javax.swing.JTable();
        search_purchase_requisition_label = new javax.swing.JLabel();
        search_pr_field = new javax.swing.JTextField();
        generate_po_button = new javax.swing.JButton();
        delete_pr_button = new javax.swing.JButton();
        reject_button = new javax.swing.JButton();
        view_se_panel = new javax.swing.JPanel();
        view_purchase_order_title1 = new javax.swing.JLabel();
        search_purchase_order_label_vpo1 = new javax.swing.JLabel();
        search_se_field = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        se_table = new javax.swing.JTable();
        delete_se_button = new javax.swing.JButton();
        view_po_panel = new javax.swing.JPanel();
        view_purchase_order_title = new javax.swing.JLabel();
        search_purchase_order_label_vpo = new javax.swing.JLabel();
        search_purchase_order_vpo = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        po_table = new javax.swing.JTable();
        delete_po_button = new javax.swing.JButton();
        profile_page = new javax.swing.JPanel();
        profile_page_title = new javax.swing.JLabel();
        user_id_label_profile = new javax.swing.JLabel();
        user_id_profile = new javax.swing.JTextField();
        username_label_profile = new javax.swing.JLabel();
        username_profile = new javax.swing.JTextField();
        role_label_profile = new javax.swing.JLabel();
        role_profile = new javax.swing.JTextField();
        change_password = new javax.swing.JButton();
        edit_profile_page = new javax.swing.JPanel();
        edit_profile_page_title = new javax.swing.JLabel();
        old_password_label_edit = new javax.swing.JLabel();
        new_password_label_edit = new javax.swing.JLabel();
        new_password_edit = new javax.swing.JPasswordField();
        comfirm_password_label_edit = new javax.swing.JLabel();
        confirm_password_edit = new javax.swing.JPasswordField();
        confirm_edit = new javax.swing.JButton();
        old_password_edit = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(new java.awt.Dimension(772, 525));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                formComponentHidden(evt);
            }
        });

        login_page.setBackground(new java.awt.Color(3, 13, 11));
        login_page.setPreferredSize(new java.awt.Dimension(772, 525));

        login_button.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        login_button.setText("Login");
        login_button.setBorderPainted(false);
        login_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                login_buttonActionPerformed(evt);
            }
        });

        company_title.setFont(new java.awt.Font("Times New Roman", 3, 24)); // NOI18N
        company_title.setForeground(new java.awt.Color(153, 255, 255));
        company_title.setText("~SIGMA SDN BHD~");

        system_title.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        system_title.setForeground(new java.awt.Color(255, 255, 255));
        system_title.setText("Purchase Order Management System");

        username_label.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        username_label.setForeground(new java.awt.Color(255, 255, 255));
        username_label.setText("Username:");

        password_label.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        password_label.setForeground(new java.awt.Color(255, 255, 255));
        password_label.setText("Password:");

        javax.swing.GroupLayout login_pageLayout = new javax.swing.GroupLayout(login_page);
        login_page.setLayout(login_pageLayout);
        login_pageLayout.setHorizontalGroup(
            login_pageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(login_pageLayout.createSequentialGroup()
                .addGroup(login_pageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(login_pageLayout.createSequentialGroup()
                        .addGap(377, 377, 377)
                        .addComponent(login_button, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(login_pageLayout.createSequentialGroup()
                        .addGap(326, 326, 326)
                        .addComponent(company_title)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(login_pageLayout.createSequentialGroup()
                .addGroup(login_pageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(login_pageLayout.createSequentialGroup()
                        .addGap(342, 342, 342)
                        .addGroup(login_pageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(password_field, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(password_label)
                            .addComponent(username_label)
                            .addComponent(username_field, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(login_pageLayout.createSequentialGroup()
                        .addGap(248, 248, 248)
                        .addComponent(system_title)))
                .addGap(0, 251, Short.MAX_VALUE))
        );
        login_pageLayout.setVerticalGroup(
            login_pageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, login_pageLayout.createSequentialGroup()
                .addGap(120, 120, 120)
                .addComponent(company_title, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(system_title, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(username_label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(username_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(password_label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(password_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57)
                .addComponent(login_button, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(140, Short.MAX_VALUE))
        );

        full_page.setVisible(false);

        menuOptions.setLayout(new java.awt.CardLayout());

        sales_manager_options.setVisible(false);
        sales_manager_options.setBackground(new java.awt.Color(0, 0, 0));

        item_entry.setText("Items");
        item_entry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                item_entryActionPerformed(evt);
            }
        });

        supplier_entry.setText("Suppliers");
        supplier_entry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplier_entryActionPerformed(evt);
            }
        });

        daily_itemwise_sales_entry.setText("Daily Item-wise Sales");
        daily_itemwise_sales_entry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                daily_itemwise_sales_entryActionPerformed(evt);
            }
        });

        create_purchase_requisition.setText("Purchase Requisition");
        create_purchase_requisition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                create_purchase_requisitionActionPerformed(evt);
            }
        });

        view_purchase_order.setText("View Purchase Order");
        view_purchase_order.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                view_purchase_orderActionPerformed(evt);
            }
        });

        exit_sales_mng.setText("Logout");
        exit_sales_mng.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exit_sales_mngActionPerformed(evt);
            }
        });

        profile_button.setText("Profile");
        profile_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profile_buttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout sales_manager_optionsLayout = new javax.swing.GroupLayout(sales_manager_options);
        sales_manager_options.setLayout(sales_manager_optionsLayout);
        sales_manager_optionsLayout.setHorizontalGroup(
            sales_manager_optionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(item_entry, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(supplier_entry, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(daily_itemwise_sales_entry, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(create_purchase_requisition, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
            .addComponent(exit_sales_mng, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(view_purchase_order, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(profile_button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        sales_manager_optionsLayout.setVerticalGroup(
            sales_manager_optionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sales_manager_optionsLayout.createSequentialGroup()
                .addComponent(item_entry, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(supplier_entry, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(daily_itemwise_sales_entry, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(create_purchase_requisition, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(view_purchase_order, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(profile_button, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 233, Short.MAX_VALUE)
                .addComponent(exit_sales_mng, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        menuOptions.add(sales_manager_options, "sales_manager_options");

        administrator_options.setVisible(false);
        administrator_options.setBackground(new java.awt.Color(0, 0, 0));

        register_user_panel_button.setText("Register User");
        register_user_panel_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                register_user_panel_buttonActionPerformed(evt);
            }
        });

        daily_itemwise_sales_entry_adm.setText("Daily Item-wise Sales");
        daily_itemwise_sales_entry_adm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                daily_itemwise_sales_entry_admActionPerformed(evt);
            }
        });

        create_purchase_requisition_adm.setText("Purchase Requisition");
        create_purchase_requisition_adm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                create_purchase_requisition_admActionPerformed(evt);
            }
        });

        view_purchase_order_adm.setText("Generate Purchase Order");
        view_purchase_order_adm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                view_purchase_order_admActionPerformed(evt);
            }
        });

        supplier_entry_adm.setText("Suppliers");
        supplier_entry_adm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplier_entry_admActionPerformed(evt);
            }
        });

        item_entry_adm.setText("Items");
        item_entry_adm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                item_entry_admActionPerformed(evt);
            }
        });

        profile_button_adm.setText("Profile");
        profile_button_adm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profile_button_admActionPerformed(evt);
            }
        });

        logout_button_adm.setText("Logout");
        logout_button_adm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logout_button_admActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout administrator_optionsLayout = new javax.swing.GroupLayout(administrator_options);
        administrator_options.setLayout(administrator_optionsLayout);
        administrator_optionsLayout.setHorizontalGroup(
            administrator_optionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(register_user_panel_button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(daily_itemwise_sales_entry_adm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(create_purchase_requisition_adm, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
            .addComponent(view_purchase_order_adm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(supplier_entry_adm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(item_entry_adm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(profile_button_adm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(logout_button_adm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        administrator_optionsLayout.setVerticalGroup(
            administrator_optionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(administrator_optionsLayout.createSequentialGroup()
                .addComponent(register_user_panel_button, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(item_entry_adm, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(supplier_entry_adm, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(daily_itemwise_sales_entry_adm, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(create_purchase_requisition_adm, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(view_purchase_order_adm, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(profile_button_adm, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 186, Short.MAX_VALUE)
                .addComponent(logout_button_adm, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        menuOptions.add(administrator_options, "adminsitrator_options");

        purchase_manager_options.setVisible(false);
        purchase_manager_options.setBackground(new java.awt.Color(0, 0, 0));

        view_items.setText("View Items");
        view_items.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                view_itemsActionPerformed(evt);
            }
        });

        view_suppliers.setText("View Suppliers");
        view_suppliers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                view_suppliersActionPerformed(evt);
            }
        });

        view_purchase_req_purchases.setText("View Purchase Requisition");
        view_purchase_req_purchases.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                view_purchase_req_purchasesActionPerformed(evt);
            }
        });

        exit_purchase_mng.setText("Logout");
        exit_purchase_mng.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exit_purchase_mngActionPerformed(evt);
            }
        });

        profile_button_pm.setText("Profile");
        profile_button_pm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profile_button_pmActionPerformed(evt);
            }
        });

        view_po_button.setText("Generate Purchase Order");
        view_po_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                view_po_buttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout purchase_manager_optionsLayout = new javax.swing.GroupLayout(purchase_manager_options);
        purchase_manager_options.setLayout(purchase_manager_optionsLayout);
        purchase_manager_optionsLayout.setHorizontalGroup(
            purchase_manager_optionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(view_items, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(view_suppliers, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(view_purchase_req_purchases, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(exit_purchase_mng, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(profile_button_pm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(view_po_button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        purchase_manager_optionsLayout.setVerticalGroup(
            purchase_manager_optionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(purchase_manager_optionsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(view_items, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(view_suppliers, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(view_purchase_req_purchases, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(view_po_button, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(profile_button_pm, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 267, Short.MAX_VALUE)
                .addComponent(exit_purchase_mng, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        menuOptions.add(purchase_manager_options, "purchase_manager_options");

        full_page.setLeftComponent(menuOptions);

        functionalities.setLayout(new java.awt.CardLayout());

        registration.setBackground(new java.awt.Color(0, 0, 0));

        registration_title.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        registration_title.setForeground(new java.awt.Color(255, 255, 255));
        registration_title.setText("~User Registration~");

        new_username_label.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        new_username_label.setForeground(new java.awt.Color(255, 255, 255));
        new_username_label.setText("Username:");

        new_username.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                new_usernameKeyPressed(evt);
            }
        });

        new_password_label.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        new_password_label.setForeground(new java.awt.Color(255, 255, 255));
        new_password_label.setText("Password:");

        confirm_password_label.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        confirm_password_label.setForeground(new java.awt.Color(255, 255, 255));
        confirm_password_label.setText("Confirm password:");

        role_label.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        role_label.setForeground(new java.awt.Color(255, 255, 255));
        role_label.setText("Role:");

        role_cBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SalesManager", "PurchaseManager", "Administrator" }));
        role_cBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                role_cBoxActionPerformed(evt);
            }
        });

        add_user_button.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        add_user_button.setText("Add user");
        add_user_button.setBorderPainted(false);
        add_user_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                add_user_buttonActionPerformed(evt);
            }
        });

        list_user_button.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        list_user_button.setText("List User");
        list_user_button.setBorderPainted(false);
        list_user_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                list_user_buttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout registrationLayout = new javax.swing.GroupLayout(registration);
        registration.setLayout(registrationLayout);
        registrationLayout.setHorizontalGroup(
            registrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(registrationLayout.createSequentialGroup()
                .addGap(215, 215, 215)
                .addGroup(registrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(registrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(registration_title)
                        .addComponent(new_username_label)
                        .addComponent(new_username, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(new_password_label)
                        .addComponent(new_password, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(confirm_password_label)
                        .addComponent(confirm_password, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(role_label)
                        .addComponent(role_cBox, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(registrationLayout.createSequentialGroup()
                        .addComponent(add_user_button)
                        .addGap(26, 26, 26)
                        .addComponent(list_user_button)))
                .addContainerGap(237, Short.MAX_VALUE))
        );
        registrationLayout.setVerticalGroup(
            registrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(registrationLayout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(registration_title, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49)
                .addComponent(new_username_label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(new_username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(new_password_label, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(new_password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(confirm_password_label, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(confirm_password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(role_label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(role_cBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addGroup(registrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(list_user_button)
                    .addComponent(add_user_button))
                .addContainerGap(140, Short.MAX_VALUE))
        );

        functionalities.add(registration, "registration");

        item_entry_panel.setBackground(new java.awt.Color(0, 0, 0));

        item_entry_page_title.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        item_entry_page_title.setForeground(new java.awt.Color(255, 255, 255));
        item_entry_page_title.setText("~Item Entry~");

        item_id_label.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        item_id_label.setForeground(new java.awt.Color(255, 255, 255));
        item_id_label.setText("Item ID:");

        item_id.setEditable(false);

        item_name_label.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        item_name_label.setForeground(new java.awt.Color(255, 255, 255));
        item_name_label.setText("Item Name:");

        item_name.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                item_nameKeyPressed(evt);
            }
        });

        item_price_label.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        item_price_label.setForeground(new java.awt.Color(255, 255, 255));
        item_price_label.setText("Item Price:");

        supplier_id_label.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        supplier_id_label.setForeground(new java.awt.Color(255, 255, 255));
        supplier_id_label.setText("Supplier ID:");

        item_supplier_id.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                item_supplier_idActionPerformed(evt);
            }
        });

        list_item.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        list_item.setText("List Items");
        list_item.setBorderPainted(false);
        list_item.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                list_itemActionPerformed(evt);
            }
        });

        add_item.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        add_item.setText("Add");
        add_item.setBorderPainted(false);
        add_item.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                add_itemActionPerformed(evt);
            }
        });

        stock_label.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        stock_label.setForeground(new java.awt.Color(255, 255, 255));
        stock_label.setText("Stock:");

        item_price.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        item_price.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                item_priceActionPerformed(evt);
            }
        });
        item_price.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                item_priceKeyTyped(evt);
            }
        });

        item_stock.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        item_stock.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                item_stockKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout item_entry_panelLayout = new javax.swing.GroupLayout(item_entry_panel);
        item_entry_panel.setLayout(item_entry_panelLayout);
        item_entry_panelLayout.setHorizontalGroup(
            item_entry_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, item_entry_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(item_entry_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(item_entry_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(item_price_label)
                        .addComponent(supplier_id_label)
                        .addComponent(item_supplier_id, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                        .addComponent(stock_label)
                        .addComponent(item_id_label)
                        .addComponent(item_id, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                        .addComponent(item_name_label)
                        .addComponent(item_price)
                        .addComponent(item_stock))
                    .addComponent(item_name, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(item_entry_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(add_item, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(list_item, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(176, 176, 176))
            .addGroup(item_entry_panelLayout.createSequentialGroup()
                .addGap(219, 219, 219)
                .addComponent(item_entry_page_title)
                .addContainerGap(316, Short.MAX_VALUE))
        );
        item_entry_panelLayout.setVerticalGroup(
            item_entry_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(item_entry_panelLayout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(item_entry_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(item_entry_panelLayout.createSequentialGroup()
                        .addComponent(item_entry_page_title, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(58, 58, 58)
                        .addComponent(add_item, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(list_item, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(item_entry_panelLayout.createSequentialGroup()
                        .addComponent(item_id_label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(item_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(item_name_label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(item_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(item_price_label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(item_price, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(supplier_id_label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(item_supplier_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stock_label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(item_stock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 195, Short.MAX_VALUE))
        );

        functionalities.add(item_entry_panel, "item_entry_panel");

        supplier_management_panel.setBackground(new java.awt.Color(0, 0, 0));

        supp_management_page_title.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        supp_management_page_title.setForeground(new java.awt.Color(255, 255, 255));
        supp_management_page_title.setText("~Supplier Management~");

        add_item_supp.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        add_item_supp.setText("Add");
        add_item_supp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                add_item_suppActionPerformed(evt);
            }
        });

        list_supplier_supp.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        list_supplier_supp.setText("List Suppliers");
        list_supplier_supp.setAlignmentY(0.0F);
        list_supplier_supp.setMargin(new java.awt.Insets(2, 2, 2, 2));
        list_supplier_supp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                list_supplier_suppActionPerformed(evt);
            }
        });

        supplier_id_label_supp.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        supplier_id_label_supp.setForeground(new java.awt.Color(255, 255, 255));
        supplier_id_label_supp.setText("Supplier ID:");

        supplier_id_supp.setEditable(false);
        supplier_id_supp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplier_id_suppActionPerformed(evt);
            }
        });

        supplier_name_label_supp.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        supplier_name_label_supp.setForeground(new java.awt.Color(255, 255, 255));
        supplier_name_label_supp.setText("Supplier Name:");

        supplier_name_supp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                supplier_name_suppKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout supplier_management_panelLayout = new javax.swing.GroupLayout(supplier_management_panel);
        supplier_management_panel.setLayout(supplier_management_panelLayout);
        supplier_management_panelLayout.setHorizontalGroup(
            supplier_management_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(supplier_management_panelLayout.createSequentialGroup()
                .addContainerGap(89, Short.MAX_VALUE)
                .addGroup(supplier_management_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, supplier_management_panelLayout.createSequentialGroup()
                        .addGroup(supplier_management_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(supplier_name_supp, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(supplier_management_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(supplier_id_label_supp)
                                .addComponent(supplier_id_supp, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(supplier_name_label_supp, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(99, 99, 99)
                        .addGroup(supplier_management_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(list_supplier_supp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(add_item_supp, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(176, 176, 176))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, supplier_management_panelLayout.createSequentialGroup()
                        .addComponent(supp_management_page_title)
                        .addGap(210, 210, 210))))
        );
        supplier_management_panelLayout.setVerticalGroup(
            supplier_management_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(supplier_management_panelLayout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(supp_management_page_title, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61)
                .addComponent(supplier_id_label_supp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(supplier_management_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(supplier_id_supp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(add_item_supp, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(supplier_management_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(supplier_management_panelLayout.createSequentialGroup()
                        .addComponent(supplier_name_label_supp)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(supplier_name_supp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(list_supplier_supp, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(312, Short.MAX_VALUE))
        );

        functionalities.add(supplier_management_panel, "supplier_management_panel");

        create_purchase_requisition_panel.setBackground(new java.awt.Color(0, 0, 0));

        create_purchase_requisition_title.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        create_purchase_requisition_title.setForeground(new java.awt.Color(255, 255, 255));
        create_purchase_requisition_title.setText("~Create Purchase Requisition~");

        purchase_requisition_id_label_cpr.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        purchase_requisition_id_label_cpr.setForeground(new java.awt.Color(255, 255, 255));
        purchase_requisition_id_label_cpr.setText("Purchase Requisition ID:");

        pr_id_cpr.setEditable(false);
        pr_id_cpr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pr_id_cprActionPerformed(evt);
            }
        });

        item_id_label_cpr.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        item_id_label_cpr.setForeground(new java.awt.Color(255, 255, 255));
        item_id_label_cpr.setText("Item ID:");

        item_id_cpr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                item_id_cprKeyPressed(evt);
            }
        });

        required_quantity_label_cpr.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        required_quantity_label_cpr.setForeground(new java.awt.Color(255, 255, 255));
        required_quantity_label_cpr.setText("Required Quantity:");

        total_price_label_cpr.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        total_price_label_cpr.setForeground(new java.awt.Color(255, 255, 255));
        total_price_label_cpr.setText("Total Amount:");

        total_price_cpr.setEditable(false);
        jScrollPane2.setViewportView(total_price_cpr);

        required_date_label_cpr.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        required_date_label_cpr.setForeground(new java.awt.Color(255, 255, 255));
        required_date_label_cpr.setText("Required Date (dd/mm/yyyy):");

        required_date_cpr.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("dd/MM/yyyy"))));

        add_item_cpr.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        add_item_cpr.setText("Add");
        add_item_cpr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                add_item_cprActionPerformed(evt);
            }
        });

        list_pr_button.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        list_pr_button.setText("List PR");
        list_pr_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                list_pr_buttonActionPerformed(evt);
            }
        });

        required_quantity_cpr.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        required_quantity_cpr.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                required_quantity_cprFocusLost(evt);
            }
        });

        javax.swing.GroupLayout create_purchase_requisition_panelLayout = new javax.swing.GroupLayout(create_purchase_requisition_panel);
        create_purchase_requisition_panel.setLayout(create_purchase_requisition_panelLayout);
        create_purchase_requisition_panelLayout.setHorizontalGroup(
            create_purchase_requisition_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(create_purchase_requisition_panelLayout.createSequentialGroup()
                .addGap(176, 176, 176)
                .addComponent(create_purchase_requisition_title)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(create_purchase_requisition_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(create_purchase_requisition_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(required_quantity_cpr, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(required_quantity_label_cpr, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pr_id_cpr, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                    .addComponent(purchase_requisition_id_label_cpr, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(item_id_label_cpr, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(item_id_cpr, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                    .addComponent(required_date_label_cpr, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(total_price_label_cpr, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(required_date_cpr, javax.swing.GroupLayout.Alignment.LEADING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 201, Short.MAX_VALUE)
                .addGroup(create_purchase_requisition_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(add_item_cpr, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(list_pr_button, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(141, 141, 141))
        );
        create_purchase_requisition_panelLayout.setVerticalGroup(
            create_purchase_requisition_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(create_purchase_requisition_panelLayout.createSequentialGroup()
                .addGroup(create_purchase_requisition_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(create_purchase_requisition_panelLayout.createSequentialGroup()
                        .addGap(139, 139, 139)
                        .addComponent(purchase_requisition_id_label_cpr)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pr_id_cpr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(item_id_label_cpr)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(item_id_cpr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(required_quantity_label_cpr))
                    .addGroup(create_purchase_requisition_panelLayout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(create_purchase_requisition_title, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(55, 55, 55)
                        .addComponent(add_item_cpr, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(list_pr_button, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(required_quantity_cpr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(required_date_label_cpr)
                .addGap(5, 5, 5)
                .addComponent(required_date_cpr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(total_price_label_cpr)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(149, Short.MAX_VALUE))
        );

        functionalities.add(create_purchase_requisition_panel, "create_purchase_requisition_panel");

        daily_itemwise_sales_panel.setBackground(new java.awt.Color(0, 0, 0));

        daily_itemwise_sales_title.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        daily_itemwise_sales_title.setForeground(new java.awt.Color(255, 255, 255));
        daily_itemwise_sales_title.setText("~Daily Item-wise Sales~");

        sales_entry_id_label_dis.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        sales_entry_id_label_dis.setForeground(new java.awt.Color(255, 255, 255));
        sales_entry_id_label_dis.setText("Sales Entry ID:");

        sales_entry_id_dis.setEditable(false);
        sales_entry_id_dis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sales_entry_id_disActionPerformed(evt);
            }
        });

        item_id_label_dis.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        item_id_label_dis.setForeground(new java.awt.Color(255, 255, 255));
        item_id_label_dis.setText("Item ID:");

        item_id_dis.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                item_id_disKeyPressed(evt);
            }
        });

        sold_quantity_label_dis.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        sold_quantity_label_dis.setForeground(new java.awt.Color(255, 255, 255));
        sold_quantity_label_dis.setText("Sold Quantity:");

        total_price_label_dis.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        total_price_label_dis.setForeground(new java.awt.Color(255, 255, 255));
        total_price_label_dis.setText("Total Amount:");

        add_item_dis.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        add_item_dis.setText("Add");
        add_item_dis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                add_item_disActionPerformed(evt);
            }
        });

        list_daily_sales_dis.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        list_daily_sales_dis.setText("List Sales");
        list_daily_sales_dis.setAlignmentY(0.0F);
        list_daily_sales_dis.setMargin(new java.awt.Insets(2, 2, 2, 2));
        list_daily_sales_dis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                list_daily_sales_disActionPerformed(evt);
            }
        });

        sales_date_dis.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("dd/MM/yyyy"))));

        sales_date_label.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        sales_date_label.setForeground(new java.awt.Color(255, 255, 255));
        sales_date_label.setText("Sales Date (dd/mm/yyyy):");

        sold_quantity_dis.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        sold_quantity_dis.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                sold_quantity_disFocusLost(evt);
            }
        });

        total_price_dis.setEditable(false);

        javax.swing.GroupLayout daily_itemwise_sales_panelLayout = new javax.swing.GroupLayout(daily_itemwise_sales_panel);
        daily_itemwise_sales_panel.setLayout(daily_itemwise_sales_panelLayout);
        daily_itemwise_sales_panelLayout.setHorizontalGroup(
            daily_itemwise_sales_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, daily_itemwise_sales_panelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(daily_itemwise_sales_title)
                .addGap(213, 213, 213))
            .addGroup(daily_itemwise_sales_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(daily_itemwise_sales_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, daily_itemwise_sales_panelLayout.createSequentialGroup()
                        .addGroup(daily_itemwise_sales_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(item_id_label_dis)
                            .addComponent(item_id_dis, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sold_quantity_label_dis)
                            .addComponent(sales_entry_id_dis, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sales_entry_id_label_dis)
                            .addComponent(total_price_label_dis))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 178, Short.MAX_VALUE)
                        .addGroup(daily_itemwise_sales_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(list_daily_sales_dis, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(add_item_dis, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(164, 164, 164))
                    .addGroup(daily_itemwise_sales_panelLayout.createSequentialGroup()
                        .addGroup(daily_itemwise_sales_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(sales_date_dis)
                            .addComponent(sales_date_label)
                            .addComponent(sold_quantity_dis, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                            .addComponent(total_price_dis))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        daily_itemwise_sales_panelLayout.setVerticalGroup(
            daily_itemwise_sales_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(daily_itemwise_sales_panelLayout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(daily_itemwise_sales_title, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58)
                .addGroup(daily_itemwise_sales_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(daily_itemwise_sales_panelLayout.createSequentialGroup()
                        .addComponent(sales_entry_id_label_dis)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sales_entry_id_dis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(add_item_dis, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(daily_itemwise_sales_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(daily_itemwise_sales_panelLayout.createSequentialGroup()
                        .addComponent(item_id_label_dis)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(item_id_dis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(list_daily_sales_dis, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(sold_quantity_label_dis)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sold_quantity_dis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addComponent(sales_date_label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sales_date_dis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(total_price_label_dis)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(total_price_dis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(139, Short.MAX_VALUE))
        );

        functionalities.add(daily_itemwise_sales_panel, "daily_itemwise_sales_panel");

        generate_po_panel.setBackground(new java.awt.Color(0, 0, 0));

        create_purchase_requisition_title1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        create_purchase_requisition_title1.setForeground(new java.awt.Color(255, 255, 255));
        create_purchase_requisition_title1.setText("~Generate Purchase Order~");

        purchase_requisition_id_label_cpr1.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        purchase_requisition_id_label_cpr1.setForeground(new java.awt.Color(255, 255, 255));
        purchase_requisition_id_label_cpr1.setText("Purchase Order ID:");

        po_id_field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                po_id_fieldActionPerformed(evt);
            }
        });

        item_id_label_cpr1.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        item_id_label_cpr1.setForeground(new java.awt.Color(255, 255, 255));
        item_id_label_cpr1.setText("Purchase Requisition ID:");

        po_pr_field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                po_pr_fieldFocusLost(evt);
            }
        });
        po_pr_field.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                po_pr_fieldKeyPressed(evt);
            }
        });

        required_quantity_label_cpr1.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        required_quantity_label_cpr1.setForeground(new java.awt.Color(255, 255, 255));
        required_quantity_label_cpr1.setText("Required Quantity:");

        total_price_label_cpr1.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        total_price_label_cpr1.setForeground(new java.awt.Color(255, 255, 255));
        total_price_label_cpr1.setText("Total Amount:");

        po_amount_field.setEditable(false);
        jScrollPane6.setViewportView(po_amount_field);

        required_date_label_cpr1.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        required_date_label_cpr1.setForeground(new java.awt.Color(255, 255, 255));
        required_date_label_cpr1.setText("Required Date (dd/mm/yyyy):");

        po_date_field.setEditable(false);
        po_date_field.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("dd/MM/yyyy"))));

        add_po_button.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        add_po_button.setText("Add");
        add_po_button.setBorderPainted(false);
        add_po_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                add_po_buttonActionPerformed(evt);
            }
        });

        list_po_button.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        list_po_button.setText("List PO");
        list_po_button.setBorderPainted(false);
        list_po_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                list_po_buttonActionPerformed(evt);
            }
        });

        po_qtt_field.setEditable(false);
        po_qtt_field.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        po_qtt_field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                po_qtt_fieldFocusLost(evt);
            }
        });

        po_item_field.setEditable(false);

        item_id_label_cpr2.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        item_id_label_cpr2.setForeground(new java.awt.Color(255, 255, 255));
        item_id_label_cpr2.setText("Item Name:");

        javax.swing.GroupLayout generate_po_panelLayout = new javax.swing.GroupLayout(generate_po_panel);
        generate_po_panel.setLayout(generate_po_panelLayout);
        generate_po_panelLayout.setHorizontalGroup(
            generate_po_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(generate_po_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(generate_po_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(po_item_field, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(po_qtt_field, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(required_quantity_label_cpr1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(po_id_field, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                    .addComponent(purchase_requisition_id_label_cpr1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(item_id_label_cpr1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(po_pr_field, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                    .addComponent(required_date_label_cpr1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(total_price_label_cpr1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(po_date_field, javax.swing.GroupLayout.Alignment.LEADING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 201, Short.MAX_VALUE)
                .addGroup(generate_po_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(add_po_button, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)
                    .addComponent(list_po_button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(141, 141, 141))
            .addGroup(generate_po_panelLayout.createSequentialGroup()
                .addGroup(generate_po_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(generate_po_panelLayout.createSequentialGroup()
                        .addGap(176, 176, 176)
                        .addComponent(create_purchase_requisition_title1))
                    .addGroup(generate_po_panelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(item_id_label_cpr2)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        generate_po_panelLayout.setVerticalGroup(
            generate_po_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(generate_po_panelLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(generate_po_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(generate_po_panelLayout.createSequentialGroup()
                        .addComponent(purchase_requisition_id_label_cpr1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(po_id_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(item_id_label_cpr1))
                    .addGroup(generate_po_panelLayout.createSequentialGroup()
                        .addComponent(create_purchase_requisition_title1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(55, 55, 55)
                        .addComponent(add_po_button, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(list_po_button, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(po_pr_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(item_id_label_cpr2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(po_item_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(required_quantity_label_cpr1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(po_qtt_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(required_date_label_cpr1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(po_date_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(total_price_label_cpr1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(108, Short.MAX_VALUE))
        );

        functionalities.add(generate_po_panel, "generate_po_panel");

        list_user_panel.setBackground(new java.awt.Color(0, 0, 0));

        view_user_title.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        view_user_title.setForeground(new java.awt.Color(255, 255, 255));
        view_user_title.setText("~User List~");

        user_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name", "Password", "Roles"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        user_table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        user_table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        user_table.setShowGrid(false);
        user_table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                user_tableMouseClicked(evt);
            }
        });
        view_user_sPane.setViewportView(user_table);

        search_user_label.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        search_user_label.setForeground(new java.awt.Color(255, 255, 255));
        search_user_label.setText("Enter User ID to search:");

        search_user_field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                search_user_fieldActionPerformed(evt);
            }
        });
        search_user_field.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                search_user_fieldKeyPressed(evt);
            }
        });

        delete_user_button.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        delete_user_button.setText("Delete");
        delete_user_button.setBorderPainted(false);
        delete_user_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delete_user_buttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout list_user_panelLayout = new javax.swing.GroupLayout(list_user_panel);
        list_user_panel.setLayout(list_user_panelLayout);
        list_user_panelLayout.setHorizontalGroup(
            list_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, list_user_panelLayout.createSequentialGroup()
                .addGroup(list_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(list_user_panelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(delete_user_button, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, list_user_panelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(view_user_sPane, javax.swing.GroupLayout.DEFAULT_SIZE, 648, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, list_user_panelLayout.createSequentialGroup()
                        .addGroup(list_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(list_user_panelLayout.createSequentialGroup()
                                .addGap(274, 274, 274)
                                .addComponent(view_user_title))
                            .addGroup(list_user_panelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(list_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(search_user_field, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(search_user_label))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        list_user_panelLayout.setVerticalGroup(
            list_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(list_user_panelLayout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(view_user_title, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48)
                .addComponent(search_user_label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(search_user_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(view_user_sPane, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(delete_user_button, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );

        functionalities.add(list_user_panel, "list_user_panel");

        list_item_panel.setBackground(new java.awt.Color(0, 0, 0));

        list_item_title.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        list_item_title.setForeground(new java.awt.Color(255, 255, 255));
        list_item_title.setText("~Item List~");

        item_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item ID", "Name", "Price", "Supplier ID", "Stock"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        item_table.setColumnSelectionAllowed(true);
        item_table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                item_tableMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(item_table);
        item_table.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        search_item_label.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        search_item_label.setForeground(new java.awt.Color(255, 255, 255));
        search_item_label.setText("Enter Item ID to search:");

        search_item_field.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                search_item_fieldKeyPressed(evt);
            }
        });

        delete_item_button.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        delete_item_button.setText("Delete");
        delete_item_button.setBorderPainted(false);
        delete_item_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delete_item_buttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout list_item_panelLayout = new javax.swing.GroupLayout(list_item_panel);
        list_item_panel.setLayout(list_item_panelLayout);
        list_item_panelLayout.setHorizontalGroup(
            list_item_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(list_item_panelLayout.createSequentialGroup()
                .addGroup(list_item_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(list_item_panelLayout.createSequentialGroup()
                        .addGap(269, 269, 269)
                        .addComponent(list_item_title))
                    .addGroup(list_item_panelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(search_item_label)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(list_item_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(list_item_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(delete_item_button, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(list_item_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 635, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(search_item_field, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 25, Short.MAX_VALUE))
        );
        list_item_panelLayout.setVerticalGroup(
            list_item_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(list_item_panelLayout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(list_item_title, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48)
                .addComponent(search_item_label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(search_item_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(delete_item_button, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        functionalities.add(list_item_panel, "list_item_panel");

        list_supplier_panel.setBackground(new java.awt.Color(0, 0, 0));

        view_supplier_title.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        view_supplier_title.setForeground(new java.awt.Color(255, 255, 255));
        view_supplier_title.setText("~Supplier List~");

        supplier_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Supplier ID", "Name"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        supplier_table.setColumnSelectionAllowed(true);
        supplier_table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                supplier_tableMouseClicked(evt);
            }
        });
        view_supplier_sPane.setViewportView(supplier_table);
        supplier_table.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        search_supplier_label.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        search_supplier_label.setForeground(new java.awt.Color(255, 255, 255));
        search_supplier_label.setText("Enter Supplier ID to search:");

        search_supplier_field.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                search_supplier_fieldKeyPressed(evt);
            }
        });

        supplied_item_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Supplied Item ID", "Supplied Item"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        supplied_item_table.setColumnSelectionAllowed(true);
        view_supplier_sPane1.setViewportView(supplied_item_table);
        supplied_item_table.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        delete_sup_button.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        delete_sup_button.setText("Delete");
        delete_sup_button.setBorderPainted(false);
        delete_sup_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delete_sup_buttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout list_supplier_panelLayout = new javax.swing.GroupLayout(list_supplier_panel);
        list_supplier_panel.setLayout(list_supplier_panelLayout);
        list_supplier_panelLayout.setHorizontalGroup(
            list_supplier_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(list_supplier_panelLayout.createSequentialGroup()
                .addGap(256, 256, 256)
                .addComponent(view_supplier_title)
                .addContainerGap(258, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, list_supplier_panelLayout.createSequentialGroup()
                .addGroup(list_supplier_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(list_supplier_panelLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(delete_sup_button, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(list_supplier_panelLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(list_supplier_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(list_supplier_panelLayout.createSequentialGroup()
                                .addComponent(view_supplier_sPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(view_supplier_sPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(list_supplier_panelLayout.createSequentialGroup()
                                .addGroup(list_supplier_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(search_supplier_label)
                                    .addComponent(search_supplier_field, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGap(25, 25, 25))
        );
        list_supplier_panelLayout.setVerticalGroup(
            list_supplier_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(list_supplier_panelLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(view_supplier_title, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54)
                .addComponent(search_supplier_label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(search_supplier_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addGroup(list_supplier_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(view_supplier_sPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(view_supplier_sPane, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(delete_sup_button, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        functionalities.add(list_supplier_panel, "list_supplier_panel");

        view_pr_panel.setBackground(new java.awt.Color(0, 0, 0));

        view_purchase_requisition_title.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        view_purchase_requisition_title.setForeground(new java.awt.Color(255, 255, 255));
        view_purchase_requisition_title.setText("~View Purchase Requisition~");

        pr_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "PR ID", "Item ID", "Item Name", "Req Quantity", "Req Date", "SalesMangerID", "Total Amount", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, false, true, true, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        pr_table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pr_tableMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(pr_table);

        search_purchase_requisition_label.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        search_purchase_requisition_label.setForeground(new java.awt.Color(255, 255, 255));
        search_purchase_requisition_label.setText("Enter Purchase Requisition ID to search:");

        search_pr_field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                search_pr_fieldActionPerformed(evt);
            }
        });

        generate_po_button.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        generate_po_button.setText("Generate Purchase Order");
        generate_po_button.setBorderPainted(false);
        generate_po_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generate_po_buttonActionPerformed(evt);
            }
        });

        delete_pr_button.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        delete_pr_button.setText("Delete");
        delete_pr_button.setBorderPainted(false);
        delete_pr_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delete_pr_buttonActionPerformed(evt);
            }
        });

        reject_button.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        reject_button.setText("Reject");
        reject_button.setBorderPainted(false);
        reject_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reject_buttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout view_pr_panelLayout = new javax.swing.GroupLayout(view_pr_panel);
        view_pr_panel.setLayout(view_pr_panelLayout);
        view_pr_panelLayout.setHorizontalGroup(
            view_pr_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(view_pr_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(view_pr_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, view_pr_panelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(view_purchase_requisition_title)
                        .addGap(181, 181, 181))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, view_pr_panelLayout.createSequentialGroup()
                        .addComponent(delete_pr_button)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(reject_button)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(generate_po_button)
                        .addContainerGap())
                    .addGroup(view_pr_panelLayout.createSequentialGroup()
                        .addGroup(view_pr_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane7)
                            .addGroup(view_pr_panelLayout.createSequentialGroup()
                                .addGroup(view_pr_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(search_pr_field, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(search_purchase_requisition_label))
                                .addGap(0, 349, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        view_pr_panelLayout.setVerticalGroup(
            view_pr_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(view_pr_panelLayout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(view_purchase_requisition_title, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(search_purchase_requisition_label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(search_pr_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE)
                .addGap(20, 20, 20)
                .addGroup(view_pr_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(generate_po_button, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(delete_pr_button, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(reject_button, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28))
        );

        functionalities.add(view_pr_panel, "view_pr_panel");

        view_se_panel.setBackground(new java.awt.Color(0, 0, 0));

        view_purchase_order_title1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        view_purchase_order_title1.setForeground(new java.awt.Color(255, 255, 255));
        view_purchase_order_title1.setText("~View Daily Item-wise Sales Entry~");

        search_purchase_order_label_vpo1.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        search_purchase_order_label_vpo1.setForeground(new java.awt.Color(255, 255, 255));
        search_purchase_order_label_vpo1.setText("Enter Sales Entry ID to search:");

        search_se_field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                search_se_fieldActionPerformed(evt);
            }
        });
        search_se_field.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                search_se_fieldKeyPressed(evt);
            }
        });

        se_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sales ID", "Item ID", "Item Name", "Sale Date", "Quantity", "Total amount"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, false, true, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        se_table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                se_tableMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(se_table);

        delete_se_button.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        delete_se_button.setText("Delete");
        delete_se_button.setBorderPainted(false);
        delete_se_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delete_se_buttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout view_se_panelLayout = new javax.swing.GroupLayout(view_se_panel);
        view_se_panel.setLayout(view_se_panelLayout);
        view_se_panelLayout.setHorizontalGroup(
            view_se_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, view_se_panelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(view_purchase_order_title1)
                .addGap(146, 146, 146))
            .addGroup(view_se_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(view_se_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(view_se_panelLayout.createSequentialGroup()
                        .addComponent(search_purchase_order_label_vpo1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(view_se_panelLayout.createSequentialGroup()
                        .addGroup(view_se_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 648, Short.MAX_VALUE)
                            .addGroup(view_se_panelLayout.createSequentialGroup()
                                .addComponent(search_se_field, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, view_se_panelLayout.createSequentialGroup()
                                .addGap(0, 549, Short.MAX_VALUE)
                                .addComponent(delete_se_button, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
        );
        view_se_panelLayout.setVerticalGroup(
            view_se_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(view_se_panelLayout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addComponent(view_purchase_order_title1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(search_purchase_order_label_vpo1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(search_se_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(delete_se_button, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42))
        );

        functionalities.add(view_se_panel, "view_se_panel");

        view_po_panel.setBackground(new java.awt.Color(0, 0, 0));

        view_purchase_order_title.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        view_purchase_order_title.setForeground(new java.awt.Color(255, 255, 255));
        view_purchase_order_title.setText("~View Purchase Order~");

        search_purchase_order_label_vpo.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        search_purchase_order_label_vpo.setForeground(new java.awt.Color(255, 255, 255));
        search_purchase_order_label_vpo.setText("Enter Purchase Order ID to search:");

        po_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "PO ID", "PR ID", "PM ID", "ItemID", "Required Date", "Quantity", "Total amount"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, true, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        po_table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                po_tableMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(po_table);

        delete_po_button.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        delete_po_button.setText("Delete");
        delete_po_button.setBorderPainted(false);
        delete_po_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delete_po_buttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout view_po_panelLayout = new javax.swing.GroupLayout(view_po_panel);
        view_po_panel.setLayout(view_po_panelLayout);
        view_po_panelLayout.setHorizontalGroup(
            view_po_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(view_po_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(view_po_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(view_po_panelLayout.createSequentialGroup()
                        .addGroup(view_po_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 648, Short.MAX_VALUE)
                            .addGroup(view_po_panelLayout.createSequentialGroup()
                                .addComponent(search_purchase_order_vpo, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, view_po_panelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(view_po_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, view_po_panelLayout.createSequentialGroup()
                                .addComponent(view_purchase_order_title)
                                .addGap(205, 205, 205))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, view_po_panelLayout.createSequentialGroup()
                                .addComponent(delete_po_button, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())))
                    .addGroup(view_po_panelLayout.createSequentialGroup()
                        .addComponent(search_purchase_order_label_vpo)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        view_po_panelLayout.setVerticalGroup(
            view_po_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(view_po_panelLayout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(view_purchase_order_title, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53)
                .addComponent(search_purchase_order_label_vpo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(search_purchase_order_vpo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(delete_po_button, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42))
        );

        functionalities.add(view_po_panel, "view_po_panel");

        profile_page.setBackground(new java.awt.Color(0, 0, 0));

        profile_page_title.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        profile_page_title.setForeground(new java.awt.Color(255, 255, 255));
        profile_page_title.setText("~Profile~");

        user_id_label_profile.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        user_id_label_profile.setForeground(new java.awt.Color(255, 255, 255));
        user_id_label_profile.setText("User ID:");

        user_id_profile.setEditable(false);

        username_label_profile.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        username_label_profile.setForeground(new java.awt.Color(255, 255, 255));
        username_label_profile.setText("Username:");

        username_profile.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                username_profileFocusLost(evt);
            }
        });

        role_label_profile.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        role_label_profile.setForeground(new java.awt.Color(255, 255, 255));
        role_label_profile.setText("Role:");

        role_profile.setEditable(false);

        change_password.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        change_password.setText("Change Password");
        change_password.setBorderPainted(false);
        change_password.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                change_passwordActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout profile_pageLayout = new javax.swing.GroupLayout(profile_page);
        profile_page.setLayout(profile_pageLayout);
        profile_pageLayout.setHorizontalGroup(
            profile_pageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(profile_pageLayout.createSequentialGroup()
                .addGap(134, 134, 134)
                .addGroup(profile_pageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(profile_page_title)
                    .addGroup(profile_pageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(user_id_label_profile, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(user_id_profile, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(username_label_profile, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(profile_pageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(role_label_profile)
                            .addComponent(username_profile, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(role_profile, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(change_password))))
                .addContainerGap(302, Short.MAX_VALUE))
        );
        profile_pageLayout.setVerticalGroup(
            profile_pageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(profile_pageLayout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(profile_page_title, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(71, 71, 71)
                .addComponent(user_id_label_profile)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(user_id_profile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(username_label_profile)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(username_profile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(role_label_profile)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(role_profile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(change_password)
                .addContainerGap(186, Short.MAX_VALUE))
        );

        functionalities.add(profile_page, "profile_page");

        edit_profile_page.setBackground(new java.awt.Color(0, 0, 0));

        edit_profile_page_title.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        edit_profile_page_title.setForeground(new java.awt.Color(255, 255, 255));
        edit_profile_page_title.setText("~Change Password~");

        old_password_label_edit.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        old_password_label_edit.setForeground(new java.awt.Color(255, 255, 255));
        old_password_label_edit.setText("Old Password:");

        new_password_label_edit.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        new_password_label_edit.setForeground(new java.awt.Color(255, 255, 255));
        new_password_label_edit.setText("New Password:");

        comfirm_password_label_edit.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        comfirm_password_label_edit.setForeground(new java.awt.Color(255, 255, 255));
        comfirm_password_label_edit.setText("Confirm Password:");

        confirm_edit.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        confirm_edit.setText("Confirm");
        confirm_edit.setBorderPainted(false);
        confirm_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirm_editActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout edit_profile_pageLayout = new javax.swing.GroupLayout(edit_profile_page);
        edit_profile_page.setLayout(edit_profile_pageLayout);
        edit_profile_pageLayout.setHorizontalGroup(
            edit_profile_pageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(edit_profile_pageLayout.createSequentialGroup()
                .addGroup(edit_profile_pageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(edit_profile_pageLayout.createSequentialGroup()
                        .addGap(130, 130, 130)
                        .addGroup(edit_profile_pageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(old_password_edit, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(edit_profile_pageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(old_password_label_edit)
                                .addComponent(new_password_label_edit)
                                .addComponent(comfirm_password_label_edit)
                                .addComponent(new_password_edit, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
                                .addComponent(confirm_password_edit))))
                    .addGroup(edit_profile_pageLayout.createSequentialGroup()
                        .addGap(231, 231, 231)
                        .addComponent(edit_profile_page_title))
                    .addGroup(edit_profile_pageLayout.createSequentialGroup()
                        .addGap(285, 285, 285)
                        .addComponent(confirm_edit, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        edit_profile_pageLayout.setVerticalGroup(
            edit_profile_pageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(edit_profile_pageLayout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addComponent(edit_profile_page_title, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54)
                .addComponent(old_password_label_edit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(old_password_edit, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(new_password_label_edit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(new_password_edit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(comfirm_password_label_edit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(confirm_password_edit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52)
                .addComponent(confirm_edit)
                .addContainerGap(170, Short.MAX_VALUE))
        );

        functionalities.add(edit_profile_page, "edit_profile_page");

        full_page.setRightComponent(functionalities);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(login_page, javax.swing.GroupLayout.DEFAULT_SIZE, 874, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(full_page, javax.swing.GroupLayout.Alignment.TRAILING))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(login_page, javax.swing.GroupLayout.DEFAULT_SIZE, 563, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(full_page, javax.swing.GroupLayout.Alignment.TRAILING))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void add_user_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_add_user_buttonActionPerformed
        // TODO add your handling code here:
        if(new_username.getText().isBlank() || new_username.getText().isEmpty() || new_password.getText().isBlank() || new_password.getText().isEmpty() || confirm_password.getText().isEmpty() || confirm_password.getText().isBlank()){
            JOptionPane.showConfirmDialog(full_page, "Do not leave blank line.","Error", JOptionPane.PLAIN_MESSAGE);
            return;
        }
        boolean usernameOverlap = false;
        String newUserId = null;
        String newUserRole = null;
        String newUsername = new_username.getText().replaceAll("[^A-Za-z0-9.!@#$%^&*]", "");
        String newPassword = new_password.getText().replaceAll("[^A-Za-z0-9.!@#$%^&*]", "");

        //prevent username overlap
        for(User user:User.getUser()){
            if(user.getUsername().equals(newUsername)){
                usernameOverlap = true;
            }
        }
        
        if(usernameOverlap){
            JOptionPane.showConfirmDialog(full_page, "Username has been registered.","Error", JOptionPane.PLAIN_MESSAGE);
            return;
        }
                
        switch(role_cBox.getSelectedItem().toString()){
            case "SalesManager":
                User new_sm = new SalesManager();
                newUserId = ((SalesManager)new_sm).generateID();
                newUserRole = "SalesManager";
                break;
            case "PurchaseManager":
                User new_pm = new PurchaseManager();
                newUserId = ((PurchaseManager)new_pm).generateID();
                newUserRole = "PurchaseManager";
                break;
            case "Administrator":
                User new_ad = new PurchaseManager();
                newUserId = ((PurchaseManager)new_ad).generateID();
                newUserRole = "Administrator";
                break;
        }
        if(confirm_password.getText().equals(new_password.getText())){
            new Administrator().registerUser(newUserId,newUsername,newPassword,newUserRole);
            JOptionPane.showConfirmDialog(full_page, "User has been successfully added.","Successful", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE);
        }
        else{
            JOptionPane.showConfirmDialog(full_page, "Your password and confirm password are mismatched.","Error", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE);
        }
        new_username.setText("");
        new_password.setText("");
        confirm_password.setText("");
    }//GEN-LAST:event_add_user_buttonActionPerformed

    private void role_cBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_role_cBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_role_cBoxActionPerformed

    private void view_itemsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_view_itemsActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel)item_table.getModel();
        // Clear existing rows
        while(model.getRowCount() > 0) {
            model.removeRow(0);
        }
        
        for(Item item:Item.getItems()){
            model.addRow(new Object[]{item.getItemID(),item.getItemName(),item.getPrice(),item.getSupplier().getSupplierID(),item.getStock()});
        }
        selectedID = null;
        cardLayout2.show(functionalities,"list_item_panel");
    }//GEN-LAST:event_view_itemsActionPerformed

    private void view_suppliersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_view_suppliersActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel)supplier_table.getModel();
        DefaultTableModel model2 = (DefaultTableModel)supplied_item_table.getModel();
        // Clear existing rows
        while(model.getRowCount() > 0) {
            model.removeRow(0);
        }
        
        while(model2.getRowCount() > 0) {
            model2.removeRow(0);
        }
        
        for(Supplier sup : Supplier.getSupplier()){    
            model.addRow(new Object[]{sup.getSupplierID(),sup.getSupplierName()});
        }
        selectedID = null;
        cardLayout2.show(functionalities,"list_supplier_panel");
    }//GEN-LAST:event_view_suppliersActionPerformed

    private void view_purchase_req_purchasesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_view_purchase_req_purchasesActionPerformed
        // TODO add your handling code here:
        selectedID = null;
        PurchaseRequisition.prStatusUpdate();
        DefaultTableModel model = (DefaultTableModel)pr_table.getModel();
        // Clear existing rows
        while(model.getRowCount() > 0) {
            model.removeRow(0);
        }
        //show pr to table
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for(PurchaseRequisition pr: PurchaseRequisition.getPR()){
            String requiredDateFormatted = sdf.format(pr.getRequiredDate());
            model.addRow(new Object[]{pr.getPRID(), pr.getPRItem().getItemID(), pr.getPRItem().getItemName(), pr.getRequiredQuantity(), requiredDateFormatted, pr.getPRSMID(), pr.getTotalPrice(), pr.getStatus()});
        }
        cardLayout2.show(functionalities,"view_pr_panel");
    }//GEN-LAST:event_view_purchase_req_purchasesActionPerformed

    private void exit_purchase_mngActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exit_purchase_mngActionPerformed
        // TODO add your handling code here:
        selectedID = null;
        username_field.setText("");
        password_field.setText("");
        delete_pr_button.setVisible(true);
        delete_item_button.setVisible(true);
        delete_sup_button.setVisible(true);
        full_page.setVisible(false);
        login_page.setVisible(true);
    }//GEN-LAST:event_exit_purchase_mngActionPerformed

    private void formComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentHidden
        // TODO add your handling code here:
    }//GEN-LAST:event_formComponentHidden

    private void supplier_entry_admActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplier_entry_admActionPerformed
        // TODO add your handling code here:
        supplier_id_supp.setText(new Supplier().previewNextID());
        selectedID = null;
        cardLayout2.show(functionalities,"supplier_management_panel");
    }//GEN-LAST:event_supplier_entry_admActionPerformed

    private void daily_itemwise_sales_entry_admActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_daily_itemwise_sales_entry_admActionPerformed
        // TODO add your handling code here:
        sales_entry_id_dis.setText(new SalesEntry().previewNextID());
        selectedID = null;
        cardLayout2.show(functionalities,"daily_itemwise_sales_panel");
    }//GEN-LAST:event_daily_itemwise_sales_entry_admActionPerformed

    private void create_purchase_requisition_admActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_create_purchase_requisition_admActionPerformed
        // TODO add your handling code here:
        PurchaseRequisition.prStatusUpdate();
        pr_id_cpr.setText(new PurchaseRequisition().previewNextID());
        selectedID = null;
        cardLayout2.show(functionalities,"create_purchase_requisition_panel");
    }//GEN-LAST:event_create_purchase_requisition_admActionPerformed

    private void register_user_panel_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_register_user_panel_buttonActionPerformed
        // TODO add your handling code here:
        item_id.setText(new Item().previewNextID());
        selectedID = null;
        cardLayout2.show(functionalities,"registration");
    }//GEN-LAST:event_register_user_panel_buttonActionPerformed

    private void item_supplier_idActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_item_supplier_idActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_item_supplier_idActionPerformed

    private void list_itemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_list_itemActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel)item_table.getModel();
        // Clear existing rows
        while(model.getRowCount() > 0) {
            model.removeRow(0);
        }
        
        for(Item item:Item.getItems()){
            model.addRow(new Object[]{item.getItemID(),item.getItemName(),item.getPrice(),item.getSupplier().getSupplierID(),item.getStock()});
        }
        selectedID = null;
        cardLayout2.show(functionalities,"list_item_panel");
    }//GEN-LAST:event_list_itemActionPerformed

    private void add_itemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_add_itemActionPerformed
        // TODO add your handling code here:
        //check blank or empty input        
        if(item_name.getText().isBlank() || item_name.getText().isEmpty() || item_price.getText().isBlank() || item_price.getText().isEmpty() || item_stock.getText().isBlank() || item_stock.getText().isEmpty()){
            JOptionPane.showConfirmDialog(full_page, "Do not leave blank line.","Error", JOptionPane.PLAIN_MESSAGE);
            return;
        }
                
        boolean supplierFound = false;
        Supplier sp = new Supplier().getByID(item_supplier_id.getText());
        if(sp != null){
            supplierFound = true;
        }
        
        if(supplierFound == false){
            JOptionPane.showConfirmDialog(full_page, "Supplier not found.","Error", JOptionPane.PLAIN_MESSAGE);
        }
        else{
            Item new_item = new Item(item_name.getText(),Double.parseDouble(item_price.getText()),sp,Integer.parseInt(item_stock.getText()));
            new_item.add(new_item);
            sp.addSuppliedItem(new_item);
            JOptionPane.showConfirmDialog(full_page, "Item has been added successfully.","Successful", JOptionPane.PLAIN_MESSAGE);
            item_id.setText(new_item.previewNextID());
        }
        item_name.setText("");
        item_price.setText("");
        item_supplier_id.setText("");
        item_stock.setText("");
    }//GEN-LAST:event_add_itemActionPerformed

    private void list_supplier_suppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_list_supplier_suppActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel)supplier_table.getModel();
        DefaultTableModel model2 = (DefaultTableModel)supplied_item_table.getModel();
        // Clear existing rows
        while(model.getRowCount() > 0) {
            model.removeRow(0);
        }
        
        while(model2.getRowCount() > 0) {
            model2.removeRow(0);
        }
        
        for(Supplier sup : Supplier.getSupplier()){    
            model.addRow(new Object[]{sup.getSupplierID(),sup.getSupplierName()});
        }
        selectedID = null;
        cardLayout2.show(functionalities,"list_supplier_panel");
    }//GEN-LAST:event_list_supplier_suppActionPerformed

    private void add_item_suppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_add_item_suppActionPerformed
        // TODO add your handling code here:
        //check blank or empty input
        if(supplier_name_supp.getText().isBlank() || supplier_name_supp.getText().isEmpty()){
            JOptionPane.showConfirmDialog(full_page, "Do not leave blank line.","Error", JOptionPane.PLAIN_MESSAGE);
            return;
        }        
        
        ArrayList<Item> new_supp_items = new ArrayList<>();
        Supplier new_sup = new Supplier(supplier_name_supp.getText(),new_supp_items);
        new_sup.add(new_sup);
        JOptionPane.showConfirmDialog(full_page, "Supplier has been added successfully.","Successful", JOptionPane.PLAIN_MESSAGE);
        supplier_id_supp.setText(new_sup.previewNextID());
        supplier_name_supp.setText("");
    }//GEN-LAST:event_add_item_suppActionPerformed

    private void supplier_id_suppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplier_id_suppActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_supplier_id_suppActionPerformed

    private void login_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_login_buttonActionPerformed
        // TODO add your handling code here:
        if(username_field.getText().isBlank() || username_field.getText().isEmpty() || password_field.getText().isBlank() || password_field.getText().isEmpty()){
            JOptionPane.showConfirmDialog(login_page, "Do not leave blank line.","Error", JOptionPane.PLAIN_MESSAGE);
            return;
        }
        currentUser = User.login(username_field.getText(), password_field.getText());
        if(currentUser != null){
            //identify role and initial user interface
            switch(currentUser.getRole()){
                case SALESMANAGER:
                    item_id.setText(new Item().previewNextID());
                    delete_po_button.setVisible(false);
                    generate_po_button.setVisible(false);
                    reject_button.setVisible(false);
                    cardLayout1.show(menuOptions,"sales_manager_options");
                    cardLayout2.show(functionalities,"item_entry_panel");
                    break;
                case PURCHASEMANAGER:
                    //initialize item table
                    DefaultTableModel model = (DefaultTableModel)item_table.getModel();
                    while(model.getRowCount() > 0) {
                        model.removeRow(0);
                    }
                    for(Item item:Item.getItems()){
                        model.addRow(new Object[]{item.getItemID(),item.getItemName(),item.getPrice(),item.getSupplier().getSupplierID(),item.getStock()});
                    }
                    //initialize user interface
                    selectedID = null;                 
                    delete_pr_button.setVisible(false);
                    delete_item_button.setVisible(false);
                    delete_sup_button.setVisible(false);                    
                    cardLayout1.show(menuOptions,"purchase_manager_options");
                    cardLayout2.show(functionalities,"list_item_panel");                    
                    break;
                case ADMINISTRATOR:
                    cardLayout1.show(menuOptions,"adminsitrator_options");
                    cardLayout2.show(functionalities,"registration");
                    break;
            }
            login_page.setVisible(false);
            full_page.setVisible(true);
        }
        else { 
            JOptionPane.showConfirmDialog(login_page, "Your username or password is incorrect.","Error",JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE);
        }
        username_field.setText("");
        password_field.setText("");
    }//GEN-LAST:event_login_buttonActionPerformed

    private void supplier_entryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplier_entryActionPerformed
        // TODO add your handling code here:
        supplier_id_supp.setText(new Supplier().previewNextID());
        selectedID = null;
        cardLayout2.show(functionalities,"supplier_management_panel");
    }//GEN-LAST:event_supplier_entryActionPerformed

    private void daily_itemwise_sales_entryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_daily_itemwise_sales_entryActionPerformed
        // TODO add your handling code here:
        sales_entry_id_dis.setText(new SalesEntry().previewNextID());
        selectedID = null;
        cardLayout2.show(functionalities,"daily_itemwise_sales_panel");
    }//GEN-LAST:event_daily_itemwise_sales_entryActionPerformed

    private void create_purchase_requisitionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_create_purchase_requisitionActionPerformed
        // TODO add your handling code here:
        PurchaseRequisition.prStatusUpdate();
        pr_id_cpr.setText(new PurchaseRequisition().previewNextID());
        selectedID = null;
        cardLayout2.show(functionalities,"create_purchase_requisition_panel");
    }//GEN-LAST:event_create_purchase_requisitionActionPerformed

    private void view_purchase_orderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_view_purchase_orderActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel)po_table.getModel();
        // Clear existing rows
        while(model.getRowCount() > 0) {
            model.removeRow(0);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for(PurchaseOrder po: PurchaseOrder.getPO()){
            String dateFormatted = sdf.format(po.getPOPR().getRequiredDate());
            model.addRow(new Object[]{po.getPOID(), po.getPOPR().getPRID(),po.getPMID(),po.getPOPR().getPRItem().getItemID(),dateFormatted,po.getPOPR().getRequiredQuantity(),po.getPOPR().getTotalPrice()});
        }
        selectedID = null;
        cardLayout2.show(functionalities,"view_po_panel");
    }//GEN-LAST:event_view_purchase_orderActionPerformed

    private void exit_sales_mngActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exit_sales_mngActionPerformed
        // TODO add your handling code here:
        selectedID = null;
        username_field.setText("");
        password_field.setText("");
        delete_po_button.setVisible(true);
        generate_po_button.setVisible(true);
        reject_button.setVisible(true);
        full_page.setVisible(false);
        login_page.setVisible(true);
    }//GEN-LAST:event_exit_sales_mngActionPerformed

    private void item_entryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_item_entryActionPerformed
        // TODO add your handling code here:
        item_id.setText(new Item().previewNextID());
        selectedID = null;
        cardLayout2.show(functionalities,"item_entry_panel");
    }//GEN-LAST:event_item_entryActionPerformed

    private void sales_entry_id_disActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sales_entry_id_disActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sales_entry_id_disActionPerformed

    private void add_item_disActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_add_item_disActionPerformed
        // TODO add your handling code here:
        if(sold_quantity_dis.getText().isBlank()||sold_quantity_dis.getText().isEmpty()){
            JOptionPane.showConfirmDialog(full_page, "Do not leave blank line.","Error", JOptionPane.PLAIN_MESSAGE);
        }
        Date newSalesDate = null;
        LocalDate currentDateLocal = LocalDate.now();
        Date currentDate = Date.from(currentDateLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            newSalesDate = sdf.parse(sales_date_dis.getText());
            //check sales date after current date
            if(newSalesDate.after(currentDate)){
                JOptionPane.showConfirmDialog(full_page, "Date exceed current date.","Error", JOptionPane.PLAIN_MESSAGE);
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            JOptionPane.showConfirmDialog(full_page, "Date format error.","Error", JOptionPane.PLAIN_MESSAGE);
            return;
        }
        
        //check quantity
        if(Integer.parseInt(sold_quantity_dis.getText()) <= 0){
            JOptionPane.showConfirmDialog(full_page, "Invalid quantity.","Error", JOptionPane.PLAIN_MESSAGE);
            return;
        }        
        
        Item seItem = new Item().getByID(item_id_dis.getText());        
        if(seItem != null){
            int newStock = seItem.getStock() - Integer.parseInt(sold_quantity_dis.getText());
            if(newStock > 0){
                seItem.setStock(newStock);
                SalesEntry new_se = new SalesEntry(seItem,Integer.parseInt(sold_quantity_dis.getText()),newSalesDate);
                new_se.add(new_se);
                sales_entry_id_dis.setText(new_se.previewNextID());
                JOptionPane.showConfirmDialog(full_page, "Sales has been added successfully.","Successful", JOptionPane.PLAIN_MESSAGE);            
            }
            else{
                JOptionPane.showConfirmDialog(full_page, "Sales quantity exceed item's stock.","Error", JOptionPane.PLAIN_MESSAGE);
            }
        }
        else{
            JOptionPane.showConfirmDialog(full_page, "Item does not exists.","Error", JOptionPane.PLAIN_MESSAGE);
        }
        
        item_id_dis.setText("");
        sold_quantity_dis.setText("");
        sales_date_dis.setText("");
        total_price_dis.setText("");
    }//GEN-LAST:event_add_item_disActionPerformed

    private void list_daily_sales_disActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_list_daily_sales_disActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel)se_table.getModel();
        // Clear existing rows
        while(model.getRowCount() > 0) {
            model.removeRow(0);
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for(SalesEntry se : SalesEntry.getSE()){
            if(se == null){
                break;
            }
            String salesDateFormatted = sdf.format(se.getSalesDate());
            model.addRow(new Object[]{se.getSalesID(),se.getItem().getItemID(),se.getItem().getItemName(),salesDateFormatted,se.getQuantitySold(),se.getTotalAmount()});
        }
        cardLayout2.show(functionalities,"view_se_panel");
    }//GEN-LAST:event_list_daily_sales_disActionPerformed

    private void pr_id_cprActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pr_id_cprActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pr_id_cprActionPerformed

    private void add_item_cprActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_add_item_cprActionPerformed
        // TODO add your handling code here:
        if(required_quantity_cpr.getText().isBlank() || required_quantity_cpr.getText().isEmpty()){
            JOptionPane.showConfirmDialog(full_page, "Do not leave blank line.","Error", JOptionPane.PLAIN_MESSAGE);
        }
        Date newRequiredDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            newRequiredDate = sdf.parse(required_date_cpr.getText());
        } catch (ParseException e) {
            e.printStackTrace();
            JOptionPane.showConfirmDialog(full_page, "Date format error.","Error", JOptionPane.PLAIN_MESSAGE);
            return;
        }
        //required date cannot before current date
        LocalDate currentDateLocal = LocalDate.now();
        Date currentDate = Date.from(currentDateLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());
        
        if (newRequiredDate.before(currentDate)) {
            JOptionPane.showConfirmDialog(full_page, "Required Date cannot before current date.","Error", JOptionPane.PLAIN_MESSAGE);
            return;
        }
        //check quantity
        if(Integer.parseInt(required_quantity_cpr.getText()) <= 0){
            JOptionPane.showConfirmDialog(full_page, "Invalid quantity.","Error", JOptionPane.PLAIN_MESSAGE);
            return;
        }
        //check item exits 
        Item prItem = new Item().getByID(item_id_cpr.getText());
        if(prItem != null){
            PurchaseRequisition new_pr = new PurchaseRequisition(prItem,Integer.parseInt(required_quantity_cpr.getText()),newRequiredDate,currentUser.getID(),"Approved"); 
            new_pr.add(new_pr);
            pr_id_cpr.setText(new_pr.previewNextID());
            JOptionPane.showConfirmDialog(full_page, "Purchase Requisition has been added successfully.","Successful", JOptionPane.PLAIN_MESSAGE);
        }
        else{
            JOptionPane.showConfirmDialog(full_page, "Item does not exists.","Error", JOptionPane.PLAIN_MESSAGE);
        }
        
        item_id_cpr.setText("");
        required_quantity_cpr.setText("");
        required_date_cpr.setText("");
        total_price_cpr.setText("");
    }//GEN-LAST:event_add_item_cprActionPerformed

    private void delete_po_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delete_po_buttonActionPerformed
        // TODO add your handling code here:
        if(selectedID == null){
            JOptionPane.showConfirmDialog(full_page, "No row selected.","Error", JOptionPane.QUESTION_MESSAGE);
            return;
        }

        int option = JOptionPane.showConfirmDialog(full_page, "Are you sure you want to delete?.","Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        PurchaseOrder po = new PurchaseOrder().getByID(selectedID);
        if (option == JOptionPane.YES_OPTION) {
            po.getPOPR().setStatus("Canceled");
            po.delete(selectedID);
            DefaultTableModel model = (DefaultTableModel)po_table.getModel();
            int selected_index = po_table.getSelectedRow();
            model.removeRow(selected_index);
            JOptionPane.showConfirmDialog(full_page, "Purchase Order has been deleted.","Successful", JOptionPane.PLAIN_MESSAGE);
        }
        
        //refresh table
        DefaultTableModel model = (DefaultTableModel)po_table.getModel();
        // Clear existing rows
        while(model.getRowCount() > 0) {
            model.removeRow(0);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for(PurchaseOrder pos: PurchaseOrder.getPO()){
            String dateFormatted = sdf.format(pos.getPOPR().getRequiredDate());
            model.addRow(new Object[]{pos.getPOID(), pos.getPOPR().getPRID(),pos.getPMID(),pos.getPOPR().getPRItem().getItemID(),dateFormatted,pos.getPOPR().getRequiredQuantity(),pos.getPOPR().getTotalPrice()});
        }
        selectedID = null;
    }//GEN-LAST:event_delete_po_buttonActionPerformed

    private void change_passwordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_change_passwordActionPerformed
        // TODO add your handling code here:
        cardLayout2.show(functionalities,"edit_profile_page");
    }//GEN-LAST:event_change_passwordActionPerformed

    private void profile_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profile_buttonActionPerformed
        // TODO add your handling code here:
        user_id_profile.setText(currentUser.getID());
        username_profile.setText(currentUser.getUsername());
        role_profile.setText(currentUser.getRole().toString());
        selectedID = null;
        cardLayout2.show(functionalities,"profile_page");
    }//GEN-LAST:event_profile_buttonActionPerformed

    private void list_user_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_list_user_buttonActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel)user_table.getModel();
        // Clear existing rows
        while(model.getRowCount() > 0) {
            model.removeRow(0);
        }
        for(User user:User.getUser()){
            model.addRow(new Object[]{user.getID(),user.getUsername(),user.getPassword(),user.getRole()});
        }
        cardLayout2.show(functionalities,"list_user_panel");
    }//GEN-LAST:event_list_user_buttonActionPerformed

    private void item_entry_admActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_item_entry_admActionPerformed
        // TODO add your handling code here:
        item_id.setText(new Item().previewNextID());
        cardLayout2.show(functionalities,"item_entry_panel");
    }//GEN-LAST:event_item_entry_admActionPerformed

    private void generate_po_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generate_po_buttonActionPerformed
        // TODO add your handling code here:
        if(selectedID == null){
            JOptionPane.showConfirmDialog(full_page, "No row selected.","Error", JOptionPane.PLAIN_MESSAGE);
            return;
        }
        //check pr status
        PurchaseRequisition pr = new PurchaseRequisition().getByID(selectedID);
        if(pr.getStatus().equals("Expired") || pr.getStatus().equals("Canceled") || pr.getStatus().equals("Rejected")){
            JOptionPane.showConfirmDialog(full_page, "Purchase order cannot be generated.\nThis purchase requisition is not available.","Error", JOptionPane.PLAIN_MESSAGE);
            return;
        }
        else if(pr.getStatus().equals("Converted to PO")){
            JOptionPane.showConfirmDialog(full_page, "Purchase order already exists.","Error", JOptionPane.PLAIN_MESSAGE);
            return;
        }
        //get option
        int option = JOptionPane.showConfirmDialog(full_page, "Are you sure you want to generate purchase order?.","Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option == JOptionPane.YES_OPTION) {
            PurchaseOrder po = new PurchaseOrder(pr,currentUser.getID());
            po.add(po);
            pr.setStatus("Conveted to PO");
            JOptionPane.showConfirmDialog(full_page, "Purchase order has been generated.","Successful", JOptionPane.PLAIN_MESSAGE);
            selectedID = null;
        }
        //refresh pr table
        PurchaseRequisition.prStatusUpdate();
        DefaultTableModel model = (DefaultTableModel)pr_table.getModel();
        // Clear existing rows
        while(model.getRowCount() > 0) {
            model.removeRow(0);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for(PurchaseRequisition prs: PurchaseRequisition.getPR()){
            String requiredDateFormatted = sdf.format(prs.getRequiredDate());
            model.addRow(new Object[]{prs.getPRID(), prs.getPRItem().getItemID(), prs.getPRItem().getItemName(), prs.getRequiredQuantity(), requiredDateFormatted, prs.getPRSMID(), prs.getTotalPrice(), prs.getStatus()});
        }           
    }//GEN-LAST:event_generate_po_buttonActionPerformed

    private void profile_button_admActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profile_button_admActionPerformed
        // TODO add your handling code here:
        user_id_profile.setText(currentUser.getID());
        username_profile.setText(currentUser.getUsername());
        role_profile.setText(currentUser.getRole().toString());
        selectedID = null;
        cardLayout2.show(functionalities,"profile_page");
    }//GEN-LAST:event_profile_button_admActionPerformed

    private void logout_button_admActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logout_button_admActionPerformed
        // TODO add your handling code here:
        selectedID = null;
        username_field.setText("");
        password_field.setText("");
        full_page.setVisible(false);
        login_page.setVisible(true);
    }//GEN-LAST:event_logout_button_admActionPerformed

    private void list_pr_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_list_pr_buttonActionPerformed
        // TODO add your handling code here:
        PurchaseRequisition.prStatusUpdate();
        DefaultTableModel model = (DefaultTableModel)pr_table.getModel();
        // Clear existing rows
        while(model.getRowCount() > 0) {
            model.removeRow(0);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for(PurchaseRequisition pr: PurchaseRequisition.getPR()){
            String requiredDateFormatted = sdf.format(pr.getRequiredDate());
            model.addRow(new Object[]{pr.getPRID(), pr.getPRItem().getItemID(), pr.getPRItem().getItemName(), pr.getRequiredQuantity(), requiredDateFormatted, pr.getPRSMID(), pr.getTotalPrice(), pr.getStatus()});
        }
        selectedID = null;
        cardLayout2.show(functionalities,"view_pr_panel");
    }//GEN-LAST:event_list_pr_buttonActionPerformed

    private void search_user_fieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_search_user_fieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_search_user_fieldActionPerformed

    private void search_user_fieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_search_user_fieldKeyPressed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel)user_table.getModel();
        TableRowSorter<DefaultTableModel> trs = new TableRowSorter<>(model);
        user_table.setRowSorter(trs);
        trs.setRowFilter(RowFilter.regexFilter(search_user_field.getText()));    
    }//GEN-LAST:event_search_user_fieldKeyPressed

    private void delete_user_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delete_user_buttonActionPerformed
        // TODO add your handling code here:
        int option = 0;
        boolean valueGet = true;
        if(selectedID == null){
            valueGet = false;
            JOptionPane.showConfirmDialog(full_page, "No selected row.","Error", JOptionPane.PLAIN_MESSAGE);
            return;
        }
        if(currentUser.getID() != selectedID){
            option = JOptionPane.showConfirmDialog(full_page, "Are you sure you want to delete?.","Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
                User user = new User();
                user.delete(selectedID);
                DefaultTableModel model = (DefaultTableModel)user_table.getModel();
                int selected_index = user_table.getSelectedRow();
                model.removeRow(selected_index);
                JOptionPane.showConfirmDialog(full_page, "User has been deleted.","Successful", JOptionPane.PLAIN_MESSAGE);
                selectedID = null;
            }
        }
        else{
            JOptionPane.showConfirmDialog(full_page, "You cannot delete yourself.","Error", JOptionPane.PLAIN_MESSAGE);
        }
    }//GEN-LAST:event_delete_user_buttonActionPerformed

    private void user_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_user_tableMouseClicked
        // TODO add your handling code here:
        DefaultTableModel model= (DefaultTableModel)user_table.getModel();
        int selectedRowIndex = user_table.getSelectedRow();
        if(selectedRowIndex != -1){
            selectedID = model.getValueAt(selectedRowIndex,0).toString();
        }
    }//GEN-LAST:event_user_tableMouseClicked

    private void item_priceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_item_priceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_item_priceActionPerformed

    private void item_priceKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_item_priceKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_item_priceKeyTyped

    private void item_stockKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_item_stockKeyTyped
        // TODO add your handling code here:       
    }//GEN-LAST:event_item_stockKeyTyped

    private void delete_item_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delete_item_buttonActionPerformed
        // TODO add your handling code here:
        if(selectedID == null){
            JOptionPane.showConfirmDialog(full_page, "No row selected.","Error", JOptionPane.QUESTION_MESSAGE);
            return;
        }
        int option = JOptionPane.showConfirmDialog(full_page, "Are you sure you want to delete?\nPurchase requisition and purchase order will be deleted if exists.","Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        Item item = new Item().getByID(selectedID);
        if (option == JOptionPane.YES_OPTION) {
            item.delete(selectedID);
            DefaultTableModel model = (DefaultTableModel)item_table.getModel();
            int selected_index = item_table.getSelectedRow();
            model.removeRow(selected_index);
            JOptionPane.showConfirmDialog(full_page, "Item has been deleted.","Successful", JOptionPane.PLAIN_MESSAGE);
            selectedID = null;
            
            Supplier sp = new Supplier().getByID(item.getSupplier().getSupplierID());
            sp.deleteSuppliedItem(item.getItemID());
            //delete pr
            for(PurchaseRequisition pr:PurchaseRequisition.getPR()){
                if(pr.getPRItem().equals(item)){
                    //delete po
                    for(PurchaseOrder po:PurchaseOrder.getPO()){
                        if(pr.getPRID().equals(po.getPOPR().getPRID())){
                            po.delete(po.getPOID());
                        }
                    }
                    pr.delete(pr.getPRID());
                }
            }
            //delete se
            for(SalesEntry se:SalesEntry.getSE()){
                if(se.getItem().getItemID().equals(item.getItemID())){
                    se.delete(se.getSalesID());
                }
            }
        } 
    }//GEN-LAST:event_delete_item_buttonActionPerformed

    private void search_item_fieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_search_item_fieldKeyPressed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel)item_table.getModel();
        TableRowSorter<DefaultTableModel> trs = new TableRowSorter<>(model);
        item_table.setRowSorter(trs);
        trs.setRowFilter(RowFilter.regexFilter(search_item_field.getText()));    
    }//GEN-LAST:event_search_item_fieldKeyPressed

    private void search_supplier_fieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_search_supplier_fieldKeyPressed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel)supplier_table.getModel();
        TableRowSorter<DefaultTableModel> trs = new TableRowSorter<>(model);
        supplier_table.setRowSorter(trs);
        trs.setRowFilter(RowFilter.regexFilter(search_supplier_field.getText()));            
    }//GEN-LAST:event_search_supplier_fieldKeyPressed

    private void supplier_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_supplier_tableMouseClicked
        // TODO add your handling code here:
        DefaultTableModel modelSupplier = (DefaultTableModel)supplier_table.getModel();
        DefaultTableModel modelItem = (DefaultTableModel)supplied_item_table.getModel();
        while(modelItem.getRowCount() > 0) {
            modelItem.removeRow(0);
        }
        int selectedRowIndex = supplier_table.getSelectedRow();
        selectedID = modelSupplier.getValueAt(selectedRowIndex,0).toString();
        Supplier sp = new Supplier().getByID(selectedID);
        if(sp != null){
            for(Item item:sp.getSuppliedItem()){
                modelItem.addRow(new Object[]{item.getItemID(),item.getItemName()});
            }            
        }
    }//GEN-LAST:event_supplier_tableMouseClicked

    private void delete_pr_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delete_pr_buttonActionPerformed
        // TODO add your handling code here:
        if(selectedID == null){
            JOptionPane.showConfirmDialog(full_page, "No row selected.","Error", JOptionPane.QUESTION_MESSAGE);
            return;
        }
        
        int option = JOptionPane.showConfirmDialog(full_page, "Are you sure you want to delete?\nPurchase Order will be deleted if exists.","Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        PurchaseRequisition pr = new PurchaseRequisition();
        if (option == JOptionPane.YES_OPTION) {
            //delete po
            for(PurchaseOrder po:PurchaseOrder.getPO()){
                if(po.getPOPR().equals(pr)){
                    po.delete(po.getPOID());
                }
            }
            //delete pr
            pr.delete(selectedID);
            DefaultTableModel model = (DefaultTableModel)pr_table.getModel();
            int selected_index = pr_table.getSelectedRow();
            model.removeRow(selected_index);
            selectedID = null;
            JOptionPane.showConfirmDialog(full_page, "Purchase Requisition has been deleted.","Successful", JOptionPane.PLAIN_MESSAGE);
            //refresh table
            while(model.getRowCount() > 0) {
                model.removeRow(0);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            for(PurchaseRequisition prs: PurchaseRequisition.getPR()){
                String formattedDate = sdf.format(prs.getRequiredDate());
                model.addRow(new Object[]{prs.getPRID(), prs.getPRItem().getItemID(), prs.getPRItem().getItemName(), prs.getRequiredQuantity(),formattedDate, prs.getPRSMID(), prs.getTotalPrice(), prs.getStatus()});
            }               
        }
    }//GEN-LAST:event_delete_pr_buttonActionPerformed

    private void profile_button_pmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profile_button_pmActionPerformed
        // TODO add your handling code here:
        user_id_profile.setText(currentUser.getID());
        username_profile.setText(currentUser.getUsername());
        role_profile.setText(currentUser.getRole().toString());
        selectedID = null;        
        cardLayout2.show(functionalities,"profile_page");
    }//GEN-LAST:event_profile_button_pmActionPerformed

    private void confirm_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirm_editActionPerformed
        // TODO add your handling code here:
        if(new_password.getText().isBlank() || new_password.getText().isEmpty() || confirm_password_edit.getText().isEmpty() || confirm_password_edit.getText().isBlank()){
            JOptionPane.showConfirmDialog(full_page, "Do not leave blank line.","Error", JOptionPane.PLAIN_MESSAGE);
            return;
        }        
        String old_pass = old_password_edit.getText().replaceAll("[^A-Za-z0-9.!@#$%^&*]","");
        String new_pass = new_password_edit.getText().replaceAll("[^A-Za-z0-9.!@#$%^&*]","");
        String confirm_pass = confirm_password_edit.getText().replaceAll("[^A-Za-z0-9.!@#$%^&*]","");
        
        if(currentUser.getPassword().equals(old_pass)){
            if(confirm_pass.equals(new_pass)){
                currentUser.setPassword(new_pass);
                JOptionPane.showConfirmDialog(full_page, "Your password has been changed.","Successful", JOptionPane.PLAIN_MESSAGE);
                currentUser.setPassword(currentUser.getPassword());
                //Serialization
                
            }
            else{
                JOptionPane.showConfirmDialog(full_page, "New password and confirm password mismatch.","Error", JOptionPane.PLAIN_MESSAGE);
            }
        }
        else{
            JOptionPane.showConfirmDialog(full_page, "Old password is incorrect.","Error", JOptionPane.PLAIN_MESSAGE);
        }      
    }//GEN-LAST:event_confirm_editActionPerformed

    private void search_pr_fieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_search_pr_fieldActionPerformed
        // TODO add your handling code here:
        //sort table
        DefaultTableModel model = (DefaultTableModel)pr_table.getModel();
        TableRowSorter<DefaultTableModel> trs = new TableRowSorter<>(model);
        pr_table.setRowSorter(trs);
        trs.setRowFilter(RowFilter.regexFilter(search_pr_field.getText()));    
    }//GEN-LAST:event_search_pr_fieldActionPerformed

    private void delete_sup_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delete_sup_buttonActionPerformed
        // TODO add your handling code here:
        if(selectedID == null){
            JOptionPane.showConfirmDialog(full_page, "No row selected.","Error", JOptionPane.PLAIN_MESSAGE);
            return;
        }
        
        int option = JOptionPane.showConfirmDialog(full_page, "Are you sure you want to delete?\nSupplier's item, purchase requisition and purchase order will also be deleted.","Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        Supplier sp = new Supplier().getByID(selectedID);
        if (option == JOptionPane.YES_OPTION) {
            //delete supplier's items, purchase requisition and purchase order if exists
            //delete item that match with supplier
            for(Item item:sp.getSuppliedItem()){
                //delete pr that match with item
                for(PurchaseRequisition pr:PurchaseRequisition.getPR()){
                    //delete po that match with pr
                    for(PurchaseOrder po:PurchaseOrder.getPO()){
                        po.delete(po.getPOID());
                    }
                    pr.delete(pr.getPRID());
                }
                //delete se
                for(SalesEntry se:SalesEntry.getSE()){
                    if(se.getItem().getItemID().equals(item.getItemID())){
                        se.delete(se.getSalesID());
                    }
                }
                item.delete(item.getItemID());
            }            
            //delete supplier
            sp.delete(selectedID);
            
            //Refresh table
            DefaultTableModel modelSupplier = (DefaultTableModel)supplier_table.getModel();
            int selected_index = supplier_table.getSelectedRow();
            modelSupplier.removeRow(selected_index);
            JOptionPane.showConfirmDialog(full_page, "Supplier has been deleted.","Successful", JOptionPane.PLAIN_MESSAGE);
            //clear supplied item table
            DefaultTableModel modelItem = (DefaultTableModel)supplied_item_table.getModel();
            while(modelItem.getRowCount() > 0) {
                modelItem.removeRow(0);
            }
        }        
    }//GEN-LAST:event_delete_sup_buttonActionPerformed

    private void item_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_item_tableMouseClicked
        // TODO add your handling code here:
        DefaultTableModel model= (DefaultTableModel)item_table.getModel();
        int selectedRowIndex = item_table.getSelectedRow();
        selectedID = model.getValueAt(selectedRowIndex,0).toString();
    }//GEN-LAST:event_item_tableMouseClicked

    private void delete_se_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delete_se_buttonActionPerformed
        // TODO add your handling code here:
        if(selectedID == null){
            JOptionPane.showConfirmDialog(full_page, "No row selected.","Error", JOptionPane.QUESTION_MESSAGE);
            return;
        }
        

        int option = JOptionPane.showConfirmDialog(full_page, "Are you sure you want to delete?.","Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (option == JOptionPane.YES_OPTION) {
            SalesEntry se = new SalesEntry().getByID(selectedID);
            se.delete(selectedID);
            int new_stock = se.getQuantitySold() + se.getItem().getStock();
            new Item().getByID(se.getItem().getItemID()).setStock(new_stock);
            DefaultTableModel model = (DefaultTableModel)se_table.getModel();
            int selected_index = se_table.getSelectedRow();
            model.removeRow(selected_index);
            JOptionPane.showConfirmDialog(full_page, "Sales Entry has been deleted.","Successful", JOptionPane.PLAIN_MESSAGE);
        }
    }//GEN-LAST:event_delete_se_buttonActionPerformed

    private void search_se_fieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_search_se_fieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_search_se_fieldActionPerformed

    private void search_se_fieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_search_se_fieldKeyPressed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel)se_table.getModel();
        TableRowSorter<DefaultTableModel> trs = new TableRowSorter<>(model);
        se_table.setRowSorter(trs);
        trs.setRowFilter(RowFilter.regexFilter(search_se_field.getText()));          
    }//GEN-LAST:event_search_se_fieldKeyPressed

    private void sold_quantity_disFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_sold_quantity_disFocusLost
        // TODO add your handling code here:
        Item item = new Item().getByID(item_id_dis.getText());
        // Check if the item is not null and if the sold_quantity_dis text is not empty.
        if(item != null && !sold_quantity_dis.getText().trim().isEmpty()){
            try {
                // Try parsing the number.
                int quantity = Integer.parseInt(sold_quantity_dis.getText());
                double total = item.getPrice() * quantity;
                total_price_dis.setText("RM" + String.format("%.2f", total));
            } catch (NumberFormatException e) {
                System.err.println("Invalid quantity entered: " + sold_quantity_dis.getText());
            }
        }
    }//GEN-LAST:event_sold_quantity_disFocusLost

    private void required_quantity_cprFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_required_quantity_cprFocusLost
        // TODO add your handling code here:
        Item item = new Item().getByID(item_id_cpr.getText());
        if(item != null){
            double total = item.getPrice() * Integer.parseInt(required_quantity_cpr.getText());
            total_price_cpr.setText("RM"+String.format("%.2f",total));       
        }
    }//GEN-LAST:event_required_quantity_cprFocusLost

    private void reject_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reject_buttonActionPerformed
        // TODO add your handling code here:
        if(selectedID == null){
            JOptionPane.showConfirmDialog(full_page, "No row selected.","Error", JOptionPane.PLAIN_MESSAGE);
            return;
        }
        
        PurchaseRequisition pr = new PurchaseRequisition().getByID(selectedID);
        //check status
        switch(pr.getStatus()){
            case "Expired":
            JOptionPane.showConfirmDialog(full_page, "You cannot reject expired purchase requisition.","Error", JOptionPane.PLAIN_MESSAGE);
            return;
            case "Rejected":
                JOptionPane.showConfirmDialog(full_page, "This purchase requisition was already rejected.","Error", JOptionPane.PLAIN_MESSAGE);
                return;
            case "Canceled":
                JOptionPane.showConfirmDialog(full_page, "You cannot reject canceled purchase requisition.","Error", JOptionPane.PLAIN_MESSAGE);
                return;
            case "Converted to PO":
                JOptionPane.showConfirmDialog(full_page, "This purchase requisition has been converted to purchase order.","Error", JOptionPane.PLAIN_MESSAGE);
        }

        int option = JOptionPane.showConfirmDialog(full_page, "Are you sure you want to reject this purchase requisition?","Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
        if(option == JOptionPane.YES_OPTION){
            pr.setStatus("Rejected");
            pr.save();
            selectedID = null;
        }
        //refresh pr table
        DefaultTableModel model = (DefaultTableModel)pr_table.getModel();
        // Clear existing rows
        while(model.getRowCount() > 0) {
            model.removeRow(0);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for(PurchaseRequisition prs: PurchaseRequisition.getPR()){
            String requiredDateFormatted = sdf.format(prs.getRequiredDate());
            model.addRow(new Object[]{prs.getPRID(), prs.getPRItem().getItemID(), prs.getPRItem().getItemName(), prs.getRequiredQuantity(), requiredDateFormatted, prs.getPRSMID(), prs.getTotalPrice(), prs.getStatus()});
        }   
    }//GEN-LAST:event_reject_buttonActionPerformed

    private void pr_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pr_tableMouseClicked
        // TODO add your handling code here:
        DefaultTableModel model= (DefaultTableModel)pr_table.getModel();
        int selectedRowIndex = pr_table.getSelectedRow();
        if(selectedRowIndex != -1){
            selectedID = model.getValueAt(selectedRowIndex,0).toString();
        }
    }//GEN-LAST:event_pr_tableMouseClicked

    private void po_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_po_tableMouseClicked
        // TODO add your handling code here:
        DefaultTableModel model= (DefaultTableModel)po_table.getModel();
        int selectedRowIndex = po_table.getSelectedRow();
        if(selectedRowIndex != -1){
            selectedID = model.getValueAt(selectedRowIndex,0).toString();
        }
    }//GEN-LAST:event_po_tableMouseClicked

    private void item_nameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_item_nameKeyPressed
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        
        if(Character.isLetter(c) || Character.isWhitespace(c) || Character.isISOControl(c) || Character.isDigit(c)){
            //iso control = delete key / backspace key
            item_name.setEditable(true);
        }
        else{
            item_name.setEditable(false);
        }
    }//GEN-LAST:event_item_nameKeyPressed

    private void supplier_name_suppKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_supplier_name_suppKeyPressed
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        
        if(Character.isLetter(c) || Character.isWhitespace(c) || Character.isISOControl(c) || Character.isDigit(c)){
            //iso control = delete key / backspace key
            supplier_name_supp.setEditable(true);
        }
        else{
            supplier_name_supp.setEditable(false);
        }
    }//GEN-LAST:event_supplier_name_suppKeyPressed

    private void new_usernameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_new_usernameKeyPressed
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        
        if(Character.isLetter(c) || Character.isWhitespace(c) || Character.isISOControl(c) || Character.isDigit(c)){
            //iso control = delete key / backspace key
            new_username.setEditable(true);
        }
        else{
            new_username.setEditable(false);
        }
    }//GEN-LAST:event_new_usernameKeyPressed

    private void username_profileFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_username_profileFocusLost
        // TODO add your handling code here:
        if(username_profile.getText().isBlank() || username_profile.getText().isEmpty()){
            JOptionPane.showConfirmDialog(full_page, "Do not leave blank line.","Error", JOptionPane.PLAIN_MESSAGE);
            return;
        }
        if(!username_profile.getText().equals(currentUser.getUsername())){
            int option = JOptionPane.showConfirmDialog(full_page, "Save changes?","Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(option == JOptionPane.YES_OPTION){
                String newUsername = username_profile.getText().replaceAll("[^A-Za-z0-9]", "");
                User user = new User().getByID(currentUser.getID());
                user.setUername(newUsername);
                currentUser = user;
                username_profile.setText(currentUser.getUsername());
            }
        }
    }//GEN-LAST:event_username_profileFocusLost

    private void item_id_cprKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_item_id_cprKeyPressed
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        
        if(Character.isLetter(c) || Character.isWhitespace(c) || Character.isISOControl(c) || Character.isDigit(c)){
            //iso control = delete key / backspace key
            item_id_cpr.setEditable(true);
        }
        else{
            item_id_cpr.setEditable(false);
        }        
    }//GEN-LAST:event_item_id_cprKeyPressed

    private void item_id_disKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_item_id_disKeyPressed
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        
        if(Character.isLetter(c) || Character.isWhitespace(c) || Character.isISOControl(c) || Character.isDigit(c)){
            //iso control = delete key / backspace key
            item_id_dis.setEditable(true);
        }
        else{
            item_id_dis.setEditable(false);
        }
    }//GEN-LAST:event_item_id_disKeyPressed

    private void po_id_fieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_po_id_fieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_po_id_fieldActionPerformed

    private void po_pr_fieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_po_pr_fieldKeyPressed
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        
        if(Character.isLetter(c) || Character.isWhitespace(c) || Character.isISOControl(c) || Character.isDigit(c)){
            //iso control = delete key / backspace key
            po_pr_field.setEditable(true);
        }
        else{
            po_pr_field.setEditable(false);
        }      
    }//GEN-LAST:event_po_pr_fieldKeyPressed

    private void add_po_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_add_po_buttonActionPerformed
        // TODO add your handling code here:
        PurchaseRequisition pr = new PurchaseRequisition().getByID(po_pr_field.getText());
        for(PurchaseOrder po:PurchaseOrder.getPO()){
            if(po.getPOPR().getPRID().equals(pr.getPRID())){
                JOptionPane.showConfirmDialog(full_page, "Purchase Order already exists.","Error", JOptionPane.PLAIN_MESSAGE);
                return;
            }
        }
        if(pr != null){
            PurchaseOrder newPo = new PurchaseOrder(pr,currentUser.getID());
            newPo.add(newPo);
            JOptionPane.showConfirmDialog(full_page, "Purchase order has been added successfully.","Successful", JOptionPane.PLAIN_MESSAGE);
        }
        else{
            JOptionPane.showConfirmDialog(full_page, "Purchase Requisition does not exists.","Error", JOptionPane.PLAIN_MESSAGE);
        }
        po_pr_field.setText("");
        po_item_field.setText("");
        po_qtt_field.setText("");
        po_date_field.setText("");
        po_amount_field.setText("");
    }//GEN-LAST:event_add_po_buttonActionPerformed

    private void list_po_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_list_po_buttonActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel)po_table.getModel();
        // Clear existing rows
        while(model.getRowCount() > 0) {
            model.removeRow(0);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for(PurchaseOrder po: PurchaseOrder.getPO()){
            String dateFormatted = sdf.format(po.getPOPR().getRequiredDate());
            model.addRow(new Object[]{po.getPOID(), po.getPOPR().getPRID(),po.getPMID(),po.getPOPR().getPRItem().getItemID(),dateFormatted,po.getPOPR().getRequiredQuantity(),po.getPOPR().getTotalPrice()});
        }
        selectedID = null;
        cardLayout2.show(functionalities,"view_po_panel");
        
    }//GEN-LAST:event_list_po_buttonActionPerformed

    private void po_qtt_fieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_po_qtt_fieldFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_po_qtt_fieldFocusLost

    private void po_pr_fieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_po_pr_fieldFocusLost
        // TODO add your handling code here:
        PurchaseRequisition pr = new PurchaseRequisition().getByID(po_pr_field.getText());
        if(pr != null){
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String reqDateFormat = sdf.format(pr.getRequiredDate());
            po_item_field.setText(pr.getPRItem().getItemName());
            po_qtt_field.setText(String.valueOf(pr.getRequiredQuantity()));
            po_date_field.setText(reqDateFormat);
            po_amount_field.setText(String.valueOf(pr.getTotalPrice()));
        }
    }//GEN-LAST:event_po_pr_fieldFocusLost

    private void view_po_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_view_po_buttonActionPerformed
        // TODO add your handling code here:
        selectedID = null;
        po_id_field.setText(new PurchaseOrder().previewNextID());
        cardLayout2.show(functionalities,"generate_po_panel");
    }//GEN-LAST:event_view_po_buttonActionPerformed

    private void view_purchase_order_admActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_view_purchase_order_admActionPerformed
        // TODO add your handling code here:
        selectedID = null;
        po_id_field.setText(new PurchaseOrder().previewNextID());
        cardLayout2.show(functionalities,"generate_po_panel");
    }//GEN-LAST:event_view_purchase_order_admActionPerformed

    private void se_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_se_tableMouseClicked
        // TODO add your handling code here:
        DefaultTableModel model= (DefaultTableModel)se_table.getModel();
        int selectedRowIndex = se_table.getSelectedRow();
        if(selectedRowIndex != -1){
            selectedID = model.getValueAt(selectedRowIndex,0).toString();
        }        
    }//GEN-LAST:event_se_tableMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        //initial data
        new User().load();
        new Supplier().load();
        new SalesEntry().load();
        new PurchaseRequisition().load();
        new Item().load();
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton add_item;
    private javax.swing.JButton add_item_cpr;
    private javax.swing.JButton add_item_dis;
    private javax.swing.JButton add_item_supp;
    private javax.swing.JButton add_po_button;
    private javax.swing.JButton add_user_button;
    private javax.swing.JPanel administrator_options;
    private javax.swing.JButton change_password;
    private javax.swing.JLabel comfirm_password_label_edit;
    private javax.swing.JLabel company_title;
    private javax.swing.JButton confirm_edit;
    private javax.swing.JPasswordField confirm_password;
    private javax.swing.JPasswordField confirm_password_edit;
    private javax.swing.JLabel confirm_password_label;
    private javax.swing.JButton create_purchase_requisition;
    private javax.swing.JButton create_purchase_requisition_adm;
    private javax.swing.JPanel create_purchase_requisition_panel;
    private javax.swing.JLabel create_purchase_requisition_title;
    private javax.swing.JLabel create_purchase_requisition_title1;
    private javax.swing.JButton daily_itemwise_sales_entry;
    private javax.swing.JButton daily_itemwise_sales_entry_adm;
    private javax.swing.JPanel daily_itemwise_sales_panel;
    private javax.swing.JLabel daily_itemwise_sales_title;
    private javax.swing.JButton delete_item_button;
    private javax.swing.JButton delete_po_button;
    private javax.swing.JButton delete_pr_button;
    private javax.swing.JButton delete_se_button;
    private javax.swing.JButton delete_sup_button;
    private javax.swing.JButton delete_user_button;
    private javax.swing.JPanel edit_profile_page;
    private javax.swing.JLabel edit_profile_page_title;
    private javax.swing.JButton exit_purchase_mng;
    private javax.swing.JButton exit_sales_mng;
    private javax.swing.JSplitPane full_page;
    private javax.swing.JPanel functionalities;
    private javax.swing.JButton generate_po_button;
    private javax.swing.JPanel generate_po_panel;
    private javax.swing.JButton item_entry;
    private javax.swing.JButton item_entry_adm;
    private javax.swing.JLabel item_entry_page_title;
    private javax.swing.JPanel item_entry_panel;
    private javax.swing.JTextField item_id;
    private javax.swing.JTextField item_id_cpr;
    private javax.swing.JTextField item_id_dis;
    private javax.swing.JLabel item_id_label;
    private javax.swing.JLabel item_id_label_cpr;
    private javax.swing.JLabel item_id_label_cpr1;
    private javax.swing.JLabel item_id_label_cpr2;
    private javax.swing.JLabel item_id_label_dis;
    private javax.swing.JTextField item_name;
    private javax.swing.JLabel item_name_label;
    private javax.swing.JFormattedTextField item_price;
    private javax.swing.JLabel item_price_label;
    private javax.swing.JFormattedTextField item_stock;
    private javax.swing.JTextField item_supplier_id;
    private javax.swing.JTable item_table;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JButton list_daily_sales_dis;
    private javax.swing.JButton list_item;
    private javax.swing.JPanel list_item_panel;
    private javax.swing.JLabel list_item_title;
    private javax.swing.JButton list_po_button;
    private javax.swing.JButton list_pr_button;
    private javax.swing.JPanel list_supplier_panel;
    private javax.swing.JButton list_supplier_supp;
    private javax.swing.JButton list_user_button;
    private javax.swing.JPanel list_user_panel;
    private javax.swing.JButton login_button;
    private javax.swing.JPanel login_page;
    private javax.swing.JButton logout_button_adm;
    private javax.swing.JPanel menuOptions;
    private javax.swing.JPasswordField new_password;
    private javax.swing.JPasswordField new_password_edit;
    private javax.swing.JLabel new_password_label;
    private javax.swing.JLabel new_password_label_edit;
    private javax.swing.JTextField new_username;
    private javax.swing.JLabel new_username_label;
    private javax.swing.JTextField old_password_edit;
    private javax.swing.JLabel old_password_label_edit;
    private javax.swing.JPasswordField password_field;
    private javax.swing.JLabel password_label;
    private javax.swing.JTextPane po_amount_field;
    private javax.swing.JFormattedTextField po_date_field;
    private javax.swing.JTextField po_id_field;
    private javax.swing.JTextField po_item_field;
    private javax.swing.JTextField po_pr_field;
    private javax.swing.JFormattedTextField po_qtt_field;
    private javax.swing.JTable po_table;
    private javax.swing.JTextField pr_id_cpr;
    private javax.swing.JTable pr_table;
    private javax.swing.JButton profile_button;
    private javax.swing.JButton profile_button_adm;
    private javax.swing.JButton profile_button_pm;
    private javax.swing.JPanel profile_page;
    private javax.swing.JLabel profile_page_title;
    private javax.swing.JPanel purchase_manager_options;
    private javax.swing.JLabel purchase_requisition_id_label_cpr;
    private javax.swing.JLabel purchase_requisition_id_label_cpr1;
    private javax.swing.JButton register_user_panel_button;
    private javax.swing.JPanel registration;
    private javax.swing.JLabel registration_title;
    private javax.swing.JButton reject_button;
    private javax.swing.JFormattedTextField required_date_cpr;
    private javax.swing.JLabel required_date_label_cpr;
    private javax.swing.JLabel required_date_label_cpr1;
    private javax.swing.JFormattedTextField required_quantity_cpr;
    private javax.swing.JLabel required_quantity_label_cpr;
    private javax.swing.JLabel required_quantity_label_cpr1;
    private javax.swing.JComboBox<String> role_cBox;
    private javax.swing.JLabel role_label;
    private javax.swing.JLabel role_label_profile;
    private javax.swing.JTextField role_profile;
    private javax.swing.JFormattedTextField sales_date_dis;
    private javax.swing.JLabel sales_date_label;
    private javax.swing.JTextField sales_entry_id_dis;
    private javax.swing.JLabel sales_entry_id_label_dis;
    private javax.swing.JPanel sales_manager_options;
    private javax.swing.JTable se_table;
    private javax.swing.JTextField search_item_field;
    private javax.swing.JLabel search_item_label;
    private javax.swing.JTextField search_pr_field;
    private javax.swing.JLabel search_purchase_order_label_vpo;
    private javax.swing.JLabel search_purchase_order_label_vpo1;
    private javax.swing.JTextField search_purchase_order_vpo;
    private javax.swing.JLabel search_purchase_requisition_label;
    private javax.swing.JTextField search_se_field;
    private javax.swing.JTextField search_supplier_field;
    private javax.swing.JLabel search_supplier_label;
    private javax.swing.JTextField search_user_field;
    private javax.swing.JLabel search_user_label;
    private javax.swing.JFormattedTextField sold_quantity_dis;
    private javax.swing.JLabel sold_quantity_label_dis;
    private javax.swing.JLabel stock_label;
    private javax.swing.JLabel supp_management_page_title;
    private javax.swing.JTable supplied_item_table;
    private javax.swing.JButton supplier_entry;
    private javax.swing.JButton supplier_entry_adm;
    private javax.swing.JLabel supplier_id_label;
    private javax.swing.JLabel supplier_id_label_supp;
    private javax.swing.JTextField supplier_id_supp;
    private javax.swing.JPanel supplier_management_panel;
    private javax.swing.JLabel supplier_name_label_supp;
    private javax.swing.JTextField supplier_name_supp;
    private javax.swing.JTable supplier_table;
    private javax.swing.JLabel system_title;
    private javax.swing.JTextPane total_price_cpr;
    private javax.swing.JTextField total_price_dis;
    private javax.swing.JLabel total_price_label_cpr;
    private javax.swing.JLabel total_price_label_cpr1;
    private javax.swing.JLabel total_price_label_dis;
    private javax.swing.JLabel user_id_label_profile;
    private javax.swing.JTextField user_id_profile;
    private javax.swing.JTable user_table;
    private javax.swing.JTextField username_field;
    private javax.swing.JLabel username_label;
    private javax.swing.JLabel username_label_profile;
    private javax.swing.JTextField username_profile;
    private javax.swing.JButton view_items;
    private javax.swing.JButton view_po_button;
    private javax.swing.JPanel view_po_panel;
    private javax.swing.JPanel view_pr_panel;
    private javax.swing.JButton view_purchase_order;
    private javax.swing.JButton view_purchase_order_adm;
    private javax.swing.JLabel view_purchase_order_title;
    private javax.swing.JLabel view_purchase_order_title1;
    private javax.swing.JButton view_purchase_req_purchases;
    private javax.swing.JLabel view_purchase_requisition_title;
    private javax.swing.JPanel view_se_panel;
    private javax.swing.JScrollPane view_supplier_sPane;
    private javax.swing.JScrollPane view_supplier_sPane1;
    private javax.swing.JLabel view_supplier_title;
    private javax.swing.JButton view_suppliers;
    private javax.swing.JScrollPane view_user_sPane;
    private javax.swing.JLabel view_user_title;
    // End of variables declaration//GEN-END:variables
}
