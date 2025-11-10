GET /api/products — список всех продуктов
GET /api/products/{id} — один продукт
Создание продукта
```
POST /api/products
{
"name": "Laptop",
"description": "Gaming laptop",
"price": 1500.00,
"quantityInStock": 10
}
```
PUT /api/products/{id} — обновить продукт
DELETE /api/products/{id} — удалить продукт


Создание заказа

``` 
POST /api/orders
{
"firstName": "Ivan",
"lastName": "Ivanov",
"email": "ivan@example.com",
"contactNumber": "+1234567890",
"productIds": [1, 2, 3],
"shippingAddress": "Some street 123, City"
}
```
GET /api/orders/{id} — информация о заказе