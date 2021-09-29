package com.epam.bank.atm.repo;

import com.epam.bank.atm.entity.Activity;
import com.epam.bank.atm.entity.User;
import java.util.List;

/* CRUD user activities repo
 */
public class ActivityRepo {

    public Activity createActivity(Activity activity) {
        return null;
    }

    public Activity updateActivity(Activity activity) {
        return null;
    }

    public long deleteActivity(long activityId) {
        return activityId;
    }

    public Activity findActivityById(long activityId) {
        return null;
    }

    public Activity findActivityByUser(long userId) {
        return null; //userRepo.findActivityByUserId(userId)
    }


    public List<Activity> findAllActivities() {
        return null;
    }

}
