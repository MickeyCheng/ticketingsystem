
package forms;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
import java.sql.DriverManager;
import java.sql.SQLException;
import net.proteanit.sql.DbUtils;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
public class frmUserMaster extends javax.swing.JFrame {
ResultSet rs;
PreparedStatement pstmt;
Connection conn;
boolean add,edit;

    public frmUserMaster() {
        initComponents();
        doConnect();
        fillCombo();
        clearTexts();
        disableTexts();
        fillTable();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(frmUserMaster.DISPOSE_ON_CLOSE);
    }
    private void enableTexts(){
        txtPassword.setEnabled(true);
        txtUserID.setEnabled(true);
        txtUserName.setEnabled(true);
        txtEmail.setEnabled(true);
    }
    private void disableTexts(){
        txtPassword.setEnabled(false);
        txtUserID.setEnabled(false);
        txtUserName.setEnabled(false);
        txtEmail.setEnabled(false);
    }
    private void clearTexts(){
        txtPassword.setText("");
        txtUserID.setText("");
        txtUserName.setText("");
        txtEmail.setText("");
    }
    private void fillTable(){
        try{
            pstmt = conn.prepareStatement("Select uc_id,uc_username,uc_category from tblUserControl order by uc_username");
            rs = pstmt.executeQuery();
            tblUserMaster.setModel(DbUtils.resultSetToTableModel(rs));
            pstmt.close();
            sortTable();
        }catch (SQLException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    private void sortTable(){
        tblUserMaster.getColumnModel().getColumn(0).setHeaderValue("USER ID");
        tblUserMaster.getColumnModel().getColumn(1).setHeaderValue("USER NAME");
        tblUserMaster.getColumnModel().getColumn(2).setHeaderValue("CATEGORY");
    }
    private void fillCombo(){
        cmbCategory.removeAllItems();
        cmbDepartment.removeAllItems();
        try{
            pstmt = conn.prepareStatement("SELECT * from tblStaffPosition order by sp_name");
            rs = pstmt.executeQuery();
            while (rs.next()){
                cmbCategory.addItem(rs.getString(2));
            }
            pstmt = conn.prepareStatement("SELECT * from tblDepartment order by de_name");
            rs = pstmt.executeQuery();
            while (rs.next()){
                cmbDepartment.addItem(rs.getString(2));
            }
            pstmt.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        
//        cmbCategory.addItem("Senior Software Developer");
//        cmbCategory.addItem("Software Developer");
//        cmbCategory.addItem("Senior Support Engineer");
//        cmbCategory.addItem("Technical Support Team Leader");
//        cmbCategory.addItem("Junior Support");
//        cmbCategory.addItem("Digital Marketing Specialist");
//        cmbCategory.addItem("Project Manager");
//        cmbCategory.addItem("Products and Services Manager");
//        cmbCategory.addItem("Other");
    }
    private void doConnect(){
    try{
        Class.forName("com.mysql.jdbc.Driver");
//        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbticketing","root","root");
//        conn = DriverManager.getConnection("jdbc:mysql://192.168.1.13/dbticketing","iSoft","iSoft123");
        conn = DriverManager.getConnection("jdbc:mysql://192.168.100.2/dbticketing","iSoft","iSoft123");
    }catch(SQLException | ClassNotFoundException e){
        JOptionPane.showMessageDialog(this, e.getMessage());
    }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblUserMaster = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtUserName = new javax.swing.JTextField();
        txtUserID = new javax.swing.JTextField();
        cmbCategory = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        cmdEdit = new javax.swing.JButton();
        cmdAdd = new javax.swing.JButton();
        cmdDelete = new javax.swing.JButton();
        cmdSave = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        txtPassword = new javax.swing.JPasswordField();
        chkAdmin = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        cmbDepartment = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(153, 153, 153));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblUserMaster.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblUserMaster.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblUserMasterMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblUserMaster);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 810, 160));

