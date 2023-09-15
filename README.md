# Запуск микросервисов в докере

1. Убедиться, что установлен Docker
2. Проверить, что порты 8888, 8761, 8082, 8083, 8081, 5432, 5433, 8180 не заняты
3. Выполнить в терминале из директории docker команду docker-compose up -d и ждать чуда
4. Точка входа в приложение http://localhost:8081/<имя_микросервиса>/

# Настройка Keycloak

Выполняется при первом запуске

1. После запуска Keycloak в контейнере, зайти по адресу http://localhost:8180
2. Имя пользователя: admin, пароль: admin
3. Нажать create realm и вставить Resource file realm-export.json (из директории realms)
4. Зайти в clients и удалить spring-admin-client и spring-microservices и frontend-client, а затем импортировать
   клиентов из файлов spring-admin-client.json и spring-microservices.json директории realms
5. Нажать на клиента spring-admin-client и перейти в Service accounts roles и добавить роли, выбрал <b>filter by
   clients</b>: manage-realm и manage-users
6. Нажать на клиента frontend-client и перейти в Advanced -> Advanced Settings, установить Proof Key for Code Exchange
   Code Challenge Method S256
7. Зайти в Users и добавить пользователей:
- почта: leha@yandex.ru, имя: Леха, фамилия: Лехович
- почта: lenchik@yahoo.com, имя: Лена, фамилия: Леновна
- почта: nadya@google.com, имя: Надя, фамилия: Надевна
- почта: polina.gj1@yandex.ru, имя: Полина, фамилия: Полиновна
- почта: voroninana1990@mail.ru, имя: Вероника, фамилия: Верониковна
8. Зайти в Realm roles, если нет ролей, добавить: ADMIN, CANDIDATE, RECRUITER, TECH_SPECIALIST. Зайти в Realm setting -> User registration и добавить роль по умолчанию CANDIDATE, если нет.
9. Зайти в realm-setting в Recruetment-system, выбрать вкладку user profile. Для first name и last name добавить
   валидаторы: **person-name-prohibited-characters** c сообщением "Используются запрещенные символы", **pattern c pattern** "
   ^[a-zA-Z\u0401\u0451\u0410-\u044f]+$" и сообщением "Только алфавитные символы", **length** "min:1 max:50(зависит от бд) trim-disabled:true". Можно поменять сообщения или добавить
   другие валидаторы, если захотите.

# Возможные проблемы

1. Если сервис не запускается из-за liquabase - удаляем контейнер postgres_rs и директорию docker/pgdata_rs в проекте. И
   запускаем контейнер postgres_rs заново.
2. Когда подтягиваете изменения с гита и хотите запустить в докере, то удалите старые образы(image) тех модулей, что
   были изменены. Обычно это testing-service и user-service. Но сейчас лучше на всякий случай удалить образы
   api-gateway, eureka и configuration-server. (кейклок и бд не трогайте, если неи хотите настраивать все по новой 😉).
   Удалить image можно, зайдя в Docker Desctop Images. Потом переходите к пункту <b>"Запуск микросервисов в докере"</b>.