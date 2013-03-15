package org.projii.client.menu;

import com.example.movingsamples.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ShipActivity extends Activity implements OnClickListener {

	Button btnShip1;
	Button btnShip2;
	Button btnShip3;
	Button btnJoinGame;
	Button btnShipSetting;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ship);
        
        btnShip1 = (Button) findViewById(R.id.btnShip1);
        btnShip1.setOnClickListener(this);
        btnShip2 = (Button) findViewById(R.id.btnShip2);
        btnShip2.setOnClickListener(this);
        btnShip3 = (Button) findViewById(R.id.btnShip3);
        btnShip3.setOnClickListener(this);
        btnJoinGame = (Button) findViewById(R.id.btnJoinGame);
        btnJoinGame.setOnClickListener(this);
        btnShipSetting = (Button) findViewById(R.id.btnShipSetting);
        btnShipSetting.setOnClickListener(this);        
        btnShip2.setText("Ship 2");
    	
	  
	}
	
	@Override
	public void onClick(View v) {      
		switch (v.getId()) {
        case R.id.btnShip1:
        	/* 2 метода
        	 * 1) для кнопки где есть ship (Выделение кнопки)
        	 * 2) если нет корабля, то создание нового окна для его создания
        	 */
        	btnShip1.setBackgroundResource(R.drawable.button2_small_up);
        	btnShip2.setBackgroundResource(R.drawable.button2_small);
        	btnShip3.setBackgroundResource(R.drawable.button2_small);
      	    break;
        case R.id.btnShip2:
    		btnShip1.setBackgroundResource(R.drawable.button2_small);
    		btnShip2.setBackgroundResource(R.drawable.button2_small_up);
    		btnShip3.setBackgroundResource(R.drawable.button2_small);
        	break;
        case R.id.btnShip3:
        	
        	break;
        case R.id.btnJoinGame:
        	Intent intentJoinGame = new Intent(this, JoinGameActivity.class);
        	startActivity(intentJoinGame); 
        	break;
        case R.id.btnShipSetting:
        	Intent intentShipSetting = new Intent(this, ShipDescriptionActivity.class);
        	startActivity(intentShipSetting); 
        	break;
        }
	}

}