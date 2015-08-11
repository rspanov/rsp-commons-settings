package org.rspanov.commons.settings;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.rspanov.commons.settings.ann.Setting;
import org.rspanov.commons.settings.exceptions.SettingsException;
import org.rspanov.commons.settings.exceptions.SettingsExceptionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements basic operations for store/retrieve settings.
 * Concrete implementations of settings storages must extends this class.
 * @see PropertySettingsStorage, MySqlSettingsStorage
 *
 * @author rspanov
 */
public abstract class SettingsStorage {

    private static final Logger logger = LoggerFactory
            .getLogger(SettingsStorage.class);

    /**
     * Synchronize field of class with settings data source (DB, file, etc).
     * If setting record not presents in source the new record with default
     * setting value must be added.
     *
     * Otherwise, the field of class must be initialized with value from source
     *
     * @param field synchronizing field of class
     * @param o instance of class contains settings, 
     * or null if static fields used
     * @throws SettingsException
     */
    public abstract void syncField(Field field, Object o)
            throws SettingsException;

    /**
     * Save filed to settings data source
     *
     * @param field saving field of class
     * @param o instance of class contains settings, 
     * or null if static fields used
     * @throws SettingsException
     */
    public abstract void saveValue(Field field, Object o)
            throws SettingsException;

    /**
     * Check for setting value is valid
     *
     * @param field
     * @param value
     * @throws SettingsException
     */
    protected static void checkValue(Field field, String value)
            throws SettingsException {
        Setting ann = field.getAnnotation(Setting.class);
        boolean nullable = (ann != null && ann.nullable());
        boolean nullValue = (value == null || (field.getType().equals(
                String.class) && value.isEmpty()));
        if (!nullable && nullValue) {
            String error = String.format("field %s can't be null",
                    field.getName());
            throw new SettingsException(error,
                    SettingsExceptionType.RESTRICTED_NULL_VALUE,
                    field.getName(), null);
        }
    }

    /**
     * Initialize field with value from settings data source
     *
     * @param field the field of class
     * @param value the field value in String representation.
     * @param o instance of class contains settings, 
     * or null if static fields used
     * @throws SettingsException
     */
    protected static void setField(Field field, String value, Object o)
            throws SettingsException {
        try {
            checkValue(field, value);
            Class<?> ft = field.getType();
            if (ft == AtomicBoolean.class) {
                if (value == null) {
                    field.set(o, null);
                } else {
                    if (field.get(o) == null) {
                        field.set(o,
                                new AtomicBoolean(Boolean.parseBoolean(value)));
                    } else {
                        ((AtomicBoolean) (field.get(o))).set(Boolean
                                .parseBoolean(value));
                    }
                }
            } else if (ft == AtomicLong.class) {
                if (value == null) {
                    field.set(o, null);
                } else {
                    if (field.get(o) == null) {
                        field.set(o, new AtomicLong(Long.parseLong(value)));
                    } else {
                        ((AtomicLong) (field.get(o)))
                                .set(Long.parseLong(value));
                    }
                }
            } else if (ft == AtomicInteger.class) {
                if (value == null) {
                    field.set(o, null);
                } else {
                    if (field.get(o) == null) {
                        field.set(o, new AtomicInteger(Integer.parseInt(value)));
                    } else {
                        ((AtomicInteger) (field.get(o))).set(Integer
                                .parseInt(value));
                    }
                }
            } else if (ft == String.class) {
                field.set(o, value);
            } else if (ft == Integer.TYPE) {
                field.set(o, Integer.parseInt(value));
            } else if (ft == Long.TYPE) {
                field.set(o, Long.parseLong(value));
            } else if (ft == Double.TYPE) {
                field.set(o, Double.parseDouble(value));
            } else if (ft == Boolean.TYPE) {
                field.set(o, Boolean.parseBoolean(value));
            } else if (ft.isEnum()) {
                try {
                    field.set(o, Enum.valueOf(ft.asSubclass(Enum.class), value));
                } catch (IllegalArgumentException iae) {
                    throw new SettingsException(iae.getMessage(),
                            SettingsExceptionType.INVALID_VALUE_EXCEPTION,
                            field.getName(), value);
                }
            } else {
                throw new SettingsException("field " + field.getName()
                        + " has unsupported type",
                        SettingsExceptionType.INVALID_TYPE_EXCEPTION,
                        field.getName(), null);
            }
        } catch (NumberFormatException nfe) {
            throw new SettingsException(nfe.getMessage(),
                    SettingsExceptionType.INVALID_VALUE_EXCEPTION,
                    field.getName(), value);
        } catch (SettingsException se) {
            throw se;
        } catch (Exception ex) {
            throw new SettingsException(ex);
        }
    }

    /**
     * check for value of filed is valid.
     *
     * @param field checking field of class
     * @param o o instance of class contains settings, 
     * or null if static fields used
     * @throws SettingsException
     */
    protected static void checkFiled(Field field, Object o)
            throws SettingsException {
        try {
            Object value = field.get(o);
            if (value == null) {
                checkValue(field, null);
            } else {
                checkValue(field, String.valueOf(o));
            }
        } catch (SettingsException se) {
            throw se;
        } catch (Exception ex) {
            throw new SettingsException(ex);
        }
    }

    /**
     * Load settings from source to annotated static fields of class.
     * Also if source have not some records, new records added into
     * source with default values
     *
     * @param clazz class contains static fields annotated with @Setting
     * @throws SettingsException
     */
    public void load(Class<?> clazz) throws SettingsException {
        load(clazz, null);
    }

