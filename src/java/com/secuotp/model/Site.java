/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.secuotp.model;

import com.secuotp.model.connection.ConnectionAgent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author zenology
 */
public class Site extends SiteConfig {

    private String siteId;
    private String siteName;
    private String domain;
    private String serialNumber;
    private String descirption;
    private String imgPath;
    private boolean siteDisabled;
    private int userCount;
    private Calendar dateCreated;

    public boolean isSiteDisabled() {
        return siteDisabled;
    }

    public void setSiteDisabled(boolean siteDisabled) {
        this.siteDisabled = siteDisabled;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getDescirption() {
        return descirption;
    }

    public void setDescirption(String descirption) {
        this.descirption = descirption;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public Calendar getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Calendar dateCreated) {
        this.dateCreated = dateCreated;
    }

    public static boolean authenService(String domain, String serial) {
        try {
            Connection con = ConnectionAgent.getInstance();
            String sql = "SELECT COUNT(*) FROM site WHERE domain = ? AND serial_number = ?";
            PreparedStatement ps = con.prepareCall(sql);
            ps.setString(1, domain);
            ps.setString(2, serial);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Site.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Site.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static Site getSite(String domain) {
        try {
            Connection con = ConnectionAgent.getInstance();
            String sql = "SELECT * FROM secuotp.site_config_full WHERE domain = \'" + domain + "\'";
            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                Site s = new Site();
                s.setSiteId("" + rs.getInt(1));
                s.setSiteName(rs.getString(2));
                s.setDomain(domain);
                s.setSerialNumber(rs.getString(4));
                s.setDescirption(rs.getString(5));
                s.setImgPath(rs.getString(6));
                s.setSiteDisabled(rs.getInt(7) > 0);
                s.setUserCount(rs.getInt(8));
                
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(rs.getTimestamp(9).getTime());
                
                s.setDateCreated(c);
                s.setSiteConfigId("" + rs.getInt(10));
                s.setPatternName(rs.getString(11));
                s.setLength(rs.getInt(12));
                s.setOtpDisable(rs.getInt(13) > 0);
                s.setTimeZone(rs.getString(14));
                return s;
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Site.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static boolean checkDisabled(String domain) throws ClassNotFoundException, SQLException {
        String sql = "SELECT disabled FROM site WHERE domain = ?";
        Connection con = ConnectionAgent.getInstance();
        PreparedStatement ps = con.prepareCall(sql);
        ps.setString(1, domain);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
        return false;
    }
}
