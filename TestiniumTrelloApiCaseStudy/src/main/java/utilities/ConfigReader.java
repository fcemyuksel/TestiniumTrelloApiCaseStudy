package utilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    static Properties properties;

    static {// static blok herseyden once calisir

        String dosyaYolu= "configuration.properties";
        try {

            FileInputStream fis= new FileInputStream(dosyaYolu);
            properties= new Properties();
            properties.load(fis);


        } catch (IOException e) {
            System.out.println("properties dosyasi okunamadi");
        }
    }

    public static String getProperty(String key){

        return properties.getProperty(key);

    }

    public static void setProperty(String key, String value) {
        try {
            FileOutputStream fos = new FileOutputStream("configuration.properties");
            properties.setProperty(key, value);
            properties.store(fos, null);
            fos.close();
        } catch (IOException e) {
            System.out.println("Error while setting property: " + e.getMessage());
        }
    }


}
