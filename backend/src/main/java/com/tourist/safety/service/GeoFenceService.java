package com.tourist.safety.service;

import com.tourist.safety.entity.Restricted_zone;
import com.tourist.safety.repository.RestrictedZoneRepo;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.distance.DistanceUtils;
import org.locationtech.spatial4j.shape.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeoFenceService {

    @Autowired
    private RestrictedZoneRepo restrictedZoneRepo;

    public Restricted_zone addRestricted_zone(Restricted_zone restricted_zone) {
        return restrictedZoneRepo.save(restricted_zone);
    }

    public boolean Check_transpass(Restricted_zone restricted_zone,Double latitude, Double longitude){
        SpatialContext context = SpatialContext.GEO;
        Point tourist_location = context.makePoint(restricted_zone.getLongitude(), restricted_zone.getLatitude());
        double dist = context.calcDistance(tourist_location, longitude, latitude) * DistanceUtils.DEG_TO_KM*1000;
        if(dist <= 100){
            return true;
        }else{
            return false;
        }
    }

}
