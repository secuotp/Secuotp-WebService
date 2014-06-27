/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.secuotp.model;

/**
 *
 * @author zenology
 */
public class SiteConfig {
    private String siteConfigId;
    private String patternName;
    private int length;
    private boolean otpDisable;
    private String timeZone;

    public String getSiteConfigId() {
        return siteConfigId;
    }

    public void setSiteConfigId(String siteConfigId) {
        this.siteConfigId = siteConfigId;
    }

    public String getPatternName() {
        return patternName;
    }

    public void setPatternName(String patternName) {
        this.patternName = patternName;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isOtpDisable() {
        return otpDisable;
    }

    public void setOtpDisable(boolean otpDisable) {
        this.otpDisable = otpDisable;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
    
    
}
