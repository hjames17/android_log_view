package james.toolbox.jlogger.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import james.toolbox.jlogger.db.DBConst;

/**
 * Created by zhanghong on 15/2/28.
 */
public class DBHelper extends SQLiteOpenHelper {


    public DBHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createLogTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createTableIfnotExists(SQLiteDatabase db){
        if(!isTableExists(db, false)){
            createLogTable(db);
        }
    }

    private void createLogTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DBConst.TABLE_LOG + "("
                +"time int primary key, "
                +"type int,"
                + "message text)");
    }



    boolean isTableExists(SQLiteDatabase db, boolean openDb) {
//        if(openDb) {
//            if(db == null || !db.isOpen()) {
//                db = getReadableDatabase();
//            }
//
//            if(!db.isReadOnly()) {
//                db.close();
//                db = getReadableDatabase();
//            }
//        }

        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+DBConst.TABLE_LOG+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }
}
