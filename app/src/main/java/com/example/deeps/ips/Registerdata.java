package com.example.deeps.ips;

public class Registerdata {

        int _id;
        String user_name;
        String email_id;
        String user_id;
        String mobile_number;
public String getPassword() {
        return password;
        }
        public void setPassword(String password) {
        this.password = password;
        }

        String password;

public Registerdata(){

        }
public Registerdata(int id, String user_name, String  email_id,String user_id,String phone_number,String mobile_number){
        this._id = id;
        this.user_name = user_name;
        this.email_id = email_id;
        this.user_id=user_id;
        this.mobile_number=mobile_number;
        }


        public int getID(){
        return this._id;
        }

        public void setID(int id){
        this. _id = id;
        }

public String getuserName() {
        // TODO Auto-generated method stub
        return user_name;
        }

        public void setuseName(String user_name){
        this.user_name =user_name;
        }
public String getEmailId() {
        // TODO Auto-generated method stub
        return email_id;
        }

        public void setEmailId(String email_id){
        this.email_id =email_id;
        }
public String getUserId() {
        // TODO Auto-generated method stub
        return user_id;
        }
        public void setUserId(String user_id){
        this.user_id =user_id;
        }
public String getMobNo() {
        // TODO Auto-generated method stub
        return mobile_number;
        }

        public void setMobNo(String mobile_number){
        this.mobile_number=mobile_number;
        }
        }