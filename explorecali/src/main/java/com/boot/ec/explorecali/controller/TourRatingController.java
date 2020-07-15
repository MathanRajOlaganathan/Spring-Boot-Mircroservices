package com.boot.ec.explorecali.controller;

import com.boot.ec.explorecali.dto.RatingDto;
import com.boot.ec.explorecali.entity.Tour;
import com.boot.ec.explorecali.entity.TourRating;
import com.boot.ec.explorecali.entity.TourRatingPk;
import com.boot.ec.explorecali.repository.TourRatingRepository;
import com.boot.ec.explorecali.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/tours/{tourId}/ratings")
public class TourRatingController {
    private TourRepository tourRepository;
    private TourRatingRepository tourRatingRepository;

    @Autowired
    public TourRatingController(TourRepository tourRepository, TourRatingRepository tourRatingRepository) {
        this.tourRepository = tourRepository;
        this.tourRatingRepository = tourRatingRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTourRating(@PathVariable("tourId") int tourId, @Validated(RatingDto.ValidationStepTwo.class)@RequestBody RatingDto ratingDto)
    {
        Tour tour = verifyTour(tourId);
        tourRatingRepository.save(new TourRating(new TourRatingPk(tour,ratingDto.getCustomerId()),
               ratingDto.getScore(),ratingDto.getComment() ));
    }

//    @GetMapping
//    public List<RatingDto> getAllTourRatings(@PathVariable("tourId") int tourId)
//    {
//        verifyTour(tourId);
//        return tourRatingRepository.findByPkTourId(tourId).stream().map(RatingDto::new)
//                .collect(Collectors.toList());
//    }
@GetMapping
public Page<RatingDto> getAllTourRatings(@PathVariable("tourId") int tourId, Pageable pageable)
{
    verifyTour(tourId);
    Page<TourRating> ratings = tourRatingRepository.findByPkTourId(tourId, pageable);
   return new PageImpl<>(
           ratings.get().map(RatingDto::new).collect(Collectors.toList()),
           pageable,ratings.getTotalElements());
}

    @GetMapping(path="/average")
public Map<String,Double> getAverage(@PathVariable("tourId") int tourId)
{
    verifyTour(tourId);
    return Map.of("average",tourRatingRepository.findByPkTourId(tourId).stream()
        .mapToInt(TourRating::getScore).average()
        .orElseThrow(()-> new NoSuchElementException("Tour has no ratings")));

}

@PutMapping
public RatingDto updateRatingWithPut(@PathVariable("tourId") int tourId, @Validated@RequestBody RatingDto ratingDto)
{
    TourRating tourRating = verifyTourRating(tourId,ratingDto.getCustomerId());
    tourRating.setComment(ratingDto.getComment());
    tourRating.setScore(ratingDto.getScore());
    return new RatingDto(tourRatingRepository.save(tourRating));
}
@PatchMapping
    public RatingDto updateRatingWithPatch(@PathVariable("tourId") int tourId, @Validated@RequestBody RatingDto ratingDto)
    {
        TourRating tourRating = verifyTourRating(tourId,ratingDto.getCustomerId());
        if(null!=ratingDto.getComment()) {
            tourRating.setComment(ratingDto.getComment());
        }
        if(null!=ratingDto.getScore()) {
            tourRating.setScore(ratingDto.getScore());
        }
        return new RatingDto(tourRatingRepository.save(tourRating));
    }

    @DeleteMapping(path="/{customerId}")
    public void delete(@PathVariable("tourId") int tourId, @PathVariable("customerId") int customerId)
    {
        TourRating rating = verifyTourRating(tourId,customerId);
       tourRatingRepository.delete(rating);
    }


    private Tour verifyTour(int tourId) throws NoSuchElementException
    {
        return tourRepository.findById(tourId)
                .orElseThrow(()->
                    new NoSuchElementException("Tour " +tourId +" does not exist for the customer")
                );
    }

    private TourRating verifyTourRating(int tourId, int customerId) throws NoSuchElementException
    {
        return tourRatingRepository.findByPkTourIdAndPkCustomerId(tourId,customerId)
                .orElseThrow(()->
                        new NoSuchElementException("Tour-Rating does not exist for"+tourId +" and customer"+customerId)
                );
    }



    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public String return400(NoSuchElementException ex)
    {
        return ex.getMessage();
    }


}
