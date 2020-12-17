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
import android.widget.Toast;

import com.example.proyectofinalambienteweb.data.AdminDB;
import com.example.proyectofinalambienteweb.dialog.DatePickerFragment;
import com.example.proyectofinalambienteweb.gestion.CitaGestion;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import model.Cita;

public class activity_nuevacita extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Spinner spinnerEstablecimiento;
    private Spinner spinnerArea;
    private Spinner spinnerHora;
    private static final String[] pathsHora = {"08:00", "10:00", "12:00", "15:00", "18:00"};
    private EditText etFecha;
    private Cita cita;
    private String UserID;
    ArrayList<Cita> listaCitas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevacita);
        AdminDB data = new AdminDB(getApplicationContext(),"FilaVirtual.db",null,1);
        CitaGestion.init(data);
        etFecha = findViewById(R.id.etFecha);
        etFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        UserID = getIntent().getStringExtra("UserID");
        //establecimiento
        spinnerEstablecimiento = findViewById(R.id.spinnerEstablecimiento);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.array_establecimientos,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstablecimiento.setAdapter(adapter);
        spinnerEstablecimiento.setOnItemSelectedListener(this);
        //area
        spinnerArea = findViewById(R.id.spinnerArea);
        //hora
        spinnerHora = findViewById(R.id.spinnerHora);
        ArrayAdapter<CharSequence> adapterHora = ArrayAdapter.createFromResource(
                this,
                R.array.array_horas,
                android.R.layout.simple_spinner_item);
        adapterHora .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHora.setAdapter(adapterHora );
        spinnerHora.setOnItemSelectedListener(this);
    }//fin del on create

    public void creaCita(View v) throws ParseException {
        boolean resultado = false;
        String datoEstablecimiento = spinnerEstablecimiento.getSelectedItem().toString();
        String datoArea = spinnerArea.getSelectedItem().toString();
        String datoFecha = etFecha.getText().toString();
        String datoHora = spinnerHora.getSelectedItem().toString();
        if (!datoEstablecimiento.equals("") && !datoArea.equals("" ) && !datoFecha.equals("") &&
        !datoHora.equals("") && !UserID.equals("")){
            cita = new Cita(0, UserID, datoEstablecimiento, datoArea,datoFecha,datoHora);
            resultado = CitaGestion.inserta(cita);
            if (resultado){
                guardaEnCalendario(datoFecha,datoHora, datoEstablecimiento, datoArea, activity_nuevacita.this);
                Toast.makeText(activity_nuevacita.this, "Cita agregada", Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(activity_nuevacita.this, activity_menuprincipal.class);
                //startActivity(intent);
            }else{
                Toast.makeText(activity_nuevacita.this, "Error...", Toast.LENGTH_SHORT).show();
            }
        }

    }//fin de la creacion de la cita

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

    public void goToMenu(View v){
        Intent intent = new Intent(activity_nuevacita.this, activity_menuprincipal.class);
        startActivity(intent);
    }//fin del goToMenu

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.spinnerEstablecimiento){
            int[] areas = {R.array.array_bcr, R.array.array_muni, R.array.array_registro};
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    areas[position],
                    android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerArea.setAdapter(adapter);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}


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
}