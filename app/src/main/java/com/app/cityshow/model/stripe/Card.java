package com.app.cityshow.model.stripe;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Card implements Parcelable {

    private String id;
    private String object;
    @SerializedName("last4")
    private String number;
    private String cvc;
    private int exp_year;
    private int exp_month;
    private String name;

    private String address_city;
    private String address_country;
    private String address_line1;
    private String address_line1_check;
    private String address_line2;
    private String address_state;
    private String address_zip;
    private String address_zip_check;
    private String brand;
    private String country;
    private String customer;
    private String cvc_check;
    private String dynamic_last4;
    private String fingerprint;
    private String funding;
    private String tokenization_method;


    public Card() {
    }


    protected Card(Parcel in) {
        id = in.readString();
        object = in.readString();
        number = in.readString();
        cvc = in.readString();
        exp_year = in.readInt();
        exp_month = in.readInt();
        name = in.readString();
        address_city = in.readString();
        address_country = in.readString();
        address_line1 = in.readString();
        address_line1_check = in.readString();
        address_line2 = in.readString();
        address_state = in.readString();
        address_zip = in.readString();
        address_zip_check = in.readString();
        brand = in.readString();
        country = in.readString();
        customer = in.readString();
        cvc_check = in.readString();
        dynamic_last4 = in.readString();
        fingerprint = in.readString();
        funding = in.readString();
        tokenization_method = in.readString();
    }

    public static final Creator<Card> CREATOR = new Creator<Card>() {
        @Override
        public Card createFromParcel(Parcel in) {
            return new Card(in);
        }

        @Override
        public Card[] newArray(int size) {
            return new Card[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }

    public int getExp_year() {
        return exp_year;
    }

    public void setExp_year(int exp_year) {
        this.exp_year = exp_year;
    }

    public int getExp_month() {
        return exp_month;
    }

    public void setExp_month(int exp_month) {
        this.exp_month = exp_month;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public static Card from(com.stripe.android.model.Card mode) {
        if (mode == null) return null;
        Card card = new Card();
//        card.setNumber(mode.getNumber());
//        card.setCvc(mode.getCvc());
        card.setExp_month(mode.getExpMonth() == null ? 0 : mode.getExpMonth());
        card.setExp_year(mode.getExpYear() == null ? 0 : mode.getExpYear());
        return card;
    }

    public String getBrand() {
        if (brand == null) return "";
        return brand;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(object);
        dest.writeString(number);
        dest.writeString(cvc);
        dest.writeInt(exp_year);
        dest.writeInt(exp_month);
        dest.writeString(name);
        dest.writeString(address_city);
        dest.writeString(address_country);
        dest.writeString(address_line1);
        dest.writeString(address_line1_check);
        dest.writeString(address_line2);
        dest.writeString(address_state);
        dest.writeString(address_zip);
        dest.writeString(address_zip_check);
        dest.writeString(brand);
        dest.writeString(country);
        dest.writeString(customer);
        dest.writeString(cvc_check);
        dest.writeString(dynamic_last4);
        dest.writeString(fingerprint);
        dest.writeString(funding);
        dest.writeString(tokenization_method);
    }

    public boolean isDefault(String default_card_id) {
        return default_card_id.equals(id);
    }
}
