package com.boot.ec.explorecali.service;

import com.boot.ec.explorecali.entity.TourPackage;
import com.boot.ec.explorecali.repository.TourPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TourPackageService {

    private TourPackageRepository tourPackageRepository;

    @Autowired
    public TourPackageService(TourPackageRepository tourPackageRepository) {
        this.tourPackageRepository = tourPackageRepository;
    }

    /**
     *
     * @param code
     * @param name
     * @return new or existing new tour package
     */
    public TourPackage createTourPackage(String code, String name)
    {
        return tourPackageRepository.findById(code)
                .orElse(tourPackageRepository.save(new TourPackage(code,name)));
    }


    /**
     *
     * @return all tour packages
     */
    public Iterable<TourPackage> lookup()
    {
        return tourPackageRepository.findAll();
    }

    /**
     *
     * @return count
     */
    public long total()
    {
        return tourPackageRepository.count();
    }
}
