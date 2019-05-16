package tr.com.bracket.trade.utils.extendable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import tr.com.bracket.trade.utils.FileUtils;
import tr.com.bracket.trade.utils.annos.DataField;
import tr.com.bracket.trade.utils.classmapper.ClassMapper;


public abstract class BaseSQLiteOpenHelper extends SQLiteOpenHelper {

    public static final String TAG = BaseSQLiteOpenHelper.class.getSimpleName();

    private String DB_NAME = "";
    private int DB_VERSION = 0;
    private String[] _arrCreateTables;
    private Context _context;


    public BaseSQLiteOpenHelper(Context context, String name, int version, String[] arrCreateTables) {
        super(context, name, null, version);
        DB_NAME = name;
        DB_VERSION = version;
        _context = context;
        _arrCreateTables = arrCreateTables;
    }

    //public abstract void onCreateBS(SQLiteDatabase db);
    //public abstract void onUpgradeBS(SQLiteDatabase db, int oldVersion, int newVersion);

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String s : _arrCreateTables) {
            db.execSQL(s);
        }
        //onCreateBS(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
        //onUpgradeBS(db,oldVersion,newVersion);
    }

    public class TableModel {

        private String sTableName = "";
        private String sPrimaryKeyField = "";
        private List<FieldModel> listFields = new ArrayList<>();


        public TableModel(String sTableName, String sPrimaryKeyField) {
            this.sTableName = sTableName;
            this.sPrimaryKeyField = sPrimaryKeyField;
        }

        public TableModel setName(String name) {
            sTableName = name;
            return this;
        }

        public TableModel addPrimaryKeyField(String name) {
            sPrimaryKeyField = name;
            return this;
        }

        public TableModel addField(String name, String type, boolean bNullable, String defaultValue) {
            FieldModel model = new FieldModel();
            model.name = name;
            model.bNullable = bNullable;
            model.defaultValue = defaultValue;
            model.type = type;
            listFields.add(model);
            return this;
        }

        public class FieldModel {
            public String name = "";
            String type = "";
            boolean bNullable;
            String defaultValue = "";
        }

    }

    public Context getContext() {
        return _context;
    }

    //                                 utils                                     //

    public Cursor getCursor(String sQuery) {
        Log.i(TAG, sQuery);
        SQLiteDatabase db = getWritableDatabase();
        return db.rawQuery(sQuery, null);
    }

    public void deleteTable(String sTable) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE " + sTable);
    }

    public void truncateTable(String sTable) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + sTable);
    }

    public boolean hasTable(String sTable) {
        return hasRows("SELECT name FROM sqlite_master WHERE type='table' AND name='" + sTable + "'");
    }

    public boolean hasColumn(String sTable, String sColumn) {
        boolean bResult = false;
        Cursor c = getCursor("select * from " + sTable);
        for (int i = 0; i < c.getColumnNames().length; i++) {
            if (c.getColumnNames()[i].equals(sColumn)) {
                bResult = true;
                break;
            }
        }
        c.close();
        return bResult;
    }

    public boolean addColumn(String sTable, String sColumn, String sType, boolean bNotNull, String sDefaultValue) {
        if (hasTable(sTable)) {
            if (hasColumn(sTable, sColumn) == false) {
                insUpdDelQuery("alter table " + sTable + " add column '" + sColumn + "' " + sType + (bNotNull ? " NOT NULL " : " ") + (bNotNull ? " DEFAULT " + sDefaultValue : " "));
                return true;
            }
        }
        return false;
    }

    public void insUpdDelQuery(String sQuery) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sQuery);
        db.close();
    }

    public Integer insertReturnID(String table, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        return (int) db.insert(table, null, values);
    }

    public int getLastRowid(String sTable, String sRowidColumn) {
        int nResult = 0;
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("select MAX(" + sRowidColumn + ") from " + sTable, null);
        if (c != null && c.moveToFirst()) {
            nResult = c.getInt(0);
        }
        c.close();
        db.close();
        return nResult;
    }

    public boolean hasRows(String sQuery) {
        Cursor c = getCursor(sQuery);
        return c.getCount() != 0;
    }

    public int getRowCount(String sQuery) {
        Cursor c = getCursor(sQuery);
        return c.getCount();
    }

    public String getSingleResult(String sQuery) {
        String sResult = "";
        Cursor c = getCursor(sQuery);
        if (c.moveToNext()) {
            sResult = c.getString(0);
        }
        c.close();
        if (sResult == null) {
            return "";
        } else {
            return sResult;
        }
    }

    public String getDateFilterQuery(String dateFieldName, String startDate_yyyyMMdd, String endData_yyyyMMdd) {
        return " (substr(" + dateFieldName + ",7)||substr(" + dateFieldName + ",4,2)||substr(" + dateFieldName + ",1,2)"
                + " between"
                + " '" + startDate_yyyyMMdd + "'"
                + " and"
                + " '" + endData_yyyyMMdd + "')";
    }

    public String getOrderByDateQuery(String dateFieldName) {
        return " date(substr(" + dateFieldName + ",7)|| '-' ||substr(" + dateFieldName + ",4,2)|| '-' ||substr(" + dateFieldName + ",1,2))";
    }

    public boolean exportDB(String targetFile) {
        try {
            File fileKaynakDB = new File(Environment.getDataDirectory(), "/data/" + _context.getPackageName() + "/databases/" + DB_NAME);
            File fileHedefDB = new File(targetFile);
            FileChannel fcKaynak = new FileInputStream(fileKaynakDB).getChannel();
            FileChannel fcHedef = new FileOutputStream(fileHedefDB).getChannel();
            fcHedef.transferFrom(fcKaynak, 0, fcKaynak.size());
            fcKaynak.close();
            fcHedef.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean importDB(String sourceFile) {
        //varsayilan boş db nin oluşması için
        hasTable("test");
        //
        return FileUtils.copyFile(sourceFile, Environment.getDataDirectory()
                + "/data/" + _context.getPackageName() + "/databases/" + DB_NAME);
    }

    public <T> List<T> getMappedClassList(final Cursor cursor, final Class<T> clazz) {
        return new ClassMapper<Cursor, T>() {
            @Override
            public List<T> getList(Cursor dataSource) {
                try {
                    List<T> list = new ArrayList<>();
                    while (dataSource.moveToNext()) {
                        T model = map(clazz);
                        list.add(model);
                    }
                    cursor.close();
                    return list;
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            public Object onMap(Cursor dataSource, ClassMapper.ClassMapperFieldInfo response) {
                DataField anno = response.anno;
                if (dataSource.getColumnIndex(anno.fieldName()) == -1)
                    return null;
                if (response.isString)
                    return dataSource.getString(dataSource.getColumnIndex(anno.fieldName()));
                else if (response.isInteger)
                    return dataSource.getInt(dataSource.getColumnIndex(anno.fieldName()));
                else if (response.isDouble)
                    return dataSource.getDouble(dataSource.getColumnIndex(anno.fieldName()));
                else if (response.isDate)
                    return dataSource.getString(dataSource.getColumnIndex(anno.fieldName()));
                else if (response.isBoolean)
                    return Boolean.valueOf(dataSource.getString(dataSource.getColumnIndex(anno.fieldName())));
                else
                    return null;
            }
        }.create(cursor);
    }

    public class BaseSQLiteOpenHelperSetupModel{

        public String dbName="";
        public int dbVersion;
        public String[] arrCreateTables;

    }

}
