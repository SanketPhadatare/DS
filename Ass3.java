import mpi.*; // Import MPJ Express package

public class ArraySumDistributed {

    public static void main(String[] args) throws Exception {
        // Initialize MPI
        MPI.Init(args);
        
        // Get rank and size
        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        int N = 8; // Total number of elements (example)
        int[] arr = new int[N];
        int elementsPerProcess = N / size;
        int[] subArray = new int[elementsPerProcess];

        if (rank == 0) {
            // Only rank 0 initializes the array
            for (int i = 0; i < N; i++) {
                arr[i] = i + 1; // Example array: [1,2,3,4,5,6,7,8]
            }
        }

        // Scatter the array to all processes
        MPI.COMM_WORLD.Scatter(
            arr,         // send buffer
            0,           // offset
            elementsPerProcess, // number of elements per process
            MPI.INT,     // data type
            subArray,    // receive buffer
            0,           // offset in receive buffer
            elementsPerProcess, // number of elements to receive
            MPI.INT,     // data type
            0            // root process
        );

        // Each process computes its intermediate sum
        int partialSum = 0;
        for (int i = 0; i < elementsPerProcess; i++) {
            partialSum += subArray[i];
        }

        System.out.println("Processor <" + rank + "> has intermediate sum = " + partialSum);

        // Gather all partial sums at root process (rank 0)
        int[] partialSums = new int[size];
        MPI.COMM_WORLD.Gather(
            new int[]{partialSum}, // send buffer
            0,
            1,
            MPI.INT,
            partialSums,           // receive buffer
            0,
            1,
            MPI.INT,
            0
        );

        // Root process calculates total sum
        if (rank == 0) {
            int totalSum = 0;
            for (int sum : partialSums) {
                totalSum += sum;
            }
            System.out.println("Total Sum = " + totalSum);
        }

        // Finalize MPI
        MPI.Finalize();
    }
}

//javac -cp $MPJ_HOME/lib/mpj.jar Ass2.java

//$MPJ_HOME/bin/mpjrun.sh -np 4 Ass2
