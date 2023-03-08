package com.app.cityshow.model.stripe;

import android.os.Parcel;
import android.os.Parcelable;

import com.app.cityshow.utility.TextUtil;

public class Account implements Parcelable {
    private String id;
    private String email;
    private String type;
    private Settings settings;
    private boolean details_submitted;
    private String default_currency;
    private String country;
    public String stripe_publishable_key;
    public String stripe_customer_code;
    private boolean charges_enabled;
    private Capabilities capabilities;
    private Business_profile business_profile;
    private String object;
    private boolean payouts_enabled;

    protected Account(Parcel in) {
        id = in.readString();
        email = in.readString();
        type = in.readString();
        details_submitted = in.readByte() != 0;
        default_currency = in.readString();
        country = in.readString();
        stripe_publishable_key = in.readString();
        stripe_customer_code = in.readString();
        charges_enabled = in.readByte() != 0;
        object = in.readString();
        payouts_enabled = in.readByte() != 0;
    }

    public static final Creator<Account> CREATOR = new Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel in) {
            return new Account(in);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };

    public String getType() {
        return type;
    }

    public Settings getSettings() {
        return settings;
    }

    public boolean getPayouts_enabled() {
        return payouts_enabled;
    }

    public String getEmail() {
        if (TextUtil.isNullOrEmpty(email)) return "Not Specified";
        return email;
    }

    public boolean getDetails_submitted() {
        return details_submitted;
    }

    public String getDefault_currency() {
        return default_currency;
    }

    public String getCountry() {
        return country;
    }

    public boolean getCharges_enabled() {
        return charges_enabled;
    }

    public Capabilities getCapabilities() {
        return capabilities;
    }

    public Business_profile getBusiness_profile() {
        return business_profile;
    }

    public String getObject() {
        return object;
    }

    public String getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(email);
        dest.writeString(type);
        dest.writeByte((byte) (details_submitted ? 1 : 0));
        dest.writeString(default_currency);
        dest.writeString(country);
        dest.writeString(stripe_publishable_key);
        dest.writeString(stripe_customer_code);
        dest.writeByte((byte) (charges_enabled ? 1 : 0));
        dest.writeString(object);
        dest.writeByte((byte) (payouts_enabled ? 1 : 0));
    }

    public static class Settings {
        private Payments payments;
        private Dashboard dashboard;
        private Card_payments card_payments;
        private Branding branding;

        public Payments getPayments() {
            return payments;
        }

        public Dashboard getDashboard() {
            return dashboard;
        }

        public Card_payments getCard_payments() {
            return card_payments;
        }

        public Branding getBranding() {
            return branding;
        }
    }

    public static class Payments {
        private String statement_descriptor;

        public String getStatement_descriptor() {
            return statement_descriptor;
        }
    }

    public static class Dashboard {
        private String timezone;
        private String display_name;

        public String getTimezone() {
            return timezone;
        }

        public String getDisplay_name() {
            return display_name;
        }
    }

    public static class Card_payments {
    }

    public static class Branding {
    }

    public static class Capabilities {
        private String legacy_payments;

        public String getLegacy_payments() {
            return legacy_payments;
        }
    }

    public static class Business_profile {
        private String url;
        private String support_url;
        private String support_phone;
        private String support_email;
        private Support_address support_address;
        private String name;
        private String mcc;

        public String getUrl() {
            return url;
        }

        public String getSupport_url() {
            return support_url;
        }

        public String getSupport_phone() {
            return support_phone;
        }

        public String getSupport_email() {
            return support_email;
        }

        public Support_address getSupport_address() {
            return support_address;
        }

        public String getName() {
            return name;
        }

        public String getMcc() {
            return mcc;
        }
    }

    public static class Support_address {
    }
}
