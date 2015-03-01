/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.secuotp.model.text;

/**
 *
 * @author Zenology
 */
public class StringText {

    //======================================   ERROR   ===============================================
    public static final String ERROR_203 = "Your XML Input Mismatch: Please check XML Request Body";

    //=================================   REGISTER END USER   ========================================
    public static final String REGISTER_END_USER_XSD = "<xs:schema attributeFormDefault=\"unqualified\" elementFormDefault=\"qualified\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n" +
"  <xs:element name=\"secuotp\">\n" +
"    <xs:complexType>\n" +
"      <xs:sequence>\n" +
"        <xs:element name=\"service\">\n" +
"          <xs:complexType>\n" +
"            <xs:simpleContent>\n" +
"              <xs:extension base=\"xs:string\">\n" +
"                <xs:attribute type=\"xs:string\" name=\"sid\"/>\n" +
"              </xs:extension>\n" +
"            </xs:simpleContent>\n" +
"          </xs:complexType>\n" +
"        </xs:element>\n" +
"        <xs:element name=\"authentication\">\n" +
"          <xs:complexType>\n" +
"            <xs:sequence>\n" +
"              <xs:element type=\"xs:string\" name=\"domain\"/>\n" +
"              <xs:element type=\"xs:string\" name=\"serial\"/>\n" +
"            </xs:sequence>\n" +
"          </xs:complexType>\n" +
"        </xs:element>\n" +
"        <xs:element name=\"parameter\">\n" +
"          <xs:complexType>\n" +
"            <xs:sequence>\n" +
"              <xs:element type=\"xs:string\" name=\"username\"/>\n" +
"              <xs:element type=\"xs:string\" name=\"email\"/>\n" +
"              <xs:element type=\"xs:string\" name=\"fname\"/>\n" +
"              <xs:element type=\"xs:string\" name=\"lname\"/>\n" +
"              <xs:element type=\"xs:string\" name=\"phone\"/>\n" +
"            </xs:sequence>\n" +
"          </xs:complexType>\n" +
"        </xs:element>\n" +
"      </xs:sequence>\n" +
"    </xs:complexType>\n" +
"  </xs:element>\n" +
"</xs:schema>";
    public static final String REGISTER_END_USER_100 = "Register End-User Completed";
    public static final String REGISTER_END_USER_200 = "Failed to Register End-User: Unable Register End-User";
    public static final String REGISTER_END_USER_300 = "Failed to Register End-User: Not Allowed to Register End-User or This site maybe Disabled";

    //==================================   DISABLE END USER   =========================================
    public static final String DISABLE_END_USER_XSD = "<xs:schema attributeFormDefault=\"unqualified\" elementFormDefault=\"qualified\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n" +
"  <xs:element name=\"secuotp\">\n" +
"    <xs:complexType>\n" +
"      <xs:sequence>\n" +
"        <xs:element name=\"service\">\n" +
"          <xs:complexType>\n" +
"            <xs:simpleContent>\n" +
"              <xs:extension base=\"xs:string\">\n" +
"                <xs:attribute type=\"xs:string\" name=\"sid\"/>\n" +
"              </xs:extension>\n" +
"            </xs:simpleContent>\n" +
"          </xs:complexType>\n" +
"        </xs:element>\n" +
"        <xs:element name=\"authentication\">\n" +
"          <xs:complexType>\n" +
"            <xs:sequence>\n" +
"              <xs:element type=\"xs:string\" name=\"domain\"/>\n" +
"              <xs:element type=\"xs:string\" name=\"serial\"/>\n" +
"            </xs:sequence>\n" +
"          </xs:complexType>\n" +
"        </xs:element>\n" +
"        <xs:element name=\"parameter\">\n" +
"          <xs:complexType>\n" +
"            <xs:sequence>\n" +
"              <xs:element type=\"xs:string\" name=\"username\"/>\n" +
"              <xs:element type=\"xs:string\" name=\"code\"/>\n" +
"            </xs:sequence>\n" +
"          </xs:complexType>\n" +
"        </xs:element>\n" +
"      </xs:sequence>\n" +
"    </xs:complexType>\n" +
"  </xs:element>\n" +
"</xs:schema>";
    public static final String DISABLE_END_USER_100 = "Disable End-User Completed";
    public static final String DISABLE_END_USER_200 = "Failed to Disable End-User: Unable Disable End-User";
    public static final String DISABLE_END_USER_300 = "Failed to Disable End-User: Not Allowed to Disable End-User or This site maybe Disabled";

