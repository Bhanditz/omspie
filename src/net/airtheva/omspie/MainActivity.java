package net.airtheva.omspie;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import java.io.*;

public class MainActivity extends Activity {

	Hive mHive = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);
			File copyShell = new File(getFilesDir(), "copy.sh");
			Utility.SaveStreamFile(getAssets().open("copy.sh"), copyShell);
			mHive = new Hive(this);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId())
		{
		case R.id.copy:
			mHive.Copy();
			break;
		case R.id.extract:
			mHive.Extract();
			break;
		case R.id.export:
			mHive.Export();
			break;
		case R.id.fusion:
			mHive.Fusion();
			break;
		case R.id.cleanup:
			mHive.Cleanup();
			break;
		case R.id.exit:
		System.exit(0);
		}
		return super.onOptionsItemSelected(item);
	}

}
