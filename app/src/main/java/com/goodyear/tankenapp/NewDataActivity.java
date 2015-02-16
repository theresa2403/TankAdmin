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
import android.widget.DatePicker;
import android.widget.EditText;

import com.goodyear.tankenapp.database.DurchschnittUndSummeDbHelper;
import com.goodyear.tankenapp.database.TankEintrag;
import com.goodyear.tankenapp.database.TankEintragDbHelper;


public class NewDataActivity extends Activity {

    private TankEintragDbHelper myDbTankEintragHelper = new TankEintragDbHelper(NewDataActivity.this);
    private DurchschnittUndSummeDbHelper myDbDsHelper = new DurchschnittUndSummeDbHelper(NewDataActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_data);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_data, menu);
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

    public void eingabenUeberpruefen(View view) {
        int anzahlEintraege;
        String wert_tankstelle, wert_datum;
        double wert_liter, wert_gesamtbetrag, wert_kilometerstand, wert_gesamtkilometer, wert_ppl, wert_verbrauch, wert_vergangeneTage ;

        System.out.println("Eingaben werden überprüft.");

        EditText tankstelle = (EditText) findViewById(R.id.tankstelle);
        DatePicker datum = (DatePicker) findViewById(R.id.datePicker);
        EditText liter = (EditText) findViewById(R.id.liter);
        EditText gesamtbetrag = (EditText) findViewById(R.id.gesamtbetrag);
        EditText kilometerstand = (EditText) findViewById(R.id.kilometer);

        SQLiteDatabase db_tankEintrag = myDbTankEintragHelper.getReadableDatabase();
        Cursor count = db_tankEintrag.rawQuery("SELECT COUNT(*) FROM " + TankEintrag.TABLE_NAME, null);
        count.moveToFirst();
        anzahlEintraege = count.getInt(0);
        count.close();

        if(anzahlEintraege == 0){
            wert_gesamtkilometer = 0;
        } else {
            //gesamtkilometer initialisieren
            Cursor cursor_gesamtkm = db_tankEintrag.rawQuery("SELECT " + TankEintrag.COLUMN_NAME_GESAMT_KILOMETER + " FROM " + TankEintrag.TABLE_NAME, null);
            cursor_gesamtkm.moveToLast();
            wert_gesamtkilometer = cursor_gesamtkm.getDouble(0);
        }



        if((tankstelle.getText().length() > 0) && (liter.getText().length() > 0) && (gesamtbetrag.getText().length() > 0) && (kilometerstand.getText().length() > 0)) {
            // Werte in Variablen speichern
            wert_tankstelle = tankstelle.getText().toString();
            wert_datum = datum.getCalendarView().toString();
            wert_liter = Double.parseDouble(liter.getText().toString());
            wert_gesamtbetrag = Double.parseDouble(gesamtbetrag.getText().toString());
            wert_kilometerstand = Double.parseDouble(kilometerstand.getText().toString());
            wert_gesamtkilometer += wert_kilometerstand;
            wert_ppl = wert_gesamtbetrag/wert_liter;
            wert_verbrauch = 0;
            wert_vergangeneTage = 0;


            // insert into Database
            db_tankEintrag = myDbTankEintragHelper.getWritableDatabase();

            ContentValues values_tankEintrag = new ContentValues();
            values_tankEintrag.put(TankEintrag.COLUMN_NAME_TANKSTELLE, wert_tankstelle);
            values_tankEintrag.put(TankEintrag.COLUMN_NAME_DATUM, wert_datum);
            values_tankEintrag.put(TankEintrag.COLUMN_NAME_LITER, wert_liter);
            values_tankEintrag.put(TankEintrag.COLUMN_NAME_GESAMTBETRAG, wert_gesamtbetrag);
            values_tankEintrag.put(TankEintrag.COLUMN_NAME_KILOMETER, wert_kilometerstand);
            values_tankEintrag.put(TankEintrag.COLUMN_NAME_GESAMT_KILOMETER, wert_gesamtkilometer);
            values_tankEintrag.put(TankEintrag.COLUMN_NAME_PREIS_PRO_LITER, wert_ppl);
            values_tankEintrag.put(TankEintrag.COLUMN_NAME_VERBRAUCH, wert_verbrauch);
            values_tankEintrag.put(TankEintrag.COLUMN_NAME_VERGANGENE_TAGE, wert_vergangeneTage);

            db_tankEintrag.insert(TankEintrag.TABLE_NAME, null, values_tankEintrag);
            db_tankEintrag.close();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            System.out.println("Es sind nicht alle Daten eingetragen.");
        }


    }

}
