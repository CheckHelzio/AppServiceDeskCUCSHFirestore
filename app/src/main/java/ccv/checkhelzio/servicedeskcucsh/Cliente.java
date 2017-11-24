package ccv.checkhelzio.servicedeskcucsh;

import android.os.Parcel;
import android.os.Parcelable;

public class Cliente implements Parcelable {
    private String codigoDelCliente;
    private String nombreDelCliente;
    private String dependenciaDelCliente;
    private String ubicacionDelCliente;
    private String telefonoDelCliente;
    private String correoElectronicoDelCliente;

    public Cliente() {
    }

    public Cliente(String codigoDelCliente, String nombreDelCliente, String dependenciaDelCliente, String ubicacionDelCliente, String telefonoDelCliente, String correoElectronicoDelCliente) {
        this.codigoDelCliente = codigoDelCliente;
        this.nombreDelCliente = nombreDelCliente;
        this.dependenciaDelCliente = dependenciaDelCliente;
        this.ubicacionDelCliente = ubicacionDelCliente;
        this.telefonoDelCliente = telefonoDelCliente;
        this.correoElectronicoDelCliente = correoElectronicoDelCliente;
    }

    public String getCodigoDelCliente() {
        return codigoDelCliente;
    }

    public void setCodigoDelCliente(String codigoDelCliente) {
        this.codigoDelCliente = codigoDelCliente;
    }

    public String getNombreDelCliente() {
        return nombreDelCliente;
    }

    public void setNombreDelCliente(String nombreDelCliente) {
        this.nombreDelCliente = nombreDelCliente;
    }

    public String getDependenciaDelCliente() {
        return dependenciaDelCliente;
    }

    public void setDependenciaDelCliente(String dependenciaDelCliente) {
        this.dependenciaDelCliente = dependenciaDelCliente;
    }

    public String getUbicacionDelCliente() {
        return ubicacionDelCliente;
    }

    public void setUbicacionDelCliente(String ubicacionDelCliente) {
        this.ubicacionDelCliente = ubicacionDelCliente;
    }

    public String getTelefonoDelCliente() {
        return telefonoDelCliente;
    }

    public void setTelefonoDelCliente(String telefonoDelCliente) {
        this.telefonoDelCliente = telefonoDelCliente;
    }

    public String getCorreoElectronicoDelCliente() {
        return correoElectronicoDelCliente;
    }

    public void setCorreoElectronicoDelCliente(String correoElectronicoDelCliente) {
        this.correoElectronicoDelCliente = correoElectronicoDelCliente;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.codigoDelCliente);
        dest.writeString(this.nombreDelCliente);
        dest.writeString(this.dependenciaDelCliente);
        dest.writeString(this.ubicacionDelCliente);
        dest.writeString(this.telefonoDelCliente);
        dest.writeString(this.correoElectronicoDelCliente);
    }

    protected Cliente(Parcel in) {
        this.codigoDelCliente = in.readString();
        this.nombreDelCliente = in.readString();
        this.dependenciaDelCliente = in.readString();
        this.ubicacionDelCliente = in.readString();
        this.telefonoDelCliente = in.readString();
        this.correoElectronicoDelCliente = in.readString();
    }

    public static final Parcelable.Creator<Cliente> CREATOR = new Parcelable.Creator<Cliente>() {
        @Override
        public Cliente createFromParcel(Parcel source) {
            return new Cliente(source);
        }

        @Override
        public Cliente[] newArray(int size) {
            return new Cliente[size];
        }
    };
}
