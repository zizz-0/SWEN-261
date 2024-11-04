package com.needs.api.needsapi.persistence;

import java.util.Comparator;

import com.needs.api.needsapi.model.Need;
import com.needs.api.needsapi.model.UrgencyTag;

public class NeedComparator implements Comparator<Need>{

    @Override
    public int compare(Need o1, Need o2) {

        if(o1.getUrgency() == UrgencyTag.HIGH && o2.getUrgency() == UrgencyTag.LOW){
            return -1;
        }
        if(o1.getUrgency() == UrgencyTag.LOW && o2.getUrgency() == UrgencyTag.HIGH){
            return 1;
        }

        return 0;
    }
    
}
