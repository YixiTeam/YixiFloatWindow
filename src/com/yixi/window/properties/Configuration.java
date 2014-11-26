package com.yixi.window.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.yixi.window.properties.ConfigurationException;

public class Configuration {
    private Properties config = new Properties();
    private String fn = null;

    public Configuration() {
    }

    public Configuration(String fileName) throws ConfigurationException {
        try {
            FileInputStream fin = new FileInputStream(fileName);
            config.load(fin);
            fin.close();
        } catch (IOException ex) {
            throw new ConfigurationException(
                    "Can't read the specified configuration file:" + fileName);
        }
        fn = fileName;
    }

    public String getValue(String itemName) {
        return config.getProperty(itemName);
    }

    public String getValue(String itemName, String defaultValue) {
        return config.getProperty(itemName, defaultValue);
    }

    public void setValue(String itemName, String value) {
        config.setProperty(itemName, value);
        return;
    }

    public void saveFile(String fileName, String description)
            throws ConfigurationException {
        try {
            FileOutputStream fout = new FileOutputStream(fileName);
            config.store(fout, description);
            fout.close();
        } catch (IOException ex) {
            throw new ConfigurationException(
                    "Can't save the specified configuration file:" + fileName);
        }
    }

    public void saveFile(String fileName) throws ConfigurationException {
        saveFile(fileName, "");
    }

    public void saveFile() throws ConfigurationException {
        if (fn.length() == 0)
            throw new ConfigurationException(
                    "Need to specify the saved configuration file name");
        saveFile(fn);
    }
}
