/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package demo_doanqlsv;

import dao.DatabaseConnection;
import java.awt.*;
import java.sql.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ScoreFrm extends JFrame {
    private JTable tblScores;
    private DefaultTableModel tableModel;
    private JTextField txtMSV, txtHoTen, txtNgaySinh, txtLop, txtKhoa, txtDiem;
    private JComboBox<String> cboMonHoc, cboLocKhoa, cboLocLop;
    private JButton btnAdd, btnUpdate, btnDelete, btnReset;

    public ScoreFrm() {
        setTitle("Quản Lý Điểm Sinh Viên");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitle = new JLabel("Quản Lý Điểm Sinh Viên", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(new Color(0, 128, 0));
        add(lblTitle, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        panelFilter.add(new JLabel("Khoa:"));
        cboLocKhoa = new JComboBox<>(new String[]{"Tất cả", "Công Nghệ Thông Tin", "Kiến Trúc"});
        panelFilter.add(cboLocKhoa);
        
        panelFilter.add(new JLabel("Lớp:"));
        cboLocLop = new JComboBox<>(new String[]{"Tất cả", "KT01", "CNTT01"});
        panelFilter.add(cboLocLop);
        centerPanel.add(panelFilter, BorderLayout.NORTH);

        String[] columns = {"Mã Sinh Viên", "Ho và Tên", "Ngày Sinh", "Lớp", "Khoa", "Môn học", "Điểm Tổng Kết"};
        tableModel = new DefaultTableModel(columns, 0);
        tblScores = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tblScores);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel panelBottom = new JPanel(new BorderLayout(10, 10));
        
        JPanel panelInputs = new JPanel(new GridLayout(5, 2, 10, 8));
        panelInputs.setBorder(BorderFactory.createTitledBorder("Thông tin điểm sinh viên"));

        panelInputs.add(new JLabel("MSV:"));
        txtMSV = new JTextField();
        panelInputs.add(txtMSV);

        panelInputs.add(new JLabel("Họ và Tên:"));
        txtHoTen = new JTextField();
        panelInputs.add(txtHoTen);

        panelInputs.add(new JLabel("Ngày Sinh:"));
        txtNgaySinh = new JTextField();
        panelInputs.add(txtNgaySinh);

        panelInputs.add(new JLabel("Lớp:"));
        txtLop = new JTextField();
        panelInputs.add(txtLop);

        panelInputs.add(new JLabel("Khoa:"));
        txtKhoa = new JTextField();
        panelInputs.add(txtKhoa);

        JPanel panelRightInputs = new JPanel(new GridLayout(2, 2, 10, 8));
        panelRightInputs.add(new JLabel("Môn Học:"));
        cboMonHoc = new JComboBox<>(new String[]{"Java", "Thiết Kế Nội Thất", "Cơ Sở Dữ Liệu"});
        panelRightInputs.add(cboMonHoc);

        panelRightInputs.add(new JLabel("Điểm Tổng Kết:"));
        txtDiem = new JTextField();
        panelRightInputs.add(txtDiem);

        JPanel panelFormWrapper = new JPanel(new GridLayout(1, 2, 15, 0));
        panelFormWrapper.add(panelInputs);
        
        JPanel panelSubRight = new JPanel(new BorderLayout());
        panelSubRight.add(panelRightInputs, BorderLayout.NORTH);
        panelFormWrapper.add(panelSubRight);

        panelBottom.add(panelFormWrapper, BorderLayout.CENTER);

        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnReset = new JButton("Reset");

        styleButton(btnAdd, new Color(30, 144, 255));
        styleButton(btnUpdate, new Color(46, 139, 87));
        styleButton(btnDelete, new Color(220, 20, 60));
        styleButton(btnReset, new Color(112, 128, 144));

        panelButtons.add(btnAdd);
        panelButtons.add(btnUpdate);
        panelButtons.add(btnDelete);
        panelButtons.add(btnReset);

        panelBottom.add(panelButtons, BorderLayout.SOUTH);
        centerPanel.add(panelBottom, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        loadScoreData();

        tblScores.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tblScores.getSelectedRow();
                if (row >= 0) {
                    txtMSV.setText(tableModel.getValueAt(row, 0).toString());
                    txtHoTen.setText(tableModel.getValueAt(row, 1).toString());
                    txtNgaySinh.setText(tableModel.getValueAt(row, 2).toString());
                    txtLop.setText(tableModel.getValueAt(row, 3).toString());
                    txtKhoa.setText(tableModel.getValueAt(row, 4).toString());
                    cboMonHoc.setSelectedItem(tableModel.getValueAt(row, 5).toString());
                    txtDiem.setText(tableModel.getValueAt(row, 6).toString());
                }
            }
        });

        btnReset.addActionListener(e -> clearForm());
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setFocusPainted(false);
    }

    public void loadScoreData() {
    tableModel.setRowCount(0); 
    
    String sql = "SELECT fldMSV, fldHo_va_ten, fldNgay_sinh, fldMa_lop, fldGioi_tinh, fldDia_chi FROM tblSinhVien";

    try (Connection conn = DatabaseConnection.openConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        
        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            row.add(rs.getString("fldMSV"));          
            row.add(rs.getString("fldHo_va_ten"));    
            row.add(rs.getString("fldNgay_sinh"));    
            row.add(rs.getString("fldMa_lop"));       
            row.add("Công Nghệ Thông Tin");           
            row.add("Java");                          
            row.add(8.0);                             
            tableModel.addRow(row);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu điểm: " + e.getMessage());
    }
}

    private void clearForm() {
        txtMSV.setText("");
        txtHoTen.setText("");
        txtNgaySinh.setText("");
        txtLop.setText("");
        txtKhoa.setText("");
        txtDiem.setText("");
        txtMSV.requestFocus();
    }
}