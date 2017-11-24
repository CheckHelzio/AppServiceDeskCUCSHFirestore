package ccv.checkhelzio.servicedeskcucsh;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.ArrayList;

import ccv.checkhelzio.servicedeskcucsh.Utils.AnimUtils;



public class DialogTipoIncidente extends Activity {

    final ArrayList<CheckBox> lista_checkbox = new ArrayList<>();
    private CheckBox fba1;
    private CheckBox fba2;
    private CheckBox fba3;
    private CheckBox fba4;
    private CheckBox fba5;
    private CheckBox fba6;
    private CheckBox fba7;
    private CheckBox fba8;
    private CheckBox fba9;
    private CheckBox fba10;
    private CheckBox fba11;
    private CheckBox fba12;
    private CheckBox fba13;
    private CheckBox fba14;
    private CheckBox fba15;
    private CheckBox fba16;
    private CheckBox fba17;
    private CheckBox fba18;
    private CheckBox fba19;
    private CheckBox fba20;
    private CheckBox fba21;
    private CheckBox fba22;
    private CheckBox fba23;
    private CheckBox fba24;
    private CheckBox fba25;
    private CheckBox fba26;
    private CheckBox fba27;
    private CheckBox fba28;
    private CheckBox fba29;
    private CheckBox fba30;
    private CheckBox fba31;
    private CheckBox fba32;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();
        setContentView(R.layout.dialog_tipo_incidente);

        findViewById(R.id.tv_guardar_evento).setOnClickListener(this::aceptar);

        Slide slide = new Slide(Gravity.BOTTOM);
        slide.setInterpolator(AnimUtils.getLinearOutSlowInInterpolator(DialogTipoIncidente.this));
        slide.excludeTarget(android.R.id.statusBarBackground, true);
        slide.excludeTarget(android.R.id.navigationBarBackground, true);
        getWindow().setEnterTransition(slide);

        startPostponedEnterTransition();

        fba1 = findViewById(R.id.computo_mantenimiento);
        fba2 = findViewById(R.id.computo_impresora);
        fba3 = findViewById(R.id.computo_software);
        fba4 = findViewById(R.id.computo_hardware);
        fba5 = findViewById(R.id.computo_correo);
        fba6 = findViewById(R.id.computo_otro);
        fba31 = findViewById(R.id.computo_servidores);

        fba7 = findViewById(R.id.redes_telefono);
        fba8 = findViewById(R.id.redes_internet);
        fba9 = findViewById(R.id.redes_wifi);
        fba10 = findViewById(R.id.redes_mantenimiento);
        fba11 = findViewById(R.id.redes_otro);


        fba12 = findViewById(R.id.umi_grabacion);
        fba13 = findViewById(R.id.umi_produccion_videos);
        fba14 = findViewById(R.id.umi_fotografia);
        fba15 = findViewById(R.id.umi_pagina_web);
        fba16 = findViewById(R.id.umi_diseño);
        fba17 = findViewById(R.id.umi_videoconferencias);
        fba18 = findViewById(R.id.umi_streaming);
        fba19 = findViewById(R.id.umi_apliaciones);
        fba20 = findViewById(R.id.umi_otro);

        fba21 = findViewById(R.id.administrativo_hector);
        fba22 = findViewById(R.id.administrativo_idania);
        fba23 = findViewById(R.id.administrativo_eduardo);

        fba24 = findViewById(R.id.aviso_taller);
        fba25 = findViewById(R.id.aviso_redes);
        fba26 = findViewById(R.id.aviso_multimedia);
        fba27 = findViewById(R.id.aviso_redes_taller);
        fba28 = findViewById(R.id.aviso_redes_multimedia);
        fba29 = findViewById(R.id.aviso_taller_multimedia);
        fba30 = findViewById(R.id.aviso_general);
        fba32 = findViewById(R.id.aviso_belenes);

