package co.edu.uniquindio.unitravel.bean;

import co.edu.uniquindio.unitravel.entidades.*;
import co.edu.uniquindio.unitravel.servicios.AdministradorHotelServicio;
import co.edu.uniquindio.unitravel.servicios.UnitravelServicio;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Component
@ViewScoped
public class HotelBean implements Serializable {

    private ArrayList<String> imagenesHotel;
    private ArrayList<String> imagenesHabitacion;
    private ArrayList<Habitacion> habitaciones;

    @Value("#{param['hotel_id']}")
    private String codigoHotel;

    @Getter @Setter
    Hotel hotelModificar;

    @Getter @Setter
    Hotel hotel;

    @Getter @Setter
    Habitacion habitacion;

    @Getter @Setter
    private String camaS;

    @Getter @Setter
    private String camaD;

    @Autowired
    private AdministradorHotelServicio administradorHotelServicio;

    @Autowired
    private UnitravelServicio unitravelServicio;

    @Value("${upload.url}")
    private String urlImagenes;

    @Getter @Setter
    private List<Ciudad> ciudades;

    @Getter @Setter
    private List<Caracteristica> caracteristicasHotel;

    @Getter @Setter
    private List<Caracteristica> caracteristicasHabitacion;

    @Getter @Setter
    private List<Cama> camasHabitacion;

    @Value("#{seguridadBean.persona}")
    private Persona personaSesion;


    @PostConstruct
    public void inicializar() {
        try{
            hotel = new Hotel();
            this.imagenesHotel = new ArrayList<>();
            ciudades = unitravelServicio.listarCiudades();
            caracteristicasHotel = unitravelServicio.listarCaracteristicasHotel();
            caracteristicasHabitacion = unitravelServicio.listarCaracteristicasHabitacion();
            imagenesHabitacion = new ArrayList<>();
            habitacion = new Habitacion();
            habitaciones = new ArrayList<>();
            camasHabitacion = new ArrayList<>();
            if(hotelModificar!=null){
                hotelModificar = unitravelServicio.obtenerHotel(Integer.parseInt(codigoHotel));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void crearHotel() {
        try {

            if(personaSesion!=null){
                if(imagenesHotel.size()>0){
                    if(habitaciones.size()>0){

                        hotel.setAdmin( (AdministradorHotel) personaSesion);
                        hotel.setFotos(imagenesHotel);

                        Hotel h = administradorHotelServicio.crearHotel(hotel);

                        habitaciones.forEach(hab -> {
                            hab.setHotel(h);
                            try {
                                administradorHotelServicio.crearHabitacion(hab);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });

                        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Alerta", "Hotel registrado");
                        FacesContext.getCurrentInstance().addMessage("msj_bean", facesMsg);
                    }else{
                        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Alerta", "El hotel debe tener habitaciones");
                        FacesContext.getCurrentInstance().addMessage("msj_bean", facesMsg);
                    }
                }else{
                    FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Alerta", "Es obligatorio subir imágenes al hotel.");
                    FacesContext.getCurrentInstance().addMessage("msj_bean", facesMsg);
                }
            }
        } catch (Exception e) {
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Alerta", e.getMessage());
            FacesContext.getCurrentInstance().addMessage("msj_bean", facesMsg);
        }
    }

    public void actualizarHotel(Hotel hotelMod){
        try {

            if(personaSesion!=null){
                if(imagenesHotel.size()>0) {

                    hotel.setAdmin((AdministradorHotel) personaSesion);
                    hotel.setFotos(imagenesHotel);

                    hotelMod.setNombre(hotel.getNombre());
                    hotelMod.setCiudad(hotel.getCiudad());
                    hotelMod.setDireccion(hotel.getDireccion());
                    hotelMod.setEstrellas(hotel.getEstrellas());
                    hotelMod.setTelefono(hotel.getTelefono());
                    hotelMod.setDescripcion(hotel.getDescripcion());
                    hotelMod.setFotos(hotel.getFotos());
                    hotelMod.setCaracteristicas(hotel.getCaracteristicas());
                    hotelMod.setAdmin(hotel.getAdmin());

                    administradorHotelServicio.crearHotel(hotelMod);

                    FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Alerta", "Hotel registrado");
                    FacesContext.getCurrentInstance().addMessage("msj_bean", facesMsg);
                }else{
                    FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Alerta", "El hotel debe tener habitaciones");
                    FacesContext.getCurrentInstance().addMessage("msj_bean", facesMsg);
                }
            }else{
                FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Alerta", "Es obligatorio subir imágenes al hotel.");
                FacesContext.getCurrentInstance().addMessage("msj_bean", facesMsg);
            }

        } catch (Exception e) {
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Alerta", e.getMessage());
            FacesContext.getCurrentInstance().addMessage("msj_bean", facesMsg);
        }
    }

    public void crearHabitacion(){
        if(!imagenesHabitacion.isEmpty()){
            habitacion.setFotos(imagenesHabitacion);
            habitaciones.add(habitacion);

            habitacion = new Habitacion();
            imagenesHabitacion = new ArrayList<>();

            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Alerta", "Habitación creada");
            FacesContext.getCurrentInstance().addMessage("msj_bean", facesMsg);
        }else{
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Alerta", "Es obligatorio subir imágenes a la habitación.");
            FacesContext.getCurrentInstance().addMessage("msj_bean", facesMsg);
        }
    }

    public void eliminarHotel(Hotel hotelMod){

        if(hotelMod!=null){
            if(personaSesion!=null){
                try {
                    administradorHotelServicio.eliminarHotel(hotelMod);
                    FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Alerta", "Hotel eliminado");
                    FacesContext.getCurrentInstance().addMessage("msj_bean", facesMsg);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }

    }

    public void subirImagenesHotel(FileUploadEvent event) {
        UploadedFile imagen = event.getFile();
        String nombreImagen = subirImagen(imagen);
        if (nombreImagen != null) {
            imagenesHotel.add(nombreImagen);
        }
    }

    public void subirImagenesHabitacion(FileUploadEvent event) {
        UploadedFile imagen = event.getFile();
        String nombreImagen = subirImagen(imagen);
        if (nombreImagen != null) {
            imagenesHabitacion.add(nombreImagen);
        }
    }

    public String subirImagen(UploadedFile imagen) {
        try {
            File archivo = new File(urlImagenes + "/" + imagen.getFileName());
            OutputStream outputStream = new FileOutputStream(archivo);
            IOUtils.copy(imagen.getInputStream(), outputStream);
            return imagen.getFileName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void crearCama(String cadena){
        System.out.println(cadena);
    }


    public String irModificarHotel(String codigoHotel){
        return "actualizar_hotel?faces-redirect=true&amp;hotel_id="+codigoHotel;
    }

}