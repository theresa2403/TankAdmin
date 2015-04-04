package com.goodyear.tankenapp;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LauncherActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.goodyear.tankenapp.database.TankEintrag;
import com.goodyear.tankenapp.database.TankEintragDbHelper;


public class UeberblickActivity extends Activity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ueberblick);

        // Einstellungen für Action Bar
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the two
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ueberblick, menu);
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

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return UeberblickFragment.newInstance(position);
                case 1:
                    return DurchschnittSummeFragment.newInstance(position);
            }


            return UeberblickFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_tankdaten).toUpperCase(l);
                case 1:
                    return getString(R.string.title_durchschnitt_summen).toUpperCase(l);
            }
            return null;
        }
    }


    public static class UeberblickFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static UeberblickFragment newInstance(int sectionNumber) {
            UeberblickFragment fragment = new UeberblickFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public UeberblickFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            TankEintragDbHelper myDbTankEintragHelper = new TankEintragDbHelper(getActivity());
            final SQLiteDatabase db_tankEintrag = myDbTankEintragHelper.getReadableDatabase();
            Cursor cursor_ueberblick= db_tankEintrag.rawQuery("SELECT *  FROM " + TankEintrag.TABLE_NAME + " ORDER BY " + TankEintrag.COLUMN_NAME_DATUM, null);
            final UeberblickAdapter ueberblickAdapter = new UeberblickAdapter(this.getActivity(), cursor_ueberblick);

            View rootView = inflater.inflate(R.layout.fragment_ueberblick, container, false);

            final ListView listView = (ListView) rootView.findViewById(R.id.testList);
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    Cursor cursor = (Cursor)parent.getItemAtPosition(position);
                    final String id_db = cursor.getString(cursor.getColumnIndexOrThrow("_id"));

                    builder.setMessage(R.string.loeschen_text);
                    builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            db_tankEintrag.delete(TankEintrag.TABLE_NAME, "_id=" + id_db, null);
                            Toast.makeText(getActivity().getApplicationContext(), R.string.toast_geloescht, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(view.getContext(), UeberblickActivity.class);
                            view.getContext().startActivity(intent);
                        }
                    });
                    builder.setNegativeButton(R.string.dialog_cancel, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    return true;
                }
            });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Cursor cursor = (Cursor)parent.getItemAtPosition(position);
                    String id_db = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                    Intent intent = new Intent(view.getContext(), EditDataActivity.class);
                    Bundle b = new Bundle();
                    b.putString("id_db", id_db); //Your id
                    intent.putExtras(b); //Put your id to your next Intent
                    startActivity(intent);
                }
            });

            listView.setAdapter(ueberblickAdapter);
            return rootView;
        }
    }

    public static class DurchschnittSummeFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static DurchschnittSummeFragment newInstance(int sectionNumber) {
            DurchschnittSummeFragment fragment = new DurchschnittSummeFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public DurchschnittSummeFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_durchschnitt_summen, container, false);
            DecimalFormat decimalFormat = new DecimalFormat("#0.00");

            TankEintragDbHelper myDbTankEintragHelper = new TankEintragDbHelper(getActivity());
            SQLiteDatabase db_tankEintrag = myDbTankEintragHelper.getReadableDatabase();
            Cursor cursor= db_tankEintrag.rawQuery("SELECT *  FROM " + TankEintrag.TABLE_NAME + " ORDER BY " + TankEintrag.COLUMN_NAME_DATUM, null);

            int gesamtkilometer = 0;
            double gesamtliter = 0.0;
            double gesamtkosten = 0.00;
            double kostenKilometer = 0.00;
            double literpreis = 0.00;
            long vergangeneTage = 0;
            if(cursor.getCount()>0) {
                double summe_literpreis = 0.00;
                int anzahl_rows = 0;
                String lastDate, firstDate;

                while (cursor.moveToNext()) {
                    gesamtliter += cursor.getDouble(cursor.getColumnIndexOrThrow(TankEintrag.COLUMN_NAME_LITER));
                    gesamtkosten += cursor.getDouble(cursor.getColumnIndexOrThrow(TankEintrag.COLUMN_NAME_GESAMTBETRAG));
                    summe_literpreis += cursor.getDouble(cursor.getColumnIndexOrThrow(TankEintrag.COLUMN_NAME_PREIS_PRO_LITER));
                    anzahl_rows ++;
                }

                cursor.moveToLast();
                gesamtkilometer = cursor.getInt(cursor.getColumnIndexOrThrow(TankEintrag.COLUMN_NAME_GESAMT_KILOMETER));
                lastDate = cursor.getString(cursor.getColumnIndexOrThrow(TankEintrag.COLUMN_NAME_DATUM));

                cursor.moveToFirst();
                firstDate = cursor.getString(cursor.getColumnIndexOrThrow(TankEintrag.COLUMN_NAME_DATUM));

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date datum_first = new Date();
                Date datum_last = new Date();
                try {
                    datum_first = dateFormat.parse(firstDate);
                    datum_last = dateFormat.parse(lastDate);
                } catch (ParseException e) {
                }

                vergangeneTage = (long) (TimeUnit.MILLISECONDS.toDays(datum_last.getTime() - datum_first.getTime()))/(anzahl_rows-1);

//

                kostenKilometer = gesamtkosten/gesamtkilometer;
                literpreis = summe_literpreis / anzahl_rows;
            }


            TextView text_vergangeneTage = (TextView) rootView.findViewById(R.id.sd_vergangeneTage);
            text_vergangeneTage.setText(vergangeneTage + " Tage");

            TextView text_gesamtkilometer = (TextView) rootView.findViewById(R.id.sd_kilometer);
            text_gesamtkilometer.setText(gesamtkilometer + " km");

            TextView text_gesamtliter = (TextView) rootView.findViewById(R.id.sd_gesamtliter);
            text_gesamtliter.setText(decimalFormat.format(gesamtliter) + " l");

            TextView text_gesamtkosten = (TextView) rootView.findViewById(R.id.sd_gesamtkosten);
            text_gesamtkosten.setText(decimalFormat.format(gesamtkosten) + " €");

            TextView text_kostenKilometer = (TextView) rootView.findViewById(R.id.sd_kostenKilometer);
            text_kostenKilometer.setText(decimalFormat.format(kostenKilometer) + " €/km");

            TextView text_ppl = (TextView) rootView.findViewById(R.id.sd_literpreis);
            text_ppl.setText(decimalFormat.format(literpreis) + " €/l");

            return rootView;
        }
    }

    // method for menu item "Settings"
    public boolean changeSettings (MenuItem item) {
        Intent intent = new Intent(this,DisplaySettingsActivity.class);
        startActivity(intent);
        return true;
    }

}
