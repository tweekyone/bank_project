package com.epam.bank.atm.service;

import com.epam.bank.atm.entity.Activity;
import com.epam.bank.atm.repo.ActivityRepo;

public class ActivityService {

    private final ActivityRepo activityRepo;

    public ActivityService(ActivityRepo activityRepo) {
        this.activityRepo = activityRepo;
    }

    public Activity recUserActivity(Activity activity) {
        return activityRepo.createActivity(activity);
    }

    public Activity getUserActivityById(long userId) {
        return activityRepo.findActivityByUser(userId);
    }

    public long deleteActivity(long activityId) {
        return activityRepo.deleteActivity(activityId);
    }
}
