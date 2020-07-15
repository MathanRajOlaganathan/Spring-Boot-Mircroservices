package com.boot.ec.explorecali.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;
import java.util.Objects;

/**
 * The Tour contains all attributes of an Explore California Tour.
 *
 * Created by Mary Ellen Bowman
 */
@Document
public class Tour {

    @Id
    private String id;

    @Indexed
    private String title;

    @Indexed
   private String tourPackageCode;

    private String tourPackageName;

    private Map<String,String> details;

    public Tour(String title, TourPackage tourPackage, Map<String, String> details) {
        this.title = title;
        this.tourPackageCode = tourPackage.getCode();
        this.tourPackageName = tourPackage.getName();
        this.details = details;
    }

    public Tour() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTourPackageCode() {
        return tourPackageCode;
    }

    public void setTourPackageCode(String tourPackageCode) {
        this.tourPackageCode = tourPackageCode;
    }

    public String getTourPackageName() {
        return tourPackageName;
    }

    public void setTourPackageName(String tourPackageName) {
        this.tourPackageName = tourPackageName;
    }

    public Map<String, String> getDetails() {
        return details;
    }

    public void setDetails(Map<String, String> details) {
        this.details = details;
    }
}
