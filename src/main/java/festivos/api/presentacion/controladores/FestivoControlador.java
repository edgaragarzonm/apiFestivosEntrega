package festivos.api.presentacion.controladores;

import java.time.LocalDate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import festivos.api.core.interfaces.servicios.IFestivoServicio;

@RestController
@RequestMapping("/api/festivos")
public class FestivoControlador {

    private IFestivoServicio servicio;

    public FestivoControlador(IFestivoServicio servicio) {
        this.servicio = servicio;
    }

    
    @GetMapping("/validar/{fecha}")
    public String esFestivo(@PathVariable String fecha) {
        try {
            LocalDate fechaValida = LocalDate.parse(fecha);
            boolean esFestivo = servicio.esFestivo(fechaValida);
            return esFestivo ? "es festivo." : "no es festivo.";

        } catch (Exception e) {
            return "Fecha inválida. Ingrese el formato yyyy-MM-dd en la fecha que ingrese.";
        }
    }

    
    @GetMapping("/listar/{año}")
    public Object listarFestivos(@PathVariable String año) {
        try {
            int añoValido = Integer.parseInt(año);
            if (añoValido < 0) {
                return "Año inválido. El año ingresado debe ser un número positivo.";
            }
            return servicio.obtenerFestivosPorAño(añoValido);
        } catch (Exception e) {
            return "Año inválido. Ingrese un año válido en formato yyyy";
        }
    }

}