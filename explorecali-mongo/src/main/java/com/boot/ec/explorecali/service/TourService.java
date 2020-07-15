package com.boot.ec.explorecali.service;

import com.boot.ec.explorecali.entity.Tour;
import com.boot.ec.explorecali.entity.TourPackage;
import com.boot.ec.explorecali.repository.TourPackageRepository;
import com.boot.ec.explorecali.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

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
     * @param tourPackageName
     * @param details
     * @return
     */
    public Tour createTour(String title, String tourPackageName, Map<String,String> details)
    {
            TourPackage tourPackage = tourPackageRepository.findByName(tourPackageName)
                    .orElseThrow(()-> new RuntimeException("Tour Package does not exist"+tourPackageName));
            return tourRespository.save(new   Tour(title,tourPackage,details));

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
