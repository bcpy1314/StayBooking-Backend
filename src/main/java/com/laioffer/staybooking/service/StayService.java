package com.laioffer.staybooking.service;

import com.laioffer.staybooking.entity.*;
import com.laioffer.staybooking.exception.StayDeleteException;
import com.laioffer.staybooking.repository.LocationRepository;
import com.laioffer.staybooking.repository.ReservationRepository;
import com.laioffer.staybooking.repository.StayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StayService {
    private StayRepository stayRepository;
    private ImageStorageService imageStorageService;
    private ReservationRepository reservationRepository;
    private LocationRepository locationRepository;
    private GeoEncodingService geoEncodingService;

    @Autowired
    public StayService(StayRepository stayRepository, ImageStorageService imageStorageService,ReservationRepository reservationRepository, LocationRepository locationRepository, GeoEncodingService geoEncodingService) {
        this.stayRepository = stayRepository;
        this.imageStorageService = imageStorageService;
        this.reservationRepository = reservationRepository;
        this.locationRepository = locationRepository;
        this.geoEncodingService = geoEncodingService;
    }

    public Stay findById(Long stayId){
        return stayRepository.findById(stayId).orElse(null);
    }


    public List<Stay> findByHost(String username){
        return stayRepository.findByHost(new User.Builder().setUsername(username).build());
    }

    public void upload(Stay stay, MultipartFile[] images) {
        LocalDate date = LocalDate.now().plusDays(1);
        List<StayAvailability> availabilities = new ArrayList<>();
        for (int i = 0; i < 30; ++i) {
            availabilities.add(
                    new StayAvailability.Builder()
                            .setId(new StayAvailabilityKey(stay.getId(), date))
                            .setStay(stay)
                            .setState(StayAvailabilityState.AVAILABLE).build());

            date = date.plusDays(1);
        }
        stay.setAvailabilities(availabilities);
        // add urls
        List<String> mediaLinks = Arrays.stream(images).parallel().map(image -> imageStorageService.save(image)).collect(Collectors.toList());
        List<StayImage> stayImages = new ArrayList<>();
        for (String mediaLink : mediaLinks) {
            stayImages.add(new StayImage(mediaLink, stay));
        }
        stay.setImages(stayImages);
        stayRepository.save(stay);
        Location location = geoEncodingService.getLatLng(stay.getId(), stay.getAddress());
        locationRepository.save(location);
    }
    public void deleteById(Long stayId) throws StayDeleteException {
        List<Reservation> reservations = reservationRepository.findByStayAndCheckoutDateAfter(new Stay.Builder().setId(stayId).build(), LocalDate.now());
        if (reservations != null && reservations.size() > 0) {
            throw new StayDeleteException("Cannot delete stay with active reservation");
        }
    }

}
