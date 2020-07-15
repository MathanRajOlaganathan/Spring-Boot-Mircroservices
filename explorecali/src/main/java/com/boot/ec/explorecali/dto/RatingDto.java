package com.boot.ec.explorecali.dto;
import com.boot.ec.explorecali.entity.TourRating;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RatingDto  {

    public interface ValidationStepOne
    {

    }

    public interface ValidationStepTwo
    {

    }
    @Min(0)
    @Max(value = 5,groups = {ValidationStepTwo.class})
    private Integer score;
    @Size(max=255,groups = {ValidationStepOne.class})
    private String comment;
    @NotNull(groups = {ValidationStepTwo.class})
    @Min(value = 2,groups = {ValidationStepTwo.class})
    @Max(value = 6,groups = {ValidationStepTwo.class})
    private Integer customerId;

    /**
     * Construct a RatingDto from a fully instantiated TourRating.
     *
     * @param tourRating Tour Rating Object
     */
    public RatingDto(TourRating tourRating) {
        this(tourRating.getScore(), tourRating.getComment(), tourRating.getPk().getCustomerId());
    }

    public RatingDto(@Min(0) @Max(value = 5, groups = {ValidationStepTwo.class}) Integer score, @Size(max = 255, groups = {ValidationStepOne.class}) String comment, @NotNull(groups = {ValidationStepTwo.class}) @Max(value = 6, groups = {ValidationStepTwo.class}) Integer customerId) {
        this.score = score;
        this.comment = comment;
        this.customerId = customerId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
}
