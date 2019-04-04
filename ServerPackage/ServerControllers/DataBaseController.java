package ServerPackage.ServerControllers;

import Models.ItemModel;
import Models.OrderLineModel;
import Models.OrderModel;
import Models.SupplierModel;

import java.sql.*;
import java.util.ArrayList;

public class DataBaseController {
    private Connection dataCon;
    private Statement statement;
    private ResultSet resultSet;

    public DataBaseController () {
        try {
            dataCon = DriverManager.getConnection("jdbc:mysql://localhost:3306/shop", "root", "ENSFProjectPassword409!");
        }catch (java.sql.SQLException e){
            System.err.println("Error connecting to the data base");
            System.err.println(e.getMessage());
        }
    }

    protected ArrayList<ItemModel> itemListFromDataBase (){
        ArrayList<ItemModel> temp = new ArrayList<ItemModel>();
        try {
            statement = dataCon.createStatement();
            ResultSet rs =  statement.executeQuery("SELECT * FROM shop.items");
            while (rs.next()){
                ItemModel tempItem = new ItemModel(rs.getInt("ItemID"), rs.getString("Description"),
                        rs.getInt("Quantity"),rs.getFloat("Price"), rs.getInt("SupplierID"));
                temp.add(tempItem);
            }
        }catch (java.sql.SQLException e){
            System.err.println("Error qhile trying to get the list of items from the server");
            e.printStackTrace();
        }
        return temp;
    }

    protected ArrayList<OrderLineModel> orderLineListFromDataBase (){
        ArrayList<OrderLineModel> orderLineList = new ArrayList<OrderLineModel>();
        try{
            statement = dataCon.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM shop.orderlines");
            while(rs.next()){
                OrderLineModel tempLine = new OrderLineModel(rs.getInt("OrderID"),rs.getString("Date Ordered"),
                        rs.getString("OrderDescription"));
            }

        }catch (java.sql.SQLException e){
            System.err.println("Error trying to retrieve the orders from the data base");
            e.printStackTrace();
        }




        return orderLineList;
    }

    protected ArrayList<SupplierModel> supplierListFromDatabase (){
        ArrayList<SupplierModel> temp = new ArrayList<SupplierModel>();
        try {
            statement = dataCon.createStatement();
            ResultSet rs =  statement.executeQuery("SELECT * FROM shop.suppliers");
            while (rs.next()){
                SupplierModel tempSupplier = new SupplierModel(rs.getInt("SupplierID"), rs.getString("SupplierName"),
                        rs.getString("Address"),rs.getString("Contact"));
                temp.add(tempSupplier);
            }
        }catch (java.sql.SQLException e){
            System.err.println("Error qhile trying to get the list of items from the server");
            e.printStackTrace();
        }
        return temp;
    }


    protected void addItem (ItemModel temp){
        try {
                String overrideQuerry = "SELECT * FROM orders WHERE date ='" + temp.getId() + "'";
                statement = dataCon.createStatement();
                resultSet = statement.executeQuery(overrideQuerry);
                if (resultSet.next()) {
                    String updateQuerry = "UPDATE items SET Description = ?, Quantity = ?, Price = ?, SupplierID = ? WHERE ItemID = ? ";
                    PreparedStatement pStat = dataCon.prepareStatement(updateQuerry);


                    pStat.setString(1, temp.getDesc());     //The description of the GIVEN item
                    pStat.setInt(2, temp.getQuantity());    //The quantity of the GIVEN item
                    pStat.setFloat(3, temp.getPrice());     //The price of the GIVEN item
                    pStat.setInt(4, temp.getSupplierID());  //The suppliers of the GIVEN item
                    pStat.setInt(5, temp.getId());          //The id of the GIVEN item


                } else {
                    insertItem(temp);
                }


        }catch (java.sql.SQLException e){
            System.err.println("Error writing into the data base form the file" +
                    "\ncheck for empty fields of the given items of the file");
        }


    }

