import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {
    private static Connection connection;

    private static void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/items_store", "root", "");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void disconnect() {
        try {

            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        connect();

        while (true) {
            System.out.println("PRESS [1] TO ADD ITEMS");
            System.out.println("PRESS [2] TO LIST ITEMS");
            System.out.println("PRESS [3] TO DELETE ITEMS");
            System.out.println("PRESS [0] TO EXIT");

            String choice = sc.next();

            if (choice.equals("1")) {
                System.out.println("Insert name:");
                String name = sc.next();
                System.out.println("Insert price:");
                double price = sc.nextDouble();

                addItem(new Items(null, name, price));
            } else if (choice.equals("2")) {
                ArrayList<Items> items = getAllItems();

                for (int i = 0; i < items.size(); i++) {
                    System.out.println(
                            items.get(i).getId()
                                    + " " + items.get(i).getName()
                                    + " " + items.get(i).getPrice() + " KZT"
                    );
                }
            }else if(choice.equals("3")){
                System.out.println("Insert id: ");
                Long id = sc.nextLong();
                deleteItems(id);
            }else if(choice.equals("0")){
                System.exit(0);
            }
        }


    }

    public static void addItem(Items item) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO items (id, name, price) VALUES (NULL, ?, ?)");
            preparedStatement.setString(1, item.getName());
            preparedStatement.setDouble(2, item.getPrice());

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static ArrayList<Items> getAllItems() {
        ArrayList<Items> allItems = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM items");
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){

                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");

                double price = resultSet.getDouble("price");

                allItems.add(new Items(id, name, price));

            }

            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return allItems;
    }

    public static void deleteItems(Long id){
        try{

            PreparedStatement st = connection.prepareStatement("DELETE FROM items where id = ?");


            st.setLong(1, id);

            st.executeUpdate();

            st.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
