package ccv.checkhelzio.servicedeskcucsh;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class IncidenteBasico implements Parcelable {

    Map<String, Boolean> areas = new HashMap<>();
    private int folio;
    private int prioridad;
    private int estatus;
    private String tecnico;
    private long fecha;
    private String quienRegistro;
    private String tipoDeIncidente;
    private String descripcionDelReporte;
    private String codigoCliente;

    public IncidenteBasico() {
    }

    public IncidenteBasico(Map<String, Boolean> areas, int folio, int prioridad, int estatus, String tecnico, long fecha, String quienRegistro, String tipoDeIncidente, String descripcionDelReporte, String codigoCliente) {
        this.areas = areas;
        this.folio = folio;
        this.prioridad = prioridad;
        this.estatus = estatus;
        this.tecnico = tecnico;
        this.fecha = fecha;
        this.quienRegistro = quienRegistro;
        this.tipoDeIncidente = tipoDeIncidente;
        this.descripcionDelReporte = descripcionDelReporte;
        this.codigoCliente = codigoCliente;
    }

    public Map<String, Boolean> getAreas() {
        return areas;
    }

    public void setAreas(Map<String, Boolean> areas) {
        this.areas = areas;
    }

    public int getFolio() {
        return folio;
    }

    public void setFolio(int folio) {
        this.folio = folio;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public String getTecnico() {
        return tecnico;
    }

    public void setTecnico(String tecnico) {
        this.tecnico = tecnico;
    }

    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    public String getQuienRegistro() {
        return quienRegistro;
    }

    public void setQuienRegistro(String quienRegistro) {
        this.quienRegistro = quienRegistro;
    }

    public String getTipoDeIncidente() {
        return tipoDeIncidente;
    }

    public void setTipoDeIncidente(String tipoDeIncidente) {
        this.tipoDeIncidente = tipoDeIncidente;
    }

    public String getDescripcionDelReporte() {
        return descripcionDelReporte;
    }

    public void setDescripcionDelReporte(String descripcionDelReporte) {
        this.descripcionDelReporte = descripcionDelReporte;
    }

    public String getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.areas.size());
        for (Map.Entry<String, Boolean> entry : this.areas.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeValue(entry.getValue());
        }
        dest.writeInt(this.folio);
        dest.writeInt(this.prioridad);
        dest.writeInt(this.estatus);
        dest.writeString(this.tecnico);
        dest.writeLong(this.fecha);
        dest.writeString(this.quienRegistro);
        dest.writeString(this.tipoDeIncidente);
        dest.writeString(this.descripcionDelReporte);
        dest.writeString(this.codigoCliente);
    }

    protected IncidenteBasico(Parcel in) {
        int areasSize = in.readInt();
        this.areas = new HashMap<String, Boolean>(areasSize);
        for (int i = 0; i < areasSize; i++) {
            String key = in.readString();
            Boolean value = (Boolean) in.readValue(Boolean.class.getClassLoader());
            this.areas.put(key, value);
        }
        this.folio = in.readInt();
        this.prioridad = in.readInt();
        this.estatus = in.readInt();
        this.tecnico = in.readString();
        this.fecha = in.readLong();
        this.quienRegistro = in.readString();
        this.tipoDeIncidente = in.readString();
        this.descripcionDelReporte = in.readString();
        this.codigoCliente = in.readString();
    }

    public static final Creator<IncidenteBasico> CREATOR = new Creator<IncidenteBasico>() {
        @Override
        public IncidenteBasico createFromParcel(Parcel source) {
            return new IncidenteBasico(source);
        }

        @Override
        public IncidenteBasico[] newArray(int size) {
            return new IncidenteBasico[size];
        }
    };
}