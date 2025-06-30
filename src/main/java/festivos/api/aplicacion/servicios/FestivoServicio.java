package festivos.api.aplicacion.servicios;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import festivos.api.core.dominio.entidades.Festivo;
import festivos.api.core.interfaces.servicios.IFestivoServicio;
import festivos.api.infraestructura.repositorios.IFestivoRepositorio;

@Service
public class FestivoServicio implements IFestivoServicio {

    private IFestivoRepositorio repositorio;

    public FestivoServicio(IFestivoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public boolean esFestivo(LocalDate fecha) {
        int año = fecha.getYear();
        List<LocalDate> festivos = obtenerFestivosPorAño(año);
        return festivos.contains(fecha);
    }

    @Override
    public List<LocalDate> obtenerFestivosPorAño(int año) {
        List<Festivo> definicionesFestivos = repositorio.findAll();
        List<LocalDate> festivos = new ArrayList<>();

        for (Festivo f : definicionesFestivos) {
            LocalDate fechaFestivo = calcularFechaFestivo(f, año);
            if (fechaFestivo != null) {
                festivos.add(fechaFestivo);
            }
        }
        return festivos;
    }

    private LocalDate calcularFechaFestivo(Festivo festivo, int año) {
        int tipo = festivo.getTipo().getId();
        switch (tipo) {
            case 1: //Fijo
                return LocalDate.of(año, festivo.getMes(), festivo.getDia());
            case 2: //Ley de "Puente festivo"
                return siguienteLunes(LocalDate.of(año, festivo.getMes(), festivo.getDia()));
            case 3: //Basado en el domingo de Pascua
                return calcularDomingoPascua(año).plusDays(festivo.getDiaspascua());
            case 4: //Basado en el domingo de Pascua y Ley de "Puente festivo"
                LocalDate temporal = calcularDomingoPascua(año).plusDays(festivo.getDiaspascua());
                return siguienteLunes(temporal);
            default:
                return null;
        }
    }

    private LocalDate calcularDomingoPascua(int año) {
        int a = año % 19;
        int b = año % 4;
        int c = año % 7;
        int d = (19 * a + 24) % 30;
        int dias = d + (2 * b + 4 * c + 6 * d + 5) % 7;
        int dia = 15 + dias;
        int mes = 3;
        if (dia > 31) {
            mes = 4;
            dia -= 31;
        }
        //Se le suman 7 dias al Domingo de Ramos para tener el Domingo de Pascua
        return LocalDate.of(año, mes, dia + 7);
    }

    private LocalDate siguienteLunes(LocalDate fecha) {
        DayOfWeek diaSemana = fecha.getDayOfWeek();
        return diaSemana == DayOfWeek.MONDAY ? fecha : fecha.plusDays(8 - diaSemana.getValue());
    }
}
