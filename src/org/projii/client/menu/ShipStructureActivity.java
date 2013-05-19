package org.projii.client.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.movingsamples.R;

public class ShipStructureActivity extends Activity implements OnClickListener{

	Button btnJoin, btnInformation;
	Button btnEngine, btnGenerator, btnShield, btnWeapon;
	Button btnShipBranch, btnShip1, btnShip2, btnShip3;
	View viewEngineSpeed1;
	RelativeLayout relativeLayoutBranch;
	private static final String TAG = "MyApp";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ship_structure);
        relativeLayoutBranch = (RelativeLayout) findViewById(R.id.relativeLayoutBranch);

        btnShipBranch = (Button) findViewById(R.id.btnShipBranch);
        btnShipBranch.setOnClickListener(this);
        btnShip1 = (Button) findViewById(R.id.btnShip1);
        btnShip1.setOnClickListener(this);
        btnShip2 = (Button) findViewById(R.id.btnShip2);
        btnShip2.setOnClickListener(this);
        btnShip3 = (Button) findViewById(R.id.btnShip3);
        btnShip3.setOnClickListener(this);
        btnShip1.setBackgroundResource(R.drawable.button2_small_up);
        
        btnEngine = (Button) findViewById(R.id.btnEngine);
        btnEngine.setOnClickListener(this);
        btnEngine.setBackgroundResource(R.drawable.button2_small_up);
        btnGenerator = (Button) findViewById(R.id.btnGenerator);
        btnGenerator.setOnClickListener(this);
        btnShield = (Button) findViewById(R.id.btnShield);
        btnShield.setOnClickListener(this);
        btnWeapon = (Button) findViewById(R.id.btnWeapon);
        btnWeapon.setOnClickListener(this);
        
        viewEngineSpeed1 = (View) findViewById(R.id.viewEngineSpeed1);
        viewEngineSpeed1.setOnClickListener(this);
        btnJoin = (Button) findViewById(R.id.btnJoin);
        btnJoin.setOnClickListener(this);
        
    }

    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btnJoin:
        	Intent intent = new Intent(this, JoinGameActivity.class);
      	    startActivity(intent);
      	    break;
        case R.id.viewEngineSpeed1:
        	viewEngineSpeed1.setBackgroundResource(R.drawable.fon);
			Log.i(TAG, "нажат вив элемент");
			break;
        case R.id.btnEngine:
        	btnEngine.setBackgroundResource(R.drawable.button2_small_up);
        	btnGenerator.setBackgroundResource(R.drawable.button2_small);
        	btnShield.setBackgroundResource(R.drawable.button2_small);
        	btnWeapon.setBackgroundResource(R.drawable.button2_small);
        	btnShipBranch.setBackgroundResource(R.drawable.button2_small);
        	break;
        case R.id.btnGenerator:
        	btnGenerator.setBackgroundResource(R.drawable.button2_small_up);
        	btnEngine.setBackgroundResource(R.drawable.button2_small);
        	btnShield.setBackgroundResource(R.drawable.button2_small);
        	btnWeapon.setBackgroundResource(R.drawable.button2_small);
        	btnShipBranch.setBackgroundResource(R.drawable.button2_small);
        	break;
        case R.id.btnShield:
        	btnShield.setBackgroundResource(R.drawable.button2_small_up);
        	btnEngine.setBackgroundResource(R.drawable.button2_small);
        	btnGenerator.setBackgroundResource(R.drawable.button2_small);
        	btnWeapon.setBackgroundResource(R.drawable.button2_small);
        	btnShipBranch.setBackgroundResource(R.drawable.button2_small);
        	break;
        case R.id.btnWeapon:
        	btnWeapon.setBackgroundResource(R.drawable.button2_small_up);
        	btnEngine.setBackgroundResource(R.drawable.button2_small);
        	btnGenerator.setBackgroundResource(R.drawable.button2_small);
        	btnShield.setBackgroundResource(R.drawable.button2_small);
        	btnShipBranch.setBackgroundResource(R.drawable.button2_small);
        	break;
        case R.id.btnShipBranch:
        	btnShipBranch.setBackgroundResource(R.drawable.button2_small_up);
        	btnWeapon.setBackgroundResource(R.drawable.button2_small);
        	btnEngine.setBackgroundResource(R.drawable.button2_small);
        	btnGenerator.setBackgroundResource(R.drawable.button2_small);
        	btnShield.setBackgroundResource(R.drawable.button2_small);
        	break;
        case R.id.btnShip1:
        	btnShip1.setBackgroundResource(R.drawable.button2_small_up);
        	btnShip2.setBackgroundResource(R.drawable.button2_small);
        	btnShip3.setBackgroundResource(R.drawable.button2_small);
        	break;
        case R.id.btnShip2:
        	btnShip2.setBackgroundResource(R.drawable.button2_small_up);
        	btnShip1.setBackgroundResource(R.drawable.button2_small);
        	btnShip3.setBackgroundResource(R.drawable.button2_small);
        	break;
        case R.id.btnShip3:
        	btnShip3.setBackgroundResource(R.drawable.button2_small_up);
        	btnShip1.setBackgroundResource(R.drawable.button2_small);
        	btnShip2.setBackgroundResource(R.drawable.button2_small);
        	break;
        
        }
      }
    
	/*
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int action = event.getAction();
		switch (v.getId()){
		case R.id.btnJoin:
			switch(action)  
			{
            	case MotionEvent.ACTION_UP:         		
            		Intent intent = new Intent(this, JoinGameActivity.class);
            		finish();
            		startActivity(intent);
            		return true;
			}
		case R.id.viewEngineSpeed1:
			switch(action)
			{
			case MotionEvent.ACTION_DOWN:v.setBackgroundResource(R.drawable.button1);
			return true;
			}
		}
		return false;
		
	}*/
	
	/*
		С помощью методов getX() и getY() получаем координаты по оси x и y соответственно
        //Следует отметить, что точка 0 располагается в левом верхнем углу экрана.
        //Ось x направлена вправо
        //Ось y направлена вниз(чем ниже, тем больше координата).
        str.append(«Location: »).append(event.getX()).append(" x ").append(event.getY()).append("\n");//Узнаем координаты
        str.append(«Edge flags: »).append(event.getEdgeFlags()).append("\n");// Метод getEdgeFlags возвращает информацию о пересечении краев экрана
        str.append(«Pressure: »).append(event.getPressure()).append("\n");// Узнаем давление
        str.append(«Size: »).append(event.getSize()).append("\n"); // Узнаем размер указателя(места соприкосновения пальца с экраном)
        str.append(«Down time: »).append(event.getDownTime()).append(«ms\n»);// Узнаем время, когда палец был опущен на экран в миллисекундах
        str.append(«Event time: »).append(event.getEventTime()).append(«ms»);//узнаем текущее время(соответствующее обрабатываемому MotionEvent'у) в миллисекундах
        str.append(" Elapsed: ").append(event.getEventTime()-event.getDownTime());//Узнаем сколько времени прошло с момента опускания пальца, до текущего MotionEvent'а
        Log.v(«Mytag», str.toString());//Для того, чтобы можно было отслеживать эти действия, записываем всю информацию о них в лог.
        return true;// Почему мы возвращаем true будет рассмотрено потом
	 */
}
