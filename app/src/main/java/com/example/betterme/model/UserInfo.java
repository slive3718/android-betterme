package com.example.betterme.model;

import android.text.TextUtils;

import org.json.JSONObject;

import java.io.Serializable;

import static com.example.betterme.APIContants.USER_IMAGE_BASE_URL;

public class UserInfo implements Serializable {
    String id;
    String username;
    String password;
    String email;
    String account_type;
    String date_created;
    String first_name;
    String middle_name;
    String last_name;
    String dob;
    String age;
    String sex;
    String city;
    String province;
    String contact;
    String weight;
    String height;
    String user_picture_status;
    String disabled;

    public UserInfo() {

    }

    public UserInfo(JSONObject jsonObject) {
        this.id = jsonObject.optString("userId");
        this.username = jsonObject.optString("username");
        this.password = jsonObject.optString("password");
        this.email = jsonObject.optString("email");
        this.account_type = jsonObject.optString("account_type");
        this.date_created = jsonObject.optString("date_created");
        this.first_name = jsonObject.optString("first_name");
        this.middle_name = jsonObject.optString("middle_name");
        this.last_name = jsonObject.optString("last_name");
        this.dob = jsonObject.optString("dob");
        this.age = jsonObject.optString("age");
        this.sex = jsonObject.optString("sex");
        this.city = jsonObject.optString("city");
        this.province = jsonObject.optString("provice");
        this.contact = jsonObject.optString("contact");
        this.weight = jsonObject.optString("weight");
        this.height = jsonObject.optString("height");
        this.user_picture_status = jsonObject.optString("user_picture_status");
        this.disabled = jsonObject.optString("disabled");
    }

    public UserInfo(String id, String username, String password, String email, String account_type, String date_created, String first_name, String middle_name, String last_name, String dob, String age, String sex, String city, String province, String contact, String weight, String height, String user_picture_status, String disabled) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.account_type = account_type;
        this.date_created = date_created;
        this.first_name = first_name;
        this.middle_name = middle_name;
        this.last_name = last_name;
        this.dob = dob;
        this.age = age;
        this.sex = sex;
        this.city = city;
        this.province = province;
        this.contact = contact;
        this.weight = weight;
        this.height = height;
        this.user_picture_status = user_picture_status;
        this.disabled = disabled;
    }

    public String getImageURL() {
        return USER_IMAGE_BASE_URL + id + ".jpg";
    }

    public String getFullName() {
        String fullName =  first_name + " " + last_name;
        if(TextUtils.isEmpty(fullName)){
            fullName = "NO NAME";
        }
        return fullName;
    }

    public String getFullNameM() {
        return first_name + " " + middle_name + " " + last_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getUser_picture_status() {
        return user_picture_status;
    }

    public void setUser_picture_status(String user_picture_status) {
        this.user_picture_status = user_picture_status;
    }

    public String getDisabled() {
        return disabled;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }
}
