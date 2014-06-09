/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.secuotp.model;

import com.secuotp.model.connection.ConnectionAgent;
import com.secuotp.model.generate.SerialNumber;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author zenology
 */
public class SiteUser extends People {

    private String siteUserId;
    private String siteId;
    private String phone;
    private String serialNumber;
    private String removalCode;
    private boolean mobileMode;

    public String getSiteUserId() {
        return siteUserId;
    }

    public void setSiteUserId(String siteUserId) {
        this.siteUserId = siteUserId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getRemovalCode() {
        return removalCode;
    }

    public void setRemovalCode(String removalCode) {
        this.removalCode = removalCode;
    }

    public boolean isMobileMode() {
        return mobileMode;
    }

    public void setMobileMode(boolean mobileMode) {
        this.mobileMode = mobileMode;

    }

    public static void addSiteUser(SiteUser user, String domainName, String serialNumber) throws ClassNotFoundException, SQLException, NoSuchAlgorithmException, UnsupportedEncodingException {
        Connection con = ConnectionAgent.getInstance();
        String sql = "INSERT INTO people (username, email, firstname, lastname) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = con.prepareCall(sql);
        ps.setString(1, user.getUsername());
        ps.setString(2, user.getEmail());
        ps.setString(3, user.getFirstname());
        ps.setString(4, user.getLastname());

        int row = ps.executeUpdate();
        if (row > 0) {
            String peopleId = getPeopleId(con, user.getEmail());
            String siteId = getSiteId(con, domainName, serialNumber);
            
            String userSerialNumber = SerialNumber.generateSerialNumber(user.getEmail());
            String removalCode = SerialNumber.generateRemovalCode(user.getFirstname()+user.getLastname());
            
            sql = "INSERT INTO site_user (site_id, people_id, phone_number, serial_number, removal_code, mobile_mode) VALUES (?,?,?,?,?,0)";
            ps = con.prepareCall(sql);
            ps.clearParameters();
            ps.setInt(1, Integer.parseInt(siteId));
            ps.setInt(2, Integer.parseInt(peopleId));
            ps.setString(3, user.getPhone());
            ps.setString(4, userSerialNumber);
            ps.setString(5, removalCode);
            
            row = ps.executeUpdate();
        }
    }

    private static String getPeopleId(Connection con, String email) throws SQLException {
        String sql = "SELECT people_id FROM people WHERE email = ?";
        PreparedStatement ps = con.prepareCall(sql);
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return "" + rs.getInt(1);
        }
        return null;
    }

    private static String getSiteId(Connection con, String domainName, String serialNumber) throws SQLException {
        String sql = "SELECT site_id FROM site WHERE domain = ? AND serial_number = ?";
        PreparedStatement ps = con.prepareCall(sql);
        ps.setString(1, domainName);
        ps.setString(2, serialNumber);
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            return "" + rs.getInt(1);
        }
        return null;
    }
}
