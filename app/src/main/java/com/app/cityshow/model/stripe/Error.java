package com.app.cityshow.model.stripe;

import java.util.List;

public class Error {
    private static final String STRIPE_ERROR = "StripeInvalidRequestError";

    private String requestId;
    private String code;
    private String type;
    private String doc_url;
    private String message;
    private int statusCode;
    private Payment_intent payment_intent;

    public String getRequestId() {
        return requestId;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getType() {
        return type;
    }

    public Payment_intent getPayment_intent() {
        return payment_intent;
    }

    public String getMessage() {
        return message;
    }

    public String getDoc_url() {
        return doc_url;
    }

    public String getCode() {
        return code;
    }

    public static class Payment_intent {
        private String status;
        private List<String> payment_method_types;
        private Payment_method_options payment_method_options;
        private String payment_method;
        private String on_behalf_of;
        private Metadata metadata;
        private boolean livemode;
        private String description;
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

        public String getStatus() {
            return status;
        }

        public List<String> getPayment_method_types() {
            return payment_method_types;
        }

        public Payment_method_options getPayment_method_options() {
            return payment_method_options;
        }

        public String getPayment_method() {
            return payment_method;
        }

        public String getOn_behalf_of() {
            return on_behalf_of;
        }

        public Metadata getMetadata() {
            return metadata;
        }

        public boolean getLivemode() {
            return livemode;
        }

        public String getDescription() {
            return description;
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
        private List<Data> data;
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

        public List<Data> getData() {
            return data;
        }

        public String getObject() {
            return object;
        }
    }

    public static class Data {
        private String status;
        private Refunds refunds;
        private boolean refunded;
        private String receipt_url;
        private String receipt_email;
        private Payment_method_details payment_method_details;
        private String payment_method;
        private String payment_intent;
        private boolean paid;
        private Outcome outcome;
        private String on_behalf_of;
        private Metadata metadata;
        private boolean livemode;
        private Fraud_details fraud_details;
        private boolean disputed;
        private String description;
        private String customer;
        private String currency;
        private int created;
        private boolean captured;
        private String calculated_statement_descriptor;
        private Billing_details billing_details;
        private String balance_transaction;
        private int amount_refunded;
        private int amount;
        private String object;
        private String id;

        public String getStatus() {
            return status;
        }

        public Refunds getRefunds() {
            return refunds;
        }

        public boolean getRefunded() {
            return refunded;
        }

        public String getReceipt_url() {
            return receipt_url;
        }

        public String getReceipt_email() {
            return receipt_email;
        }

        public Payment_method_details getPayment_method_details() {
            return payment_method_details;
        }

        public String getPayment_method() {
            return payment_method;
        }

        public String getPayment_intent() {
            return payment_intent;
        }

        public boolean getPaid() {
            return paid;
        }

        public Outcome getOutcome() {
            return outcome;
        }

        public String getOn_behalf_of() {
            return on_behalf_of;
        }

        public Metadata getMetadata() {
            return metadata;
        }

        public boolean getLivemode() {
            return livemode;
        }

        public Fraud_details getFraud_details() {
            return fraud_details;
        }

        public boolean getDisputed() {
            return disputed;
        }

        public String getDescription() {
            return description;
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

        public boolean getCaptured() {
            return captured;
        }

        public String getCalculated_statement_descriptor() {
            return calculated_statement_descriptor;
        }

        public Billing_details getBilling_details() {
            return billing_details;
        }

        public String getBalance_transaction() {
            return balance_transaction;
        }

        public int getAmount_refunded() {
            return amount_refunded;
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
    }

    public static class Refunds {
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

    public static class Payment_method_details {
        private String type;
        private Card card;

        public String getType() {
            return type;
        }

        public Card getCard() {
            return card;
        }
    }


    public static class Checks {
        private String address_postal_code_check;

        public String getAddress_postal_code_check() {
            return address_postal_code_check;
        }
    }

    public static class Outcome {
        private String type;
        private String seller_message;
        private int risk_score;
        private String risk_level;
        private String network_status;

        public String getType() {
            return type;
        }

        public String getSeller_message() {
            return seller_message;
        }

        public int getRisk_score() {
            return risk_score;
        }

        public String getRisk_level() {
            return risk_level;
        }

        public String getNetwork_status() {
            return network_status;
        }
    }

    public static class Fraud_details {
    }

    public static class Billing_details {
        private Address address;

        public Address getAddress() {
            return address;
        }
    }

    public static class Address {
        private String postal_code;

        public String getPostal_code() {
            return postal_code;
        }
    }
}
