package de.koelly.pauker;

import java.util.regex.Pattern;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

public class ViewInfo extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewinfo);
        
        Log.d("ViewInfo", "onCreate");
        
        TextView contentView = (TextView)findViewById(R.id.content);
        
                
        Bundle bundle = this.getIntent().getExtras();
        
        if (bundle.getString("type").equalsIgnoreCase("description")){
        	String content = bundle.getString("description");
           	contentView.setText(content);
        } else if (bundle.getString("type").equalsIgnoreCase("info")){
        	String content = bundle.getString("info");
           	//contentView.setText(content);
           	contentView.setText(Html.fromHtml(content));
           	contentView.setMovementMethod(LinkMovementMethod.getInstance());
        	
        }
    }

}
