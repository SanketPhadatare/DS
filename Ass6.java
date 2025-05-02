
//Bully.java

package Ass6;

import java.io.*;
import java.util.Scanner;

public class Bully {
    static boolean[] state = new boolean[6]; // index 1 to 5
    static int coordinator;

    public static void up(int up) {
        if (state[up]) {
            System.out.println("Process " + up + " is already up.");
        } else {
            state[up] = true;
            System.out.println("Process " + up + " is up.");
            System.out.println("Process " + up + " held election.");

            for (int i = up + 1; i <= 5; i++) {
                System.out.println("Election message sent from process " + up + " to process " + i);
            }

            for (int i = 5; i >= 1; i--) {
                if (state[i]) {
                    coordinator = i;
                    break;
                }
            }

            System.out.println("Coordinator is: " + coordinator);
        }
    }

    public static void down(int down) {
        if (!state[down]) {
            System.out.println("Process " + down + " is already down.");
        } else {
            state[down] = false;
            System.out.println("Process " + down + " is down.");
        }
    }

    public static void mess(int mess) {
        if (!state[mess]) {
            System.out.println("Process " + mess + " is down.");
        } else {
            if (!state[coordinator]) {
                System.out.println("Process " + mess + " held election.");

                for (int i = mess + 1; i <= 5; i++) {
                    System.out.println("Election message sent from process " + mess + " to process " + i);
                }

                for (int i = 5; i >= 1; i--) {
                    if (state[i]) {
                        coordinator = i;
                        break;
                    }
                }

                System.out.println("Coordinator message sent from process " + coordinator + " to all.");
            } else {
                System.out.println("Message sent to coordinator from process " + mess);
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        for (int i = 1; i <= 5; i++) {
            state[i] = true;
        }

        coordinator = 5;
        System.out.println("5 active processes are: p1 p2 p3 p4 p5");
        System.out.println("Process 5 is coordinator");

        int choice;
        do {
            System.out.println("\n1. Up a process\n2. Down a process\n3. Send a message\n4. Exit");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Bring up which process?");
                    int up = sc.nextInt();
                    if (up >= 1 && up <= 5)
                        up(up);
                    else
                        System.out.println("Invalid process ID.");
                    break;
                case 2:
                    System.out.println("Bring down which process?");
                    int down = sc.nextInt();
                    if (down >= 1 && down <= 5)
                        down(down);
                    else
                        System.out.println("Invalid process ID.");
                    break;
                case 3:
                    System.out.println("Which process will send message?");
                    int mess = sc.nextInt();
                    if (mess >= 1 && mess <= 5)
                        mess(mess);
                    else
                        System.out.println("Invalid process ID.");
                    break;
                case 4:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        } while (choice != 4);

        sc.close();
    }
}




//Ring.java

package Ass6;

import java.util.Scanner;

class Rr {
    int id;
    int state; // 0 = active, 1 = inactive
    int index;
}

public class Ring {
    public static void main(String[] args) {
        int i, j, num, temp;
        Scanner in = new Scanner(System.in);

        System.out.print("Enter the number of process :\n");
        num = in.nextInt();
        Rr[] proc = new Rr[num];

        for (i = 0; i < num; i++) {
            proc[i] = new Rr();
        }

        for (i = 0; i < num; i++) {
            proc[i].index = i;
            System.out.print("Enter the id of process :\n");
            proc[i].id = in.nextInt();
            proc[i].state = 0; // active
        }

        // Sorting process ids
        for (i = 0; i < num - 1; i++) {
            for (j = 0; j < num - i - 1; j++) {
                if (proc[j].id > proc[j + 1].id) {
                    temp = proc[j].id;
                    proc[j].id = proc[j + 1].id;
                    proc[j + 1].id = temp;
                }
            }
        }
        

        int ch;
        do {
            System.out.println("1.election 2.quit");
            ch = in.nextInt();

            if (ch == 1) {
                System.out.println("Enter the Process number who initialised election :");
                int initiator = in.nextInt();
                initiator--; // make 0-based

                int maxId = proc[initiator].id;
                int current = (initiator + 1) % num;

                System.out.println("Process " + proc[initiator].id + " send message to " + proc[current].id);

                while (current != initiator) {
                    int next = (current + 1) % num;
                    System.out.println("Process " + proc[current].id + " send message to " + proc[next].id);
                    if (proc[current].id > maxId) {
                        maxId = proc[current].id;
                    }
                    current = next;
                }

                System.out.println("process " + maxId + "select as co-ordinator");
            }

        } while (ch != 2);

        System.out.println("Program terminated ...");
        in.close();
    }
}
