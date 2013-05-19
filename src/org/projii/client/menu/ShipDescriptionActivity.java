package org.projii.client.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.example.movingsamples.R;

public class ShipDescriptionActivity extends Activity implements OnClickListener {

	Button btnShip;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ship_description);
        
        btnShip= (Button) findViewById(R.id.btnShip);
        btnShip.setOnClickListener(this);
    }

    @Override
	public void onClick(View v) {
		switch (v.getId()) {
        case R.id.btnShip:
        	Intent intentStruct = new Intent(this, ShipStructureActivity.class);
  	    	startActivity(intentStruct);  
      	    break;
		}
	}
}
