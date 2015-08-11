package org.rspanov.commons.settings;

import org.rspanov.commons.settings.ann.Setting;

/**
 *
 * @author r_panov
 */
public class SampleNotNullSettings {

    @Setting(description = "sample of not null string", nullable = false)
    public static volatile String NOT_NULL_STRING;
    
}
