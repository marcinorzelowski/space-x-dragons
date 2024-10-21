package com.orzelowski.spacex.dragons.util;

import com.orzelowski.spacex.dragons.model.Mission;

import java.util.Comparator;

public class MissionComparator implements Comparator<Mission> {

    @Override
    public int compare(Mission o1, Mission o2) {
        int listLengthCompare = Integer.compare(o2.getRockets().size(), o1.getRockets().size());
        if (listLengthCompare != 0)
            return listLengthCompare;
        else
            return o2.getName().compareTo(o1.getName());
    }
}