        jPanel2.setBackground(new java.awt.Color(153, 153, 153));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("USER ID:");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 160, 40));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("USER NAME:");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 160, 40));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("CATEGORY:");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, 160, 40));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("PASSWORD:");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 160, 40));

        txtUserName.setNextFocusableComponent(txtUserID);
        jPanel2.add(txtUserName, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 20, 290, 40));

        txtUserID.setNextFocusableComponent(txtPassword);
        jPanel2.add(txtUserID, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 80, 290, 40));

        jPanel2.add(cmbCategory, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 200, 290, 40));

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cmdEdit.setText("EDIT");
        cmdEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdEditActionPerformed(evt);
            }
        });
        jPanel3.add(cmdEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, 120, 40));

        cmdAdd.setText("ADD");
        cmdAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAddActionPerformed(evt);
            }
        });
        jPanel3.add(cmdAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(19, 11, 120, 40));

        cmdDelete.setText("DELETE");
        cmdDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdDeleteActionPerformed(evt);
            }
        });
        jPanel3.add(cmdDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 120, 40));

        cmdSave.setText("SAVE");
        cmdSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSaveActionPerformed(evt);
            }
        });
        jPanel3.add(cmdSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 60, 120, 40));

        jButton5.setText("CLOSE");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 250, 40));

        jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 20, 280, 220));

        txtPassword.setNextFocusableComponent(cmbCategory);
        jPanel2.add(txtPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 140, 290, 40));

        chkAdmin.setText("ADMIN");
        jPanel2.add(chkAdmin, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 310, -1, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("EMAIL:");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 300, 160, 40));

        jPanel2.add(cmbDepartment, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 250, 290, 40));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("DEPARTMENT:");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 160, 40));

        txtEmail.setNextFocusableComponent(txtPassword);
        jPanel2.add(txtEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 300, 290, 40));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 810, 350));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 830, 550));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblUserMasterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblUserMasterMouseClicked
        int row = tblUserMaster.getSelectedRow();
        int ba = tblUserMaster.convertRowIndexToModel(row);
        String tblClick = tblUserMaster.getModel().getValueAt(ba,0).toString();
        String fetchData ="Select * from tblUserControl where uc_id=?";
        try{
            pstmt = conn.prepareStatement(fetchData);
            pstmt.setString(1,tblClick);
            rs = pstmt.executeQuery();
            if (rs.next()){
                txtPassword.setText(rs.getString("uc_password"));
                txtUserID.setText(rs.getString("uc_id"));
                txtUserName.setText(rs.getString("uc_username"));
                cmbCategory.setSelectedItem(rs.getString("uc_category"));
                String isAdmin = rs.getString("uc_admin");
                cmbDepartment.setSelectedItem((rs.getString("uc_department")));
                if (isAdmin.equals("Y")){
                    chkAdmin.setSelected(true);
                }else if(isAdmin.equals("N")){
                    chkAdmin.setSelected(false);
                }
                txtEmail.setText(rs.getString("uc_email"));
                pstmt.close();
            }
        }catch(SQLException e){
            e.getMessage();
        }
    }//GEN-LAST:event_tblUserMasterMouseClicked

    private void cmdEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdEditActionPerformed
        add = false;
        edit = true;
        enableTexts();
        txtUserName.requestFocus();
    }//GEN-LAST:event_cmdEditActionPerformed

    private void cmdAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAddActionPerformed
        add = true;
        edit = false;
        clearTexts();
        enableTexts();
        chkAdmin.setVisible(true);
        txtUserName.requestFocus();
    }//GEN-LAST:event_cmdAddActionPerformed

    private void cmdSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSaveActionPerformed
        if (add==true && edit==false){
            try{
                String insertQuery ="INSERT INTO tblUserControl(uc_id,uc_username,uc_password,uc_category,uc_admin,uc_department,uc_email)"
                + " values(?,?,?,?,?,?,?)";
                pstmt = conn.prepareStatement(insertQuery);
                pstmt.setString(1,txtUserID.getText());
                pstmt.setString(2,txtUserName.getText());
                pstmt.setString(3, String.valueOf(txtPassword.getPassword()));
                pstmt.setString(4,cmbCategory.getSelectedItem().toString());
                String isAdmin;
                if (chkAdmin.isSelected()){
                    isAdmin = "Y";
                }else{
                    isAdmin = "N";
                }
                pstmt.setString(5,isAdmin);
                pstmt.setString(6,cmbDepartment.getSelectedItem().toString());
                pstmt.setString(7,txtEmail.getText());
                pstmt.execute();
                JOptionPane.showMessageDialog(this, "USER CREDENTIALS SAVED");
                pstmt.close();
                fillTable();
                disableTexts();
                clearTexts();
                chkAdmin.setSelected(false);
            }catch(SQLException e){
                JOptionPane.showMessageDialog(this,e.getMessage());
            }
        }else if(add==false && edit== true){
            try{
                String editQuery ="UPDATE tblUserControl set uc_id=?,uc_username=?,uc_password=?,"
                        + "uc_category=?,uc_admin=?,uc_department=?, uc_email=? where uc_id=?";
                pstmt = conn.prepareStatement(editQuery);
                pstmt.setString(1,txtUserID.getText());
                pstmt.setString(2,txtUserName.getText());
                pstmt.setString(3,String.valueOf(txtPassword.getPassword()));
                pstmt.setString(4,cmbCategory.getSelectedItem().toString());String isAdmin;
                if (chkAdmin.isSelected()){
                    isAdmin = "Y";
                }else{
                    isAdmin = "N";
                }
                pstmt.setString(5,isAdmin);
                pstmt.setString(6,cmbDepartment.getSelectedItem().toString());
                pstmt.setString(7,txtEmail.getText());
                pstmt.setString(8,txtUserID.getText());
                
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "USER CREDENTIALS UPDATED");
                pstmt.close();
                fillTable();
                disableTexts();
                clearTexts();
                chkAdmin.setSelected(false);
            }catch(SQLException e){
                JOptionPane.showMessageDialog(this,e.getMessage());
            }
            
        }
    }//GEN-LAST:event_cmdSaveActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void cmdDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDeleteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmdDeleteActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(frmUserMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmUserMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmUserMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmUserMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmUserMaster().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chkAdmin;
    private javax.swing.JComboBox<String> cmbCategory;
    private javax.swing.JComboBox<String> cmbDepartment;
    private javax.swing.JButton cmdAdd;
    private javax.swing.JButton cmdDelete;
    private javax.swing.JButton cmdEdit;
    private javax.swing.JButton cmdSave;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblUserMaster;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUserID;
    private javax.swing.JTextField txtUserName;
    // End of variables declaration//GEN-END:variables
}
