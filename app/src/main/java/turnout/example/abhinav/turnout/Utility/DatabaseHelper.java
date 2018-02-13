package turnout.example.abhinav.turnout.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by msi1 on 13-Feb-18.
 */


public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="profile.db";
    public static final String TABLE_NAME="profile_table";
    public static final String COL_1="ID";
    public static final String COL_2="NAME";
    public static final String COL_3="COLLEGE_ID";
    public static final String COL_4="COLLEGE_NAME";
    public static final String COL_5="PROFILEPIC";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);


    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, COLLEGE_ID TEXT, COLLEGE_NAME TEXT, PROFILEPIC BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insertData(String name, String college_id, String college_name, byte[] profilepic){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, college_id);
        contentValues.put(COL_4, college_name);
        contentValues.put(COL_5, profilepic);
        long result = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        if (result == -1){
            return false;
        }
        else {
            return true;
        }
    }

    public Cursor getAllData(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "SELECT  * FROM " + TABLE_NAME;
        Cursor res = sqLiteDatabase.rawQuery(query, null);
        return res;
    }

    public boolean updateData(String id, String name, String college_id, String college_name, byte[] profilepic){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, college_id);
        contentValues.put(COL_4, college_name);
        contentValues.put(COL_5, profilepic);
        sqLiteDatabase.update(TABLE_NAME,contentValues, "id = ?",new String[] {id} );

        return true;
    }
}
