package ccv.checkhelzio.servicedeskcucsh.Utils;

/**
 * Created by check on 16/10/2017.
 */

public class Datos {

    public static String crearTextoTipoIncidente(int area, int tipo) {
        StringBuilder tipoIncidente = new StringBuilder();
        switch (area) {
            case 0:
                tipoIncidente.append("Redes y Telefonía - ");
                switch (tipo) {
                    case 0:
                        tipoIncidente.append("Teléfono");
                        break;
                    case 1:
                        tipoIncidente.append("Internet");
                        break;
                    case 2:
                        tipoIncidente.append("WIFI");
                        break;
                    case 3:
                        tipoIncidente.append("Mantenimiento correctivo");
                        break;

                }
                break;
            case 1:
                tipoIncidente.append("Taller de cómputo - ");
                switch (tipo) {
                    case 0:
                        tipoIncidente.append("Mantenimiento correctivo");
                        break;
                    case 1:
                        tipoIncidente.append("Impresoras");
                        break;
                    case 2:
                        tipoIncidente.append("Software");
                        break;
                    case 3:
                        tipoIncidente.append("Hardware");
                        break;
                    case 4:
                        tipoIncidente.append("Correo");
                        break;

                }
                break;
            case 2:
                tipoIncidente.append("Multimedia - ");
                switch (tipo) {
                    case 0:
                        tipoIncidente.append("Grabación de eventos academicos");
                        break;
                    case 1:
                        tipoIncidente.append("Producción de videos");
                        break;
                    case 2:
                        tipoIncidente.append("Fotografia de eventos academicos");
                        break;
                    case 3:
                        tipoIncidente.append("Página web");
                        break;
                    case 4:
                        tipoIncidente.append("Diseño");
                        break;
                    case 5:
                        tipoIncidente.append("Videoconferencias");
                        break;
                    case 6:
                        tipoIncidente.append("Streaming");
                        break;
                    case 7:
                        tipoIncidente.append("Prestamo de equipo de audio y video");
                        break;

                }
                break;
            case 3:
                tipoIncidente.append("Administrativo");
                break;
        }
        return tipoIncidente.toString();
    }

    public static String getFondoPrioridad(int prioridadDelServicio) {
        switch (prioridadDelServicio) {
            case 1:
                return "#006A3D";
            case 2:
                return "#DB963F";
            case 3:
                return "#8E171A";
        }
        return "";
    }

    public static String getStringStatus(int int_status) {
        switch (int_status) {
            case 0:
                return "Registrado";
            case 1:
                return "Tomado";
            case 2:
                return "En progreso";
            case 3:
                return "En espera";
            case 4:
                return "Finalizado";
        }
        return null;
    }
}
