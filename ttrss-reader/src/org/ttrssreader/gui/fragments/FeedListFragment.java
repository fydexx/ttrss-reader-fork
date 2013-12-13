/*
 * ttrss-reader-fork for Android
 * 
 * Copyright (C) 2010 N. Braden.
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 3 as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 */

package org.ttrssreader.gui.fragments;

import org.ttrssreader.R;
import org.ttrssreader.controllers.Controller;
import org.ttrssreader.gui.MenuActivity;
import org.ttrssreader.gui.dialogs.YesNoUpdaterDialog;
import org.ttrssreader.gui.interfaces.IItemSelectedListener.TYPE;
import org.ttrssreader.model.FeedAdapter;
import org.ttrssreader.model.updaters.ReadStateUpdater;
import org.ttrssreader.model.updaters.UnsubscribeUpdater;
import org.ttrssreader.model.updaters.Updater;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import com.actionbarsherlock.view.Menu;

public class FeedListFragment extends MainListFragment {
    
    protected static final TYPE THIS_TYPE = TYPE.FEED;
    public static final String FRAGMENT = "FEED_FRAGMENT";
    
    public static final String FEED_CAT_ID = "FEED_CAT_ID";
    public static final String FEED_CAT_TITLE = "FEED_CAT_TITLE";
    
    // Extras
    private int categoryId;
    
    public static FeedListFragment newInstance(int id) {
        // Create a new fragment instance
        FeedListFragment detail = new FeedListFragment();
        detail.categoryId = id;
        detail.setHasOptionsMenu(true);
        detail.setRetainInstance(true);
        return detail;
    }
    
    @Override
    public void onCreate(Bundle instance) {
        Controller.getInstance().lastOpenedFeeds.clear();
        if (instance != null)
            categoryId = instance.getInt(FEED_CAT_ID);
        super.onCreate(instance);
    }
    
    @Override
    public void onActivityCreated(Bundle instance) {
        adapter = new FeedAdapter(getActivity(), categoryId);
        setListAdapter(adapter);
        super.onActivityCreated(instance);
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(FEED_CAT_ID, categoryId);
        super.onSaveInstanceState(outState);
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(MenuActivity.MARK_GROUP, MenuActivity.UNSUBSCRIBE, Menu.NONE, R.string.Subscribe_unsubscribe);
    }
    
    @Override
    public boolean onContextItemSelected(android.view.MenuItem item) {
        AdapterContextMenuInfo cmi = (AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case MenuActivity.MARK_READ:
                new Updater(getActivity(), new ReadStateUpdater(adapter.getId(cmi.position), 42)).exec();
                return true;
            case MenuActivity.UNSUBSCRIBE:
                YesNoUpdaterDialog dialog = YesNoUpdaterDialog
                        .getInstance(getActivity(), new UnsubscribeUpdater(adapter.getId(cmi.position)),
                                R.string.Dialog_unsubscribeTitle, R.string.Dialog_unsubscribeText);
                dialog.show(getFragmentManager(), YesNoUpdaterDialog.DIALOG);
                return true;
        }
        return false;
    }
    
    @Override
    public TYPE getType() {
        return THIS_TYPE;
    }
    
    public int getCategoryId() {
        return categoryId;
    }
    
}
