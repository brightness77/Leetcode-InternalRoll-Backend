package com.b77.leetcodeapi.leetcodeapi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

@Data
@AllArgsConstructor

public class Submission implements Comparable {

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate date;

    int count;


    @Override
    public int compareTo(@NotNull Object o) {
        Submission s2 = (Submission) o;
        if(this.getDate().isBefore(s2.getDate())){
            return -1;
        } else {
            return 1;
        }
    }
}
