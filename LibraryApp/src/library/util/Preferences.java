package library.util;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import library.alert.AlertMaker;
import org.apache.commons.codec.digest.DigestUtils;

public class Preferences {
	public static final String CONFIG_FILE = "config.txt";
	
    int days_with_no_fine;
    float fine_per_day;
    
    public Preferences() {
    	 days_with_no_fine = 10;
    	 fine_per_day = 50;
    }

    public  int getDaysWithNoFine() {
        return days_with_no_fine;
    }

    public void setDaysWithNoFine(int days_with_no_fineIn) {
        this.days_with_no_fine = days_with_no_fineIn;
    }

    public  float getFinePerDay() {
        return fine_per_day;
    }

    public void setFinePerDay(float fine_per_dayIn) {
        this.fine_per_day = fine_per_dayIn;
    }
    
    public static void initConfig() {
        Writer writer = null;
        try {
            Preferences preference = new Preferences();
            Gson gson = new Gson();
            writer = new FileWriter(CONFIG_FILE);
            gson.toJson(preference, writer);
        } catch (IOException ex) {
            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static Preferences getPreferences() {
        Gson gson = new Gson();
        Preferences preferences = new Preferences();
        try {
            preferences = gson.fromJson(new FileReader(CONFIG_FILE), Preferences.class);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Preferences.class.getName()).info("Config file is missing. Creating new one with default config");
            initConfig();
        }
        return preferences;
    }

    public static void writePreferenceToFile(Preferences preference) {
        Writer writer = null;
        try {
            Gson gson = new Gson();
            writer = new FileWriter(CONFIG_FILE);
            gson.toJson(preference, writer);
            AlertMaker.showSimpleAlert("Success", "Settings updated");
        } catch (IOException ex) {
            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
            AlertMaker.showErrorMessage("Failed", "Cant save configuration file");
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
