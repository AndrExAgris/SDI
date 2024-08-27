
/*
 * O servidor deve oferecer:
 * - Operações com a base de dados (implementando IDatabase)
  */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerDatabase implements IDatabase{
  public ServerDatabase() {}

   public static void main(String[] args) {
      try {
         // Instancia o objeto servidor e a sua stub
         ServerDatabase server = new ServerDatabase();
         IDatabase stub = (IDatabase) UnicastRemoteObject.exportObject(server, 0);
         // Registra a stub no RMI Registry para que ela seja obtAida pelos clientes
         Registry registry = LocateRegistry.createRegistry(6660);
         //Registry registry = LocateRegistry.getRegistry(9999);
         registry.bind("database_service", stub);
         System.out.println("Servidor pronto");
      } catch (Exception ex) {
         ex.printStackTrace();
      }
   }

    
    public void save(double[][] a, String filename) throws RemoteException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (double[] row : a) {
                for (int i = 0; i < row.length; i++) {
                    writer.write(Double.toString(row[i]));
                    if (i < row.length - 1) {
                        writer.write(","); 
                    }
                }
                writer.newLine(); 
            }
        } catch (IOException e) {
            throw new RemoteException("Error saving data to file", e);
        }
    }

    public double[][] load(String filename) throws RemoteException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int rowCount = 0;
            while ((line = reader.readLine()) != null) {
                rowCount++;
            }
            double[][] result = new double[rowCount][];
            
            try (BufferedReader reader2 = new BufferedReader(new FileReader(filename))) {
                int currentRow = 0;
                while ((line = reader2.readLine()) != null) {
                    String[] values = line.split(",");
                    result[currentRow] = new double[values.length];
                    for (int i = 0; i < values.length; i++) {
                        result[currentRow][i] = Double.parseDouble(values[i]);
                    }
                    currentRow++;
                }
            }

            return result;
        } catch (IOException | NumberFormatException e) {
            throw new RemoteException("Error loading data from file", e);
        }
    }


    public void remove(String filename) throws RemoteException {
        File file = new File(filename);
        if (!file.delete()) {
            throw new RemoteException("Failed to delete the file");
        }
    }

}
