package org.projii.client.menu;

import org.projii.client.GameActivity;

import com.example.movingsamples.R;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class JoinGameActivity extends ListActivity {
    
	public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        //С‚СѓС‚ Р±СѓРґСѓС‚ Р·Р°РїРёСЃР°РЅРЅС‹Р№ РґР°РЅРЅС‹Рµ РїРѕР»СѓС‡РµРЅРЅС‹Рµ РѕС‚ Р·Р°РїСЂРѕСЃР° Рє СЃРµСЂРІРµСЂСѓ
        String[] values = new String[] { "Game1 BvsB 5/8 big" , "Game2 FFA 3/4 small" , "Game3 FFA 3/8 very",
                "Game4 BvsB 8/8", "Game5 FFA 3/8", "Game6 BvsB 4/8" , "Game7 FFA 3/8", "Game8 BvsB 2/8",
                "Game9 BvsB 3/8", "Game10 FFA 3/8" };
        // Р�СЃРїРѕР»СЊР·РѕРІР°РЅРёРµ СЃРѕР±СЃС‚РІРµРЅРЅРѕРіРѕ С€Р°Р±Р»РѕРЅР°
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.activity_join_game, R.id.label, values);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
      
    	String item = (String) getListAdapter().getItem(position);
        Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
		Intent intent = new Intent(this, GameActivity.class);
	    startActivity(intent);
    
    }
}

/*
 * 	Button btnJoin;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);
        btnJoin = (Button) findViewById(R.id.btnJoin);
        btnJoin.setOnClickListener(this);
    }
  
 
public void onClick(View v) {
	switch (v.getId()) {
	case R.id.btnJoin:
		Intent intent = new Intent(this, MainActivity.class);
		finish();
	    	startActivity(intent);
	}
}*/