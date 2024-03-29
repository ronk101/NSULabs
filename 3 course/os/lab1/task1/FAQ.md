# FAQ

## 1.1

### **Как создать и завершить потоки в программе?**

Для создания и завершения потоков в программе UNIX используются следующие функции:

- **pthread_create()**: Эта функция служит для создания нового потока. Она принимает четыре аргумента:
  - Идентификатор потока (`pthread_t`): Этот аргумент будет заполнен функцией идентификатором созданного потока.
  - Атрибуты потока (обычно устанавливаются в `NULL`, что означает использование атрибутов по умолчанию).
  - Функция, которая будет выполняться в потоке.
  - Аргументы для функции потока.

Пример создания потока с использованием `pthread_create()`:

```c
pthread_t thread_id;
pthread_create(&thread_id, NULL, thread_function, NULL);
```

- **pthread_exit()**: Для завершения выполнения текущего потока используется данная функция. Вызов этой функции указывает, что текущий поток завершает свою работу и его ресурсы будут освобождены после завершения. Пример:

  ```c
  void *thread_function(void *arg) {
      // Ваш код поточной функции
      pthread_exit(NULL);
  }
  ```

- **pthread_join()**: Для ожидания завершения выполнения другого потока используется `pthread_join()`. Вы передаете ей идентификатор потока, который вы хотите подождать. Пример:

  ```c
  pthread_t thread_id;
  // Создание потока
  pthread_create(&thread_id, NULL, thread_function, NULL);

  // Ожидание завершения потока
  pthread_join(thread_id, NULL);
  ```

### **Почему для сравнения идентификаторов POSIX потоков надо использовать функцию `pthread_equal()`?**

В POSIX-совместимых системах идентификаторы потоков (TID) могут быть представлены числами, но сравнение их напрямую с помощью операторов `==` может быть недостаточно надежным из-за различий во внутреннем представлении.

Функция `pthread_equal()` предоставляет надежный и переносимый способ сравнения идентификаторов потоков. Она возвращает ненулевое значение (истина), если идентификаторы указывают на один и тот же поток, и 0 (ложь) в противном случае. Пример:

```c
pthread_t thread1, thread2;
// Создание потоков thread1 и thread2
// ...
if (pthread_equal(thread1, thread2)) {
    printf("Идентификаторы указывают на один и тот же поток.\n");
} else {
    printf("Идентификаторы разные.\n");
}
```

Использование `pthread_equal()` обеспечивает корректное сравнение идентификаторов потоков в разных окружениях, делая ваш код более переносимым.

### **Области видимости переменных**

#### Локальные переменные:

- **Расположение**: Локальные переменные обычно располагаются на стеке. Каждый поток имеет свой собственный стек, и переменные, созданные внутри функции или блока кода, хранятся на стеке этого потока.
- **Изменение**: Изменения, сделанные в локальных переменных, видны только внутри того же потока, в котором они были созданы. Они не видны другим потокам.

#### Локальные статические переменные:

- **Расположение**: Локальные статические переменные также располагаются на стеке, но они имеют статическую продолжительность жизни, что означает, что они сохраняют свое значение между вызовами функции.
- **Изменение**: Изменения в локальных статических переменных видны только внутри потока, в котором они были созданы, и сохраняются между вызовами функции.

#### Локальные константные переменные:

- **Расположение**: Локальные константные переменные также располагаются на стеке.
- **Изменение**: Локальные константные переменные нельзя изменить после их инициализации. Они также видны только внутри потока, в котором они были созданы, и не могут быть изменены внутри этого потока.

#### Глобальные переменные:

- **Расположение**: Глобальные переменные располагаются в сегменте данных или сегменте BSS памяти. Они доступны для всех потоков в программе.
- **Изменение**: Глобальные переменные видны и изменяемы из всех потоков, поэтому изменения в них могут повлиять на все потоки.

Обратите внимание, что для обеспечения безопасности при работе с глобальными переменными из разных потоков может потребоваться использовать механизмы синхронизации, такие как мьютексы или атомарные операции, чтобы избежать конфликтов при одновременном доступе.

