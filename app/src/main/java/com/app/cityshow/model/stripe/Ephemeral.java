package com.app.cityshow.model.stripe;

import java.util.List;

public class Ephemeral {

    private String id;
    private String secret;
    private boolean livemode;
    private int expires;
    private int created;
    private String object;
    private List<Associated_objects> associated_objects;

    public String getSecret() {
        return secret;
    }

    public boolean getLivemode() {
        return livemode;
    }

    public int getExpires() {
        return expires;
    }

    public int getCreated() {
        return created;
    }

    public String getObject() {
        return object;
    }

    public String getId() {
        return id;
    }

    public List<Associated_objects> getAssociated_objects() {
        return associated_objects;
    }

    public static class Associated_objects {
        private String id;
        private String type;

        public String getId() {
            return id;
        }

        public String getType() {
            return type;
        }
    }
}
