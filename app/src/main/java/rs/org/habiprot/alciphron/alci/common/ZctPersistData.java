package rs.org.habiprot.alciphron.alci.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.text.TextUtils;

/**
 * Created by Rade on 5/14/2015.
 */
public class ZctPersistData {

    /**
     * TODO
     * - I should make workaround for read data, make array of all currently stored prefs to match it with its type
     * - Make its own logger
     * - Change it to factory pattern
     */
    private static ZctLogger mLogger;

    public static final Boolean INVALID_BOOLEAN = Boolean.FALSE;
    public static final String INVALID_STRING = "";

    private SharedPreferences mPreferences;

    public enum Type {
        INVALID(-1),
        BOOLEAN(0),
        FLOAT(1),
        INTEGER(2),
        LONG(3),
        STRING(4);

        private Integer mType;

        Type(Integer mType) {
            this.mType = mType;
        }

        public Integer getType() {
            return this.mType;
        }

        @Override
        public String toString() {
            String retStr;
            switch (mType) {
                case 0:
                    retStr = "BOOLEAN";
                    break;
                case 1:
                    retStr = "FLOAT";

                    break;
                case 2:
                    retStr = "INTEGER";
                    break;
                case 3:
                    retStr = "LONG";
                    break;
                case 4:
                    retStr = "STRING";
                    break;
                default:
                    retStr = "INVALID";
            }
            return retStr;
        }
    }

