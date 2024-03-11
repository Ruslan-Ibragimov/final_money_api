1) Таблицы users(ID, Balance) и transactions(transaction_id, user_id, receiver_id, amount, type, date)
2) Методы getBalance(), takeMoney(), putMoney(), transferMoney(), getOperationsList()
3) Использование методов реализовано в классе Controller
4) Используется Hibernate, Lombok и Spring Open API
5) Последний содержит в себе swagger, проверить работу API можно сразу по ссылке http://localhost:9090/swagger-ui/index.html
6) Даты для getOperationsList() в формате yyyy-MM-dd
7) Можно вводить только одну из дат или вообще не вводить их, будет выведен результат.
8) Основной класс - OperationsService. Покрыты тестами методы takeMoney(), putMoney(, transferMoney()
9) Транзакции проводятся через Spring @Transactions. Происходит rollback при неудачном этапе на каком-либо из уровней вложенности (например вызов addTransaction() после putMoney())
10) Типы транзакций выполнены через Enum.
11) Структура базы данных:
<img width="684" alt="image" src="https://github.com/Ruslan-Ibragimov/final_money_api/assets/74320763/3656519e-34b0-4e8c-ad5f-d35488268f18">
