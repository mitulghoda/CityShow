package com.app.cityshow.payment;

import com.google.gson.Gson;

import java.util.List;

public class StripePaymentIntent {
    private String status;
    private String source;
    private List<String> payment_method_types;
    private Payment_method_options payment_method_options;
    private Metadata metadata;
    private boolean livemode;
    private String customer;
    private String currency;
    private int created;
    private String confirmation_method;
    private String client_secret;
    private Charges charges;
    private String capture_method;
    private int amount_received;
    private int amount_capturable;
    private int amount;
    private String object;
    private String id;
    private String stripe_account_id;

    public static StripePaymentIntent create(String raw) {
        return new Gson().fromJson(raw, StripePaymentIntent.class);
    }

    public String getStatus() {
        return status;
    }

    public String getSource() {
        return source;
    }

    public List<String> getPayment_method_types() {
        return payment_method_types;
    }

    public Payment_method_options getPayment_method_options() {
        return payment_method_options;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public boolean getLivemode() {
        return livemode;
    }

    public String getCustomer() {
        return customer;
    }

    public String getCurrency() {
        return currency;
    }

    public int getCreated() {
        return created;
    }

    public String getConfirmation_method() {
        return confirmation_method;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public Charges getCharges() {
        return charges;
    }

    public String getCapture_method() {
        return capture_method;
    }

    public int getAmount_received() {
        return amount_received;
    }

    public int getAmount_capturable() {
        return amount_capturable;
    }

    public int getAmount() {
        return amount;
    }

    public String getObject() {
        return object;
    }

    public String getId() {
        return id;
    }

    public String getStripe_account_id() {
        return stripe_account_id;
    }

    public static class Payment_method_options {
        private Card card;

        public Card getCard() {
            return card;
        }
    }

    public static class Card {
        private String request_three_d_secure;

        public String getRequest_three_d_secure() {
            return request_three_d_secure;
        }
    }

    public static class Metadata {
    }

    public static class Charges {
        private String url;
        private int total_count;
        private boolean has_more;
        private List<String> data;
        private String object;

        public String getUrl() {
            return url;
        }

        public int getTotal_count() {
            return total_count;
        }

        public boolean getHas_more() {
            return has_more;
        }

        public List<String> getData() {
            return data;
        }

        public String getObject() {
            return object;
        }
    }
}
