package com.xitij.adzap.model;

/**
 * Created by Krishna on 19-04-2015.
 */
import com.google.gson.annotations.SerializedName;
public class User {

    @SerializedName("Balance")
    public String Balance;

    @SerializedName("EmailId")
    public String EmailId;

    @SerializedName("Image")
    public String Image;

    @SerializedName("Name")
    public String Name;

    @SerializedName("Password")
    public String Password;

    @SerializedName("Phone")
    public String Phone;

    @SerializedName("verifyCode")
    public String verifyCode;


    @SerializedName("Gender")
    public String Gender;


    @SerializedName("Birthdate")
    public String Birthdate;


    @SerializedName("UserId")
    public long UserId;

    @SerializedName("ReferanceCode")
    public String ReferanceCode;

    @SerializedName("Stateid")
    public long Stateid;

    @SerializedName("CityId")
    public long CityId;

}
