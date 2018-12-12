package be.ucll.da.recommendation.controllers;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class RecommendedItem {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private UUID id;
    private String userId;
    private UUID ratedItem;
    private float rating;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UUID getRatedItem() {
        return ratedItem;
    }

    public void setRatedItem(UUID ratedItem) {
        this.ratedItem = ratedItem;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
