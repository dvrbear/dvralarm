package com.dvr.dvralarm.DataBase;


import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

public class DataBase extends SQLiteOpenHelper
{
private static String TAG = "DataBaseHelper";
private static String DB_PATH = "∂";
private static String DB_NAME = "alarm.sqlite";
private SQLiteDatabase mDataBase;
private final Context mContext;



public DataBase(Context context)
{
    super(context, DB_NAME, null, 1);
    DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
    this.mContext = context;
}

    public boolean checkDataBase()
    {
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }
    public boolean createDataBase() throws IOException
{
    boolean mDataBaseExist = checkDataBase();
    if(!mDataBaseExist)
    {
        this.getReadableDatabase();
        this.close();
        try
        {
            copyDataBase();
            ////Log.e(TAG, "createDatabase database created questions");
            return true;
        }
        catch (IOException mIOException)
        {
            throw new Error("ErrorCopyingDataBase");
        }


    }
    else
    {
    	return false;
    }


}

    private void copyDataBase() throws IOException
    {
        InputStream mInput = mContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer))>0)
        {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    public boolean openDataBase() throws SQLException
    {
        String mPath = DB_PATH + DB_NAME;
        mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return mDataBase != null;
    }

    @Override
    public synchronized void close() 
    {
        if(mDataBase != null)
            mDataBase.close();
        super.close();
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		System.out.println("old version "+oldVersion+"new version "+newVersion);
	}

    public void exportDB() {
        // TODO Auto-generated method stub

        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String  currentDBPath= "//data//" + "PackageName"
                        + "//databases//" + "DatabaseName";
                String backupDBPath  = "/BackupFolder/DatabaseName";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(mContext, backupDB.toString(),
                        Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {

            Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG)
                    .show();

        }
    }

}