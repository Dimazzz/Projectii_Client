package org.projii.client.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.movingsamples.R;

public class MainMenuActivity extends Activity implements OnClickListener{

    Menu myMenu;  
 
	
	Button btnPlay,btnSettings,btnExit;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        
        btnPlay = (Button) findViewById(R.id.btnMainMenuPlay);
        btnPlay.setOnClickListener(this);
        btnSettings = (Button) findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(this);
        btnExit = (Button) findViewById(R.id.btnMainMenuExit);
        btnExit.setOnClickListener(this);
        
    }
    

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btnMainMenuPlay:
          		Intent intent = new Intent(this, SelectTypeOfGameActivity.class);
        		
      	    	startActivity(intent);
        	break;
        case R.id.btnMainMenuExit:
        	finish();
        	break;
        }
     }
    
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {        
        myMenu = menu;    
        menu.add (Menu.FIRST, 1, 1, "Player");    
        return true;  
    }
    
   
}
