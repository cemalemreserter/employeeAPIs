package com.emre.employeeAPI.utility;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static com.emre.employeeAPI.constants.AppConstants.*;

public class DateTimeUtility {

    public String setPattern(Date date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_PATTERN);

        return  sdf.format(date);
    }

}
