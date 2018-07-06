package com.android.ofoo.sql;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.android.ofoo.bean.BikeBean;
import com.android.ofoo.util.LogUtils;
import com.j256.ormlite.table.TableUtils;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;

/**
 * Created by hp on 2017/2/10.
 */

public class SQLiteHelperOrm extends OrmLiteSqliteOpenHelper {
    private static final String TAG = "SQLiteHelperOrm";
    private static final String COLON = ":";
    private static final String DATABASE_NAME = "ofoo.db";
    private static final int DATABASE_VERSION = 1;
    private static SQLiteHelperOrm instance;

    private SQLiteHelperOrm(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, BikeBean.class);
        } catch (SQLException e) {
            LogUtils.e(TAG + COLON + "onCreate", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int arg2, int arg3) {
        try {
            TableUtils.dropTable(connectionSource, BikeBean.class, true);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            LogUtils.e(TAG + COLON + "onUpgrade", e);
        }
    }

    /**
     * 单例获取该Helper
     * @param context
     * @return
     */
    public static synchronized SQLiteHelperOrm getHelper(Context context){
        context = context.getApplicationContext();
        if(instance == null){
            synchronized(SQLiteHelperOrm.class){
                if(instance == null){
                    instance = new SQLiteHelperOrm(context);
                }
            }
        }
        return instance;
    }

}
