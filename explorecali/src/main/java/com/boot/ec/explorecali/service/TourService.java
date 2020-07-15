package com.boot.ec.explorecali.service;

import com.boot.ec.explorecali.entity.Difficulty;
import com.boot.ec.explorecali.entity.Region;
import com.boot.ec.explorecali.entity.Tour;
import com.boot.ec.explorecali.entity.TourPackage;
import com.boot.ec.explorecali.repository.TourPackageRepository;
import com.boot.ec.explorecali.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TourService {

    private TourRepository tourRespository;

    private TourPackageRepository tourPackageRepository;

    @Autowired
    public TourService(TourRepository tourRepository, TourPackageRepository tourPackageRepository) {
        this.tourRespository = tourRepository;
        this.tourPackageRepository = tourPackageRepository;
    }

    /**
     *
     * @param title
     * @param description
     * @param blurb
     * @param price
     * @param duration
     * @param bullets
     * @param keywords
     * @param tourPackageName
     * @param difficulty
     * @param region
     * @return Tour entity
     */
    public Tour createTour(String title, String description, String blurb, Integer price, String duration, String bullets,
                          String keywords, String tourPackageName, Difficulty difficulty, Region region)
    {
            TourPackage tourPackage = tourPackageRepository.findByName(tourPackageName)
                    .orElseThrow(()-> new RuntimeException("Tour Package does not exist"+tourPackageName));
            return tourRespository.save(new   Tour(title,description,blurb,price,
            duration,bullets,keywords,tourPackage,difficulty,region));

    }


    /**
     *
     * @return count
     */
    public long total()
    {
        return tourRespository.count();
    }
}
