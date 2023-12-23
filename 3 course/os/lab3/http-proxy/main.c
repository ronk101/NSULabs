#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <pthread.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include "networks/network_utils.h"
#include "proxy.h"
#include "networks/http_message.h"

#define MAX_THREADS 10
#define PORT 80
#define LISTENQ 10
#define DEFAULT_BUFFER_SIZE (1024*8)

typedef struct ThreadArgs_t {
    int client_socket;
    // TODO добавить кэш
} threadArgs;

void *client_handler(void *args) {
    threadArgs *thread_args = (threadArgs *)args;
    int client_socket = thread_args->client_socket;
    int buffer_size = DEFAULT_BUFFER_SIZE;

    char *line;
    int website_socket;

    http_request *request = http_read_header(client_socket);
    if (request == NULL) {
        printf("Failed to parse the header\n");

        close(client_socket);
        return NULL;
    }

    website_socket = http_connect(request);
    if (website_socket == -1) {
        printf("Failed to connect to host\n");

        http_request_destroy(request);
        close(client_socket);
        return NULL;
    }
    http_request_send(website_socket, request);
    http_request_destroy(request);

    printf("Start to retrieve the response header\n");
    int line_length;
    while (1) {
        line = read_line(website_socket);
        line_length = strlen(line);
        int err = send_to_client(client_socket, line, 0, line_length);
        if (err == -1) {
            printf("Send to client headers end with ERROR\n");
            free(line);

            return NULL;
        }

        if (strstr(line, "Content-Length: ")) {
            // Adjust buffer size based on Content-Length
            ssize_t content_length = atoll(line + strlen("Content-Length: "));
            buffer_size = (content_length < DEFAULT_BUFFER_SIZE) ? content_length : DEFAULT_BUFFER_SIZE;
        }

        if (line[0] == '\r' && line[1] == '\n') {
            printf("get the end of the HTTP response header\n");
            free(line);
            break;
        }

        free(line);
    }

    printf("Start to send BODY with buffer %d bytes in package\n", buffer_size);
    while (1) {
        ssize_t body_length;
        char *body = http_read_body(website_socket, &body_length, buffer_size);
        if (body == NULL) {
            break;
        }

        int err = send_to_client(client_socket, body, 0, body_length);
        if (err == -1) {
            printf("Send to client body ended with ERROR\n");
            free(body);

            return NULL;
        }
        free(body);
    }

    printf("Send to client body was success\n");

    close(website_socket);
    close(client_socket);
    return NULL;
}

void *start_proxy_server(void *arg) {
    int server_socket;
    struct sockaddr_in client_addr;
    socklen_t client_len = sizeof(client_addr);

    init_server_socket(&server_socket, PORT, LISTENQ);

    while (1) {
        int client_socket = accept(server_socket, (struct sockaddr *)&client_addr, &client_len);
        printf("Accepted a new connection\n");
        if (client_socket == -1) {
            perror("Error accepting connection");
            continue;
        }

        threadArgs *args = malloc(sizeof(threadArgs));
        args->client_socket = client_socket;

        pthread_t thread;
        if (pthread_create(&thread, NULL, client_handler, args) != 0) {
            perror("Error creating thread");
            free(args);
            close(client_socket);
        } else {
            pthread_detach(thread);
        }
    }

    close(server_socket);
    return NULL;
}

int main() {
    pthread_t server_thread;

    if (pthread_create(&server_thread, NULL, start_proxy_server, NULL) != 0) {
        perror("Error creating server thread");
        exit(EXIT_FAILURE);
    }

    pthread_join(server_thread, NULL);

    return 0;
}