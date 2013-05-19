package org.projii.client.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.movingsamples.R;

public class SelectTypeOfGameActivity extends Activity implements OnClickListener{

    Menu myMenu;  
 
	
	Button btnSelectRemoteServer,btnSelectBluetooth,btnSingle;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_type_game);
        
        btnSelectRemoteServer = (Button) findViewById(R.id.btnServer);
        btnSelectRemoteServer.setOnClickListener(this);
        btnSelectBluetooth = (Button) findViewById(R.id.btnBluetooth);
        btnSelectBluetooth.setOnClickListener(this);
        btnSingle = (Button) findViewById(R.id.btnvsDroid);
        btnSingle.setOnClickListener(this);
        
    }
    

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btnServer:
          		Intent intent = new Intent(this, LoginActivity.class);
        		
      	    	startActivity(intent);
        	break;
        case R.id.btnBluetooth:
        	
        	break;
        case R.id.btnvsDroid:
        	
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
