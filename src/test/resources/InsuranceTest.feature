#language: ru
Функционал: Страхование


  Сценарий: Заявка на ДМС

    Когда открыто меню
    И выбран пункт меню "Здоровье"

    Тогда заголовок страницы - Страхование здоровья равен "Страхование здоровья"

    Когда выполнено нажати на кнопку ДМС
    Тогда заголовок страницы - ДМС равен "добровольное медицинское страхование"

    Когда выполнено нажати на кнопку Отправить заявку
    Тогда заголовок страницы - Заявка на ДМС равен "Заявка на добровольное медицинское страхование"

    Когда заполняются поля:
      | Имя         | Иван        |
      | Фамилия     | Иванов      |
      | Отчество    | Иванович    |
      | Регион      | Москва      |
      | Телефон     | 9191111112  |
      | Эл. почта   | teststststs |
      | Комментарии | Autotest    |

    Тогда значения полей равны:
      | Имя         | Иван               |
      | Фамилия     | Иванов             |
      | Отчество    | Иванович           |
      | Регион      | Москва             |
      | Телефон     | +7 (919) 111-11-12 |
      | Эл. почта   | teststststs        |
      | Комментарии | Autotest           |

    И в поле "Эл. почта" присутствует сообщение об ошибке "Введите адрес электронной почты"