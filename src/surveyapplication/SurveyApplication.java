package surveyapplication;

import java.sql.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SurveyApplication {

    public static ResultSet resultSet;
    public static PreparedStatement preparedStatement;
    DatabaseConnection db = new DatabaseConnection();

    public SurveyApplication() {
        Scanner input = new Scanner(System.in);

        int number;
        String answer;
        String response;

        System.out.println("Enter phone number");
        answer = input.nextLine();
        while (isPhoneNumberExist(answer)) {
            System.out.println("This number already exists, Try again");
            answer = input.nextLine();
        }
        createUser(answer);
        System.out.println("Welcome");

//        if(createUser(answer)) {
//            System.out.println("Welcome");
//        } else {
//            System.out.println("\n This number already exists, Try again!");
//
//            System.exit(0);
//
//        }
        String phoneNum = answer;

        System.out.println("On a scale of 0-10, how do you rate our services?");
        number = input.nextInt();

        if (number > 7) {
            createResponse(phoneNum, 2, 0, String.valueOf(number));
            System.out.println("Thank you for using our services!");
        } else {
            createResponse(phoneNum, 2, 0, String.valueOf(number));

            System.out.println("What service were you dissatisfied with?(Pick a number)");
            System.out.println("1. Bulk sms \n2. Bulk Email \n3. Survey \n4. USSD \n5. Other");

            number = input.nextInt();
            int serviceId = number;
            input.nextLine();
            createResponse(phoneNum, 3, serviceId, String.valueOf(number));

            if (number == 5) {
                System.out.println("Enter the service");
                answer = input.nextLine();
                createResponse(phoneNum, 4, 0, answer);

            }

            System.out.println("Why were you dissatisfied with this service?");
            response = input.nextLine();
            createResponse(phoneNum, 4, serviceId, response);

            System.out.println("We have received your complaints and our support team will attend to it.\n Thank you for using our services!");

        }
    }

    public static void main(String[] args) throws Exception {

        new SurveyApplication();
    }

    public boolean createUser(String number) {
        boolean saved = false;
        String saveUser = "insert into tUsers(phonenumber)values(?)";

        try {
            preparedStatement = db.databaseConnection().prepareStatement(saveUser);
            preparedStatement.setString(1, number);
            preparedStatement.execute();
            saved = true;

        } catch (SQLException ex) {
            Logger.getLogger(SurveyApplication.class.getName()).log(Level.SEVERE, null, ex);
        }

        return saved;

    }

    public boolean isPhoneNumberExist(String pNumber) {
        boolean exists = true;
        String query = "select * from tUsers where phonenumber = ?";
        try {
            preparedStatement = db.databaseConnection().prepareStatement(query);
            preparedStatement.setString(1, pNumber);
            preparedStatement.execute();
            resultSet = preparedStatement.getResultSet();

            if (!resultSet.next()) {
                exists = false;
            }
            return exists;

        } catch (SQLException ex) {
            Logger.getLogger(SurveyApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
        return exists;
    }

    public void createResponse(String phonenumber, int questionId, int serviceId, String responseParam) {
        String response = "insert into tResponses (id, phone_number, question_id, service_id, responses) values(null, ?, ?, ?, ?)";
        try {
            preparedStatement = db.databaseConnection().prepareStatement(response);
            preparedStatement.setString(1, phonenumber);
            preparedStatement.setInt(2, questionId);
            preparedStatement.setInt(3, serviceId);
            preparedStatement.setString(4, responseParam);
            preparedStatement.execute();

        } catch (SQLException ex) {
            Logger.getLogger(SurveyApplication.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