    //==================================   GENERATE OTP   =========================================
    public static final String GENERATE_OTP_XSD = "<?xml version=\"1.0\"?>\n" +
"<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" attributeFormDefault=\"unqualified\" elementFormDefault=\"qualified\">\n" +
"  <xs:element name=\"secuotp\" type=\"secuotpType\"/>\n" +
"  <xs:complexType name=\"serviceType\">\n" +
"    <xs:simpleContent>\n" +
"      <xs:extension base=\"xs:string\">\n" +
"        <xs:attribute type=\"xs:string\" name=\"sid\"/>\n" +
"      </xs:extension>\n" +
"    </xs:simpleContent>\n" +
"  </xs:complexType>\n" +
"  <xs:complexType name=\"authenticationType\">\n" +
"    <xs:sequence>\n" +
"      <xs:element type=\"xs:string\" name=\"domain\"/>\n" +
"      <xs:element type=\"xs:string\" name=\"serial\"/>\n" +
"    </xs:sequence>\n" +
"  </xs:complexType>\n" +
"  <xs:complexType name=\"parameterType\">\n" +
"    <xs:sequence>\n" +
"      <xs:element type=\"xs:string\" name=\"username\"/>\n" +
"    </xs:sequence>\n" +
"  </xs:complexType>\n" +
"  <xs:complexType name=\"secuotpType\">\n" +
"    <xs:sequence>\n" +
"      <xs:element type=\"serviceType\" name=\"service\"/>\n" +
"      <xs:element type=\"authenticationType\" name=\"authentication\"/>\n" +
"      <xs:element type=\"parameterType\" name=\"parameter\"/>\n" +
"    </xs:sequence>\n" +
"  </xs:complexType>\n" +
"</xs:schema>";
    public static final String GENERATE_OTP_100 = "Send OTP SMS Complete";
    public static final String GENERATE_OTP_300 = "Failed to Generate One-Time Password: Not Allowed to Generate One-Time Password or This site maybe Disabled";
    public static final String GENERATE_OTP_301_1 = "Failed to Generate One-Time Password: End-User Not Found";
    public static final String GENERATE_OTP_301_2 = "Failed to Generate One-Time Password: Site Not Found";
    public static final String GENERATE_OTP_302 = "Failed to Generate One-Time Password: End-User One-Time Password Mode Mismatch";
    public static final String GENERATE_OTP_401 = "Failed to Generate One-Time Password: Can't Send SMS to End-User";

    //==================================   AUTHENTICATE OTP   =========================================
    public static final String AUTHENTICATE_OTP_XSD = "<xs:schema attributeFormDefault=\"unqualified\" elementFormDefault=\"qualified\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n" +
"  <xs:element name=\"secuotp\">\n" +
"    <xs:complexType>\n" +
"      <xs:sequence>\n" +
"        <xs:element name=\"service\">\n" +
"          <xs:complexType>\n" +
"            <xs:simpleContent>\n" +
"              <xs:extension base=\"xs:string\">\n" +
"                <xs:attribute type=\"xs:string\" name=\"sid\"/>\n" +
"              </xs:extension>\n" +
"            </xs:simpleContent>\n" +
"          </xs:complexType>\n" +
"        </xs:element>\n" +
"        <xs:element name=\"authentication\">\n" +
"          <xs:complexType>\n" +
"            <xs:sequence>\n" +
"              <xs:element type=\"xs:string\" name=\"domain\"/>\n" +
"              <xs:element type=\"xs:string\" name=\"serial\"/>\n" +
"            </xs:sequence>\n" +
"          </xs:complexType>\n" +
"        </xs:element>\n" +
"        <xs:element name=\"parameter\">\n" +
"          <xs:complexType>\n" +
"            <xs:sequence>\n" +
"              <xs:element type=\"xs:string\" name=\"username\"/>\n" +
"              <xs:element type=\"xs:string\" name=\"password\"/>\n" +
"            </xs:sequence>\n" +
"          </xs:complexType>\n" +
"        </xs:element>\n" +
"      </xs:sequence>\n" +
"    </xs:complexType>\n" +
"  </xs:element>\n" +
"</xs:schema>";
    public static final String AUTHENTICATE_OTP_100 = "End-User Authication Passed";
    public static final String AUTHENTICATE_OTP_301_1 = "Failed to Authenticate One-Time Password: End-User Not Found";
    public static final String AUTHENTICATE_OTP_301_2 = "Failed to Authenticate One-Time Password: Site Not Found";
    public static final String AUTHENTICATE_OTP_303 = "End-User Authication Failed";

