package de.koelly.pauker;

import de.koelly.pauker.PaukerProvider;
import de.koelly.pauker.Preferences;

import java.io.File;
import java.util.ArrayList;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Paukeroid extends Activity implements OnClickListener, OnSharedPreferenceChangeListener{
    
	private static final int PREFERENCES_ID = 0;
	private static final int ABOUT_ID = 1;
	private static final int DESCRIPTION = 2;
	private static String path = "";
	TextView title, question, answer;
    Button next, back;
    ProgressDialog myProgressDialog = null;
    boolean answerVisible = false;
    boolean askOrdered = true; //Not yet in use
    int status = 0;
    
    ArrayList<PaukerDataSet> data;
    PaukerProvider paPro = new PaukerProvider();
    

    
    private void next(){
    	if (answerVisible){
    		status++;
    		if (status+1 <= data.size()){
    			
    			int tmp_status = status + 1;
    	        title.setText("Frage: " + tmp_status + "/" + data.size());
    	        
    			question.setText(data.get(status).getFrontText());
    			answer.setText("");
    			
    			answerVisible = false;
    			next.setClickable(true);
    			back.setClickable(true);
    		} else {
    			question.setText("Keine weiteren Daten vorhanden!");
    			answer.setText("");
    			next.setClickable(false);
    			back.setClickable(true);
    		}
    	} else {
    		answer.setText(data.get(status).getBackText());
    		answerVisible = true;
    	}
    }
	
    private void back(){
    	status--;
    	if (status+1 > 0){
    		
    		int tmp_status = status + 1;
            title.setText("Frage: " + tmp_status + "/" + data.size());
            
    		question.setText(data.get(status).getFrontText());
    		answer.setText(data.get(status).getBackText());
    		
    		answerVisible = true;
    		next.setClickable(true);
    		back.setClickable(true);
    		
    		if (status == 0){
    			back.setClickable(false);
    			answerVisible = false;
    			answer.setText("");
    		}
       	}
    }

    private void drawUI(){
    	
        int tmp_status = status + 1;
        title.setText("Frage: " + tmp_status + "/" + data.size());
        
        if (status <= data.size()){
        	question.setText(data.get(status).getFrontText());
        } else {
        	question.setText("ERROR: Keine (weiteren) Daten vorhanden!");
        }

    }
    
    private void loadData(){
    	myProgressDialog = ProgressDialog.show(this, "Bitte warten...", "Lektionen werden geladen", true);
    	Thread t = new Thread() {
    		public void run(){
    			data = paPro.getData(path);
    			dataUp2Date.sendMessage(Message.obtain(dataUp2Date, 1));
    			myProgressDialog.dismiss();
    		}
    	};
    	t.start();
    }
    
    private Handler dataUp2Date = new Handler() {
        @Override
        public void handleMessage(Message msg) {  
            switch(msg.what) {
            	case 1:
            		drawUI();
            }
        }

    };
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Register the OnSharedPreferenceChangeListener
        Context context = getApplicationContext();
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.registerOnSharedPreferenceChangeListener(this);

        
        // Check if external Storage is readable/writeable
        // No Function, just good to know...
        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
        	Log.d("SD Status", "read & write");
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
        	Log.d("SD Status", "readonly");
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Something else is wrong. It may be one of many other states, but all we need
            //  to know is we can neither read nor write
        	Log.d("SD Status", "????");
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }
        
        // Create /sdcard/pauker/ directory 
        File dir = new File(Environment.getExternalStorageDirectory()+ "/pauker/");
        Log.d("neuer Pfad", ""+dir.getAbsolutePath());
        dir.mkdirs();
        
        // Register UI elements
        title = (TextView)findViewById(R.id.title);
        question = (TextView)findViewById(R.id.question);
        answer = (TextView)findViewById(R.id.answer);
        next = (Button)findViewById(R.id.next);
        back = (Button)findViewById(R.id.back);

        // This makes Q field scrollable if question is too long
        question.setMovementMethod(new ScrollingMovementMethod()); 
        
        // Register onClickHandler
        next.setOnClickListener(nextHandler);
        back.setOnClickListener(backHandler);


        //If no lesson selected, display warning in question field
        if (prefs.getString("lesson", "").equalsIgnoreCase("")){
        	question.setText("Bitte Lektion wählen!\nInfos dazu im Menü unter \"About\" ");
        } else {
        	//Let's go!
        	path = Environment.getExternalStorageDirectory()+ "/pauker/" + prefs.getString("lesson", "");
            loadData();
        }
        

          
	}
    
    
    
    private OnClickListener nextHandler = new OnClickListener(){
		@Override
		public void onClick(View v) {
			next();
		}    	
    };

    private OnClickListener backHandler = new OnClickListener(){
		@Override
		public void onClick(View v) {
			back();
		}    	
    };

    public boolean onPrepareOptionsMenu(Menu menu) {
		
		menu.clear();
    	
        menu.add(1, PREFERENCES_ID, 0, "Preferences").setIcon(android.R.drawable.ic_menu_preferences);
        menu.add(2, ABOUT_ID, 0, "About").setIcon(android.R.drawable.ic_menu_info_details);
        menu.add(3, DESCRIPTION, 0, "Lektionsbeschreibung").setIcon(android.R.drawable.ic_menu_help);
        
        return true;
	
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    		
    	case PREFERENCES_ID:
    		startActivity(new Intent(this, Preferences.class));
    		return(true);

    	case ABOUT_ID:
        	Intent j = new Intent(Paukeroid.this, ViewInfo.class);
        	Bundle info_bundle = new Bundle();
        	info_bundle.clear();
        	info_bundle.putString("type", "info");
        	info_bundle.putString("info",	"<b>PAUKDROID</b><br /><br />" +
        											"Paukdroid liest die Lektionen des Programmes Pauker. Hoffentlich.<br /><br />" +
        											"<b>WICHTIG!</b><br />die XML oder PAU Dateien müssen auf der SD Card im Ordner \"pauker\" liegen!<br /><br />" +
        											"Diese Version ist weit davon entfernt gut zu sein. Momentan" +
        											"befindet sie sich eher in einem Proof-of-Concept Status<br /><br />" +
        											"Pauker Homepage: <a href=\"http://pauker.sourceforge.net\">pauker.sourceforge.net</a><br /> " +
        											"Meine Homepage: <a href=\"http://www.koelly.de\">koelly.de</a><br /><br />" +
        											"Viel Spaß!<br />Kölly");
        	j.putExtras(info_bundle);
        	startActivity(j);
        	return(true);
    		
    	case DESCRIPTION:
        	Intent i = new Intent(Paukeroid.this, ViewInfo.class);
        	Bundle bundle = new Bundle();
        	bundle.clear();
        	bundle.putString("type", "description");
        	bundle.putString("description", paPro.getDescription());
        	Log.d("Menu","übergeben: "+paPro.getDescription());
        	i.putExtras(bundle);
        	startActivity(i);
        	return true;
        	
    	}
    return(super.onOptionsItemSelected(item));
    }
    
    
	@Override
	public void onClick(View v) {
	}

	// If lesson is changed in preferences menu, this fancy listener gets it
	@Override
	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
		if (key.equalsIgnoreCase("lesson")){
			Log.d("Neues Kartenset gewählt", Environment.getExternalStorageDirectory()+ "/pauker/" + prefs.getString(key, ""));
			path = Environment.getExternalStorageDirectory()+ "/pauker/" + prefs.getString(key, "");
			loadData();
			status = 0;
		} else if (key.equalsIgnoreCase("ordered")){
			askOrdered = prefs.getBoolean(key, true);
		}
	}

}
