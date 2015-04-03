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


public class NewDataActivity extends Activity {

    private TankEintragDbHelper myDbTankEintragHelper = new TankEintragDbHelper(NewDataActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_data);

        SQLiteDatabase db_tankEintrag = myDbTankEintragHelper.getWritableDatabase();
        Cursor cursor= db_tankEintrag.rawQuery("SELECT DISTINCT " + TankEintrag.COLUMN_NAME_TANKSTELLE + " FROM " + TankEintrag.TABLE_NAME + " ORDER BY " + TankEintrag.COLUMN_NAME_DATUM, null);
        ArrayList<String> liste = new ArrayList<String>();
        while(cursor.moveToNext()) {
            liste.add(cursor.getString(cursor.getColumnIndexOrThrow(TankEintrag.COLUMN_NAME_TANKSTELLE)));
        }
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.tankstelle);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, liste);
        textView.setAdapter(adapter);
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
        String wert_tankstelle, wert_datum;
        double wert_liter, wert_gesamtbetrag, wert_gesamtkilometer, wert_ppl;

        DecimalFormat integerFormat = new DecimalFormat("00");
        DecimalFormat decimalFormat = new DecimalFormat("#.00");

        System.out.println("Eingaben werden überprüft.");

        EditText tankstelle = (EditText) findViewById(R.id.tankstelle);
        DatePicker datum = (DatePicker) findViewById(R.id.datePicker);
        EditText liter = (EditText) findViewById(R.id.liter);
        EditText gesamtbetrag = (EditText) findViewById(R.id.gesamtbetrag);
        EditText kilometerstand = (EditText) findViewById(R.id.kilometer);

        if((tankstelle.getText().length() > 0) && (liter.getText().length() > 0) && (gesamtbetrag.getText().length() > 0) && (kilometerstand.getText().length() > 0)) {
            // Werte in Variablen speichern
            wert_tankstelle = tankstelle.getText().toString();
            wert_datum = datum.getYear() + "-" + integerFormat.format(datum.getMonth()+1) + "-" + integerFormat.format(datum.getDayOfMonth());
            System.out.println(wert_datum);
            wert_liter = Double.parseDouble(liter.getText().toString());
            wert_gesamtbetrag = Double.parseDouble(gesamtbetrag.getText().toString());
            wert_gesamtkilometer = Double.parseDouble(kilometerstand.getText().toString());
            wert_ppl = wert_gesamtbetrag/wert_liter;

            // in Datenbank eintragen
            SQLiteDatabase db_tankEintrag = myDbTankEintragHelper.getWritableDatabase();

            ContentValues values_tankEintrag = new ContentValues();
            values_tankEintrag.put(TankEintrag.COLUMN_NAME_TANKSTELLE, wert_tankstelle);
            values_tankEintrag.put(TankEintrag.COLUMN_NAME_DATUM, wert_datum);
            values_tankEintrag.put(TankEintrag.COLUMN_NAME_LITER, decimalFormat.format(wert_liter));
            values_tankEintrag.put(TankEintrag.COLUMN_NAME_GESAMTBETRAG, decimalFormat.format(wert_gesamtbetrag));
            values_tankEintrag.put(TankEintrag.COLUMN_NAME_GESAMT_KILOMETER, integerFormat.format(wert_gesamtkilometer));
            values_tankEintrag.put(TankEintrag.COLUMN_NAME_PREIS_PRO_LITER, decimalFormat.format(wert_ppl));

            db_tankEintrag.insert(TankEintrag.TABLE_NAME, null, values_tankEintrag);
            db_tankEintrag.close();

            finish();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            System.out.println("Es sind nicht alle Daten eingetragen.");
            Toast.makeText(getApplicationContext(),"Bitte alle Daten eintragen" ,Toast.LENGTH_LONG);
        }


    }

    // method for menu item "Settings"
    public boolean changeSettings (MenuItem item) {
        Intent intent = new Intent(this,DisplaySettingsActivity.class);
        startActivity(intent);
        return true;
    }

}
