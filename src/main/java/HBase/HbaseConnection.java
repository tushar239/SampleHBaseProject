package HBase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

// https://archanaschangale.wordpress.com/2013/09/11/connecting-hbase-using-java/

public class HbaseConnection {
    public static void main(String[] args) throws Exception {
        createTableUser();// if table is already there, then from terminal, you can disable and drop the table first.
        addRowsInUserTable();
    }
    private static void createTableUser() throws Exception {
        HBaseConfiguration hc = new HBaseConfiguration(new Configuration());

        HTableDescriptor ht = new HTableDescriptor("User");

        ht.addFamily(new HColumnDescriptor("Id"));

        ht.addFamily(new HColumnDescriptor("Name"));

        System.out.println("connecting");

        HBaseAdmin hba = new HBaseAdmin(hc);

        System.out.println("Creating Table");

        hba.createTable(ht);

        System.out.println("Done......");
    }
    private static void addRowsInUserTable() throws Exception {
        /* When you create a HBaseConfiguration, it reads in whatever you've set
  into your hbase-site.xml and in hbase-default.xml, as long as these can
  be found on the CLASSPATH */

        org.apache.hadoop.conf.Configuration config = HBaseConfiguration.create();

  /*This instantiates an HTable object that connects you to the "test" table*/

        HTable table = new HTable(config, "User");

  /* To add to a row, use Put. A Put constructor takes the name of the row
  you want to insert into as a byte array. */

        Put p = new Put(Bytes.toBytes("row1"));

  /* To set the value you'd like to update in the row 'row1', specify
  the column family, column qualifier, and value of the table cell you'd
  like to update.  The column family must already exist in your table
  schema.  The qualifier can be anything.  */

        p.add(Bytes.toBytes("Id"), Bytes.toBytes("col1"), Bytes.toBytes("Emp1"));

        p.add(Bytes.toBytes("Name"), Bytes.toBytes("col2"), Bytes.toBytes("Archana"));

  /* Once you've adorned your Put instance with all the updates you want to
  make, to commit it do the following */

        table.put(p);

        // Now, to retrieve the data we just wrote.

        Get g = new Get(Bytes.toBytes("row1"));

        Result r = table.get(g);

        byte[] value = r.getValue(Bytes.toBytes("Id"), Bytes.toBytes("col1"));

        byte[] value1 = r.getValue(Bytes.toBytes("Name"), Bytes.toBytes("col2"));

        String valueStr = Bytes.toString(value);

        String valueStr1 = Bytes.toString(value1);

        System.out.println("GET: " + "Id: " + valueStr + "Name: " + valueStr1);

        Scan s = new Scan();

        s.addColumn(Bytes.toBytes("Id"), Bytes.toBytes("col1"));

        s.addColumn(Bytes.toBytes("Name"), Bytes.toBytes("col2"));

        ResultScanner scanner = table.getScanner(s);

        try {
            for (Result rr = scanner.next(); rr != null; rr = scanner.next()) {
                System.out.println("Found row : " + rr);
            }
        } finally {
            // Make sure you close your scanners when you are done!
            scanner.close();
        }
    }

}
