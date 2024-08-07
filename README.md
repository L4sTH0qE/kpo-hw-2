# Домашнее задание №2 по КПО (Соловкин Александр БПИ223)
### Руководство по эксплуатации

Запуск консольного приложения производится из класса Main.
- __Авторизация.__ Пользователь может войти в приложение как посетитель или админстратор. В обоих случаях требуются логин и пароль. Если пользователь такого типа (админ/посетитель) с таким логином уже есть в системе, то пользователю предлагается либо использовать другой логин для входа, либо зайти в существующий аккаунт с этим логином. Для входа как администратор пользователь также должен ввести секретный код __IAMADMIN__.
- __Меню администратора.__ Администратор может __просматривать меню__, __добавлять__ и __удалять блюда__ из меню, а также __изменять__ их (устанавливать их количество, цену и время, которое они будут готовиться).
- __Меню посетителя.__ Посетители могут __просматривать свои заказы__ с отображением их текущего состояния,   __составлять новый заказ__, выбирая блюда из актуального меню, __отменять готовящийся заказ__, __добавлять в готовящийся заказ блюдо__ из актуального меню и __оплачивать готовый заказ__.
- __Заказы.__ Заказы обрабатываются в отдельных потоках, симулируя процесс приготовления. Приготовления каждого блюда также осуществляется в отдельных потоках.
- __Хранение данных.__ Программа сохраняет __актуальное меню__, __сумму выручки__ и __список зарегистрированных в системе пользователей__ в соответствующих файлах в папке __data__.
- __Дополнительно.__ Посетитель может ставить оценки и оставлять комментарий к оплаченным заказам, а администратор может просматривать их.

Команды выбираются с помощью ввода соответствующего идентификатора в консоль.

### Шаблоны проектирования

В программе используется паттерн 'Одиночка' (см. OrderHandler) для обработки заказов. Использование данного паттерна обеспечивает глобальный доступ к заказам и гарантирует хранение и обработку заказов в одном месте.