    /**
     * Load settings from source to annotated fields of instance.
     * Also if source have not some records, new records added into
     * source with default values
     *
     * @param o instance of class contains fields annotated with @Setting
     * @throws SettingsException
     */
    public void load(Object o) throws SettingsException {
        load(o.getClass(), o);
    }

    /**
     * Save settings from static fields of class into source
     *
     * @param clazz class contains static fields annotated with @Setting
     * @throws SettingsException
     */
    public void save(Class<?> clazz) throws SettingsException {
        save(clazz, null);
    }

    /**
     * Save settings from fields of instance of class
     *
     * @param o instance of class contains fields annotated with @Setting
     * @throws SettingsException
     */
    public void save(Object o) throws SettingsException {
        save(o.getClass(), o);
    }

    /*
     * common saving implementation
     */
    private void save(Class<?> cl, Object o) throws SettingsException {
        try {
            Field[] fields = cl.getDeclaredFields();
            for (Field field : fields) {
                Setting ant = field.getAnnotation(Setting.class);
                if (ant != null) {
                    // skip non-static fields if instance is null
                    if (!Modifier.isStatic(field.getModifiers()) && (o == null)) {
                        continue;
                    }
                    field.setAccessible(true);
                    saveValue(field, o);
                }
            }
        } catch (SettingsException se) {
            logger.error(se.getMessage(), se);
            throw se;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new SettingsException(ex);
        }
    }

    /*
     * common loading implementation
     */
    private void load(Class<?> cl, Object o) throws SettingsException {
        try {
            Field[] fields = cl.getDeclaredFields();
            for (Field field : fields) {
                Setting ant = field.getAnnotation(Setting.class);
                if (ant != null) {
                    // skip non-static fields if instance is null
                    if (!Modifier.isStatic(field.getModifiers()) && (o == null)) {
                        continue;
                    }
                    field.setAccessible(true);
                    syncField(field, o);
                }
            }
        } catch (SettingsException se) {
            logger.error(se.getMessage(), se);
            throw se;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new SettingsException(ex);
        }
    }

    private static List<SettingListElement> getSettingsList(Class<?> cl,
            Object o) throws SettingsException {
        try {
            List<SettingListElement> result = new ArrayList<SettingListElement>();
            Field[] fields = cl.getDeclaredFields();
            for (Field field : fields) {
                Setting ant = field.getAnnotation(Setting.class);
                if (ant != null) {
                    if (!Modifier.isStatic(field.getModifiers()) && (o == null)) {
                        continue;
                    }
                    field.setAccessible(true);
                    Object value = field.get(o);
                    result.add(new SettingListElement(field.getName(),
                            (value != null) ? String.valueOf(value) : "", ant
                            .description()));
                }
            }
            return result;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new SettingsException(ex);
        }
    }

    /**
     * Get list of POJO beans with settings from static fields of class
     *
     * @param clazz class contains static fields annotated with @Setting
     * @return list of POJO beans SettingListElement representing settings
     * for use in UI
     * @throws SettingsException
     */
    public static List<SettingListElement> getSettingsList(Class<?> clazz)
            throws SettingsException {
        return getSettingsList(clazz, null);
    }

    /**
     * get list of POJO beans with settings from fields of instance of class
     *
     * @param o instance of class contains fields annotated with @Setting
     * @return list of POJO beans SettingListElement representing settings
     * for use in UI
     * @throws SettingsException
     */
    public static List<SettingListElement> getSettingsList(Object o)
            throws SettingsException {
        return getSettingsList(o.getClass(), o);
    }

    private static void setSettingsList(Class<?> cl, Object o,
            List<SettingListElement> settings) throws SettingsException {
        try {
            Field[] fields = cl.getDeclaredFields();
            for (Field field : fields) {
                Setting ant = field.getAnnotation(Setting.class);
                if (ant != null) {
                    if (!Modifier.isStatic(field.getModifiers()) && (o == null)) {
                        continue;
                    }
                    field.setAccessible(true);
                    int ndx = -1;
                    for (int i = 0; i < settings.size(); i++) {
                        if (field.getName().equals(
                                settings.get(i).getSettingName())) {
                            ndx = i;
                            break;
                        }
                    }
                    if (ndx < 0) {
                        throw new SettingsException(
                                "settings list does not contains field"
                                + field.getName(),
                                SettingsExceptionType.OTHERS, field.getName(),
                                null);
                    }
                    String value = settings.get(ndx).getSettingValue();
                    setField(field, value, o);
                }
            }
        } catch (SettingsException se) {
            logger.error(se.getMessage(), se);
            throw se;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new SettingsException(ex);
        }
    }

    /**
     * set static fields of class from list of POJO beans
     *
     * @param clazz class contains static fields annotated with @Setting
     * @param settings list of POJO beans SettingListElement 
     * representing settings
     * @throws SettingsException
     */
    public static void setSettingsList(Class<?> clazz,
            List<SettingListElement> settings) throws SettingsException {
        setSettingsList(clazz, null, settings);
    }

    /**
     * set fields of class instance from list of POJO beans
     *
     * @param o instance of class contains fields annotated with @Setting
     * @param settings list of POJO beans SettingListElement 
     * representing settings
     * @throws SettingsException
     */
    public static void setSettingsList(Object o,
            List<SettingListElement> settings) throws SettingsException {
        setSettingsList(o.getClass(), o, settings);
    }
}
