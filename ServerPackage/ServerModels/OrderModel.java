package ServerPackage.ServerModels;

import ServerPackage.ServerModels.ItemModel;
import ServerPackage.ServerModels.OrderLineModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class OrderModel implements Serializable, Cloneable
{
	static final long serialVersionUID = 62L;
	
	
    private PrintWriter fileWrite;
    private File orderFile;
    private ArrayList<OrderLineModel> orderLines;
    private Date date;
    private SimpleDateFormat format;
    String dateString;
    private int orderID;

    public OrderModel(File file)
    {
        orderFile = file;
        orderLines = new ArrayList<OrderLineModel>();
        date = new Date ();
        format = new SimpleDateFormat("MMMM dd, yyyy");
        dateString = format.format(date);
        orderID = (int)(Math.random() * 90000) + 10000;
    }


    public void addLine(ItemModel item)
    {
        OrderLineModel ol = new OrderLineModel(item);
        orderLines.add(ol);
    }

    public void createOrder(boolean newLine) throws FileNotFoundException
    {
        fileWrite = new PrintWriter(new FileOutputStream(orderFile, true));
        if (newLine) {
            fileWrite.println("**********************************************************************");
            fileWrite.println("ORDER ID:\t\t" + orderID + "\r\n" +
                              "Date Ordered:\t\t" + dateString);
        }
        for (OrderLineModel o: orderLines)
            fileWrite.println(o);
        fileWrite.close();
    }

    public String getDateString(){return dateString;}
	
	public Object clone() throws CloneNotSupportedException
	{
		OrderModel temp = (OrderModel)super.clone();
		if(temp.orderLines != null) 
		{
			temp.orderLines = new ArrayList<OrderLineModel>();
			for(OrderLineModel orderLineInList : orderLines)
				temp.orderLines.add((OrderLineModel)orderLineInList.clone());
		}
		
		if(temp.date != null)
			temp.date = (Date)date.clone();
		
		if(temp.format != null)
			temp.format = (SimpleDateFormat)format.clone();
		
		return temp;
	}

}