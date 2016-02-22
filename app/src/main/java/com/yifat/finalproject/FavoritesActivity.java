package com.yifat.finalproject;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.yifat.finalproject.Helpers.Constants;
import com.yifat.finalproject.Helpers.PopupHelper;
import com.yifat.finalproject.Helpers.Types;

public class FavoritesActivity extends AppCompatActivity implements PopupHelper.FavoritesHelperCallback, FavoritesFragment.Callbacks {

    private Toolbar toolbar;
    private String deviceType;
    private FavoritesFragment favoritesFragment;
    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        // Set a toolbar to replace the action bar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        deviceType = (String) findViewById(R.id.linearLayoutRoot).getTag();

        Intent intent = new Intent();

        favoritesFragment = new FavoritesFragment();
        favoritesFragment.callbacks = this;

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayoutContainer, favoritesFragment)
                .commit();

        // If the device is tablet - show both fragments:
        if (deviceType.equals("tablet")) {

            mapFragment = new MapFragment();

            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.frameLayoutContainerMap, mapFragment)
                    .commit();

        }

    }

    // Creating OPTIONS MENU:
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // The possible actions of the menu:
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent = null;

        switch (item.getItemId()) {

            case android.R.id.home:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);

            case R.id.action_search:
                EditText editTextSearch = (EditText) toolbar.findViewById(R.id.editTextSearch);
                String term = editTextSearch.getText().toString();
//                resultsFragment.searchByTerm(term);
                break;

            case R.id.action_favorites:
                break;

            case R.id.km:
                Types.DistanceFormat format = Types.DistanceFormat.METER;
                favoritesFragment.changeDistanceFormat(format);
                break;

            case R.id.miles:
                favoritesFragment.changeDistanceFormat(Types.DistanceFormat.FEET);
                break;

            case R.id.deleteAllFavorites:
                favoritesFragment.removeAllFavorites();
                Toast.makeText(this, "All Favorites have been deleted", Toast.LENGTH_LONG).show();
                break;

        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void didAddFavorite(PopupHelper popupHelper, Place favoritesPlace) {
    }

    @Override
    public void didRemoveFavorite(PopupHelper popupHelper, Place favoritesPlace) {

        favoritesFragment.removeFavorite();

    }

    @Override
    public void didClick(FavoritesFragment fragment, Place place) {

        if (deviceType.equals("phone")) {

        Intent intent = new Intent(FavoritesActivity.this, MapActivity.class);
        intent.putExtra(Constants.LATITUDE, place.getLat());
        intent.putExtra(Constants.LONGITUDE, place.getLng());
        startActivity(intent);

        } else if (deviceType.equals("tablet")) {

            mapFragment.setNewPlace(place);

        }
    }

    @Override
    public void didLongClick(FavoritesFragment fragment, View pressedView, Place place) {

        if (place == null) {
            return;
        }
        PopupHelper popupHelper = new PopupHelper(this, pressedView, place);
        popupHelper.callback = this;

        popupHelper.showPopup();

    }

    @Override
    protected void onResume() {
        super.onResume();
        MainActivity.sharedFavoritesLogic.open();

        ComponentName component = new ComponentName(this, PowerReceiver.class);
        getPackageManager()
                .setComponentEnabledSetting(component,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onPause() {
        super.onPause();

        ComponentName component = new ComponentName(this, PowerReceiver.class);
        getPackageManager()
                .setComponentEnabledSetting(component,
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);
    }

}
