# SimulMarket App

SimulMarket es una aplicación web para gestionar listas de compras. Permite a los usuarios registrados crear
y administrar listas de compras, agregar productos y realizar seguimientos de sus artículos.

## Funcionalidades Actuales

- Registro de usuarios.
- Inicio de sesión y autenticación de usuarios mediante tokens JWT.
- Creación y gestión de listas de compras.
- Agregar y eliminar productos de las listas de compras.
- Visualización de productos en las listas de compras.

## Funcionalidades Futuras

- Implementación de notificaciones.
- Funcionalidades especiales para los usuarios de rol administrador
- Metodos de pago
- Filtros avanzados para la gestión y visualización de productos
- Inyeccion de productos al ejecutarse por primera vez la aplicacion

## Tecnologías Utilizadas

- Spring Boot Starter Security: Proporciona soporte para la seguridad de aplicaciones, incluyendo
autenticación y autorización.
- Spring Boot Starter Data JPA: Facilita la interacción con la base de datos utilizando JPA
(Java Persistence API).
- Spring Boot Starter Validation: Ofrece soporte para validación de datos en el backend.
- Spring Boot Starter Web: Proporciona funcionalidades para la creación de aplicaciones web
y APIs RESTful.
- Spring Boot DevTools: Ofrece herramientas adicionales para mejorar la experiencia de
desarrollo, como recarga automática.
- MySQL Connector/J: Conector JDBC para MySQL, utilizado para la conexión a la base de datos.
- Spring Boot Starter Test: Incluye bibliotecas y herramientas para realizar pruebas en la aplicación.
- JJWT API: Biblioteca para la creación y verificación de tokens JWT.
- JJWT Impl: Implementación de la biblioteca JJWT para manejar tokens JWT en tiempo de ejecución.
- JJWT Jackson: Proporciona soporte para la serialización y deserialización de JWT utilizando Jackson.

## Instalación y Uso

Requisitos previos:

- JDK 17 o superior.
- MySQL instalado en tu sistema.
- Configuración de una base de datos en MySQL.

Pasos para la configuración:

1) Clona el repositorio en tu máquina local

2) Navega al directorio del proyecto

3) Configura la base de datos:
- Asegúrate de que MySQL está corriendo.
- Crea una base de datos en MySQL

4) Configura el archivo de propiedades:
Edita el archivo src/main/resources/application.properties con los siguientes parámetros:

spring.datasource.url=jdbc:mysql://localhost:3306/(nombre de la base de datos)

spring.datasource.username=(nombre del usuario)

spring.datasource.password=(contraseña)

spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect

spring.jpa.show-sql=true

spring.jpa.hibernate.ddl-auto=update



## Contribución

Si deseas contribuir a este proyecto, sigue estos pasos:

1. Haz un fork del repositorio.
2. Crea una nueva rama (git checkout -b feature/nueva-funcionalidad).
3. Realiza tus cambios y haz commits (git commit -am 'Agrega nueva funcionalidad').
4. Haz push a la rama (`git push origin feature/nueva-funcionalidad`).
5. Crea un nuevo Pull Request.

## Autores

- Cristian Giraldo Villegas [CristianGiVi](https://github.com/CristianGiVi)