    public ZctPersistData(Context context, Boolean includeTraces) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        String appName = stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
        mPreferences = context.getSharedPreferences(appName, context.MODE_PRIVATE);
        mLogger = new ZctLogger(ZctPersistData.class.getSimpleName(), includeTraces);
        mLogger.d("ZctPersistData for " + appName + " init successful.");
    }

    /**
     * Store data in shared preference with key - value pair. Every time data needs to be stored,
     * it is checked if value of data is already stored in shared preferences.
     * In that case, nothing will be done.
     *
     * @param key   - Key that is used as index for reading data from shared preference.
     * @param value - Value that needs to be stored.
     * @return Returns - <b>false</b> if there is an error while storing <b>value</b> with given <b>key</b>, else returns <b>true</b>.
     */
    public boolean storeData(String key, Object value) {

        if (key == null || TextUtils.isEmpty(key)) {
            mLogger.e("storeData: Invalid key value.");
            return false;
        }

        Type type = getPrefType(value);
        if (type == Type.INVALID) {
            mLogger.e("storeData: Invalid data type.");
            return false;
        }

        Object oldValue = readData(type, key);
        Editor editor = mPreferences.edit();
        Boolean retVal = true;
        Boolean shouldStore = true;

        switch (type) {
            case BOOLEAN:
                if (oldValue != null && (Boolean) oldValue == (Boolean) value) {
                    mLogger.i("storeData: [" + type + " : " + (Boolean) oldValue + "] already in pref.");
                    shouldStore = false;
                    break;
                }
                editor.putBoolean(key, (Boolean) value);
                break;
            case FLOAT:
                if (oldValue != null && Math.abs((Float) oldValue - (Float) value) < 0.00000001) {
                    mLogger.i("storeData: [" + type + " : " + (Float) oldValue + "] already in pref.");
                    shouldStore = false;
                    break;
                }
                editor.putFloat(key, (Float) value);
                break;
            case INTEGER:
                if (oldValue != null && (Integer) oldValue == (Integer) value) {
                    mLogger.i("storeData: [" + type + " : " + (Integer) oldValue + "] already in pref.");
                    shouldStore = false;
                    break;
                }
                editor.putInt(key, (Integer) value);
                break;
            case LONG:
                if (oldValue != null && (Long) oldValue == (Long) value) {
                    mLogger.i("storeData: [" + type + " : " + (Long) oldValue + "] already in pref.");
                    shouldStore = false;
                    break;
                }
                editor.putLong(key, (Long) value);
                break;
            case STRING:
                if (oldValue != null && ((String) oldValue).equals((String) value)) {
                    mLogger.i("storeData: [" + type + " : \"" + oldValue + "\"] already in pref.");
                    shouldStore = false;
                    break;
                }
                editor.putString(key, (String) value);
                break;
        }

        if (shouldStore) {
            retVal = editor.commit();
            if (retVal) {
                mLogger.d("storeData: [" + type + " : \"" + value + "\"] finished successful.");
            } else {
                mLogger.d("storeData: [" + type + " : \"" + value + "\"] failed.");
            }
        }

        return retVal;
    }

    /**
     * Read data from shared preference and return them as generic Object.
     *
     * @param type Type of data that needs to be read.
     * @param key  Key that is used as index for reading data from shared preference.
     * @return Generic object is returned, if there is an error, this method will return <b>null</b>.
     */
    public Object readData(Type type, String key) {

        if (key == null || TextUtils.isEmpty(key)) {
            mLogger.e("readData: Invalid key value.");
            return null;
        }

        if (type == null || type == Type.INVALID) {
            mLogger.e("readData: Invalid data type.");
            return null;
        }

        Object retVal = null;

        switch (type) {
            case BOOLEAN:
                try {
                    retVal = mPreferences.getBoolean(key, INVALID_BOOLEAN);
                } catch (ClassCastException cce) {
                    mLogger.e(cce.toString());
                    break;
                } catch (NullPointerException npe) {
                    mLogger.e(npe.toString());
                    break;
                }
                break;
            case FLOAT:
                try {
                    retVal = mPreferences.getFloat(key, Float.MIN_VALUE);
                } catch (ClassCastException cce) {
                    mLogger.e(cce.toString());
                    break;
                } catch (NullPointerException npe) {
                    mLogger.e(npe.toString());
                    break;
                }
                if (retVal.equals(Float.MIN_VALUE)) {
                    retVal = null;
                }
                break;
            case INTEGER:
                try {
                    retVal = mPreferences.getInt(key, Integer.MIN_VALUE);
                } catch (ClassCastException cce) {
                    mLogger.e(cce.toString());
                    break;
                } catch (NullPointerException npe) {
                    mLogger.e(npe.toString());
                    break;
                }
                if (retVal.equals(Integer.MIN_VALUE)) {
                    retVal = null;
                }
                break;
            case LONG:
                try {
                    retVal = mPreferences.getLong(key, Long.MIN_VALUE);
                } catch (ClassCastException cce) {
                    mLogger.e(cce.toString());
                    break;
                } catch (NullPointerException npe) {
                    mLogger.e(npe.toString());
                    break;
                }
                if (retVal.equals(Long.MIN_VALUE)) {
                    retVal = null;
                }
                break;
            case STRING:
                try {
                    retVal = mPreferences.getString(key, INVALID_STRING);
                } catch (ClassCastException cce) {
                    mLogger.e(cce.toString());
                    break;
                } catch (NullPointerException npe) {
                    mLogger.e(npe.toString());
                    break;
                }
                if (retVal.equals(INVALID_STRING)) {
                    retVal = null;
                }
                break;
        }

        mLogger.i("readData: [" + type + " : \"" + retVal + "\"] from shared preferences.");
        return retVal;
    }

    /**
     * Remove data from shared preferences with corresponding key.
     *
     * @param key Key of the data that should be removed.
     * @return Return <b>true</b> if everything goes OK, otherwise return <b>false</b>.
     */
    public boolean removeData(String key) {
        boolean retVal = false;
        if (key == null || TextUtils.isEmpty(key)) {
            mLogger.e("removeData: Invalid key value.");
            return false;
        }
        Editor editor = mPreferences.edit();
        retVal = editor.remove(key).commit();
        if (retVal) {
            mLogger.d("removeData: [ " + key + " ] finished successful.");
        } else {
            mLogger.e("removeData: [ " + key + " ] failed.");
        }
        return retVal;
    }

    /**
     * Clear all data from shared prefs
     *
     * @return Return <b>true</b> if everything goes OK, otherwise return <b>false</b>.
     */
    public boolean clearData() {
        boolean retVal = false;

        Editor editor = mPreferences.edit();
        retVal = editor.clear().commit();
        if (retVal) {
            mLogger.d("clearData: finished successful.");
        } else {
            mLogger.e("clearData: failed.");
        }
        return retVal;
    }

    /**
     * Determine object Class type
     *
     * @param detectType - Object that needs to be check
     * @return - <b>Type</b> if object is one of primitive types (bool, int, long, float, string) else return <b>INVALID</b>
     */
    public Type getPrefType(Object detectType) {
        Type retType;
        if (detectType == null) {
            retType = Type.INVALID;
        } else if (detectType instanceof Boolean) {
            retType = Type.BOOLEAN;
        } else if (detectType instanceof Float) {
            retType = Type.FLOAT;
        } else if (detectType instanceof Integer) {
            retType = Type.INTEGER;
        } else if (detectType instanceof Long) {
            retType = Type.LONG;
        } else if (detectType instanceof String) {
            retType = Type.STRING;
        } else {
            retType = Type.INVALID;
        }

        return retType;
    }

    public void testMe() {
        String STRING_KEY = "STR";
        String LONG_KEY = "LONG";
        String INTEGER_KEY = "INTEGER";
        String BOOLEAN_KEY = "BOOLEAN";
        String FLOAT_KEY = "FLOAT";

        String STRING_TEST_VAL = "Test str";
        Long LONG_TEST_VAL = 10L;
        Integer INTEGER_TEST_VAL = 19;
        Boolean BOOLEAN_TEST_VAL = Boolean.TRUE;
        Float FLOAT_TEST_VAL = 16.459F;

        /* TEST STRING */
        readData(ZctPersistData.Type.STRING, STRING_KEY);
        storeData(STRING_KEY, STRING_TEST_VAL);
        readData(ZctPersistData.Type.STRING, STRING_KEY);
        storeData(STRING_KEY, STRING_TEST_VAL);
        removeData(STRING_KEY);
        readData(ZctPersistData.Type.STRING, STRING_KEY);
        storeData(STRING_KEY, STRING_TEST_VAL);
        readData(ZctPersistData.Type.STRING, STRING_KEY);
        removeData(STRING_KEY);
        mLogger.e("-------");

        /* TEST LONG */
        readData(ZctPersistData.Type.LONG, LONG_KEY);
        storeData(LONG_KEY, LONG_TEST_VAL);
        readData(ZctPersistData.Type.LONG, LONG_KEY);
        storeData(LONG_KEY, LONG_TEST_VAL);
        removeData(LONG_KEY);
        readData(ZctPersistData.Type.LONG, LONG_KEY);
        storeData(LONG_KEY, LONG_TEST_VAL);
        readData(ZctPersistData.Type.LONG, LONG_KEY);
        removeData(LONG_KEY);
        mLogger.e("-------");

        /* TEST INTEGER */
        readData(ZctPersistData.Type.INTEGER, INTEGER_KEY);
        storeData(INTEGER_KEY, INTEGER_TEST_VAL);
        readData(ZctPersistData.Type.INTEGER, INTEGER_KEY);
        storeData(INTEGER_KEY, INTEGER_TEST_VAL);
        removeData(INTEGER_KEY);
        readData(ZctPersistData.Type.INTEGER, INTEGER_KEY);
        storeData(INTEGER_KEY, INTEGER_TEST_VAL);
        readData(ZctPersistData.Type.INTEGER, INTEGER_KEY);
        removeData(INTEGER_KEY);
        mLogger.e("-------");

        /* TEST BOOLEAN */
        readData(ZctPersistData.Type.BOOLEAN, BOOLEAN_KEY);
        storeData(BOOLEAN_KEY, BOOLEAN_TEST_VAL);
        readData(ZctPersistData.Type.BOOLEAN, BOOLEAN_KEY);
        storeData(BOOLEAN_KEY, BOOLEAN_TEST_VAL);
        removeData(BOOLEAN_KEY);
        readData(ZctPersistData.Type.BOOLEAN, BOOLEAN_KEY);
        storeData(BOOLEAN_KEY, BOOLEAN_TEST_VAL);
        readData(ZctPersistData.Type.BOOLEAN, BOOLEAN_KEY);
        removeData(BOOLEAN_KEY);
        mLogger.e("-------");

        /* TEST FLOAT */
        readData(ZctPersistData.Type.FLOAT, FLOAT_KEY);
        storeData(FLOAT_KEY, FLOAT_TEST_VAL);
        readData(ZctPersistData.Type.FLOAT, FLOAT_KEY);
        storeData(FLOAT_KEY, FLOAT_TEST_VAL);
        removeData(FLOAT_KEY);
        readData(ZctPersistData.Type.FLOAT, FLOAT_KEY);
        storeData(FLOAT_KEY, FLOAT_TEST_VAL);
        readData(ZctPersistData.Type.FLOAT, FLOAT_KEY);
        removeData(FLOAT_KEY);
        mLogger.e("-------");
    }
}