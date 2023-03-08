package com.app.cityshow.model.stripe;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Customer {
    private Tax tax_ids;
    private String tax_exempt;
    private Subscription subscriptions;
    private CardBase sources;
    private List<String> preferred_locales;
    private int next_invoice_sequence;
    private String name;
    private Metadata metadata;
    private boolean livemode;
    private Invoice_settings invoice_settings;
    private String invoice_prefix;
    private String email;
    private boolean delinquent;
    private String default_source;
    private int created;
    private int balance;
    private int account_balance;
    private String object;
    private String id;

    public Tax getTax_ids() {
        return tax_ids;
    }

    public String getTax_exempt() {
        return tax_exempt;
    }

    public Subscription getSubscriptions() {
        return subscriptions;
    }

    public List<String> getPreferred_locales() {
        return preferred_locales;
    }

    public int getNext_invoice_sequence() {
        return next_invoice_sequence;
    }

    public String getName() {
        return name;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public boolean getLivemode() {
        return livemode;
    }

    public Invoice_settings getInvoice_settings() {
        return invoice_settings;
    }

    public String getInvoice_prefix() {
        return invoice_prefix;
    }

    public String getEmail() {
        return email;
    }

    public boolean getDelinquent() {
        return delinquent;
    }

    public String getDefault_source() {
        return default_source;
    }

    public int getCreated() {
        return created;
    }

    public int getBalance() {
        return balance;
    }

    public int getAccount_balance() {
        return account_balance;
    }

    public String getObject() {
        return object;
    }

    public String getId() {
        return id;
    }

    public List<Card> getCards() {
        CardBase source = getSource();
        if (source == null) return null;
        return source.getCards();
    }

    public CardBase getSource() {
        if (sources == null) return null;
        if (sources.getCards() == null) return null;
        List<Card> cards = sources.getCards();
        for (Card card : cards) {
            if (card.getId().equalsIgnoreCase(default_source)) break;
        }
        return sources;
    }

    public static class Tax {
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

    public static class Subscription {
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

    public static class Source {
        private String url;
        private int total_count;
        private boolean has_more;
        @SerializedName("data")
        private List<Card> data;
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

        public List<Card> getData() {
            return data;
        }

        public String getObject() {
            return object;
        }
    }


    public static class Metadata {
    }

    public static class Invoice_settings {
    }
}
