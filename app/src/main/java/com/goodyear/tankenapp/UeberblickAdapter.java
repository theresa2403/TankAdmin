package com.goodyear.tankenapp;

import android.content.Context;
import android.database.Cursor;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.goodyear.tankenapp.database.TankEintrag;

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
        // Extract properties from cursor
        String datum = cursor.getString(cursor.getColumnIndexOrThrow(TankEintrag.COLUMN_NAME_DATUM));
        String gesamtkilometer = cursor.getString(cursor.getColumnIndexOrThrow(TankEintrag.COLUMN_NAME_GESAMT_KILOMETER)) + " km";
        String liter = cursor.getString(cursor.getColumnIndexOrThrow(TankEintrag.COLUMN_NAME_LITER)) + " l";
        String kilometer = cursor.getString(cursor.getColumnIndexOrThrow(TankEintrag.COLUMN_NAME_KILOMETER)) + " km";
        String ppl = cursor.getString(cursor.getColumnIndexOrThrow(TankEintrag.COLUMN_NAME_PREIS_PRO_LITER)) + " €/l";
        String tage = cursor.getString(cursor.getColumnIndexOrThrow(TankEintrag.COLUMN_NAME_VERGANGENE_TAGE)) + " Tage";
        String tankstelle = cursor.getString(cursor.getColumnIndexOrThrow(TankEintrag.COLUMN_NAME_TANKSTELLE));
        String verbrauch = cursor.getString(cursor.getColumnIndexOrThrow(TankEintrag.COLUMN_NAME_VERBRAUCH)) + "l/100km";
        String gesamtpreis = cursor.getString(cursor.getColumnIndexOrThrow(TankEintrag.COLUMN_NAME_GESAMTBETRAG)) + " €";
        // Populate fields with extracted properties
        textview_datum.setText(datum);
        textview_gesamtkilometer.setText(String.valueOf(gesamtkilometer));
        textview_liter.setText(liter);
        textview_kilometer.setText(kilometer);
        textview_ppl.setText(ppl);
        textview_tage.setText(tage);
        textview_tankstelle.setText(tankstelle);
        textview_verbrauch.setText(verbrauch);
        textview_gesamtpreis.setText(gesamtpreis);
    }
}
