import java.sql.*;
import java.util.Scanner;

public class HotelManagement {
    private static final String url = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String username = "root";
    private static final String password = "Ashish$786";

    public static void main(String[] args) throws ClassNotFoundException{
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        try{
            Connection connection = DriverManager.getConnection(url,username,password);
            while(true){
                System.out.println();
                System.out.println("HOTEL MANAGEMENT SYSTEM");
                Scanner scanner = new Scanner(System.in);
                System.out.println("1. Reserve a Room");
                System.out.println("2. View Reservations");
                System.out.println("3. Get Room Number");
                System.out.println("4. Update Reservation");
                System.out.println("5. Delete Reservation");
                System.out.println("0. Exit");
                System.out.println("Choose an Option: ");
                int opt = scanner.nextInt();
                switch (opt){
                    case 1:
                        reserveRoom(connection, scanner);
                        break;
                    case 2:
                        viewReservation(connection);
                        break;
                    case 3:
                        getRoomNumber(connection, scanner);
                        break;
                    case 4:
                        updateReservation(connection, scanner);
                        break;
                    case 5:
                        deleteReservation(connection, scanner);
                        break;
                    case 0:
                        exit();
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid Choice! Try again!");
                }
;            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        catch(InterruptedException e){
            throw new RuntimeException(e);
        }

    }
    private static void reserveRoom(Connection connection, Scanner scanner){
        System.out.println("Enter the Guest Name: ");
        String guest_name = scanner.next();
        System.out.println("Enter the Room Number: ");
        int room_no = scanner.nextInt();
        System.out.println("Enter the Contact Number: ");
        String contact_no = scanner.next();
        String query = "INSERT INTO reservations (guest_name, room_no, contact_number) VALUES ('"+guest_name+"', "+room_no+", '"+contact_no+"');";
        try(Statement statement = connection.createStatement()){
            int rowsAffected = statement.executeUpdate(query);
            if(rowsAffected>0){
                System.out.println("Reservation Successful!");
            }
            else{
                System.out.println("Reservation Unsuccessful!");
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
    private static void viewReservation(Connection connection){
        String query = "SELECT * FROM reservations;";
        try(Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query)){
            while(rs.next()){
                int id = rs.getInt("reservation_id");
                System.out.println("Reservation Id: "+id);
                String guestname = rs.getString("guest_name");
                System.out.println("Guest Name: "+guestname);
                int room_no = rs.getInt("room_no");
                System.out.println("Room Number: "+room_no);
                String contact_no = rs.getString("contact_number");
                System.out.println("Contact Number: "+contact_no);
                String timestamp = rs.getTimestamp("reservation_data").toString();
                System.out.println("Timestamp: "+timestamp);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    private static void getRoomNumber(Connection connection, Scanner scanner){
        System.out.println("Enter the reservation id: ");
        int id = scanner.nextInt();
        System.out.println("Enter the Guest Name: ");
        String guest_name = scanner.next();
        String query = "SELECT room_no from reservations WHERE reservation_id="+id+" AND guest_name='"+guest_name+"';";
        try(Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query)){
            if (rs.next()) {
                int roomNumber = rs.getInt("room_no");
                System.out.println("The Room Number for the Reservation ID " + id + " and Guest Name " + guest_name + " is " + roomNumber);
            } else {
                System.out.println("Reservation not Found for the given ID and Guest Name!");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    private static void updateReservation(Connection connection, Scanner scanner){
        try{
            System.out.println("Enter the Reservation ID to be updated: ");
            int id = scanner.nextInt();
            if(!reservationExists(connection, id)){
                System.out.println("Reservation not Found for the given ID!");
                return;
            }
            System.out.println("Enter the New Guest Name: ");
            String guest_name = scanner.next();
            System.out.println("Enter the new Room Number: ");
            int room_no = scanner.nextInt();
            System.out.println("Enter the New Contact Number: ");
            String contact_no = scanner.next();
            String query = "UPDATE reservations SET guest_name=~'"+guest_name+"' , room_no="+room_no+", contact_number='"+contact_no+"' WHERE reservation_id="+id+";";
            try(Statement statement = connection.createStatement()){
                int affectedRows = statement.executeUpdate(query);
                if(affectedRows>0){
                    System.out.println("Reservation Update Successful!");
                }
                else{
                    System.out.println("Reservation Update Unsuccessful!");
                }
            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }
    private static void deleteReservation(Connection connection, Scanner scanner){
        try{
            System.out.println("Enter the reservation id: ");
            int id = scanner.nextInt();
            if(!reservationExists(connection, id)){
                System.out.println("Reservation not found for the given ID!");
                return;
            }
            String query = "DELETE FROM reservations WHERE reservation_id="+id+";";
            try(Statement statement = connection.createStatement()){
                int affectedRows = statement.executeUpdate(query);
                if(affectedRows>0){
                    System.out.println("Reservation Deleted Successfully!");
                }
                else{
                    System.out.println("Reservation Deleted Unsuccessfully!");
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    private static boolean reservationExists(Connection connection, int id){
        try{
            String query = "SELECT reservation_id FROM reservations WHERE reservation_id="+id+";";
            try(Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(query)) {
                return rs.next();
            }

        }
        catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    private static void exit() throws InterruptedException{
        System.out.print("Exiting System!");
        int i = 5;
        while(i!=0){
            System.out.print(".");
            Thread.sleep(450);
            i--;
        }
        System.out.println();
        System.out.println("Thank You For Using Hotel Management System!!!");
    }
}
