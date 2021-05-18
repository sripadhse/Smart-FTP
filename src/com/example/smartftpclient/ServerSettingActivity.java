package com.example.smartftpclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ServerSettingActivity extends Activity implements OnClickListener{
  private Button btnAdd;
  private Button btnList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.server_setting_layout);
       btnAdd=(Button)findViewById(R.id.btnAdd);
       btnList=(Button)findViewById(R.id.btnList);
       btnAdd.setOnClickListener(this);
       btnList.setOnClickListener(this);
	
	}
	public void onClick(View v)
	{
 		switch (v.getId()) {
		case R.id.btnAdd:
			startActivity(new Intent(this,NewServer.class));
			break;
        case R.id.btnList:
      
     startActivity(new Intent(this,ServerListActivity.class));
			break;
      
        default:
			break;
		}
}
}
