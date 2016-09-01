package com.savvato;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        // write your code here
        JSONParser parser = new JSONParser();
        try {
            JSONObject questData = (JSONObject) parser.parse(new FileReader("quests/quest1.json"));
            Quest quest = new Quest(questData);
            quest.run();
        }
        catch (IOException e) {
            System.out.println("Ошибка ввода/вывода");
        }
        catch (ParseException e) {
            System.out.println("Ошибка парсинга");
        }
    }
}
