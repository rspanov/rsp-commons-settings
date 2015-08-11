package org.rspanov.commons.settings;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;
import org.rspanov.commons.settings.exceptions.SettingsException;
import org.rspanov.commons.settings.exceptions.SettingsExceptionType;

/**
 * This simple storage allow to store settings in .properties file
 * using default java.util.Properties
 * 
 * Using this way the descriptions of fields will not be stored
 *
 * @author rspanov
 */
public class PropertySettingStorage extends SettingsStorage {

    private Properties settings;
    private String fileName;
    private boolean modified;

    @Override
    public void syncField(Field field, Object o) throws SettingsException {
        try {
            if (settings.getProperty(field.getName()) == null) {
                saveValue(field, o);
            } else {
                String value = settings.getProperty(field.getName());
                setField(field, value, o);
            }
        } catch (SettingsException se) {
            throw se;
        } catch (Exception ex) {
            throw new SettingsException(ex);
        }
    }

    private void loadProperties() throws SettingsException {
        modified = false;
        try {
            this.settings = new Properties();
            if (new File(fileName).exists()) {
                this.settings.load(new FileReader(fileName));
            }
        } catch (IOException ioe) {
            throw new SettingsException(ioe);
        }
    }

    private void saveProperties() throws SettingsException {
        try {
            FileWriter fw = new FileWriter(fileName);
            try {
                this.settings.store(fw, "");
                fw.flush();
            } finally {
                fw.close();
            }
        } catch (IOException ioe) {
            throw new SettingsException(ioe);
        }
    }

    public PropertySettingStorage() {
    }

    public PropertySettingStorage(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void load(Class<?> clazz) throws SettingsException {
        loadProperties();
        super.load(clazz);
        if (modified) {
            saveProperties();
        }
    }

    @Override
    public void load(Object o) throws SettingsException {
        loadProperties();
        super.load(o);
        if (modified) {
            saveProperties();
        }
    }

    @Override
    public void save(Class<?> clazz) throws SettingsException {
        this.settings = new Properties();
        super.save(clazz);
        saveProperties();
    }

    @Override
    public void save(Object o) throws SettingsException {
        this.settings = new Properties();
        super.save(o);
        saveProperties();
    }

    @Override
    public void saveValue(Field field, Object o) throws SettingsException {
        try {
            if (field.get(o) == null) {
                throw new SettingsException("field " + field.getName() + " can't be null in properties", SettingsExceptionType.RESTRICTED_NULL_VALUE, field.getName(), null);
            }
            settings.put(field.getName(), String.valueOf(field.get(o)));
            modified = true;
        } catch (Exception ex) {
            throw new SettingsException(ex);
        }
    }


}
