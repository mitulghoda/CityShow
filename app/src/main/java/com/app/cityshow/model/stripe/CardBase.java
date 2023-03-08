package com.app.cityshow.model.stripe;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CardBase {
    @SerializedName("object")
    private String type;
    @SerializedName("data")
    private List<Card> cards;
    private String url;
    private boolean has_more;

    public List<Card> getCards() {
        return cards;
    }
}
