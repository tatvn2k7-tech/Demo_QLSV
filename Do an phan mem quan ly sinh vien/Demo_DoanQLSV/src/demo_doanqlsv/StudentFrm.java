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

public class StudentFrm extends JFrame {
    private JTable tblStudents;
    private DefaultTableModel tableModel;
    private JTextField txtMaSV, txtHoTen, txtLop, txtNgaySinh, txtDiaChi;
    private JComboBox<String> cboGioiTinh;
    private JButton btnAdd, btnUpdate, btnDelete, btnRefresh, btnScoreMgr;

    public StudentFrm() {
        setTitle("Quản Lý Sinh Viên");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitle = new JLabel("Danh Sách Sinh Viên", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(new Color(0, 128, 0));
        add(lblTitle, BorderLayout.NORTH);

        String[] columns = {"Mã sinh viên", "Họ và Tên", "Lớp", "Ngày Sinh", "Giới Tính", "Địa Chỉ"};
        tableModel = new DefaultTableModel(columns, 0);
        tblStudents = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tblStudents);
        
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel panelBottom = new JPanel(new BorderLayout(10, 10));
        
        JPanel panelInputs = new JPanel(new GridLayout(3, 4, 10, 10));
        panelInputs.setBorder(BorderFactory.createTitledBorder("Thông tin chi tiết sinh viên"));

        panelInputs.add(new JLabel("Mã Sinh Viên:"));
        txtMaSV = new JTextField();
        panelInputs.add(txtMaSV);

        panelInputs.add(new JLabel("Ngày Sinh:"));
        txtNgaySinh = new JTextField();
        panelInputs.add(txtNgaySinh);

        panelInputs.add(new JLabel("Họ Và Tên:"));
        txtHoTen = new JTextField();
        panelInputs.add(txtHoTen);

        panelInputs.add(new JLabel("Giới Tính:"));
        cboGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        panelInputs.add(cboGioiTinh);

        panelInputs.add(new JLabel("Lớp:"));
        txtLop = new JTextField();
        panelInputs.add(txtLop);

        panelInputs.add(new JLabel("Địa Chỉ:"));
        txtDiaChi = new JTextField();
        panelInputs.add(txtDiaChi);

        panelBottom.add(panelInputs, BorderLayout.CENTER);

        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnAdd = new JButton("ADD");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnRefresh = new JButton("Làm mới");
        btnScoreMgr = new JButton("Quản lý điểm");

        styleButton(btnAdd, new Color(30, 144, 255));
        styleButton(btnUpdate, new Color(46, 139, 87));
        styleButton(btnDelete, new Color(220, 20, 60));
        styleButton(btnRefresh, new Color(112, 128, 144));
        styleButton(btnScoreMgr, new Color(138, 43, 228));

        btnScoreMgr.addActionListener(e -> {
            new ScoreFrm().setVisible(true);
        });

        panelButtons.add(btnAdd);
        panelButtons.add(btnUpdate);
        panelButtons.add(btnDelete);
        panelButtons.add(btnRefresh);
        panelButtons.add(btnScoreMgr); 

        panelBottom.add(panelButtons, BorderLayout.SOUTH);
        centerPanel.add(panelBottom, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        loadDataToTable();

        tblStudents.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tblStudents.getSelectedRow();
                if (row >= 0) {
                    txtMaSV.setText(tableModel.getValueAt(row, 0).toString());
                    txtHoTen.setText(tableModel.getValueAt(row, 1).toString());
                    txtLop.setText(tableModel.getValueAt(row, 2).toString());
                    txtNgaySinh.setText(tableModel.getValueAt(row, 3).toString());
                    cboGioiTinh.setSelectedItem(tableModel.getValueAt(row, 4).toString());
                    txtDiaChi.setText(tableModel.getValueAt(row, 5).toString());
                }
            }
        });

        btnAdd.addActionListener(e -> addStudent());
        btnUpdate.addActionListener(e -> updateStudent());
        btnDelete.addActionListener(e -> deleteStudent());
        btnRefresh.addActionListener(e -> clearForm());
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setFocusPainted(false);
    }

    public void loadDataToTable() {
        tableModel.setRowCount(0);
        try (Connection conn = DatabaseConnection.openConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM tblSinhVien")) {
            
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("fldMSV"));
                row.add(rs.getString("fldHo_va_ten"));
                row.add(rs.getString("fldMa_lop"));
                row.add(rs.getString("fldNgay_sinh"));
                row.add(rs.getString("fldGioi_tinh"));
                row.add(rs.getString("fldDia_chi"));
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + e.getMessage());
        }
    }

    private void addStudent() {
        try (Connection conn = DatabaseConnection.openConnection();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO tblSinhVien (fldMSV, fldHo_va_ten, fldMa_lop, fldNgay_sinh, fldGioi_tinh, fldDia_chi) VALUES (?, ?, ?, ?, ?, ?)")) {
            pstmt.setString(1, txtMaSV.getText().trim());
            pstmt.setString(2, txtHoTen.getText().trim());
            pstmt.setString(3, txtLop.getText().trim());
            pstmt.setString(4, txtNgaySinh.getText().trim());
            pstmt.setString(5, cboGioiTinh.getSelectedItem().toString());
            pstmt.setString(6, txtDiaChi.getText().trim());
            
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Thêm sinh viên thành công!");
            loadDataToTable();
            clearForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi thêm: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateStudent() {
        try (Connection conn = DatabaseConnection.openConnection();
             PreparedStatement pstmt = conn.prepareStatement("UPDATE tblSinhVien SET fldHo_va_ten=?, fldMa_lop=?, fldNgay_sinh=?, fldGioi_tinh=?, fldDia_chi=? WHERE fldMSV=?")) {
            pstmt.setString(1, txtHoTen.getText().trim());
            pstmt.setString(2, txtLop.getText().trim());
            pstmt.setString(3, txtNgaySinh.getText().trim());
            pstmt.setString(4, cboGioiTinh.getSelectedItem().toString());
            pstmt.setString(5, txtDiaChi.getText().trim());
            pstmt.setString(6, txtMaSV.getText().trim());
            
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            loadDataToTable();
            clearForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi sửa: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteStudent() {
        try (Connection conn = DatabaseConnection.openConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM tblSinhVien WHERE fldMSV=?")) {
            pstmt.setString(1, txtMaSV.getText().trim());
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Xóa thành công!");
            loadDataToTable();
            clearForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi xóa: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        txtMaSV.setText("");
        txtHoTen.setText("");
        txtLop.setText("");
        txtNgaySinh.setText("");
        txtDiaChi.setText("");
        txtMaSV.requestFocus();
    }
}