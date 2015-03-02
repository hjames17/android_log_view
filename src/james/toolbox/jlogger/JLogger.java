package james.toolbox.jlogger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import james.toolbox.jlogger.db.DBConst;
import james.toolbox.jlogger.db.DBHelper;

/**
 * Created by zhanghong on 15/2/28.
 */
public class JLogger {

    //用以数据库操作的工具
    private static DBHelper dbHelper;
    protected static SQLiteDatabase db;

    public static void init(Context context, String databaseName, int version){

        dbHelper = new DBHelper(context
                , databaseName == null ? DBConst.DEFAULT_DB_NAME : databaseName
                , version == 0 ? DBConst.DEFAULT_DB_VERSION : version);
        db = dbHelper.getReadableDatabase();
        dbHelper.createTableIfnotExists(db);
    }

    private static void log(int type, String message){
        ContentValues values = new ContentValues();
        values.put("time", System.currentTimeMillis());
        values.put("type", type);
        values.put("message", message);
        db.insert(DBConst.TABLE_LOG, null, values);
        mDataSetObservable.notifyChanged();
    }


    private static DataSetObservable mDataSetObservable = new DataSetObservable();
    public static void registerDatasetObserver(DataSetObserver observer){
        mDataSetObservable.registerObserver(observer);
    }
    public static void unRegisterDatasetObserver(DataSetObserver observer){
        mDataSetObservable.unregisterObserver(observer);
    }

    public static final int TYPE_INFO = 0;
    public static final int TYPE_DEBUG = 1;
    public static final int TYPE_WARN = 2;
    public static final int TYPE_ERROR = 3;


    public static void info(String message){
        log(TYPE_INFO, message);
    }
    public static void debug(String message){
        log(TYPE_DEBUG, message);
    }
    public static void warn(String message){
        log(TYPE_WARN, message);
    }
    public static void error(String message){
        log(TYPE_ERROR, message);
    }


    //Cursor cursor;
    static int position = 0;
    public static void clear(){
        db.delete(DBConst.TABLE_LOG, null, null);
        position = 0;
        mDataSetObservable.notifyInvalidated();
    }

    public static List<Record> readFromStart(){
        Cursor cursor = db.query(DBConst.TABLE_LOG, null, null, null, null, null, null);
        return getRecords(cursor);
    }

    public static List<Record> readContinue(){
        Cursor cursor = db.query(DBConst.TABLE_LOG, null, null, null, null, null, null);
        if(position != 0){
            cursor.moveToPosition(position);
        }

        return getRecords(cursor);
    }

    static List<Record> getRecords(Cursor cursor){
        List<Record> list = new ArrayList<Record>();

        try {
            while (cursor.moveToNext()) {
                Record record = new Record();
                record.time = cursor.getLong(cursor.getColumnIndex("time"));
                record.type = cursor.getInt(cursor.getColumnIndex("type"));
                record.message = cursor.getString(cursor.getColumnIndex("message"));
                list.add(record);
            }
        }finally {
            if(cursor != null){
                position = cursor.getPosition();
                cursor.close();
            }
        }

        return list;
    }


    public static class Record{
        public long time;
        public int type;
        public String message;
    }
}
