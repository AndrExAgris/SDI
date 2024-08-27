/*
 * O servidor deve oferecer:
 * - Operações com matriz (implementando a interface IMatrix);
  */

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

public class ServerMatrix implements IMatrix{
  public ServerMatrix() {}

  public static void main(String[] args) {
      try {
         // Instancia o objeto servidor e a sua stub
         ServerMatrix server = new ServerMatrix();
         IMatrix stub = (IMatrix) UnicastRemoteObject.exportObject(server, 0);
         // Registra a stub no RMI Registry para que ela seja obtida pelos clientes
         Registry registry = LocateRegistry.createRegistry(6600);
         //Registry registry = LocateRegistry.getRegistry(9999);
         registry.bind("matrix_service", stub);
         System.out.println("Servidor pronto");
      } catch (Exception ex) {
         ex.printStackTrace();
      }
   }

    public double[][] sum(double[][] a, double[][] b) throws RemoteException {
        if (a.length != b.length || a[0].length != b[0].length) {
            throw new RemoteException("Matrices dimensions do not match for addition.");
        }

        int rows = a.length;
        int cols = a[0].length;
        double[][] result = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = a[i][j] + b[i][j];
            }
        }
        return result;
    }

    public double[][] mult(double[][] a, double[][] b) throws RemoteException {
        if (a[0].length != b.length) {
            throw new RemoteException("Matrices dimensions do not match for multiplication.");
        }

        int rows = a.length;
        int cols = b[0].length;
        int commonDim = a[0].length;
        double[][] result = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = 0;
                for (int k = 0; k < commonDim; k++) {
                    result[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return result;
    }

    public double[][] randfill(int rows, int cols) throws RemoteException {
        Random random = new Random();
        double[][] result = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = random.nextDouble(); 
            }
        }
        return result;
    }

  

}
