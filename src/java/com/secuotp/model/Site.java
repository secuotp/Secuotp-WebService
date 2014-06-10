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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author zenology
 */
public class Site {

    private String siteId;
    private String siteName;
    private String domain;
    private String serialNumber;
    private String descirption;
    private String imgPath;

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

    public static boolean authenService(String domain, String serial) {
        try {
            Connection con = ConnectionAgent.getInstance();
            String sql = "SELECT COUNT(*) FROM site WHERE domain = ? AND serial_number = ?";
            PreparedStatement ps = con.prepareCall(sql);
            ps.setString(1, domain);
            ps.setString(2, serial);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getInt(1) > 0;
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Site.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Site.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
