package com.example.smartdispatch;

import com.google.firebase.firestore.GeoPoint;

public class Request
{

    private String requester;
    private String typeofemergency;
    private int scaleofemergency;
    private GeoPoint location;
    private String vehicleid;
    private String hospitalid;

    public Request()
    {

    }

    public Request(String requester, String typeofemergency, int scaleofemergency, GeoPoint location, String vehicleid, String hospitalid) {
        this.requester=requester;
        this.typeofemergency = typeofemergency;
        this.scaleofemergency = scaleofemergency;
        this.location = location;
        this.vehicleid = vehicleid;
        this.hospitalid=hospitalid;
    }

    public String getTypeofemergency() {
        return typeofemergency;
    }

    public int getScaleofemergency() {
        return scaleofemergency;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public String getVehicleid() {
        return vehicleid;
    }

    public String getRequester() {
        return requester;
    }

    public String getHospitalid() {
        return hospitalid;
    }
}
