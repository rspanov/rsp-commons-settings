package org.rspanov.commons.settings;

import java.io.Serializable;

/**
 * POJO bean for representation settings in GUI and so on
 *
 * @author rspanov
 */
public class SettingListElement implements Serializable {

    private static final long serialVersionUID = 3637810628623052420L;

    /**
     * name of setting
     */
    private String settingName;

    public String getSettingName() {
        return settingName;
    }

    public void setSettingName(String settingName) {
        this.settingName = settingName;
    }

    /**
     * value of setting as String
     */
    private String settingValue;

    public String getSettingValue() {
        return settingValue;
    }

    public void setSettingValue(String settingValue) {
        this.settingValue = settingValue;
    }

    /**
     * description of setting
     */
    private String settingDescription;

    public String getSettingDescription() {
        return settingDescription;
    }

    public void setSettingDescription(String settingDescription) {
        this.settingDescription = settingDescription;
    }

    public SettingListElement(String settingName, String settingValue, String settingDescription) {
        this.settingName = settingName;
        this.settingValue = settingValue;
        this.settingDescription = settingDescription;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.settingName != null ? this.settingName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SettingListElement other = (SettingListElement) obj;
        if ((this.settingName == null) ? (other.settingName != null) : !this.settingName.equals(other.settingName)) {
            return false;
        }
        return true;
    }

}
