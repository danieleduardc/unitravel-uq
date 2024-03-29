package co.edu.uniquindio.unitravel.converter;

import co.edu.uniquindio.unitravel.entidades.Ciudad;
import co.edu.uniquindio.unitravel.servicios.ClienteServicio;
import co.edu.uniquindio.unitravel.servicios.UnitravelServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import java.io.Serializable;

import static java.lang.String.valueOf;

@Component
public class CiudadConverter implements Converter<Ciudad>, Serializable {

    @Autowired
    private UnitravelServicio unitravelServicio;

    @Override
    public Ciudad getAsObject(FacesContext context, UIComponent component, String value) {

        Ciudad ciudad = unitravelServicio.obtenerCiudad(Integer.parseInt(value));

        return ciudad;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Ciudad value) {
        if(value!=null){
            String variable = String.valueOf(value.getCodigo());
            return variable;
        }
        return "";
    }
}
