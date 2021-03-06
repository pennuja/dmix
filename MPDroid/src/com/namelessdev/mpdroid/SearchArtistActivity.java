package com.namelessdev.mpdroid;

import java.util.ArrayList;
import java.util.List;

import org.a0z.mpd.MPD;
import org.a0z.mpd.MPDServerException;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class SearchArtistActivity extends BrowseActivity {
	private List<String> musicList = null;
	String searchKeywords = "";

	public SearchArtistActivity() {
		super(R.string.addArtist, R.string.artistAdded, MPD.MPD_SEARCH_ARTIST);
		items = new ArrayList<String>();
		musicList = new ArrayList<String>();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Intent queryIntent = getIntent();
		final String queryAction = queryIntent.getAction();

		if (Intent.ACTION_SEARCH.equals(queryAction)) {
			searchKeywords = queryIntent.getStringExtra(SearchManager.QUERY);
		} else {
			return; // Bye !
		}
		setContentView(R.layout.artists);
		setTitle(getTitle() + " : " + searchKeywords);
		pd = ProgressDialog.show(SearchArtistActivity.this, getResources().getString(R.string.loading), getResources().getString(
				R.string.loadingArtists));

		registerForContextMenu(getListView());

		super.UpdateList();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(this, AlbumsActivity.class);
		intent.putExtra("artist", items.get(position));
		startActivityForResult(intent, -1);
	}

	@Override
	protected void asyncUpdate() {
		try {
			MPDApplication app = (MPDApplication) getApplication();
			musicList = app.oMPDAsyncHelper.oMPD.listArtists();
		} catch (MPDServerException e) {
		}

		searchKeywords = searchKeywords.toLowerCase().trim();
		for (String music : musicList) {
			if (music.toLowerCase().contains(searchKeywords))
				items.add(music);
		}
	}
}
