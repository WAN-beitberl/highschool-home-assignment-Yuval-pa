import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Scanner;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

public class CSVConv {
    private static final String jdbcUrl = "jdbc:mysql://localhost:3306/school";
    private static final String username = "root";
    private static final String password = "testserver";
    private static final String filePathStudInfo = "C:\\Users\\User\\Documents\\tel bar\\highschool_sql_assignment\\highschool.csv";
    private static final String filePathFriends = "C:\\Users\\User\\Documents\\tel bar\\highschool_sql_assignment\\highschool_friendships.csv";
    private static final int batchSize = 20;
    private static Connection connection = null;


    public static void insertDataToStudentInfoAndCar_color() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            connection.setAutoCommit(false);

            String dropTable = "delete from student_info";
            Statement statement = connection.createStatement();
            statement.execute(dropTable);
            connection.commit();

            String sqlStud = "insert into student_info(row_id,identification_card,first_name,last_name" +
                    ",email,gender,ip_address,cm_height,age,grade,grade_avg)" +
                    " values(?,?,?,?,?,?,?,?,?,?,?)";
            String sqlCar = "insert into car_detail(identification_card,car_color) values(?,?)";

            PreparedStatement statementStud_info = connection.prepareStatement(sqlStud);
            PreparedStatement statementCar = connection.prepareStatement(sqlCar);

            BufferedReader lineReader = new BufferedReader(new FileReader(filePathStudInfo));

            String lineText = null;
            int countStu = 0;
            int countColor = 0;

            lineReader.readLine();
            while ((lineText = lineReader.readLine()) != null) {
                String[] data = lineText.split(",");

                String row_id = data[0];
                String first_name = data[1];
                String last_name = data[2];
                String email = data[3];
                String gender = data[4];
                String ip = data[5];
                String height = data[6];
                String age = data[7];
                String has_car = data[8];
                String color = data[9];
                String grade = data[10];
                String avg = data[11];
                String id = data[12];

                // inserting the data to the database
                statementStud_info.setInt(1, parseInt(row_id));
                statementStud_info.setLong(2, parseLong(id));
                statementStud_info.setString(3, first_name);
                statementStud_info.setString(4, last_name);
                statementStud_info.setString(5, email);
                statementStud_info.setString(5, gender);
                statementStud_info.setString(6, ip);
                statementStud_info.setInt(7, parseInt(height));
                statementStud_info.setInt(8, parseInt(age));
                statementStud_info.setInt(9, parseInt(grade));
                statementStud_info.setFloat(10, parseFloat(avg));

                countStu++;
                statementStud_info.addBatch();
                if (countStu % batchSize == 0) {
                    statementStud_info.executeBatch();
                    countStu = 0;
                }

                //add to car_details
                if (has_car.equalsIgnoreCase("true") && !color.isBlank()) {
                    statementCar.setLong(1, parseLong(id));
                    statementCar.setString(2, color);

                    countColor++;
                    statementCar.addBatch();
                    if (countColor % batchSize == 0) {
                        statementCar.executeBatch();
                        countColor = 0;
                    }
                }
            }
            lineReader.close();
            statementStud_info.executeBatch();
            statementCar.executeBatch();
            connection.commit();
            connection.close();
            System.out.println("Data insert successful");

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void addToFriendsTable() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            connection.setAutoCommit(false);
            String dropTableFriends = "delete  from friends";
            Statement statement2 = connection.createStatement();
            statement2.execute(dropTableFriends);
            connection.commit();

            String sql = "insert into friends(id,row_id_student,row_id_friend) values(?,?,?)";

            PreparedStatement statement = connection.prepareStatement(sql);

            BufferedReader lineReader = new BufferedReader(new FileReader(filePathFriends));

            String lineText = null;
            int count = 0;

            lineReader.readLine();
            while ((lineText = lineReader.readLine()) != null) {
                String[] data = lineText.split(",");

                String id = data[0];
                String stu_id = data[1];
                String friend_id = data[2];

                //add to friends table
                if (!friend_id.isBlank() && !stu_id.isBlank()) {
                    statement.setInt(1, parseInt(id));
                    statement.setInt(2, parseInt(stu_id));
                    statement.setInt(3, parseInt(friend_id));

                    count++;
                    statement.addBatch();
                    if (count % batchSize == 0) {
                        statement.executeBatch();
                        count = 0;
                    }
                }
            }
            lineReader.close();
            statement.executeBatch();
            connection.commit();
            connection.close();
            System.out.println("Data insert successful");
        } catch (Exception exception) {
            exception.printStackTrace();

        }
    }

    public static void makeReport() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            connection.setAutoCommit(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("hello Sima what do you want to do today?");
        while (true) {
            int res = printMenu();
            switch (res){
                case 1:
                {
                    String query ="select avg(grade_avg) from student_info";
                    try (Statement statement = connection.createStatement()){
                        ResultSet rs = statement.executeQuery(query);
                        double avg = rs.getFloat("grade_avg");
                        System.out.println(avg);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                }
                case 2:
                {
                    String query ="select avg(grade_avg) from student_info where gender =\"Male\"";
                    try (Statement statement = connection.createStatement()){
                        ResultSet rs = statement.executeQuery(query);
                        double avg = rs.getFloat("grade_avg");
                        System.out.println(avg);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                }
                case 3:
                {
                    String query ="select avg(grade_avg) from student_info where gender =\"Female\"";
                    try (Statement statement = connection.createStatement()){
                        ResultSet rs = statement.executeQuery(query);
                        double avg = rs.getFloat("grade_avg");
                        System.out.println(avg);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                }
                case 4:
                {
                    String query ="select avg(cm_height) " +
                            "from student_info as stu" +
                            "inner join car_details as car " +
                            "on stu.identification_card = car.identification_card " +
                            "where cm_height>=200 and car_color = \"purple\"";
                    try (Statement statement = connection.createStatement()){
                        ResultSet rs = statement.executeQuery(query);
                        double avg = rs.getFloat("grade_avg");
                        System.out.println(avg);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                }
                case 5:
                case 6: {
                    System.out.println("TODO");
                    break;
                }
                case 7:
                {
                    long stud_id;
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("enter the id of the student:");
                    stud_id=scanner.nextInt();
                    String query ="select * form stud_view where identification_card="+stud_id;
                    try (Statement statement = connection.createStatement()) {
                        ResultSet rs = statement.executeQuery(query);
                        long id = rs.getLong("identification_card");
                        String fName = rs.getString("first_name");
                        String lName = rs.getString("last_name");
                        float avg = rs.getFloat("grade_avg");
                        System.out.println("id: "+id+", fist name: "+fName+", last name: "+ lName+", average grade:"+avg);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                }
                case 8:
                {
                    try {
                        connection.close();
                        return;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

    }

    private static int printMenu() {
        int res = 0;
        Scanner scanner = new Scanner(System.in);
        while (res < 1 || res > 8) {
            System.out.println("~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~");
            System.out.println(" enter 1 for a school average.");
            System.out.println(" enter 2 for the average score of the boys.");
            System.out.println(" enter 3 for the average score of the girls.");
            System.out.println(" enter 4 for the average height above 2 meters\n for the students that have purple cars");
            System.out.println(" enter 5 and a student id to get the id's \n of all his friends and their friends");
            System.out.println(" enter 6 see the present of popular, regular and lonely students in the school");
            System.out.println(" enter 7 and a student id to see his average grade");
            System.out.println(" enter 8 to exit");
            System.out.println("~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~");
            res = scanner.nextInt();
        }
        return res;
    }
}