Расположение переменных в памяти играет важную роль в их видимости и доступности для разных потоков. Оно также влияет на продолжительность жизни переменных и их поведение при многозадачной обработке.

### **Что происходит при создании нового потока `pthread_create()`?**

При создании нового потока с использованием функции `pthread_create()`, происходит следующее:

1. **Выделение ресурсов**: ОС выделяет необходимые ресурсы для нового потока, включая стек, идентификатор потока и другие атрибуты.

2. **Инициализация потока**: Создается новый поток, и в нем начинает выполняться функция, переданная как аргумент в `pthread_create()`. Эта функция может быть вашей пользовательской поточной функцией.

3. **Стек потока**: Каждый поток имеет свой собственный стек, который используется для хранения локальных переменных и данных, связанных с выполнением функции потока. Стек потока обычно располагается в виртуальной памяти процесса и представляет собой набор памяти, в которой хранятся вызовы функций и локальные переменные.

4. **Контекст потока**: ОС поддерживает контекст каждого потока, который включает в себя значения регистров процессора, указатель на стек и другие данные, необходимые для выыполнения потока. Когда поток переключается между выполнением, сохраняется и восстанавливается его контекст.

5. **Идентификация потока**: Каждому потоку присваивается уникальный идентификатор (TID), который можно использовать для идентификации и управления потоком.

Таким образом, при создании нового потока с помощью `pthread_create()`, операционная система выделяет ресурсы, включая стек, для этого потока, и начинает выполнение указанной функции в этом потоке. Это позволяет множеству потоков внутри процесса выполнять задачи параллельно.

Стеки потоков обычно хранятся в виртуальной памяти процесса и позволяют потокам работать с локальными переменными и вызывать функции без вмешательства других потоков. Важно понимать, что каждый поток имеет свой собственный стек, что оыбеспечивает изоляцию данных между потоками.

## 1.2

### Joinable потоки (Joinable Threads):

Joinable (присоединяемые) потоки предоставляют механизм для синхронизации выполнения потоков в программе. Основные особенности:

- Joinable потоки ожидают завершения других потоков с помощью функции `pthread_join()`. Это позволяет контролировать порядок выполнения потоков и получать результаты их работы.

- Joinable потоки позволяют избегать утечек ресурсов, так как ресурсы, связанные с потоком (например, память), освобождаются автоматически после завершения потока.

- Оставляет после себя стек потока

### Detached потоки (Detached Threads):

Detached (отсоединенные) потоки предназначены для выполнения задач, которые не требуют явной синхронизации или ожидания завершения потоков.  Основные особенности:

- Detached потоки не ожидают завершения других потоков и не блокируют вызывающий поток. Они выполняются асинхронно и не ожидают `pthread_join()`.

`pthread_detach()` - это функция, которая используется для отсоединения (Detached) потока от текущего потока. Если вызвать `pthread_detach()` внутри поточной функции, то это означает, что поток становится отсоединенным, сразу после завершения своей работы.

- Поток, который был отсоединен с помощью `pthread_detach()`, больше не ожидает `pthread_join()` и не может быть присоединен обратно.

Для создания потока с типом DETACHED, можно передать атрибуты в функцию `pthread_create()`, указав тип потока. Этот тип можно задать как `PTHREAD_CREATE_DETACHED`.

### По умолчанию:

По умолчанию, потоки, созданные с помощью `pthread_create()`, создаются как Joinable. Это означает, что они ожидают `pthread_join()` для завершения.

### Когда использовать Joinable и Detached потоки:

- Используйте Joinable потоки, когда вам необходимо контролировать порядок выполнения и ожидать завершения потоков, чтобы получить результаты их работы.

- Используйте Detached потоки, когда вам не важно ожидание завершения и вы хотите, чтобы ресурсы автоматически освобождались после завершения потока.

Общее правило - если вы хотите получить результаты работы потока или убедиться, что он завершил выполнение, используйте Joinable потоки. Если вам нужно выполнять задачи асинхронно и не ожидать их завершения, используйте Detached потоки.

## 1.3

### Передача параметров в поточную функцию

Когда поток создается как `detached` (отсоединенный) поток, структура, которая передается в поток, должна быть размещена в области памяти, которая будет доступна для чтения потоком после его создания и до его завершения выполнения.

