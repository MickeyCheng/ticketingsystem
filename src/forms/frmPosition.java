
package forms;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
import java.sql.DriverManager;
import java.sql.SQLException;
import net.proteanit.sql.DbUtils;
public class frmPosition extends javax.swing.JFrame {
ResultSet rs;
PreparedStatement pstmt;
Connection conn;
boolean add,edit;
int getMaxID,getSelectedID;
    public frmPosition() {
        initComponents();
        doConnect();
        fillTable();
        setDefaultCloseOperation(frmPosition.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    private void fillTable(){
        try{
            pstmt = conn.prepareStatement("SELECT * from tblStaffPosition order by sp_id");
            rs = pstmt.executeQuery();
            tblPosition.setModel(DbUtils.resultSetToTableModel(rs));
            pstmt.close();
            tblPosition.getColumnModel().getColumn(0).setHeaderValue("ID");
            tblPosition.getColumnModel().getColumn(1).setHeaderValue("POSITION");
        }catch(SQLException e){
            e.getMessage();
        }
    }
    private void getNextID(){
        try{
            pstmt = conn.prepareStatement("SELECT * from tblStaffPosition order by sp_id DESC LIMIT 1");
            rs = pstmt.executeQuery();
            if (rs.next()){
                getMaxID = rs.getInt(1);
                getMaxID++;
            }else{
                getMaxID=1;
            }           
            pstmt.close();
        }catch(SQLException e){
            e.getMessage();
        }
    
    }
    public void doConnect(){
    try{
        Class.forName("com.mysql.jdbc.Driver");
//        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbticketing","root","root");
//        conn = DriverManager.getConnection("jdbc:mysql://192.168.1.13/dbticketing","iSoft","iSoft123");
        conn = DriverManager.getConnection("jdbc:mysql://192.168.100.2/dbticketing","iSoft","iSoft123");
    }catch(SQLException | ClassNotFoundException e){
        JOptionPane.showMessageDialog(this, e.getMessage());
    }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtPosition = new javax.swing.JTextField();
        btnEdit = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPosition = new javax.swing.JTable();
        btnClose = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnClose1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("POSITION:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 20, -1, 20));
        jPanel1.add(txtPosition, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 20, 210, -1));

        btnEdit.setText("EDIT");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        jPanel1.add(btnEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 50, 80, -1));

        btnSave.setText("SAVE");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        jPanel1.add(btnSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, 70, 50));

        tblPosition.setModel(new javax.swing.table.DefaultTableModel(
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
        tblPosition.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPositionMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblPosition);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 370, 180));

        btnClose.setText("CANCEL");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        jPanel1.add(btnClose, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 80, 80, -1));

        btnAdd.setText("ADD");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        jPanel1.add(btnAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 50, 80, -1));

        btnClose1.setText("CLOSE");
        jPanel1.add(btnClose1, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 80, 80, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        if (txtPosition.getText().equals("")){
            JOptionPane.showMessageDialog(this, "PLEASE TYPE IN THE TEXT FIELD","SAVE",JOptionPane.ERROR_MESSAGE);
        }else{
            if (add == true && edit == false){
                getNextID();
                try{
                    pstmt = conn.prepareStatement("INSERT INTO tblStaffPosition (sp_id,sp_name) values (?,?)");
                    pstmt.setInt(1,getMaxID);
                    pstmt.setString(2,txtPosition.getText());
                    pstmt.execute();
                    pstmt.close();
                    JOptionPane.showMessageDialog(this, "RECORD SAVED");
                    fillTable();
                    txtPosition.setText("");
                    btnAdd.setEnabled(true);
                    btnEdit.setEnabled(true);
                }catch(SQLException e){
                    JOptionPane.showMessageDialog(this, e.getMessage());
                }
            }if (edit == true && add == false){
                try{
                    pstmt = conn.prepareStatement("UPDATE tblStaffPosition set sp_name=? where sp_id=?");
                    pstmt.setString(1,txtPosition.getText());
                    pstmt.setInt(2,getSelectedID);
                    pstmt.executeUpdate();
                    pstmt.close();
                    JOptionPane.showMessageDialog(this, "RECORD UPDATED");
                    fillTable();
                    txtPosition.setText("");
                    btnAdd.setEnabled(true);
                    btnEdit.setEnabled(true);
                }catch(SQLException e){
                    JOptionPane.showMessageDialog(this, e.getMessage());
                }
            }
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        add = true;
        edit = false;
        btnAdd.setEnabled(false);
        btnEdit.setEnabled(false);
        txtPosition.setEnabled(true);
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        add = false;
        edit = false;
        btnAdd.setEnabled(true);
        btnEdit.setEnabled(true);
        txtPosition.setText("");
        txtPosition.setEnabled(false);
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        add = false;
        edit = true;
        btnAdd.setEnabled(false);
        btnEdit.setEnabled(false);
        txtPosition.setEnabled(true);
    }//GEN-LAST:event_btnEditActionPerformed

    private void tblPositionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPositionMouseClicked
        int row = tblPosition.getSelectedRow();
        int ba = tblPosition.convertRowIndexToModel(row);
        String tblClick = tblPosition.getModel().getValueAt(ba, 0).toString();
        getSelectedID = Integer.valueOf(tblClick);
        txtPosition.setText(tblPosition.getModel().getValueAt(ba, 1).toString());
    }//GEN-LAST:event_tblPositionMouseClicked

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
            java.util.logging.Logger.getLogger(frmPosition.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmPosition.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmPosition.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmPosition.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmPosition().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnClose1;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnSave;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblPosition;
    private javax.swing.JTextField txtPosition;
    // End of variables declaration//GEN-END:variables
}