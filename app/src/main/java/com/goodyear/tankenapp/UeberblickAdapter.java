package com.goodyear.tankenapp;

import android.app.LauncherActivity;
import android.content.Context;
import android.database.Cursor;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.goodyear.tankenapp.database.TankEintrag;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Theresa Reus on 19.03.2015.
 */
public class UeberblickAdapter extends CursorAdapter {

    public UeberblickAdapter(Context context, Cursor cursor) {
        super(context,cursor,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.ueberblick_list, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView textview_datum = (TextView) view.findViewById(R.id.ueberblick_datum);
        TextView textview_gesamtkilometer = (TextView) view.findViewById(R.id.ueberblick_gesamtkilometer);
        TextView textview_liter = (TextView) view.findViewById(R.id.ueberblick_gesamtliter);
        TextView textview_kilometer = (TextView) view.findViewById(R.id.ueberblick_kilometer);
        TextView textview_ppl = (TextView) view.findViewById(R.id.ueberblick_ppl);
        TextView textview_tage = (TextView) view.findViewById(R.id.ueberblick_tage);
        TextView textview_tankstelle = (TextView) view.findViewById(R.id.ueberblick_tankstelle);
        TextView textview_verbrauch = (TextView) view.findViewById(R.id.ueberblick_verbrauch);
        TextView textview_gesamtpreis = (TextView) view.findViewById(R.id.ueberblick_gesamtpreis);
        TextView textview_id = (TextView) view.findViewById(R.id.list_item_id);
        // Extract properties from cursor
        String datum = cursor.getString(cursor.getColumnIndexOrThrow(TankEintrag.COLUMN_NAME_DATUM));
        String gesamtkilometer = cursor.getString(cursor.getColumnIndexOrThrow(TankEintrag.COLUMN_NAME_GESAMT_KILOMETER)) + " km";
        Double liter = cursor.getDouble(cursor.getColumnIndexOrThrow(TankEintrag.COLUMN_NAME_LITER));
        String tankstelle = cursor.getString(cursor.getColumnIndexOrThrow(TankEintrag.COLUMN_NAME_TANKSTELLE));
        String ppl = cursor.getDouble(cursor.getColumnIndexOrThrow(TankEintrag.COLUMN_NAME_PREIS_PRO_LITER)) + " €/l";
        String gesamtpreis = cursor.getString(cursor.getColumnIndexOrThrow(TankEintrag.COLUMN_NAME_GESAMTBETRAG)) + " €";
        String id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));

        DecimalFormat decimalFormat2 = new DecimalFormat("#0.00");
        DecimalFormat decimalFormat1 = new DecimalFormat("#0.0");

        String kilometer, tage, verbrauch;
        if(cursor.isFirst()) {
            kilometer = "k.A.";
            tage = "k.A.";
            verbrauch = "k.A.";
        } else {

            double gesamtkilometer_neu = cursor.getDouble(cursor.getColumnIndexOrThrow(TankEintrag.COLUMN_NAME_GESAMT_KILOMETER));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date datum_neu = new Date();
            try {
                datum_neu = dateFormat.parse(datum);
            } catch (ParseException e) {
            }
            cursor.moveToPrevious();
            double gesamtkilometer_alt = cursor.getDouble(cursor.getColumnIndexOrThrow(TankEintrag.COLUMN_NAME_GESAMT_KILOMETER));

            Date datum_alt = new Date();
            try {
                datum_alt = dateFormat.parse(cursor.getString(cursor.getColumnIndexOrThrow(TankEintrag.COLUMN_NAME_DATUM)));
            } catch (ParseException e) {
            }
            double gefahreneKilometer = gesamtkilometer_neu - gesamtkilometer_alt;
            kilometer = decimalFormat1.format(gefahreneKilometer) + " km";

            long vergangeneTage = TimeUnit.MILLISECONDS.toDays(datum_neu.getTime() - datum_alt.getTime());

            tage = vergangeneTage + " Tage";

            verbrauch = decimalFormat2.format(liter / gefahreneKilometer * 100) + " l/100km";
            cursor.moveToNext();
        }

        // Populate fields with extracted properties
        textview_id.setText(id);
        textview_datum.setText(datum);
        textview_gesamtkilometer.setText(String.valueOf(gesamtkilometer));
        textview_liter.setText(liter + " l");
        textview_kilometer.setText(kilometer);
        textview_ppl.setText(ppl);
        textview_tage.setText(tage);
        textview_tankstelle.setText(tankstelle);
        textview_verbrauch.setText(verbrauch);
        textview_gesamtpreis.setText(gesamtpreis);
    }
}
