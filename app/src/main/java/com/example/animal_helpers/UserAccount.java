package com.example.animal_helpers;


public class UserAccount {

    private String Uid;                     // firebase Uid (고유 토큰정보)
    private String name;                    // 사용자명
    private String emailId;                 // 이메일
    private String password;                // 비밀번호
    private String organizationName;        // 기관명
    private String address;                 // 주소
    private String tel;                     // 전화번호

    public UserAccount() {
    }

    public UserAccount(String Uid, String name, String emailId, String password, String organizationName, String address, String tel) {
        this.Uid = Uid;
        this.name = name;
        this.emailId = emailId;
        this.password = password;
        this.organizationName = organizationName;
        this.address = address;
        this.tel = tel;
    }

    public String getIdToken() {
        return Uid;
    }
    public void setIdToken(String idToken) {
        this.Uid = idToken;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmailId() { return emailId; }
    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getOrganizationName() { return organizationName; }
    public void setOrganizationName(String organizationName) { this.organizationName = organizationName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getTel() { return tel; }
    public void setTel(String tel) { this.tel = tel; }

}
