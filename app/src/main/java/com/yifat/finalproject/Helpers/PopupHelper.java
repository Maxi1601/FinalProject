package com.yifat.finalproject.Helpers;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.yifat.finalproject.MainActivity;
import com.yifat.finalproject.Place;
import com.yifat.finalproject.R;

public class PopupHelper implements PopupMenu.OnMenuItemClickListener {

    // region Properties
    private Place sharedPlace;
    private Place favoritePlace;
    public FavoritesHelperCallback callback;
    private View view;
    private Activity activity;
    // endregion

    //region Constructor
    public PopupHelper(Activity activity, View view, Place favoritePlace) {
        this.activity = activity;
        this.view = view;
        this.favoritePlace = favoritePlace;
    }
    //endregion

    // Creating and showing the popup
    public void showPopup() {

        PopupMenu popup = new PopupMenu(activity, view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.menu_place);

        // Managing items titles:
        MenuItem item = popup.getMenu().findItem(R.id.favoriteAction);
        if (MainActivity.sharedFavoritesLogic.isFavorite(favoritePlace)) {
            item.setTitle(Constants.POPUP_REMOVE);
        } else {
            //Add to Favorites
            item.setTitle(Constants.POPUP_ADD);
            // by default the text will be "Add to favorites"
        }
        popup.show();

    }

    // Called when a menu item has been invoked
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.share:
                Place sharedPlace = this.favoritePlace;
                this.favoritePlace = null;

                String placeName = sharedPlace.getName();
                String placeAddress = sharedPlace.getAddress();

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, "Place's name: " + placeName + "\n" + "Address: " + placeAddress);
                intent.setType("text/plain");
                activity.startActivity(intent);
                break;

            case R.id.favoriteAction:
                Place place = this.favoritePlace;
                this.favoritePlace = null;
                String itemTitle = item.getTitle().toString();
                if (itemTitle.equals(Constants.POPUP_REMOVE)) {
                    long affectedRows = MainActivity.sharedFavoritesLogic.deleteFavorite(place);
                    callback.didRemoveFavorite(this, place);
                    assert affectedRows == 1;
                    break;

                }
                long createdId = MainActivity.sharedFavoritesLogic.addFavorite(place);
                if (createdId != -1) {
                    callback.didAddFavorite(this, place);
                }
                break;

        }

        return true;

    }

    // Creating Interface which can be implemented by any activity/fragment
    public interface FavoritesHelperCallback {
        void didAddFavorite(PopupHelper popupHelper, Place favoritesPlace);
        void didRemoveFavorite(PopupHelper popupHelper, Place favoritesPlace);
    }

}
