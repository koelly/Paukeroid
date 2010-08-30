package de.koelly.pauker;

import java.io.File;

import android.os.Bundle;
import android.os.Environment;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.util.Log;

public class Preferences extends PreferenceActivity{
	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setPreferenceScreen(createPreferenceHierarchy());
	}
	
	
	private PreferenceScreen createPreferenceHierarchy() {
		PreferenceScreen root = getPreferenceManager().createPreferenceScreen(this);
		
		PreferenceCategory dialogBasedPrefCat = new PreferenceCategory(this);
		dialogBasedPrefCat.setTitle("Lektionsauswahl");
		root.addPreference(dialogBasedPrefCat);
		
		
		File f = new File(Environment.getExternalStorageDirectory()+ "/pauker/");
		File[] files = f.listFiles();
		
		int count = files.length;
		Log.d("Anzahl files: ",""+count);
		Log.d("File 0: ",""+files[0].getName());
		Log.d("File 1: ",""+files[1].getName());
		
		CharSequence[] entries = new CharSequence[count];
		CharSequence[] entryValues = new CharSequence[count];
		
		for(int i=0; i<count; i++) {
			entries[i] = files[i].getName();
			entryValues[i] = files[i].getName();
		}
		
		ListPreference targets = new ListPreference(this);
		targets.setKey("lesson");
		targets.setEntries(entries);
		targets.setEntryValues(entryValues);
		targets.setDialogTitle("Lektion auswählen");
		targets.setTitle("Lektion wählen");
		targets.setSummary("Welche Datei soll geöffnet werden?");
		targets.setDefaultValue(files[0].getName());
		dialogBasedPrefCat.addPreference(targets);
		
		
		PreferenceCategory options = new PreferenceCategory(this);
		options.setTitle("Reihenfolge");
		root.addPreference(options);
		
		CheckBoxPreference order = new CheckBoxPreference(this);
		order.setKey("ordered");
		order.setTitle("Fragen geordnet");
		order.setSummary("Noch ohne Funktion.");
		options.addPreference(order);
		
		
		return root;
	}
	
	
}
