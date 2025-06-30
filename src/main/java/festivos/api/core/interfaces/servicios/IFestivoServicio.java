package festivos.api.core.interfaces.servicios;

import java.time.LocalDate;
import java.util.List;


public interface IFestivoServicio {
    boolean esFestivo(LocalDate fecha);
    List<LocalDate> obtenerFestivosPorAño(int año);
}
