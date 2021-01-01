package util;

import javafx.beans.property.Property;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class LoadConfigUtils {
    public static Properties getProps(){
        Properties props = new Properties();
        try {
            props.load(new FileInputStream("src/config.properties"));
        }
        catch (IOException e){
            System.out.println("no such file or directory");
        }
        return props;
    }
}
