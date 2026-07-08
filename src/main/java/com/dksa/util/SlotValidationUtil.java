package com.dksa.util;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

import com.dksa.entity.Slot;

public class SlotValidationUtil {

    public static boolean
    areConsecutiveSlots(
            List<Slot> slots) {

        slots.sort(
                Comparator.comparing(
                        Slot::getSlotTime));

        for (int i = 0;
             i < slots.size() - 1;
             i++) {

            LocalTime current =
                    slots.get(i)
                            .getSlotTime();

            LocalTime next =
                    slots.get(i + 1)
                            .getSlotTime();

            if (!current.plusMinutes(30)
                    .equals(next)) {

                return false;
            }
        }

        return true;
    }
}