        lista_checkbox.add(fba1);
        lista_checkbox.add(fba2);
        lista_checkbox.add(fba3);
        lista_checkbox.add(fba4);
        lista_checkbox.add(fba5);
        lista_checkbox.add(fba6);
        lista_checkbox.add(fba7);
        lista_checkbox.add(fba8);
        lista_checkbox.add(fba9);
        lista_checkbox.add(fba10);
        lista_checkbox.add(fba11);
        lista_checkbox.add(fba12);
        lista_checkbox.add(fba13);
        lista_checkbox.add(fba14);
        lista_checkbox.add(fba15);
        lista_checkbox.add(fba16);
        lista_checkbox.add(fba17);
        lista_checkbox.add(fba18);
        lista_checkbox.add(fba19);
        lista_checkbox.add(fba20);
        lista_checkbox.add(fba21);
        lista_checkbox.add(fba22);
        lista_checkbox.add(fba23);
        lista_checkbox.add(fba24);
        lista_checkbox.add(fba25);
        lista_checkbox.add(fba26);
        lista_checkbox.add(fba27);
        lista_checkbox.add(fba28);
        lista_checkbox.add(fba29);
        lista_checkbox.add(fba30);
        lista_checkbox.add(fba31);
        lista_checkbox.add(fba32);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        cerrar(null);
    }

    public void aceptar(View view) {
        String st_tipo_incidente = "";
        if (fba1.isChecked()) {
            st_tipo_incidente += "Taller de computo - Mantenimiento correctivo";
        } else if (fba2.isChecked()) {
            st_tipo_incidente += "Taller de computo - Impresora";
        } else if (fba3.isChecked()) {
            st_tipo_incidente += "Taller de computo - Software";
        } else if (fba4.isChecked()) {
            st_tipo_incidente += "Taller de computo - Hardware";
        } else if (fba5.isChecked()) {
            st_tipo_incidente += "Taller de computo - Correo electrónico";
        } else if (fba6.isChecked()) {
            st_tipo_incidente += "Taller de computo - Otro";
        } else if (fba7.isChecked()) {
            st_tipo_incidente += "Redes y telefonía - Teléfono";
        } else if (fba8.isChecked()) {
            st_tipo_incidente += "Redes y telefonía - Internet";
        } else if (fba9.isChecked()) {
            st_tipo_incidente += "Redes y telefonía - WIFI";
        } else if (fba10.isChecked()) {
            st_tipo_incidente += "Redes y telefonía - Mantenimiento correctivo";
        } else if (fba11.isChecked()) {
            st_tipo_incidente += "Redes y telefonía - Otro";
        } else if (fba12.isChecked()) {
            st_tipo_incidente += "Multimedia - Grabación de eventos académicos";
        } else if (fba13.isChecked()) {
            st_tipo_incidente += "Multimedia - Producción de videos";
        } else if (fba14.isChecked()) {
            st_tipo_incidente += "Multimedia - Fotografía de eventos académicos";
        } else if (fba15.isChecked()) {
            st_tipo_incidente += "Multimedia - Página web";
        } else if (fba16.isChecked()) {
            st_tipo_incidente += "Multimedia - Diseño";
        } else if (fba17.isChecked()) {
            st_tipo_incidente += "Multimedia - Videoconferencia";
        } else if (fba18.isChecked()) {
            st_tipo_incidente += "Multimedia - Streaming";
        } else if (fba19.isChecked()) {
            st_tipo_incidente += "Multimedia - Aplicaciones";
        } else if (fba20.isChecked()) {
            st_tipo_incidente += "Multimedia - Otro";
        } else if (fba21.isChecked()) {
            st_tipo_incidente += "Administrativo - Héctor Aceves Shimizu y López";
        } else if (fba22.isChecked()) {
            st_tipo_incidente += "Adminitrativo - Idania Gómez Cosio";
        } else if (fba23.isChecked()) {
            st_tipo_incidente += "Adminitrativo - Eduardo Solano Guzmán";
        } else if (fba24.isChecked()) {
            st_tipo_incidente += "Aviso - Taller de computo";
        } else if (fba25.isChecked()) {
            st_tipo_incidente += "Aviso - Redes y telefonía";
        } else if (fba26.isChecked()) {
            st_tipo_incidente += "Aviso - Multimedia";
        } else if (fba27.isChecked()) {
            st_tipo_incidente += "Aviso - Redes y Taller";
        } else if (fba28.isChecked()) {
            st_tipo_incidente += "Aviso - Redes y Multimedia";
        } else if (fba29.isChecked()) {
            st_tipo_incidente += "Aviso - Taller y Multimedia";
        } else if (fba30.isChecked()) {
            st_tipo_incidente += "Aviso - Aviso general";
        } else if (fba31.isChecked()) {
            st_tipo_incidente += "Taller de computo - Servidores";
        } else if (fba32.isChecked()) {
            st_tipo_incidente += "Aviso - Belenes";
        }

        Intent i = getIntent();
        i.putExtra("TIPO_DE_INCIDENTE", st_tipo_incidente);
        setResult(RESULT_OK, i);
        cerrar(null);
    }

    public void cerrar(View view) {
        Slide slide = new Slide(Gravity.BOTTOM);
        slide.setInterpolator(AnimUtils.getFastOutLinearInInterpolator(DialogTipoIncidente.this));
        slide.excludeTarget(android.R.id.statusBarBackground, true);
        slide.excludeTarget(android.R.id.navigationBarBackground, true);
        getWindow().setExitTransition(slide);
        finishAfterTransition();
    }

    public void togle(View view) {
        for (CheckBox c : lista_checkbox){
            c.setChecked(false);
        }
        ((CheckBox) ((ViewGroup) view).getChildAt(0)).setChecked(true);
    }
}