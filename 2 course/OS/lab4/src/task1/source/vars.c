#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <errno.h>

int global_not_inited[8192];
int global_inited = 10;

const int global_const = 5;

void f() {
    int local;
    static int static_not_inited;
    static int static_inited = 5;

    const int local_const = 30;

    printf("\nfunction variables:\n");
    printf("local:             %p (%d)\n", &local, local);
    printf("static inited:     %p (%d)\n", &static_inited, static_inited);
    printf("static not inited: %p (%d)\n", &static_not_inited, static_not_inited);
    printf("local const        %p (%d)\n", &local_const, local_const);
}

int* f2() {
    int local = 1234;

    printf("\nlocal var from f2 in f2: %p (%d)\n", &local, local);

    return &local;
}

void environ() {
    char* env = getenv("PORT");
    printf("\nPORT=%s\n", env);

    if (setenv("PORT", "new_value", 1) == -1) {
        perror("setenv");
    }
    
    env = getenv("PORT");
    printf("PORT=%s\n", env);
}

void heap() {
    char* buffer = malloc(100);
    if (buffer == NULL) {
        perror("malloc");
    }
    strcpy(buffer, "Hello world");
    printf("\nHEAP: %s", buffer);
    free(buffer);

    printf("\nHEAP: %s", buffer);

    buffer = malloc(100);
    if (buffer == NULL) {
        perror("malloc");
    }
    strcpy(buffer, "Hello world");
    buffer += 50;
    free(buffer);
    printf("\nHEAP: %s", buffer);
}

int main() {

    printf("function f():      %p\n", f);

    printf("\nglobal variables:\n");
    printf("global ininted:    %p (%d)\n", &global_inited, global_inited);
    printf("global not inited: %p %p (%d)\n", &global_not_inited, &global_not_inited[8100], global_not_inited[8100]);
    printf("global const       %p (%d)\n", &global_const, global_const);

    f();

    // Возвращаем адресс локальной переменной из функции
    int* local2 = f2();
    printf("Address local from func: %p\n", local2);
    printf("Value of local from func: %d\n", local2);

    // Переменая окружения
    environ();

    // Работа с кучей
    //heap();

    printf("\npid: %d\n", getpid());
    sleep(30);

    return 0;
}