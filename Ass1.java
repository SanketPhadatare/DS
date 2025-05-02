
//AddClient.java

import java.rmi.*;
public class AddClient{
    public static void main(String[] args) {
        try {
            String addServerURL="rmi://"+args[0]+"/AddServer";
        

            AddServerIntf addServerIntf=(AddServerIntf)Naming.lookup(addServerURL);
            System.out.println("The First Number is:"+args[1]);
            double d1=Double.valueOf(args[1]).doubleValue();
            System.out.println("The Second Number is:"+args[2]);
            double d2=Double.valueOf(args[2]).doubleValue();
            System.out.println("The Sum is:"+ addServerIntf.add(d1,d2));
        } catch (Exception e) {
            System.out.println("Exception:"+e);
        }
    }
}



//AddServer.java

import java.rmi.*;
public class AddServer{
    public static void main(String[] args) {
        try {
            AddServerImpl addServerImpl=new AddServerImpl();
            Naming.rebind("AddServer",addServerImpl);
        } catch (Exception e) {
            System.out.println("Exception"+e);
        }
    }
}


//AddServerImpl.java
import java.rmi.*;
import java.rmi.server.*;
public class AddServerImpl extends UnicastRemoteObject implements AddServerIntf{
public AddServerImpl()throws RemoteException{

}
public double add(double d1,double d2)throws RemoteException{
    return d1+d2;
}
}



//AddServerIntf.java
import java.rmi.*;
public interface AddServerIntf extends Remote{
    double add(double d1,double d2)throws RemoteException;
}
