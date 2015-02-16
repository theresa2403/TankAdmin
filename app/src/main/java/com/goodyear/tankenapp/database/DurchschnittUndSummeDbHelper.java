package com.goodyear.tankenapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Theresa Reus on 16.02.2015.
 */
public class DurchschnittUndSummeDbHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TankAdmin.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DurchschnittUndSummen.TABLE_NAME + " (" +
                    DurchschnittUndSummen.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    DurchschnittUndSummen.COLUMN_NAME_DURCHSCHNITT_KOSTEN_PRO_KM + REAL_TYPE + COMMA_SEP +
                    DurchschnittUndSummen.COLUMN_NAME_DURCHSCHNITT_LITERPREIS + REAL_TYPE + COMMA_SEP +
                    DurchschnittUndSummen.COLUMN_NAME_DURCHSCHNITT_VERGANGENE_TAGE + REAL_TYPE + COMMA_SEP +
                    DurchschnittUndSummen.COLUMN_NAME_SUMME_GEFAHRENE_KILOMETER + REAL_TYPE + COMMA_SEP +
                    DurchschnittUndSummen.COLUMN_NAME_SUMME_KOSTEN + REAL_TYPE + COMMA_SEP +
                    DurchschnittUndSummen.COLUMN_NAME_SUMME_LITER + REAL_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TankEintrag.TABLE_NAME;


    public DurchschnittUndSummeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
