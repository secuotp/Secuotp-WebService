/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.secuotp.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Zenology
 */
@Path("/Service")
public class Test {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getHTML() {
        String html = "<html><body style=\"background-color:rgb(150, 172, 240)\"><h1 style=\"color:rgb(43, 101, 224)\">Jersey Web Service</h2><table><tr><td>Type</td><td>RESTful Web Service</td></tr></table></body></html>";
        return html;
    }
}
