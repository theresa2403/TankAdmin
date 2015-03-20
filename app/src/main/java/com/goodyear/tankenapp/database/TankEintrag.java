package com.goodyear.tankenapp.database;

import android.provider.BaseColumns;

/**
 * Created by Theresa Reus on 16.02.2015.
 */
public class TankEintrag {

        public static final String TABLE_NAME = "tankeintrag";
        public static final String COLUMN_NAME_ID = "_id";
        public static final String COLUMN_NAME_TANKSTELLE = "tankstelle";
        public static final String COLUMN_NAME_DATUM = "datum";
        public static final String COLUMN_NAME_LITER = "liter";
        public static final String COLUMN_NAME_GESAMTBETRAG = "gesamtbetrag";
        public static final String COLUMN_NAME_KILOMETER = "kilometer";
        public static final String COLUMN_NAME_GESAMT_KILOMETER = "gesamtkilometer";
        public static final String COLUMN_NAME_PREIS_PRO_LITER = "ppl";
        public static final String COLUMN_NAME_VERBRAUCH = "verbrauch";
        public static final String COLUMN_NAME_VERGANGENE_TAGE = "vergangenetage";

}
