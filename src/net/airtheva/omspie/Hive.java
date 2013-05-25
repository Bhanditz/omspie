package net.airtheva.omspie;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class Hive {

	static final File DIR_FILES = new File("/sdcard/files/");
	//static final File DIR_FILES = new File("D:\\cygwin\\workspace\\OMSPE\\files");

	ArrayList<Crawler> mCrawlers = null;
	Thread mThread = null;
	boolean mIsStopped = false;
	int mCurrentIndex = 0;
		
	Hive()
	{
		mCrawlers = new ArrayList<Crawler>();
	}
	
	void Start()
	{
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
			}
		
		});
		for(File file : DIR_FILES.listFiles())
		{
			mCrawlers.add(new Crawler(file));
		}
		mThread.run();
	}
	
	void Stop()
	{
		mIsStopped = true;
	}
	
	void Export()
	{
		try{
			JSONArray array = new JSONArray();
			for(Crawler crawler : mCrawlers)
			{
				JSONObject object = new JSONObject();
				object.put("filename", crawler.Filename);
				object.put("title", crawler.Title);
				object.put("lastModified", crawler.LastModified);
				object.put("address", crawler.Address);
				array.put(object);
			}
			FileWriter writer = new FileWriter(new File(DIR_FILES, "output.json"));
			writer.write(array.toString(4));
			writer.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
