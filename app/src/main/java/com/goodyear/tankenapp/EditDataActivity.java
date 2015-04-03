package com.goodyear.tankenapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.goodyear.tankenapp.database.TankEintrag;
import com.goodyear.tankenapp.database.TankEintragDbHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class EditDataActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);
        Bundle b = getIntent().getExtras();
        String id_db = b.getString("id_db");

        TankEintragDbHelper myDbTankEintragHelper = new TankEintragDbHelper(this);
        final SQLiteDatabase db_tankEintrag = myDbTankEintragHelper.getReadableDatabase();
        Cursor cursor= db_tankEintrag.rawQuery("SELECT *  FROM " + TankEintrag.TABLE_NAME + " WHERE _id=" + id_db, null);
        if(cursor.moveToFirst()) {
            String datum = cursor.getString(cursor.getColumnIndexOrThrow(TankEintrag.COLUMN_NAME_DATUM));
            Double gesamtkilometer = cursor.getDouble(cursor.getColumnIndexOrThrow(TankEintrag.COLUMN_NAME_GESAMT_KILOMETER));
            Double liter = cursor.getDouble(cursor.getColumnIndexOrThrow(TankEintrag.COLUMN_NAME_LITER));
            String tankstelle = cursor.getString(cursor.getColumnIndexOrThrow(TankEintrag.COLUMN_NAME_TANKSTELLE));
            Double gesamtpreis = cursor.getDouble(cursor.getColumnIndexOrThrow(TankEintrag.COLUMN_NAME_GESAMTBETRAG));
            String id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));

            EditText edit_tankstelle = (EditText) findViewById(R.id.edit_tankstelle);
            edit_tankstelle.setText(tankstelle);

            DatePicker edit_datum = (DatePicker) findViewById(R.id.edit_datePicker);
            String[] datum_array = datum.split("-");
            int year = Integer.parseInt(datum_array[0]);
            int month = Integer.parseInt(datum_array[1])-1;
            int day = Integer.parseInt(datum_array[2]);
            edit_datum.updateDate(year, month, day);

            EditText edit_liter = (EditText) findViewById(R.id.edit_liter);
            edit_liter.setText(liter.toString());

            EditText edit_gesamtbetrag = (EditText) findViewById(R.id.edit_gesamtbetrag);
            edit_gesamtbetrag.setText(gesamtpreis.toString());

            EditText edit_kilometerstand = (EditText) findViewById(R.id.edit_kilometer);
            edit_kilometerstand.setText(gesamtkilometer.toString());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_data, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void aenderungenSpeichern(View view) {
        String wert_tankstelle, wert_datum;
        double wert_liter, wert_gesamtbetrag, wert_gesamtkilometer, wert_ppl;

        DecimalFormat integerFormat = new DecimalFormat("00");
        DecimalFormat decimalFormat = new DecimalFormat("#.00");

        System.out.println("Eingaben werden überprüft.");

        EditText tankstelle = (EditText) findViewById(R.id.edit_tankstelle);
        DatePicker datum = (DatePicker) findViewById(R.id.edit_datePicker);
        EditText liter = (EditText) findViewById(R.id.edit_liter);
        EditText gesamtbetrag = (EditText) findViewById(R.id.edit_gesamtbetrag);
        EditText kilometerstand = (EditText) findViewById(R.id.edit_kilometer);

        if((tankstelle.getText().length() > 0) && (liter.getText().length() > 0) && (gesamtbetrag.getText().length() > 0) && (kilometerstand.getText().length() > 0)) {
            // Werte in Variablen speichern
            wert_tankstelle = tankstelle.getText().toString();
            wert_datum = datum.getYear() + "-" + integerFormat.format(datum.getMonth()+1) + "-" + integerFormat.format(datum.getDayOfMonth());
            wert_liter = Double.parseDouble(liter.getText().toString());
            wert_gesamtbetrag = Double.parseDouble(gesamtbetrag.getText().toString());
            wert_gesamtkilometer = Double.parseDouble(kilometerstand.getText().toString());
            wert_ppl = wert_gesamtbetrag/wert_liter;

            // in Datenbank eintragen
            TankEintragDbHelper myDbTankEintragHelper = new TankEintragDbHelper(this);
            SQLiteDatabase db_tankEintrag = myDbTankEintragHelper.getWritableDatabase();

            ContentValues values_tankEintrag = new ContentValues();
            values_tankEintrag.put(TankEintrag.COLUMN_NAME_TANKSTELLE, wert_tankstelle);
            values_tankEintrag.put(TankEintrag.COLUMN_NAME_DATUM, wert_datum);
            values_tankEintrag.put(TankEintrag.COLUMN_NAME_LITER, decimalFormat.format(wert_liter));
            values_tankEintrag.put(TankEintrag.COLUMN_NAME_GESAMTBETRAG, decimalFormat.format(wert_gesamtbetrag));
            values_tankEintrag.put(TankEintrag.COLUMN_NAME_GESAMT_KILOMETER, integerFormat.format(wert_gesamtkilometer));
            values_tankEintrag.put(TankEintrag.COLUMN_NAME_PREIS_PRO_LITER, decimalFormat.format(wert_ppl));

            Bundle b = getIntent().getExtras();
            String id_db = b.getString("id_db");

            db_tankEintrag.update(TankEintrag.TABLE_NAME,values_tankEintrag,"_id="+id_db,null);
            db_tankEintrag.close();
            finish();
            Intent intent = new Intent(this, UeberblickActivity.class);
            startActivity(intent);
        } else {
            System.out.println("Es sind nicht alle Daten eingetragen.");
            Toast.makeText(getApplicationContext(), "Bitte alle Daten eintragen", Toast.LENGTH_LONG);
        }


    }
}
