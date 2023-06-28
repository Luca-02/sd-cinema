package it.unimib.finalproject.server.utility;

import java.text.SimpleDateFormat;

public class DateTimeFormat {

    public static final String dateFormatString = "yyyy-MM-dd";
    public static final String timeFormatString = "hh:mm";
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatString);
    public static final SimpleDateFormat timeFormat = new SimpleDateFormat(timeFormatString);

}
