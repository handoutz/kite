package com.hatch.kite.api;

import java.util.ArrayList;

/**
 * Created by vince on 9/15/13.
 */
public class UserFeedback {
    public ArrayList<Level> feedbacks = new ArrayList<Level>();

    public enum Level {
        Scry, Buy, Try
    }
}
