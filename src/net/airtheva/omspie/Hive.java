package net.airtheva.omspie;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;
import eu.chainfire.libsuperuser.Shell;

public class Hive {

	static final File DIR_OPERA_MINI_FILES = new File("/data/data/com.opera.mini.android/files/");
	static final File DIR_OPERA_MOBILE_FILES = new File(Environment.getExternalStorageDirectory(), "./download/");
	static final File FILE_COPY_SHELL = new File("/data/data/net.airtheva.omspie/files/copy.sh");
	static final File DIR_OMSPIE = new File(Environment.getExternalStorageDirectory(), "./omspie/");
	static final File FILE_OMSPIE_JSON = new File(Environment.getExternalStorageDirectory(), "omspie.json");
	static final File FILE_FUSION_JSON = new File(Environment.getExternalStorageDirectory(), "fusion.json");

	Context mContext = null;
	
	ArrayList<ICrawler> mCrawlers = null;
	Thread mThread = null;
	boolean mIsStopped = false;
	boolean mIsExtracted = false;
	int mCurrentIndex = 0;
		
	Hive(Context context)
	{
		mContext = context;
		mCrawlers = new ArrayList<ICrawler>();
	}
	
	boolean entryExistInArray(JSONObject entry, JSONArray array) throws Exception
	{
		for(int i = 0; i < array.length(); i++)
		{
			JSONObject entryInArray;
			entryInArray = array.getJSONObject(i);
			if(entryInArray.getString("title").contentEquals(entry.getString("title")))
			{
				return true;
			}
			if(entryInArray.getString("address").contentEquals(entry.getString("address")))
			{
				return true;
			}
		}
		return false;
	}
	
	void Copy()
	{
		// FIXME: Debug.
		
		AsyncTask<Void, Void, Void> copyTask = new AsyncTask<Void, Void, Void>()
		{
			
			StringBuilder mBuilder = null;
			
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				Toast.makeText(mContext, "Copying. Wait until informed, or error will happen.", Toast.LENGTH_SHORT).show();
			}

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				List<String> result = Shell.SU.run(new String[]
				{
					"chmod 0755 " + FILE_COPY_SHELL.toString(),
					"sh -c " + FILE_COPY_SHELL.toString()
				});
				
				mBuilder = new StringBuilder();
				for(String line : result)
				{
					mBuilder.append(line);
					mBuilder.append("\n");
				}
				
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				Toast.makeText(mContext, "Copied.", Toast.LENGTH_SHORT).show();
			}
			
		};
		
		copyTask.execute();
	}
	
	void Extract()
	{
		Toast.makeText(mContext, "Extracting.", Toast.LENGTH_SHORT).show();
		mThread = new Thread(new Runnable()
		{

			@Override
			public void run() {
				// TODO Auto-generated method stub
				for(; mCurrentIndex < mCrawlers.size();)
				{
					mCrawlers.get(mCurrentIndex).Extract();
					if(mIsStopped == true)
					{
						break;
					}
					mCurrentIndex++;
				}
				mIsExtracted = true;
				Toast.makeText(mContext, "Extracted. You can then export to save the information.", Toast.LENGTH_SHORT).show();
			}
		
		});
		
		for(File file : DIR_OMSPIE.listFiles())
		{
			String ext = Utility.GetExtensionName(file.getName());
			if(ext.contentEquals(".obml16"))
			{
				mCrawlers.add(new obmlCrawler(file));
			}
			else if(ext.contentEquals(".mhtml"))
			{
				mCrawlers.add(new mhtmlCrawler(file));
			}
		}
		mThread.run();
	}
	
	void Stop()
	{
		mIsStopped = true;
	}
	
	void Export()
	{
		if(mIsExtracted != true)
		{
			Toast.makeText(mContext, "Extract first.", Toast.LENGTH_SHORT).show();
			return;
		}
		try{
			Toast.makeText(mContext, "Exporting.", Toast.LENGTH_SHORT).show();
			
			if(FILE_OMSPIE_JSON.exists() == false)
			{
				(new FileWriter(FILE_OMSPIE_JSON)).append("[]").close();
			}
			BufferedReader omspieReader = new BufferedReader(new FileReader(FILE_OMSPIE_JSON));
			String omspie = Utility.ReadLines(omspieReader);
			omspieReader.close();
			
			JSONArray array = new JSONArray(omspie);
			for(ICrawler crawler : mCrawlers)
			{
				JSONObject entry = new JSONObject();
				entry.put("filename", crawler.GetFilename());
				entry.put("lastModified", crawler.GetLastModified());
				entry.put("title", crawler.GetTitle());
				entry.put("address", crawler.GetAddress());
				if(entryExistInArray(entry, array) == false)
				{
					array.put(entry);
				}
			}
			FileWriter writer = new FileWriter(FILE_OMSPIE_JSON);
			writer.write(array.toString(4));
			writer.close();
			
			Toast.makeText(mContext, "Exported. omspie.json is located at /sdcard/.", Toast.LENGTH_SHORT).show();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	void Fusion()
	{
		Toast.makeText(mContext, "Fusion is not constructed yet.", Toast.LENGTH_SHORT).show();
	}

	void Cleanup()
	{
		Toast.makeText(mContext, "Cleaning up.", Toast.LENGTH_SHORT).show();
		for(File file : DIR_OMSPIE.listFiles())
		{
			file.delete();
		}
		Toast.makeText(mContext, "Cleaned up.", Toast.LENGTH_SHORT).show();
	}
	
}
