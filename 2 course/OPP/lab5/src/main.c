#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <unistd.h>
#include <math.h>

#include <pthread.h>
#include <mpi.h>

#define TASKS 320
#define REQUEST_TAG 0
#define TASK_TAG 1

typedef struct Task {
    int taskNumber;
    int complexity;
    char completed;
} Task;

pthread_mutex_t mutex;
Task* listOfTasks;

void executeTask(Task* task, int rank) {
    pthread_mutex_lock(&mutex);
    if (task->completed == 0) {
        // printf("Worker %d: Start task %d(%d)\n", rank, task->taskNumber, task->complexity);
        task->completed = 1;
        pthread_mutex_unlock(&mutex);
        usleep(task->complexity);
    } else {
        pthread_mutex_unlock(&mutex);
    }
}

int request_task(int request_rank) {
    int task_id = -1;
    int request = 1;
    MPI_Send(&request, 1, MPI_INT, request_rank, REQUEST_TAG, MPI_COMM_WORLD);
    MPI_Recv(&task_id, 1, MPI_INT, request_rank, TASK_TAG, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
    return task_id;
}

void* executorTread(void* args) {
    int rank = *((int*) args);
    int size = *((int*) args + 1);

    while (1) {
        Task* curTask = NULL;
        for (int i = rank * TASKS / size; i < rank * TASKS / size + TASKS / size; ++i) {
            pthread_mutex_lock(&mutex);
            if (!listOfTasks[i].completed) {
                curTask = &listOfTasks[i];
                pthread_mutex_unlock(&mutex);
                break;
            }
            pthread_mutex_unlock(&mutex);
        }

        if (curTask == NULL) {
            for (int i = 0; i < size; ++i) {
                if (i != rank) {
                    int task_id = request_task(i);
                    if (task_id != -1) {
                        curTask = &listOfTasks[task_id];
                        break;
                    }
                }
            }

            if (curTask == NULL) {
                MPI_Barrier(MPI_COMM_WORLD);
                MPI_Send(NULL, 0, MPI_INT, rank, REQUEST_TAG, MPI_COMM_WORLD);
                break;
            }
        }

        executeTask(curTask, rank);
    }
    return NULL;
}

void* serverThread(void* args) {
    int rank = *((int*) args);
    int size = *((int*) args + 1);
    int request;
    MPI_Status status;

    while (1) {
        MPI_Recv(&request, 1, MPI_INT, MPI_ANY_SOURCE, REQUEST_TAG, MPI_COMM_WORLD, &status);
        if (status.MPI_SOURCE == rank) {
            break;
        }

        int task_id = -1;
        for (int i = rank * TASKS / size; i < rank * TASKS / size + TASKS / size; ++i) {
            pthread_mutex_lock(&mutex);
            if (listOfTasks[i].completed == 0) {
                task_id = i;
                listOfTasks[i].completed = 1;
                pthread_mutex_unlock(&mutex);
                break;
            }
            pthread_mutex_unlock(&mutex);
        }
        // printf("Server %d: send %d to %d\n", rank, task_id, status.MPI_SOURCE);
        MPI_Send(&task_id, 1, MPI_INT, status.MPI_SOURCE, TASK_TAG, MPI_COMM_WORLD);
    }
    return NULL;
}

int main(int argc, char* argv[]) {
    int rank, size;
    int provided;
    pthread_attr_t attrs;
    pthread_t threads[2];
    double start_time, end_time;
    MPI_Init_thread(&argc, &argv, MPI_THREAD_MULTIPLE, &provided);

    if (provided != MPI_THREAD_MULTIPLE) {
        perror("init thread");
        MPI_Finalize();
        return -1;
    }

    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &size);

    pthread_mutex_init(&mutex, NULL);
    pthread_attr_init(&attrs);
    pthread_attr_setdetachstate(&attrs, PTHREAD_CREATE_JOINABLE);

    size_t sizeOfTasks = sizeof(Task) * TASKS;
    listOfTasks = (Task*) malloc(sizeOfTasks);
    if (rank == 0) {
        for (int i = 0; i < TASKS; ++i) {
            listOfTasks[i].taskNumber = i;
            listOfTasks[i].complexity = (i + 1) * 1000;
            listOfTasks[i].completed = 0;
        }
    }
    MPI_Bcast(listOfTasks, sizeOfTasks, MPI_BYTE, 0, MPI_COMM_WORLD);

    int* threadArgs = (int*) malloc(sizeof(int) * 2);
    threadArgs[0] = rank;
    threadArgs[1] = size;
    pthread_create(&threads[0], &attrs, executorTread, (void*) threadArgs);
    pthread_create(&threads[1], &attrs, serverThread, (void*) threadArgs);

    start_time = MPI_Wtime();

    pthread_join(threads[0], NULL);
    pthread_join(threads[1], NULL);

    end_time = MPI_Wtime();

    if (rank == 0) {
        double finalTime = end_time - start_time;
        double allTasksComplexity;
        int sumDifficulties = 0;
        for (int i = 0; i < TASKS; ++i) {
            sumDifficulties += listOfTasks[i].complexity;
        }
        allTasksComplexity = sumDifficulties / 1000000.0;


        printf("Tasks completed! Time left: %lf\n", finalTime);
        printf("All tasks complexity: %lf\n", allTasksComplexity);
        printf("Complexity per proc: %lf\n", allTasksComplexity / size);
        printf("Difference: %lf\n", fabs(allTasksComplexity / size - finalTime));
        printf("Efficiency: %lf\n", (allTasksComplexity / size) / (finalTime));
    }

    free(threadArgs);
    free(listOfTasks);

    pthread_mutex_destroy(&mutex);
    pthread_attr_destroy(&attrs);

    MPI_Finalize();
    return 0;
}