Чтобы обеспечить корректное использование структуры в этом случае, можно выбрать один из следующих способов:

1. **Глобальная переменная:** Если структура будет использоваться всеми потоками в программе и не будет изменяться одновременно из нескольких потоков, то можно объявить ее как глобальную переменную. Глобальные переменные доступны для всех потоков в программе.

2. **Динамически выделенная память (heap):** Выделите память для структуры с помощью функций, таких как `malloc()`, передайте указатель на выделенную память в поток и освободите память после завершения потока с помощью `free()`. Это обеспечит изоляцию данных и безопасность доступа из разных потоков.

Таким образом, при создании `detached` потока, обеспечьте правильное расположение структуры в памяти, чтобы избежать ошибок и гарантировать доступность данных для потока во время его выполнения.

## 1.4

Функция `pthread_cancel()` отправляет запрос на отмену в поток thread. Как отреагирует поток зависит от двух атрибутов: его состояние и тип возможности отмены.

Состояние отмены потока, определяемое параметром `pthread_setcancelstate(3)`, может быть включено
(по умолчанию для новых потоков) или отключено. Если поток отключил отмену, то
запрос на отмену остается в очереди до тех пор, пока поток не включит отмену.

Тип отмены потока, определяемый параметром `pthread_setcanceltype(3)`, может быть либо
асинхронный или отложенный (по умолчанию для новых потоков). Средства асинхронной отмены
что поток может быть отменен в любое время (обычно немедленно, но система этого не гарантирует). Отложенная возможность отмены означает, что отмена будет отложена до тех
пор, пока поток в следующий раз не вызовет функцию, которая является точкой отмены. Список функций, которые являются или могут быть точками отмены, приведен в [pthreads(7)](https://manpages.ubuntu.com/manpages/trusty/man7/pthreads.7.html).

**Обработчик очистки** - это функция, которая автоматически выполняется, когда поток
отменяется

Функция `pthread_cleanup_push()` помещает процедуру на вершину стека операций очистки
обработчики. Когда процедура будет вызвана позже, ей будет присвоен arg в качестве аргумента.

Функция `pthread_cleanup_pop()` удаляет процедуру, находящуюся в верхней части стека очистки
обработчики и необязательно выполняет его, если значение execute ненулевое.

Обработчик очистки отмены извлекается из стека и выполняется следующим образом
обстоятельства:
  1. Когда поток отменяется, все накопленные обработчики очистки извлекаются и выполняются
  в порядке, обратном тому, в котором они были помещены в стопку.

  2. Когда поток завершается вызовом `pthread_exit(3)`, выполняются все обработчики очистки
  как описано в предыдущем пункте. (Обработчики очистки не вызываются, если поток
  завершается выполнением возврата из функции запуска потока.)

  3. Когда поток вызывает функцию `pthread_cleanup_pop()` с ненулевым аргументом execute, самый верхний
  обработчик очистки извлекается и выполняется.

## 1.5

Можно ли установить обработчик одного и того же сигнала на несколько потоков (нет, сигнал приходит на процесс, а какой-то поток его принимает, т.е. не блокирует)

`sigset_t` - тип данных, в которой хранится маска сигналов,\
1 - сигнал заблокирован\
0 - сигнал разблокирован

`sigfillset()` - заполняет маску 1
`sigempyset()` - заполняет маску 0
`sigdelset()` - ставить 0 напротив выбранного сигнала
`sigaddset()` - ставит 1 напротив выбранного сигнала

`SIGKILL` и `SIGSTOP` - нельзя заблокировать

Функция `pthread_sigmask()` собственную маску, аргумент how принимает следующие значения:
- SIG_BLOCK Результирующий набор должен быть объединением текущего набора и набора сигналов
на который указывает set.
- SIG_SETMASK Результирующий набор должен быть набором сигналов, на который указывает set.
- SIG_UNBLOCK Результирующий набор должен быть пересечением текущего набора и
дополнением набора сигналов, на который указывает set.

Функция `sigwait()` приостанавливает выполнение вызывающего потока до тех пор, пока не поступит один из сигналов
указанный в наборе сигналов.




