package com.example.proyectofinalambienteweb;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectofinalambienteweb.dialog.DatePickerFragment;
import com.example.proyectofinalambienteweb.gestion.CitaGestion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import model.Cita;

public class activity_editarcita extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Cita cita;
    private TextView tvLugar;
    private TextView tvArea;
    private TextView tvFecha;
    private TextView tvHora;
    private EditText etFecha;

    private Spinner spinnerHora;
    private static final String[] pathsHora = {"08:00", "10:00", "12:00", "15:00", "18:00"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editarcita);
        tvLugar = findViewById(R.id.tvLugar);
        tvArea = findViewById(R.id.tvArea);
        tvFecha = findViewById(R.id.tvFechaActual);
        tvHora = findViewById(R.id.tvHoraActual);

        cita = (Cita) getIntent().getSerializableExtra("cita");
        tvLugar.setText(cita.getEstablecimiento());
        tvArea.setText(cita.getArea());
        tvFecha.setText(cita.getFecha());
        tvHora.setText(cita.getHora());
        listasSpinner();

        etFecha = findViewById(R.id.etFecha);
        etFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }//fin del onCreate

    public void eliminarCita(View v){
        if (CitaGestion.elimina(cita.getIdCita())){
            Toast.makeText(activity_editarcita.this, "Cita eliminada", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(activity_editarcita.this, activity_menuprincipal.class);
            startActivity(intent);
        }else{
            Toast.makeText(activity_editarcita.this, "Error...", Toast.LENGTH_SHORT).show();
        }
    }//fin de eliminar cita

    public void modficaCita(View v) throws ParseException {
        String datoFecha = etFecha.getText().toString();
        String datoHora = spinnerHora.getSelectedItem().toString();
        if(!datoFecha.equals("") && !datoHora.equals("")){
            cita.setFecha(datoFecha);
            cita.setHora(datoHora);
            if (CitaGestion.modifica(cita)){
                Toast.makeText(activity_editarcita.this, "Cita actualizada", Toast.LENGTH_SHORT).show();
                guardaEnCalendario(datoFecha,datoHora, tvLugar.getText().toString(), tvArea.getText().toString(), activity_editarcita.this);
                //Intent intent = new Intent(activity_editarcita.this, activity_menuprincipal.class);
                //startActivity(intent);
            }else{
                Toast.makeText(activity_editarcita.this, "Error...", Toast.LENGTH_SHORT).show();
            }
        }//fin del if de los vacios
    }//fin de eliminar cita

    private void guardaEnCalendario(String fecha, String hora, String ubicacion, String area, Activity activity) throws ParseException {
        try {
            SimpleDateFormat Formatfecha = new SimpleDateFormat("dd/MM/yyyy hh:mm");
            Date dateFecha = Formatfecha.parse(fecha + " " + hora);

            Calendar cal = Calendar.getInstance();
            cal.setTime(dateFecha);

            Intent calendarIntent = new Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI);
            Calendar beginTime = Calendar.getInstance();
            beginTime.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
            Calendar endTime = Calendar.getInstance();
            endTime.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
            calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis());
            calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis());
            calendarIntent.putExtra(CalendarContract.Events.TITLE, "Cita en " + area);
            calendarIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, ubicacion);
            activity.startActivity(calendarIntent);
        }catch (NullPointerException e){
            e.toString();
        }

    }//fin del guardado en el calendario


    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                final String selectedDate = day + "/" + (month+1) + "/" + year;
                etFecha.setText(selectedDate);
            }
        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void listasSpinner(){
        //hora
        spinnerHora = (Spinner)findViewById(R.id.spinnerHora);
        ArrayAdapter<String>adapterHora = new ArrayAdapter<String>(activity_editarcita.this,
                android.R.layout.simple_spinner_item, pathsHora);
        adapterHora.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHora.setAdapter(adapterHora);
        spinnerHora.setOnItemSelectedListener(this);
    }
    public void goToMenu(View v){
        Intent intent = new Intent(activity_editarcita.this, activity_menuprincipal.class);
        startActivity(intent);
    }//fin del goToMenu

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}//fin de la clase