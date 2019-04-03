package com.example.deeps.ips;

public class SpinnerGet {

    int _id;
    String _class;
    Double _lat;
    Double _lng;
    String _uuid;

    /*public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    String password;
*/
    public SpinnerGet(){

    }
    public SpinnerGet(int id, String _class, Double  _lat,Double _lng,String _uuid){
        this._id = id;
        this._class = _class;
        this._lat = _lat;
        this._lng=_lng;
        this._uuid=_uuid;
    }


    public int getID(){
        return this._id;
    }

    public void setID(int id){
        this. _id = id;
    }

    public String get_class() {
        // TODO Auto-generated method stub
        return _class;
    }

    public void set_class(String _class){
        this._class =_class;
    }
    public Double get_lat() {
        // TODO Auto-generated method stub
        return _lat;
    }

    public void set_lat(Double _lat){
        this._lat =_lat;
    }
    public Double get_lng() {
        // TODO Auto-generated method stub
        return _lng;
    }
    public void set_lng(Double _lng){
        this._lng =_lng;
    }
    public String get_uuid() {
        // TODO Auto-generated method stub
        return _uuid;
    }

    public void set_uuid(String _uuid){
        this._uuid=_uuid;
    }
}