package com.android.ofoo.sql;

/**
 * Created by hp on 2017/2/10.
 */

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.os.Build;

import com.android.ofoo.util.LogUtils;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;

public class DbHelper<T> {
    private static final String TAG = "DbHelper";
    private static final String COLON = ":";
    /** 新增一条记录 */
    public int create(Context mContext, T po) {
        SQLiteHelperOrm db = SQLiteHelperOrm.getHelper(mContext);
        try {
            Dao dao = db.getDao(po.getClass());
            return dao.create(po);
        } catch (SQLException e) {
            LogUtils.e(TAG + COLON + "create",e);
        } finally {
            if (db != null)
                db.close();
        }
        return -1;
    }

    public boolean exists(Context mContext, T po, Map<String, Object> where) {
        SQLiteHelperOrm db = SQLiteHelperOrm.getHelper(mContext);
        try {
            Dao dao = db.getDao(po.getClass());
            if (dao.queryForFieldValues(where).size() > 0) {
                return true;
            }
        } catch (SQLException e) {
            LogUtils.e(TAG + COLON + "exists",e);
        } finally {
            if (db != null)
                db.close();
        }
        return false;
    }

    public int createIfNotExists(Context mContext, T po, Map<String, Object> where) {
        SQLiteHelperOrm db = SQLiteHelperOrm.getHelper(mContext);
        try {
            Dao dao = db.getDao(po.getClass());
            if (dao.queryForFieldValues(where).size() < 1) {
                return dao.create(po);
            }
        } catch (SQLException e) {
            LogUtils.e(TAG + COLON + "createIfNotExists",e);
        } finally {
            if (db != null)
                db.close();
        }
        return -1;
    }

    /** 查询一条记录 */
    public List<T> queryForEq(Context mContext, Class<T> c, String fieldName, Object value) {
        SQLiteHelperOrm db = SQLiteHelperOrm.getHelper(mContext);
        try {
            Dao dao = db.getDao(c);
            return dao.queryForEq(fieldName, value);
        } catch (SQLException e) {
            LogUtils.e(TAG + COLON + "queryForEq",e);
        } finally {
            if (db != null)
                db.close();
        }
        return new ArrayList<T>();
    }

    /** 删除一条记录 */
    public int remove(Context mContext, T po) {
        SQLiteHelperOrm db = SQLiteHelperOrm.getHelper(mContext);
        try {
            Dao dao = db.getDao(po.getClass());
            return dao.delete(po);
        } catch (SQLException e) {
            LogUtils.e(TAG + COLON + "remove",e);
        } finally {
            if (db != null)
                db.close();
        }
        return -1;
    }

    /** 删除一条记录 */
    public int removeList(Context mContext, List<T> po) {
        SQLiteHelperOrm db = SQLiteHelperOrm.getHelper(mContext);
        try {
            Dao dao = db.getDao(po.getClass());
            return dao.delete(po);
        } catch (SQLException e) {
            LogUtils.e(TAG + COLON + "remove",e);
        } finally {
            if (db != null)
                db.close();
        }
        return -1;
    }

    /**
     * 根据特定条件更新特定字段
     *
     * @param c
     * @param values
     * @param columnName where字段
     * @param value where值
     * @return
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public int update(Context mContext, Class<T> c, ContentValues values, String columnName, Object value) {
        SQLiteHelperOrm db = SQLiteHelperOrm.getHelper(mContext);
        try {
            Dao dao = db.getDao(c);
            UpdateBuilder<T, Long> updateBuilder = dao.updateBuilder();
            updateBuilder.where().eq(columnName, value);
            for (String key : values.keySet()) {
                updateBuilder.updateColumnValue(key, values.get(key));
            }
            return updateBuilder.update();
        } catch (SQLException e) {
            LogUtils.e(TAG + COLON + "update",e);
        } finally {
            if (db != null)
                db.close();
        }
        return -1;
    }

    /** 更新一条记录 */
    public int update(Context mContext, T po) {
        SQLiteHelperOrm db = SQLiteHelperOrm.getHelper(mContext);
        try {

            Dao dao = db.getDao(po.getClass());
            return dao.update(po);
        } catch (SQLException e) {
            LogUtils.e(TAG + COLON + "update",e);
        } finally {
            if (db != null)
                db.close();
        }
        return -1;
    }

    /** 查询所有记录 */
    public List<T> queryForAll(Context mContext, Class<T> c) {
        SQLiteHelperOrm db = SQLiteHelperOrm.getHelper(mContext);
        try {
            Dao dao = db.getDao(c);
            return dao.queryForAll();
        } catch (SQLException e) {
            LogUtils.e(TAG + COLON + "queryForAll",e);
        } finally {
            if (db != null)
                db.close();
        }
        return new ArrayList<T>();
    }

    /** 查询所有记录 */
    public List<T> queryList(Context mContext, Class<T> c, Map<String, Object> fieldValues) {
        SQLiteHelperOrm db = SQLiteHelperOrm.getHelper(mContext);
        try {
            Dao dao = db.getDao(c);
            return dao.queryForFieldValues(fieldValues);
        } catch (SQLException e) {
            LogUtils.e(TAG + COLON + "queryForAll",e);
        } finally {
            if (db != null)
                db.close();
        }
        return new ArrayList<T>();
    }

}
