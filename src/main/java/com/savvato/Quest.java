package com.savvato;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Quest implements Runnable {

    /**
     * JSON квест считанный из файла
     */
    private JSONObject data;

    /**
     * Поток чтения консоли.
     * Закрывается при завершении квеста
     */
    private BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

    /**
     * Коструктор
     * @param data Содержимое файла
     */
    public Quest(JSONObject data) {
        this.data = data;
    }

    /**
     * Проход по структуре квеста
     */
    public void run() {
        try {

            JSONObject currentBranch = this.data; //Корень квеста
            while (currentBranch.containsKey("question")) {
                String answer = this.askQuestion(currentBranch); //Получение ответа
                while (getAnswer(currentBranch, answer) == null) { // Поиск целевой ветки квеста пока не будет введен корректный вариант ответа
                    answer = this.askQuestion(currentBranch);
                }
                currentBranch = getAnswer(currentBranch, answer);
                this.printMessage(currentBranch);
            }
            consoleReader.close();

        } catch (IOException e) {
            System.out.println("Ошибка ввода/вывода");
        }
    }

    /**
     * Метод вывода вопроса и получения ответа
     * @param branch Ветка квеста
     * @return Строка ответа, введенная пользователем
     * @throws IOException
     */
    private String askQuestion(JSONObject branch) throws IOException {
        String question = branch.get("question") + this.getAnswerOptions((JSONArray) branch.get("answers"));
        System.out.println(question);
        return consoleReader.readLine();
    }

    /**
     * Получение целевой ветки квеста
     * @param branch Текущая ветка квеста, внутри нее будет вестись поиск по массиву answers
     * @param answer Строка ответа, введенная пользователем
     * @return Целевая ветка квеста
     * @throws IOException
     */
    private JSONObject getAnswer(JSONObject branch, String answer) throws IOException {
        JSONArray answers = (JSONArray) branch.get("answers");
        JSONObject answerObject = null;
        for (Object object : answers) {
            JSONObject iteratedBranch = (JSONObject) object;
            if (answer.equals(iteratedBranch.get("answer"))) {
                answerObject = iteratedBranch;
                break;
            }
        }
        return answerObject;
    }

    /**
     * Вывод сообщения
     * @param branch Текущая ветка квеста
     */
    private void printMessage(JSONObject branch) {
        System.out.println(branch.get("message"));
    }

    /**
     * Формирование строки с вариантами ответов: [<A1>/<A2>...]
     * @param answers Массив ответвлений текущей ветки квеста
     * @return Строка с вариантами ответов
     */
    private String getAnswerOptions(JSONArray answers) {
        StringBuilder optionsBuilder = new StringBuilder();
        optionsBuilder.append("[");
        for (Object object : answers) {
            JSONObject answerObject = (JSONObject) object;
            optionsBuilder.append(answerObject.get("answer")).append("/");
        }
        optionsBuilder.deleteCharAt(optionsBuilder.length() - 1);
        optionsBuilder.append("]");
        return optionsBuilder.toString();
    }
}
