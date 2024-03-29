# Задача

Разработка скрипта Bash, предназначенного для обхода веток git-репозитория, извлечения информации о ветках, анализа данных о последних коммитах и выявления веток, в которых не было коммитов в течение заданного периода времени. Скрипт предполагает уведомление владельца репозитория по электронной почте о ветках, не обновлявшихся в течение установленного периода времени.

## Настройка переменных:

Внутри скрипта можно настроить следующие переменные:

- `REPO_PATH`: Путь к git-репозиторию, который вы хотите проанализировать. Укажите полный путь к вашему репозиторию.

- `N_DAYS`: Количество дней, после которого ветка считается необновленной. Задайте целое число, указывающее количество дней.

- `YOUR_EMAIL`: Ваш адрес электронной почты, от которого будут отправляться уведомления. Укажите вашу электронную почту.

## Запуск скрипта:

Добавьте права доступа на запуск файла

```bash
chmod +x git-bot.sh
```

Запуск скрипта:

```bash
./git-bot.sh
```

`Для полноценной работы скрипта вам необходимо настроить ваш SMTP сервер.`

# Предварительная настройка почтовой рассылки

Для выполнения данного скрипта необходимо следующее:

- Unix-подобная операционная система (Проверено на Ubuntu 22.04.4 LTS).
- Установленные и настроенные Postfix и MailUtils.

## 1. Настройка внешнего почтового ящика

Необходимо получить пароль для приложения, который будет использоваться для отправки уведомлений.

В некоторых случаях потребуется включить работу внешних сервисов в настройках безопасности.

Так же вы можете использовать собственный `SMTP` сервер (если такой имеется)

P.S. Я использовал yandex.ru - [ссылка на документацию](https://yandex.ru/support/mail/mail-clients/others.html)

## 2. Установка необходимого программного обеспечения

```bash
sudo apt-get install postfix mailutils
```

В процессе установки откроется окно конфигурации; выберите "Без настроек"/"No configuration".

## 3. Настройка конфигураций

Отредактируйте файл `/etc/postfix/sasl_passwd`:

```bash
sudo vim /etc/postfix/sasl_passwd
```

Добавьте следующую запись:

```vim
smtp.yandex.ru:587 mailbox@yourdomain.ru:your_app_password
```

Затем измените права доступа к файлу:

```bash
chmod 600 /etc/postfix/sasl_passwd
```

Создайте конфигурационный файл Postfix:

```bash
sudo vim /etc/postfix/main.cf
```

Добавьте следующие строки:

```properties
relayhost = [smtp.yandex.ru]:587

smtp_use_tls = yes

smtp_sasl_auth_enable = yes
smtp_sasl_password_maps = hash:/etc/postfix/sasl_passwd
smtp_sasl_security_options = noanonymous
smtp_sasl_tls_security_options = noanonymous
smtp_tls_CAfile = /etc/ssl/certs/ca-certificates.crt

alias_maps = hash:/etc/aliases
```

Создайте файл базы данных `sasl_passwd.db` в каталоге `/etc/postfix/` с помощью команды `postmap`(Этот файл используется для запросов `Postfix`):

```bash
postmap /etc/postfix/sasl_passwd
```

Перезапустите сервис Postfix:

```bash
systemctl restart postfix.service
```

Проверьте состояние сервиса Postfix:

```bash
sudo service postfix status
```

Выполните тестовую отправку письма на произвольный почтовый ящик:

```bash
echo "Тестовое сообщение" | mailx -s "Тема тестового сообщения" recipient@domain -aFrom:mailbox@yourdomain.ru
```