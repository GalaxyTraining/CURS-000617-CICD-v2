package pe.edu.galaxy.training.java.arq.layered.api_be_project_manager.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static java.util.Objects.isNull;

public class DateFormatUtil {
    public static final String DD_MM_YYYY = "dd/MM/yyyy";

    public static String format(LocalDate date){
        if (isNull(date)) return null;
        return date.format(DateTimeFormatter.ofPattern(DD_MM_YYYY));
    }
    public static String format(LocalDate date, String format){
        if (isNull(date)) return null;
        if (isNull(format)) {
            return date.format(DateTimeFormatter.ofPattern(DD_MM_YYYY));
        }else{
            return date.format(DateTimeFormatter.ofPattern(format));
        }
    }
}
