
package forms;
import java.awt.Image;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
public class frmDashboard extends javax.swing.JFrame {
ResultSet rs;
PreparedStatement pstmt;
Connection conn;
boolean add,edit;
SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
LocalDate today = LocalDate.now();
public static String showQuery;
int minutes =1;
Timer timer = new Timer();

   public frmDashboard() {
        initComponents();
        doConnect();
        fillPendingTickets();
        fillResolvedTickets();
        fillAllPending();
        fillUnassignedTickets();
        fillClosedTickets();
        fillUnassignedReq();
        fillPendingReq();
        fillResolvedReq();
        fillClosedReq();
        fillOverallPendingTix();
        fillOverallPendingReq();
        fillOverallResolved();
        fillOverallClosed();
        getDate();  
        setLocationRelativeTo(null);
        lblUserName.setText(frmLogin.username);
        lblPosition.setText(frmLogin.accessLevel);
        lblLogo.setIcon(new ImageIcon(new ImageIcon("src\\isoft logo.jpg").getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH)));
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try{
                    rs.close();
                    conn.close();
                    pstmt.close();
                }catch(SQLException e){
                    e.getMessage();
                }
                doConnect();
                fillPendingTickets();
                fillResolvedTickets();
                fillUnassignedTickets();
                fillUnassignedReq();
                fillPendingReq();
                fillResolvedReq();
                fillClosedReq();
                fillOverallPendingTix();
                fillOverallPendingReq();
                fillOverallResolved();
                fillOverallClosed();
                getDate();  
            }
        }, 0,1000*60 * minutes);
        if (frmLogin.isAdmin.equals("Y")){
            panelAdmin.setVisible(true);
        }else{
            
            panelAdmin.setVisible(false);
        }
    }
    private void fillOverallClosed(){
        try{
            pstmt = conn.prepareStatement("Select * from tblTicket where ti_status=? and ti_assigned=?");
            pstmt.setString(1,"ClOSED");
            pstmt.setString(2,frmLogin.username);
            rs = pstmt.executeQuery();
            int getCount =0;
            while (rs.next()){
                getCount++;
            }
            btnOverallClosed.setText(String.valueOf(getCount));
            pstmt.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    private void fillOverallResolved(){
        try{
            pstmt = conn.prepareStatement("Select * from tblTicket where ti_status=? and ti_assigned=?");
            pstmt.setString(1,"RESOLVED");
            pstmt.setString(2,frmLogin.username);
            rs = pstmt.executeQuery();
            int getCount =0;
            while (rs.next()){
                getCount++;
            }
            btnOverallResolved.setText(String.valueOf(getCount));
            pstmt.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    private void fillOverallPendingReq(){
        try{
            pstmt = conn.prepareStatement("Select * from tblTicket where ti_status=? and ti_assigned<> ?"
                    + "and ti_type=? and ti_assigned=?");
            pstmt.setString(1,"PENDING");
            pstmt.setString(2,"UNASSIGNED");
            pstmt.setString(3,"CHANGE REQUEST");
            pstmt.setString(4,frmLogin.username);
            rs = pstmt.executeQuery();
            int getCount =0;
            while (rs.next()){
                getCount++;
            }
            btnOverallPendingReq.setText(String.valueOf(getCount));
            pstmt.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    private void fillOverallPendingTix(){
        try{
            pstmt = conn.prepareStatement("Select * from tblTicket where ti_status=? and ti_assigned<> ?"
                    + "and ti_type=? and ti_assigned=?");
            pstmt.setString(1,"PENDING");
            pstmt.setString(2,"UNASSIGNED");
            pstmt.setString(3,"ISSUE - TICKET");
            pstmt.setString(4,frmLogin.username);
            rs = pstmt.executeQuery();
            int getCount =0;
            while (rs.next()){
                getCount++;
            }
            btnOverallPendingTix.setText(String.valueOf(getCount));
            pstmt.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    private void fillAllPending(){
        try{
            pstmt = conn.prepareStatement("Select * from tblTicket where ti_status=? and ti_assigned<> ?"
                    + "and ti_type=? and ti_dateRaised between ? and ?");
            pstmt.setString(1,"PENDING");
            pstmt.setString(2,"UNASSIGNED");
            pstmt.setString(3,"ISSUE - TICKET");
            pstmt.setString(4, String.valueOf(today.withDayOfMonth(1)));
            pstmt.setString(5, String.valueOf(today.withDayOfMonth(today.lengthOfMonth())));
            rs = pstmt.executeQuery();
            int getCount=0;
            while (rs.next()){
                getCount++;
            }
            btnAllPendingTix.setText(String.valueOf(getCount));
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        } 
        
        try{
            pstmt = conn.prepareStatement("Select * from tblTicket where ti_status=? and ti_assigned<> ?"
                    + "and ti_type=? and ti_dateRaised between ? and ?");
            pstmt.setString(1,"PENDING");
            pstmt.setString(2,"UNASSIGNED");
            pstmt.setString(3,"CHANGE REQUEST");
            pstmt.setString(4, String.valueOf(today.withDayOfMonth(1)));
            pstmt.setString(5, String.valueOf(today.withDayOfMonth(today.lengthOfMonth())));
            rs = pstmt.executeQuery();
            int getCount=0;
            while (rs.next()){
                getCount++;
            }
            btnAllPendingReq.setText(String.valueOf(getCount));
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }   
    }
    private void fillClosedReq(){
        try{
                String getUnassignTix = "select * from tblTicket where ti_status=? "
                        + " and ti_type=? and ti_assigned=? and ti_dateRaised between ? and ?";
            pstmt = conn.prepareStatement(getUnassignTix);
            pstmt.setString(1, "CLOSED");
            pstmt.setString(2, "CHANGE REQUEST");
            pstmt.setString(3,frmLogin.username);
            pstmt.setString(4, String.valueOf(today.withDayOfMonth(1)));
            pstmt.setString(5, String.valueOf(today.withDayOfMonth(today.lengthOfMonth())));
            rs = pstmt.executeQuery();
            int getCount=0;
            while (rs.next()){
                getCount++;
            }
            btnClosedReq.setText(String.valueOf(getCount));
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    private void getDate(){
        LocalDate today = LocalDate.now();    
    }
    private void fillResolvedReq(){
    try{
            String getResolvedTix = "select * from tblTicket where ti_status=? and ti_type=? "
                    + "and ti_assigned=? and ti_dateRaised between ? and ?";
            pstmt = conn.prepareStatement(getResolvedTix);
            pstmt.setString(1,"RESOLVED");
            pstmt.setString(2,"CHANGE REQUEST");
            pstmt.setString(3,frmLogin.username);
            pstmt.setString(4, String.valueOf(today.withDayOfMonth(1)));
            pstmt.setString(5, String.valueOf(today.withDayOfMonth(today.lengthOfMonth())));
            rs = pstmt.executeQuery();
            int getCount=0;
            while (rs.next()){
                getCount++;
            }
            btnResolveReq.setText(String.valueOf(getCount));
        }catch(SQLException e){
            e.getMessage();
        }
    }
    private void fillPendingReq(){
        try{
            String getPendingTix = "select * from tblTicket where ti_status=? and ti_assigned<>? and ti_dateRaised between ? and ?"
                    + "and ti_type=? and ti_assigned=?";
            pstmt = conn.prepareStatement(getPendingTix);
            pstmt.setString(1,"PENDING");
            pstmt.setString(2,"UNASSIGNED");
            pstmt.setString(3, String.valueOf(today.withDayOfMonth(1)));
            pstmt.setString(4, String.valueOf(today.withDayOfMonth(today.lengthOfMonth())));
            pstmt.setString(5,"CHANGE REQUEST");
            pstmt.setString(6,frmLogin.username);
            rs = pstmt.executeQuery();
            int getCount=0;
            while (rs.next()){
                getCount++;
            }
            btnPendingReq.setText(String.valueOf(getCount));
        }catch(SQLException e){
            e.getMessage();
        }
    }
    private void fillUnassignedReq(){
        try{
                String getUnassignTix = "select * from tblTicket where ti_status=? "
                        + "and ti_assigned=? and ti_type=? and ti_dateRaised between ? and ?";
            pstmt = conn.prepareStatement(getUnassignTix);
            pstmt.setString(1, "PENDING");
            pstmt.setString(2,"UNASSIGNED");
            pstmt.setString(3, "CHANGE REQUEST");
            pstmt.setString(4, String.valueOf(today.withDayOfMonth(1)));
            pstmt.setString(5, String.valueOf(today.withDayOfMonth(today.lengthOfMonth())));
            rs = pstmt.executeQuery();
            int getCount=0;
            while (rs.next()){
                getCount++;
            }
            btnUnassignedReq.setText(String.valueOf(getCount));
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    private void fillClosedTickets(){
        try{
                String getUnassignTix = "select * from tblTicket where ti_status=? "
                        + " and ti_type=? and ti_assigned=? and ti_dateRaised between ? and ?";
            pstmt = conn.prepareStatement(getUnassignTix);
            pstmt.setString(1, "CLOSED");
            pstmt.setString(2, "ISSUE - TICKET");
            pstmt.setString(3,frmLogin.username);
            pstmt.setString(4, String.valueOf(today.withDayOfMonth(1)));
            pstmt.setString(5, String.valueOf(today.withDayOfMonth(today.lengthOfMonth())));
            rs = pstmt.executeQuery();
            int getCount=0;
            while (rs.next()){
                getCount++;
            }
            btnClosedTickets.setText(String.valueOf(getCount));
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    private void fillUnassignedTickets(){
        try{
//            String getUnassignTix = "select * from tblTicket where ti_status='PENDING' and ti_assigned=''"
//                    + "and ti_type='ISSUE - TICKET'";
                String getUnassignTix = "select * from tblTicket where ti_status=? "
                        + "and ti_assigned=? and ti_type=? and ti_dateRaised between ? and ?";
            pstmt = conn.prepareStatement(getUnassignTix);
            pstmt.setString(1, "PENDING");
            pstmt.setString(2,"UNASSIGNED");
            pstmt.setString(3, "ISSUE - TICKET");
            pstmt.setString(4, String.valueOf(today.withDayOfMonth(1)));
            pstmt.setString(5, String.valueOf(today.withDayOfMonth(today.lengthOfMonth())));
            rs = pstmt.executeQuery();
            int getCount=0;
            while (rs.next()){
                getCount++;
            }
            btnUnassignedTickets.setText(String.valueOf(getCount));
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
   private void fillResolvedTickets(){
        try{
            String getResolvedTix = "select * from tblTicket where ti_status=? and ti_type=? "
                    + "and ti_assigned=? and ti_dateRaised between ? and ?";
            pstmt = conn.prepareStatement(getResolvedTix);
            pstmt.setString(1,"RESOLVED");
            pstmt.setString(2,"ISSUE - TICKET");
            pstmt.setString(3,frmLogin.username);
            pstmt.setString(4, String.valueOf(today.withDayOfMonth(1)));
            pstmt.setString(5, String.valueOf(today.withDayOfMonth(today.lengthOfMonth())));
            rs = pstmt.executeQuery();
            int getCount=0;
            while (rs.next()){
                getCount++;
            }
            btnResolvedTickets.setText(String.valueOf(getCount));
        }catch(SQLException e){
            e.getMessage();
        }
    }
    private void fillPendingTickets(){
        try{
            String getPendingTix = "select * from tblTicket where ti_status=? and ti_assigned<>? and ti_dateRaised between ? and ?"
                    + "and ti_type=? and ti_assigned=?";
            pstmt = conn.prepareStatement(getPendingTix);
            pstmt.setString(1,"PENDING");
            pstmt.setString(2,"UNASSIGNED");
            pstmt.setString(3, String.valueOf(today.withDayOfMonth(1)));
            pstmt.setString(4, String.valueOf(today.withDayOfMonth(today.lengthOfMonth())));
            pstmt.setString(5,"ISSUE - TICKET");
            pstmt.setString(6,frmLogin.username);
            rs = pstmt.executeQuery();
            int getCount=0;
            while (rs.next()){
                getCount++;
            }
            btnPendingTickets.setText(String.valueOf(getCount));
        }catch(SQLException e){
            e.getMessage();
        }
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnUnassignedTickets = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        btnPendingTickets = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel34 = new javax.swing.JPanel();
        jPanel35 = new javax.swing.JPanel();
        btnClosedReq = new javax.swing.JButton();
        jLabel28 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jPanel32 = new javax.swing.JPanel();
        jPanel33 = new javax.swing.JPanel();
        btnResolveReq = new javax.swing.JButton();
        jLabel26 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jPanel30 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jPanel31 = new javax.swing.JPanel();
        btnPendingReq = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jPanel28 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jPanel29 = new javax.swing.JPanel();
        btnUnassignedReq = new javax.swing.JButton();
        jPanel21 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        btnResolvedTickets = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        btnClosedTickets = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel36 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        panelAdmin = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        btnAllPendingTix = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        btnAllPendingReq = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        lblPosition = new javax.swing.JLabel();
        lblUserName = new javax.swing.JLabel();
        panelAdmin1 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        btnOverallPendingTix = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        btnOverallClosed = new javax.swing.JButton();
        jPanel17 = new javax.swing.JPanel();
        btnOverallPendingReq = new javax.swing.JButton();
        jPanel18 = new javax.swing.JPanel();
        btnOverallResolved = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        lblLogo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(153, 153, 153));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel9.setBackground(new java.awt.Color(255, 102, 0));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("UNASSIGNED");
        jPanel9.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 250, 60));

        btnUnassignedTickets.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        btnUnassignedTickets.setText("jButton1");
        btnUnassignedTickets.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUnassignedTicketsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnUnassignedTickets, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnUnassignedTickets, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel9.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 70, 150, -1));

        jPanel2.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 270, 180));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, 310, 210));

        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel10.setBackground(new java.awt.Color(153, 0, 0));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 36)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("PENDING");
        jPanel10.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 250, 60));

        btnPendingTickets.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        btnPendingTickets.setText("jButton1");
        btnPendingTickets.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPendingTicketsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnPendingTickets, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnPendingTickets, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel10.add(jPanel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 70, 150, -1));

        jPanel4.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 270, 180));

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 260, 310, 210));

        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel34.setBackground(new java.awt.Color(153, 153, 0));
        jPanel34.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnClosedReq.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        btnClosedReq.setText("jButton1");
        btnClosedReq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClosedReqActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnClosedReq, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnClosedReq, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel34.add(jPanel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 60, 150, -1));

        jLabel28.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 36)); // NOI18N
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setText("CLOSED");
        jPanel34.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 250, 60));

        jPanel5.add(jPanel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 270, 170));

        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 560, 310, 210));

        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel32.setBackground(new java.awt.Color(0, 51, 0));
        jPanel32.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnResolveReq.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        btnResolveReq.setText("jButton1");
        btnResolveReq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResolveReqActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnResolveReq, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnResolveReq, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel32.add(jPanel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 70, 150, -1));

        jLabel26.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 36)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setText("RESOLVED");
        jPanel32.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 250, 60));

        jPanel6.add(jPanel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 270, 180));

        jPanel1.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 560, 310, 210));

        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel30.setBackground(new java.awt.Color(153, 0, 0));
        jPanel30.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel23.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 36)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("PENDING");
        jPanel30.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 250, 60));

        btnPendingReq.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        btnPendingReq.setText("jButton1");
        btnPendingReq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPendingReqActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnPendingReq, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnPendingReq, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel30.add(jPanel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 70, 150, -1));

        jPanel7.add(jPanel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 270, 180));

        jPanel1.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 560, 310, 210));

        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel28.setBackground(new java.awt.Color(255, 102, 0));
        jPanel28.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel16.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 36)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("UNASSIGNED");
        jPanel28.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 250, 60));

        btnUnassignedReq.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        btnUnassignedReq.setText("jButton1");
        btnUnassignedReq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUnassignedReqActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnUnassignedReq, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnUnassignedReq, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel28.add(jPanel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 70, 150, -1));

        jPanel8.add(jPanel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 270, 180));

        jPanel1.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 560, 310, 210));

        jPanel21.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("CHANGE REQUEST");
        jPanel21.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 290, 40));

        jPanel1.add(jPanel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 510, 310, 40));

        jLabel3.setText("-------------------------------------------------------------------------------------------------------------------------------");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 520, 510, -1));

        jPanel15.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel23.setBackground(new java.awt.Color(0, 51, 0));
        jPanel23.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnResolvedTickets.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        btnResolvedTickets.setText("jButton1");
        btnResolvedTickets.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResolvedTicketsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnResolvedTickets, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnResolvedTickets, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel23.add(jPanel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 70, 150, -1));

        jLabel18.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 36)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("RESOLVED");
        jPanel23.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 250, 60));

        jPanel15.add(jPanel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 270, 180));

        jPanel1.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 260, 310, 210));

        jPanel25.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel26.setBackground(new java.awt.Color(153, 153, 0));
        jPanel26.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnClosedTickets.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        btnClosedTickets.setText("jButton1");
        btnClosedTickets.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClosedTicketsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnClosedTickets, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnClosedTickets, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel26.add(jPanel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 70, 150, -1));

        jLabel20.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 36)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("CLOSED");
        jPanel26.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 250, 60));

        jPanel25.add(jPanel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 270, 180));

        jPanel1.add(jPanel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 260, 310, 210));

        jLabel21.setText("-------------------------------------------------------------------------------------------------------------------------------");
        jPanel1.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 520, 510, -1));

        jLabel5.setText("-------------------------------------------------------------------------------------------------------------------------------");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, 510, -1));

        jPanel36.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("TICKET");
        jPanel36.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 290, 40));

        jPanel1.add(jPanel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 210, 310, 40));

        jLabel29.setText("-------------------------------------------------------------------------------------------------------------------------------");
        jPanel1.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 220, 510, -1));

        panelAdmin.setBackground(new java.awt.Color(153, 153, 153));
        panelAdmin.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("MY TEAM WORKING ON:");
        panelAdmin.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 270, 40));

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("CHANGE REQUESTS");
        panelAdmin.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 40, 120, -1));

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("TICKETS");
        panelAdmin.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 120, -1));

        jPanel12.setBackground(new java.awt.Color(102, 102, 0));

        btnAllPendingTix.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnAllPendingTix.setText("jButton1");
        btnAllPendingTix.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAllPendingTixActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAllPendingTix, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAllPendingTix, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelAdmin.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 120, 60));

        jPanel11.setBackground(new java.awt.Color(102, 102, 0));

        btnAllPendingReq.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnAllPendingReq.setText("jButton1");
        btnAllPendingReq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAllPendingReqActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAllPendingReq, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAllPendingReq, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelAdmin.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 60, 120, 60));

        jPanel1.add(panelAdmin, new org.netbeans.lib.awtextra.AbsoluteConstraints(1040, 10, 290, 130));

        jPanel13.setBackground(new java.awt.Color(153, 153, 153));
        jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 102, 0));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("POSITION:");
        jPanel13.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 60, 70, 20));

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 102, 0));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("NAME:");
        jPanel13.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 0, 70, 20));

        lblPosition.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPosition.setText("jLabel10");
        jPanel13.add(lblPosition, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 190, 20));

        lblUserName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblUserName.setText("jLabel10");
        jPanel13.add(lblUserName, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 190, 20));

        jPanel1.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 10, 210, 130));

        panelAdmin1.setBackground(new java.awt.Color(153, 153, 153));
        panelAdmin1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("MY OVERALL TASKS TRACKER");
        panelAdmin1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 540, 40));

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("CLOSED TICKETS");
        panelAdmin1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 40, 120, -1));

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("TICKETS");
        panelAdmin1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 120, -1));

        jPanel14.setBackground(new java.awt.Color(102, 102, 0));
        jPanel14.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnOverallPendingTix.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnOverallPendingTix.setText("jButton1");
        btnOverallPendingTix.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOverallPendingTixActionPerformed(evt);
            }
        });
        jPanel14.add(btnOverallPendingTix, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 102, 30));

        panelAdmin1.add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 120, 50));

        jPanel16.setBackground(new java.awt.Color(102, 102, 0));
        jPanel16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnOverallClosed.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnOverallClosed.setText("jButton1");
        btnOverallClosed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOverallClosedActionPerformed(evt);
            }
        });
        jPanel16.add(btnOverallClosed, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 102, 30));

        panelAdmin1.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 60, 120, 50));

        jPanel17.setBackground(new java.awt.Color(102, 102, 0));
        jPanel17.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnOverallPendingReq.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnOverallPendingReq.setText("jButton1");
        btnOverallPendingReq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOverallPendingReqActionPerformed(evt);
            }
        });
        jPanel17.add(btnOverallPendingReq, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 102, 30));

        panelAdmin1.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 60, 120, 50));

        jPanel18.setBackground(new java.awt.Color(102, 102, 0));
        jPanel18.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnOverallResolved.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnOverallResolved.setText("jButton1");
        btnOverallResolved.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOverallResolvedActionPerformed(evt);
            }
        });
        jPanel18.add(btnOverallResolved, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 102, 30));

        panelAdmin1.add(jPanel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 60, 120, 50));

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("CHANGE REQUESTS");
        panelAdmin1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 40, 120, -1));

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("RESOLVED TICKETS");
        panelAdmin1.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 40, 120, -1));

        jPanel1.add(panelAdmin1, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 10, 570, 120));

        lblLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/forms/isoft logo.jpg"))); // NOI18N
        jPanel1.add(lblLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 180, 130));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, 790));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnUnassignedTicketsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUnassignedTicketsActionPerformed
        showQuery = "UNASSIGNED TICKETS";
                try{
                    fmrTicketing obj = new fmrTicketing();
                    obj.setVisible(true);
                    this.dispose();
                    rs.close();
                    pstmt.close();
                    conn.close();
                }catch(SQLException e){
                    e.getMessage();
                }
          
    }//GEN-LAST:event_btnUnassignedTicketsActionPerformed

    private void btnPendingTicketsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPendingTicketsActionPerformed
        showQuery = "PENDING TICKETS";
        
            try{
                fmrTicketing obj = new fmrTicketing();
                obj.setVisible(true);
                this.dispose();
                rs.close();
                pstmt.close();
                conn.close();
            }catch(SQLException e){
                e.getMessage();
            }
        

    }//GEN-LAST:event_btnPendingTicketsActionPerformed

    private void btnResolvedTicketsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResolvedTicketsActionPerformed
        showQuery = "RESOLVED TICKETS";
                try{
                    fmrTicketing obj = new fmrTicketing();
                    obj.setVisible(true);
                    this.dispose();
                    rs.close();
                    pstmt.close();
                    conn.close();
                }catch(SQLException e){
                    e.getMessage();
                }
        
    }//GEN-LAST:event_btnResolvedTicketsActionPerformed

    private void btnClosedTicketsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClosedTicketsActionPerformed
        showQuery = "CLOSED TICKETS";
                try{
                    fmrTicketing obj = new fmrTicketing();
                    obj.setVisible(true);
                    this.dispose();
                    rs.close();
                    pstmt.close();
                    conn.close();
                }catch(SQLException e){
                    e.getMessage();
                }
        
    }//GEN-LAST:event_btnClosedTicketsActionPerformed

    private void btnUnassignedReqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUnassignedReqActionPerformed
        showQuery = "UNASSIGNED REQUEST";
                try{
                    fmrTicketing obj = new fmrTicketing();
                    obj.setVisible(true);
                    this.dispose();
                    rs.close();
                    pstmt.close();
                    conn.close();
                }catch(SQLException e){
                    e.getMessage();
                }
        
    }//GEN-LAST:event_btnUnassignedReqActionPerformed

    private void btnPendingReqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPendingReqActionPerformed
        showQuery = "PENDING REQUEST";
                try{
                    fmrTicketing obj = new fmrTicketing();
                    obj.setVisible(true);
                    this.dispose();
                    rs.close();
                    pstmt.close();
                    conn.close();
                }catch(SQLException e){
                    e.getMessage();
                }
         
    }//GEN-LAST:event_btnPendingReqActionPerformed

    private void btnResolveReqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResolveReqActionPerformed
        showQuery = "RESOLVED REQUEST";
                try{
                    fmrTicketing obj = new fmrTicketing();
                    obj.setVisible(true);
                    this.dispose();
                    rs.close();
                    pstmt.close();
                    conn.close();
                }catch(SQLException e){
                    e.getMessage();
                }
        
    }//GEN-LAST:event_btnResolveReqActionPerformed

    private void btnClosedReqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClosedReqActionPerformed
        showQuery = "CLOSED REQUEST";
                try{
                    fmrTicketing obj = new fmrTicketing();
                    obj.setVisible(true);
                    this.dispose();
                    rs.close();
                    pstmt.close();
                    conn.close();
                }catch(SQLException e){
                    e.getMessage();
                }
       
    }//GEN-LAST:event_btnClosedReqActionPerformed

    private void btnAllPendingTixActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAllPendingTixActionPerformed
        showQuery = "ALL PENDING TIX";
        try{
            fmrTicketing obj = new fmrTicketing();
            obj.setVisible(true);
            this.dispose();
            rs.close();
            pstmt.close();
            conn.close();
        }catch(SQLException e){
            e.getMessage();
        }    
    }//GEN-LAST:event_btnAllPendingTixActionPerformed

    private void btnAllPendingReqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAllPendingReqActionPerformed
    showQuery = "ALL PENDING REQ";
        try{
            fmrTicketing obj = new fmrTicketing();
            obj.setVisible(true);
            this.dispose();
            rs.close();
            pstmt.close();
            conn.close();
        }catch(SQLException e){
            e.getMessage();
        }    
    }//GEN-LAST:event_btnAllPendingReqActionPerformed

    private void btnOverallPendingTixActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOverallPendingTixActionPerformed
       showQuery = "OVERALL PENDING TIX";
        try{
            fmrTicketing obj = new fmrTicketing();
            obj.setVisible(true);
            this.dispose();
            rs.close();
            pstmt.close();
            conn.close();
        }catch(SQLException e){
            e.getMessage();
        }    
    }//GEN-LAST:event_btnOverallPendingTixActionPerformed

    private void btnOverallClosedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOverallClosedActionPerformed
        showQuery = "OVERALL CLOSED";
        try{
            fmrTicketing obj = new fmrTicketing();
            obj.setVisible(true);
            this.dispose();
            rs.close();
            pstmt.close();
            conn.close();
        }catch(SQLException e){
            e.getMessage();
        }
    }//GEN-LAST:event_btnOverallClosedActionPerformed

    private void btnOverallPendingReqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOverallPendingReqActionPerformed
        showQuery = "OVERALL PENDING REQ";
        try{
            fmrTicketing obj = new fmrTicketing();
            obj.setVisible(true);
            this.dispose();
            rs.close();
            pstmt.close();
            conn.close();
        }catch(SQLException e){
            e.getMessage();
        }  
    }//GEN-LAST:event_btnOverallPendingReqActionPerformed

    private void btnOverallResolvedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOverallResolvedActionPerformed
        showQuery = "OVERALL RESOLVED";
        try{
            fmrTicketing obj = new fmrTicketing();
            obj.setVisible(true);
            this.dispose();
            rs.close();
            pstmt.close();
            conn.close();
        }catch(SQLException e){
            e.getMessage();
        }
    }//GEN-LAST:event_btnOverallResolvedActionPerformed

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
            java.util.logging.Logger.getLogger(frmDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmDashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAllPendingReq;
    private javax.swing.JButton btnAllPendingTix;
    private javax.swing.JButton btnClosedReq;
    private javax.swing.JButton btnClosedTickets;
    private javax.swing.JButton btnOverallClosed;
    private javax.swing.JButton btnOverallPendingReq;
    private javax.swing.JButton btnOverallPendingTix;
    private javax.swing.JButton btnOverallResolved;
    private javax.swing.JButton btnPendingReq;
    private javax.swing.JButton btnPendingTickets;
    private javax.swing.JButton btnResolveReq;
    private javax.swing.JButton btnResolvedTickets;
    private javax.swing.JButton btnUnassignedReq;
    private javax.swing.JButton btnUnassignedTickets;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblPosition;
    private javax.swing.JLabel lblUserName;
    private javax.swing.JPanel panelAdmin;
    private javax.swing.JPanel panelAdmin1;
    // End of variables declaration//GEN-END:variables
}