    //==================================   GET END-USER DATA   =========================================
    public static final String GET_END_USER_DATA_XSD = "<xs:schema attributeFormDefault=\"unqualified\" elementFormDefault=\"qualified\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n" +
"  <xs:element name=\"secuotp\">\n" +
"    <xs:complexType>\n" +
"      <xs:sequence>\n" +
"        <xs:element name=\"service\">\n" +
"          <xs:complexType>\n" +
"            <xs:simpleContent>\n" +
"              <xs:extension base=\"xs:string\">\n" +
"                <xs:attribute type=\"xs:string\" name=\"sid\"/>\n" +
"              </xs:extension>\n" +
"            </xs:simpleContent>\n" +
"          </xs:complexType>\n" +
"        </xs:element>\n" +
"        <xs:element name=\"authentication\">\n" +
"          <xs:complexType>\n" +
"            <xs:sequence>\n" +
"              <xs:element type=\"xs:string\" name=\"domain\"/>\n" +
"              <xs:element type=\"xs:string\" name=\"serial\"/>\n" +
"            </xs:sequence>\n" +
"          </xs:complexType>\n" +
"        </xs:element>\n" +
"        <xs:element name=\"parameter\">\n" +
"          <xs:complexType>\n" +
"            <xs:sequence>\n" +
"              <xs:element type=\"xs:string\" name=\"username\"/>\n" +
"              <xs:element type=\"enum\" name=\"type\" />\n" +
"            </xs:sequence>\n" +
"          </xs:complexType>\n" +
"        </xs:element>\n" +
"      </xs:sequence>\n" +
"    </xs:complexType>\n" +
"  </xs:element>\n" +
"\n" +
"  <xs:simpleType name=\"enum\">\n" +
"    <xs:restriction  base=\"xs:string\">\n" +
"                  <xs:enumeration value=\"full\" />\n" +
"                  <xs:enumeration value=\"default\" />\n" +
"                </xs:restriction >\n" +
"  </xs:simpleType>\n" +
"</xs:schema>";
    public static final String GET_END_USER_DATA_101 = "End-User Data";
    public static final String GET_END_USER_DATA_300 = "Failed to Get End-User: Not Allowed to Get End-User or This site maybe Disabled";
    public static final String GET_END_USER_DATA_301 = "Failed to Get End-User: End-User Not Found";
    
    //==================================   SET END-USER DATA   =========================================
    public static final String SET_END_USER_DATA_XSD = "<xs:schema attributeFormDefault=\"unqualified\" elementFormDefault=\"qualified\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n" +
"  <xs:element name=\"secuotp\">\n" +
"    <xs:complexType>\n" +
"      <xs:sequence>\n" +
"        <xs:element name=\"service\">\n" +
"          <xs:complexType>\n" +
"            <xs:simpleContent>\n" +
"              <xs:extension base=\"xs:string\">\n" +
"                <xs:attribute type=\"xs:string\" name=\"sid\"/>\n" +
"              </xs:extension>\n" +
"            </xs:simpleContent>\n" +
"          </xs:complexType>\n" +
"        </xs:element>\n" +
"        <xs:element name=\"authentication\">\n" +
"          <xs:complexType>\n" +
"            <xs:sequence>\n" +
"              <xs:element type=\"xs:string\" name=\"domain\"/>\n" +
"              <xs:element type=\"xs:string\" name=\"serial\"/>\n" +
"            </xs:sequence>\n" +
"          </xs:complexType>\n" +
"        </xs:element>\n" +
"        <xs:element name=\"parameter\">\n" +
"          <xs:complexType>\n" +
"            <xs:sequence>\n" +
"              <xs:element type=\"xs:string\" name=\"username\"/>\n" +
"              <xs:element name=\"change\" maxOccurs=\"unbounded\" minOccurs=\"1\">\n" +
"                <xs:complexType>\n" +
"                  <xs:sequence>\n" +
"                    <xs:element type=\"xs:string\" name=\"param\"/>\n" +
"                    <xs:element type=\"xs:string\" name=\"value\"/>\n" +
"                  </xs:sequence>\n" +
"                </xs:complexType>\n" +
"              </xs:element>\n" +
"            </xs:sequence>\n" +
"          </xs:complexType>\n" +
"        </xs:element>\n" +
"      </xs:sequence>\n" +
"    </xs:complexType>\n" +
"  </xs:element>\n" +
"</xs:schema>";
    public static final String SET_END_USER_DATA_100 = "Set End-User Data Success";
    public static final String SET_END_USER_DATA_202 = "Failed to Set End-User: Wrong Parameter";
    public static final String SET_END_USER_DATA_300 = "Failed to Set End-User: Not Allowed to Set End-User or This site maybe Disabled";
    public static final String SET_END_USER_DATA_301 = "Failed to Set End-User: End-User Not Found";
}
