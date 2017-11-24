package ccv.checkhelzio.servicedeskcucsh;

public class IncidenteDetalle {
    private int tipoDeServicio;
    private String descripcionDelReporte;
    private String codigoDeQuienLevandoElReporte;

    public IncidenteDetalle() {
    }

    public IncidenteDetalle(int tipoDeServicio, String descripcionDelReporte, String codigoDeQuienLevandoElReporte) {
        this.tipoDeServicio = tipoDeServicio;
        this.descripcionDelReporte = descripcionDelReporte;
        this.codigoDeQuienLevandoElReporte = codigoDeQuienLevandoElReporte;
    }

    public int getTipoDeServicio() {
        return tipoDeServicio;
    }

    public void setTipoDeServicio(int tipoDeServicio) {
        this.tipoDeServicio = tipoDeServicio;
    }

    public String getDescripcionDelReporte() {
        return descripcionDelReporte;
    }

    public void setDescripcionDelReporte(String descripcionDelReporte) {
        this.descripcionDelReporte = descripcionDelReporte;
    }

    public String getCodigoDeQuienLevandoElReporte() {
        return codigoDeQuienLevandoElReporte;
    }

    public void setCodigoDeQuienLevandoElReporte(String codigoDeQuienLevandoElReporte) {
        this.codigoDeQuienLevandoElReporte = codigoDeQuienLevandoElReporte;
    }
}