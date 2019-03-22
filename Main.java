package com.dolnikova;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        String date = dialogDate();
        if( date != null) {
            String request = "https://api.privatbank.ua/p24api/exchange_rates?json=true&date=" + date;
            String response = HttpUtil.sendRequest(request, null, null);

            System.out.println(response);
        }
    }

    public static String dialogDate() {
        String userDate;
        StringBuilder rightFormatOfDate = new StringBuilder();

        Scanner in = new Scanner(System.in);
        System.out.print("Введите дату в формате дд.мм.гггг : ");
        userDate = in.next().trim();

        for (int i = 0; i < userDate.length(); i++) {

            if ((i >= 0 && i <= 1) || (i >= 3 && i <= 4) || (i >= 6 && i <= 9)) {
                if (Character.isDigit(userDate.charAt(i))) {
                    rightFormatOfDate.append(userDate.charAt(i));
                } else {
                    System.out.println("Exception. Invalid date format.");
                    return null;
                }
            }

            if (rightFormatOfDate.length() == 2 || rightFormatOfDate.length() == 5) {
                rightFormatOfDate.append(".");
            }
        }

        return rightFormatOfDate.toString();
    }

}
