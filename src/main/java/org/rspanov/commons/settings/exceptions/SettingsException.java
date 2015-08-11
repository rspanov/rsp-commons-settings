package org.rspanov.commons.settings.exceptions;

/**
 *
 * @author rspanov
 */
public class SettingsException extends Exception {

    private static final long serialVersionUID = 7124982563416737706L;

    private SettingsExceptionType exceptionType;

    /**
     * Get the value of exceptionType
     *
     * @return the value of exceptionType
     */
    public SettingsExceptionType getExceptionType() {
        return exceptionType;
    }

    /**
     * Set the value of exceptionType
     *
     * @param exceptionType new value of exceptionType
     */
    public void setExceptionType(SettingsExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    /**
     * Creates a new instance of <code>SettingsException</code> without detail
     * message.
     */
    public SettingsException() {
    }

    private String fieldName;

    /**
     * Get the value of fieldName
     *
     * @return the value of fieldName
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Set the value of fieldName
     *
     * @param fieldName new value of fieldName
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    private String value;

    /**
     * Get the value of value
     *
     * @return the value of value
     */
    public String getValue() {
        return value;
    }

    /**
     * Set the value of value
     *
     * @param value new value of value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Constructs an instance of <code>SettingsException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     * @param exceptionType
     */
    public SettingsException(String msg, SettingsExceptionType exceptionType) {
        super(msg);
        this.exceptionType = exceptionType;
    }

    public SettingsException(Throwable cause) {
        super(cause);
        this.exceptionType = SettingsExceptionType.OTHERS;
    }

    public SettingsException(String message, Throwable cause) {
        super(message, cause);
        this.exceptionType = SettingsExceptionType.OTHERS;
    }

    public SettingsException(String message, SettingsExceptionType exceptionType, String fieldName, String value) {
        super(message);
        this.exceptionType = exceptionType;
        this.fieldName = fieldName;
        this.value = value;
    }

}