    private void addSupplier(SupplierModel temp){
        try {
                String overrideQuerry = "SELECT * FROM suppliers WHERE date ='" + temp.getId() + "'";
                statement = dataCon.createStatement();
                resultSet = statement.executeQuery(overrideQuerry);
                if (resultSet.next()) {
                    String updateQuerry = "UPDATE suppliers SET SupplierName = ?, Address = ?, Contact = ? WHERE SupplierID = ? ";
                    PreparedStatement pStat = dataCon.prepareStatement(updateQuerry);

                    pStat.setString(1, temp.getCompanyName());
                    pStat.setString(2, temp.getAddress());
                    pStat.setString(3, temp.getSalesContact());
                    pStat.setInt(4, temp.getId());

                } else {
                    insertSupplier(temp);
                }
        }catch (java.sql.SQLException e){
            System.err.println("Error writing into the data base form the file" +
                    "\ncheck for empty fields of the given items of the file");
        }
    }

    protected void updateItemList (ArrayList<ItemModel> updateItemList){
        try {
            statement = dataCon.createStatement();
            statement.executeQuery("TRUNCATE TABLE items");

            for (ItemModel temp: updateItemList ) {
              insertItem(temp);
            }

        }catch (java.sql.SQLException e){
            System.err.println("Error updating the item table in the data base");
        }
    }

    //Done
    protected void updateSupplierList (ArrayList<SupplierModel> updatedSupplierList){
        try {
            statement = dataCon.createStatement();
            statement.executeQuery("TRUNCATE TABLE suppliers");

            for (SupplierModel temp: updatedSupplierList) {
                insertSupplier(temp);
            }

        }catch (java.sql.SQLException e){
            System.err.println("Error updating the item table in the data base");
        }
    }

    //Done
    protected void updateOrderList (ArrayList<OrderLineModel> updatedOrderList){
        try {
            statement = dataCon.createStatement();
            statement.executeQuery("TRUNCATE TABLE suppliers");

            for (OrderLineModel temp: updatedOrderList) {
                addOrderLine(temp);
            }

        }catch (java.sql.SQLException e){
            System.err.println("Error updating the item table in the data base");
        }
    }

    protected void addOrderLine (OrderLineModel temp){
        try {
            String insertQuery = "INSERT orderlines (OrderID, DateOrdered, OrderDescription) VALUES (?,?,?)";
            PreparedStatement pStat = dataCon.prepareStatement(insertQuery);

            pStat.setInt(1, temp.getOrderID());
            pStat.setString(2, temp.getDateString());
            pStat.setString(3, temp.getOrderLine());
            pStat.execute();

        }catch (java.sql.SQLException e){
            System.err.println("Error trying to insert an order line into the data basse");
            e.printStackTrace();
        }

    }

    private void insertItem (ItemModel temp ){
        try {
            String insertQuery = "INSERT items (ItemID, Description, Quantity, Price, SupplierID) VALUES (?,?,?,?,?)";
            PreparedStatement pStat = dataCon.prepareStatement(insertQuery);
            pStat.setInt(1, temp.getId());
            pStat.setString(2, temp.getDesc());
            pStat.setInt(3, temp.getQuantity());
            pStat.setFloat(4,temp.getPrice());
            pStat.setInt(5, temp.getId());

            pStat.execute();
        }catch (java.sql.SQLException e){
            System.err.println("Error inserting an item to the table");
            e.printStackTrace();
        }
    }



    private void insertSupplier (SupplierModel temp){
        try {
            String query = "INSERT suppliers (SupplierID, SupplierName, Address, Contact) VALUES (?,?,?,?)";
            PreparedStatement pStat = dataCon.prepareStatement(query);
            pStat.setInt(1, temp.getId());
            pStat.setString(2, temp.getCompanyName());
            pStat.setString(3, temp.getAddress());
            pStat.setString(4, temp.getSalesContact());
            pStat.execute();
        }catch (java.sql.SQLException e){
            System.err.println("Error inserting supplier to the data base");
            e.printStackTrace();
        }

    }


    protected void removeItem (ItemModel item){
        try {
            String querry = "DELETE FROM items WHERE itemID = ?";
            PreparedStatement pStat = dataCon.prepareStatement(querry);
            pStat.setInt(1, item.getId());
            pStat.execute();
        }catch (java.sql.SQLException e){
            System.err.println("Error removing an item from the data base, Already removed?");
            e.printStackTrace();
        }
    }
}

