package ccv.checkhelzio.servicedeskcucsh;

/**
 * Created by check on 27/10/2017.
 */

public class Progreso {

    private long timestamp;
    private int progreso;
    private String nombre;
    private String mensaje;

    public Progreso() {
    }

    public Progreso(long timestamp, int progreso, String nombre, String mensaje) {
        this.timestamp = timestamp;
        this.progreso = progreso;
        this.nombre = nombre;
        this.mensaje = mensaje;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getProgreso() {
        return progreso;
    }

    public void setProgreso(int progreso) {
        this.progreso = progreso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setQuien(String nombre) {
        this.nombre = nombre;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
