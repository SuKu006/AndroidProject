package com.basicapp.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Search {

@SerializedName("FullName")
@Expose
private String fullName;
@SerializedName("UserName")
@Expose
private String userName;
@SerializedName("Password")
@Expose
private String password;
@SerializedName("ConfirmPassword")
@Expose
private String confirmPassword;
@SerializedName("Email")
@Expose
private String email;

public String getFullName() {
return fullName;
}

public void setFullName(String fullName) {
this.fullName = fullName;
}

public String getUserName() {
return userName;
}

public void setUserName(String userName) {
this.userName = userName;
}

public String getPassword() {
return password;
}

public void setPassword(String password) {
this.password = password;
}

public String getConfirmPassword() {
return confirmPassword;
}

public void setConfirmPassword(String confirmPassword) {
this.confirmPassword = confirmPassword;
}

public String getEmail() {
return email;
}

public void setEmail(String email) {
this.email = email;
}

}