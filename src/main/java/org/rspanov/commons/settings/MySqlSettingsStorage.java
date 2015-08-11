package org.rspanov.commons.settings;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.rspanov.commons.settings.ann.Setting;
import org.rspanov.commons.settings.exceptions.SettingsException;

/**
 * This storage allow to store settings into MySql database table
 *
 * @author Ivan Belov i.belov@inlinetelecom.ru
 */
public class MySqlSettingsStorage extends SettingsStorage {


    /**
     * database table name to store settings
     */
    private String tableName = "SETTINGS";

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * table column to store filed name
     */
    private String nameField = "NAME";

    public String getNameField() {
        return nameField;
    }

    public void setNameField(String nameField) {
        this.nameField = nameField;
    }

    /**
     * table column to store field value
     */
    private String valueFiled = "VALUE";

    public String getValueFiled() {
        return valueFiled;
    }

    public void setValueFiled(String valueFiled) {
        this.valueFiled = valueFiled;
    }

    /**
     * table column to store field description
     */
    private String descriptionField = "DESCRIPTION";

    public String getDescriptionField() {
        return descriptionField;
    }

    public void setDescriptionField(String descriptionField) {
        this.descriptionField = descriptionField;
    }

    /**
     * MySql data source
     */
    private DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public MySqlSettingsStorage() {
    }
    
    /**
     *  Create storage with lookup data source in JNDI
     *
     * @param jndiName JNDI-name of MySql data source
     * @throws SettingsException
     */
    public MySqlSettingsStorage(String jndiName) throws SettingsException {
        super();
        try {
            Context ctx = new InitialContext();
            this.dataSource = (DataSource) ctx.lookup(jndiName);
        } catch (NamingException ne) {
            throw new SettingsException(ne);
        }
    }

    /**
     * Create storage with data source
     *
     * @param dataSource
     * @throws SettingsException
     */
    public MySqlSettingsStorage(DataSource dataSource) throws SettingsException {
        super();
        this.dataSource = dataSource;
    }

    @Override
    public void saveValue(Field field, Object o) throws SettingsException {
        // implements upsert operation for setting field in MySql manner
        // (insert into ... on duplicate key update)
        final String query = "insert into `" + getTableName() + "` (`"
                + getNameField() + "`, `" + getValueFiled() + "`, `"
                + getDescriptionField()
                + "`) values (?, ?, ?) on duplicate key update `"
                + getValueFiled() + "` =?";

        checkFiled(field, o);
        try {
            Connection conn = dataSource.getConnection();
            try {
                PreparedStatement stmt = conn.prepareStatement(query);
                try {
                    stmt.setString(1, field.getName());
                    if (field.get(o) == null) {
                        stmt.setNull(2, Types.VARCHAR);
                        stmt.setNull(4, Types.VARCHAR);
                    } else {
                        stmt.setString(2, String.valueOf(field.get(o)));
                        stmt.setString(4, String.valueOf(field.get(o)));
                    }
                    Setting s = field.getAnnotation(Setting.class);
                    stmt.setString(3, s.description());
                    stmt.executeUpdate();
                } finally {
                    stmt.close();
                }
            } finally {
                conn.close();
            }
        } catch (Exception ex) {
            throw new SettingsException(ex);
        }
    }

    @Override
    public void syncField(Field field, Object o) throws SettingsException {
        final String query = "select " + getValueFiled() + " from `"
                + getTableName() + "` where " + getNameField() + " = ?";
        try {
            Connection conn = getDataSource().getConnection();
            try {
                PreparedStatement stmt = conn.prepareStatement(query);
                try {
                    stmt.setString(1, field.getName());
                    ResultSet rs = stmt.executeQuery();
                    try {
                        if (rs.next()) {
                            String value = rs.getString("value");
                            setField(field, value, o);
                        } else {
                            saveValue(field, o);
                        }
                    } finally {
                        rs.close();
                    }
                } finally {
                    stmt.close();
                }
            } finally {
                conn.close();
            }
        } catch (SettingsException se) {
            throw se;
        } catch (Exception ex) {
            throw new SettingsException(ex);
        }
    }
    
    

}
