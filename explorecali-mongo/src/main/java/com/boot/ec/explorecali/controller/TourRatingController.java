package com.boot.ec.explorecali.controller;

import com.boot.ec.explorecali.entity.TourRating;
import com.boot.ec.explorecali.repository.TourRatingRepository;
import com.boot.ec.explorecali.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.NoSuchElementException;

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
    public void createTourRating(@PathVariable("tourId") String tourId,
                                 @Validated@RequestBody TourRating tourRating)
    {
        verifyTour(tourId);
        tourRatingRepository.save(new TourRating(tourId,tourRating.getCustomerId(),
                tourRating.getScore(),tourRating.getComment() ));
    }

//    @GetMapping
//    public List<RatingDto> getAllTourRatings(@PathVariable("tourId") int tourId)
//    {
//        verifyTour(tourId);
//        return tourRatingRepository.findByPkTourId(tourId).stream().map(RatingDto::new)
//                .collect(Collectors.toList());
//    }
@GetMapping
public Page<TourRating> getAllTourRatings(@PathVariable("tourId") String tourId, Pageable pageable)
{
    verifyTour(tourId);
    return tourRatingRepository.findByTourId(tourId, pageable);
}

    @GetMapping(path="/average")
public Map<String,Double> getAverage(@PathVariable("tourId") String tourId)
{
    verifyTour(tourId);
    return Map.of("average",tourRatingRepository.findByTourId(tourId).stream()
        .mapToInt(TourRating::getScore).average()
        .orElseThrow(()-> new NoSuchElementException("Tour has no ratings")));

}

@PutMapping
public TourRating updateRatingWithPut(@PathVariable("tourId") String tourId, @Validated@RequestBody TourRating rating)
{
    TourRating tourRating = verifyTourRating(tourId,rating.getCustomerId());
    tourRating.setComment(rating.getComment());
    tourRating.setScore(rating.getScore());
    return tourRatingRepository.save(tourRating);
}
@PatchMapping
    public TourRating updateRatingWithPatch(@PathVariable("tourId") String tourId, @Validated@RequestBody TourRating rating)
    {
        TourRating tourRating = verifyTourRating(tourId,rating.getCustomerId());
        if(null!=rating.getComment()) {
            tourRating.setComment(rating.getComment());
        }
        if(null!=rating.getScore()) {
            tourRating.setScore(rating.getScore());
        }
        return tourRatingRepository.save(tourRating);
    }

    @DeleteMapping(path="/{customerId}")
    public void delete(@PathVariable("tourId") String tourId, @PathVariable("customerId") int customerId)
    {
        TourRating rating = verifyTourRating(tourId,customerId);
       tourRatingRepository.delete(rating);
    }


    private void verifyTour(String tourId) throws NoSuchElementException
    {
        if (!tourRepository.existsById(tourId)) {
            throw new NoSuchElementException("Tour does not exist " + tourId);
        }

    }

    private TourRating verifyTourRating(String tourId, int customerId) throws NoSuchElementException
    {
        return tourRatingRepository.findByTourIdAndCustomerId(tourId,customerId)
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
