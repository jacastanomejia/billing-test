# Facturación de Productos

Esta API de facturación de productos es una solución robusta y eficiente diseñada para gestionar el proceso completo de facturación, asegurando un control preciso y detallado de cada transacción comercial. A continuación, se destacan los principales requerimientos abordados por la aplicación:

## Características Principales

1. **Identificación de Persona:**
   - Registra y gestiona la información del cliente a quien se le factura, facilitando la identificación y seguimiento de cada transacción.

2. **Número Consecutivo y Fecha de la Factura:**
   - Asigna automáticamente un número consecutivo a cada factura, proporcionando un sistema ordenado y organizado.
   - Registra la fecha de emisión de cada factura para un seguimiento temporal preciso.

3. **Detalle de lo Facturado:**
   - Permite ingresar y consultar el detalle completo de cada factura, incluyendo información detallada sobre las líneas de productos facturados.
   - La información detallada incluye número de línea, cantidad y descripción del producto.

4. **Información del Producto:**
   - Registra datos esenciales sobre cada producto, como unidad de medida, precio y descripción.
   - Facilita la gestión y control de inventario al proporcionar información detallada sobre los productos facturados.

## Uso

1. **Registro de Facturas:**
   - Ingresa la información del cliente y los productos facturados para generar una factura.

2. **Consulta Detallada:**
   - Accede a detalles específicos de cada factura, incluyendo la identificación del cliente, productos facturados y fechas asociadas.

3. **Gestión de Productos:**
   - Mantén actualizada la información de productos, incluyendo unidades de medida, precios y descripciones.

## Requisitos del Sistema

- Java 17 o superior.
- Base de datos compatible (MySQL, PostgreSQL).
- Conexión a Internet para actualizaciones y soporte.

Este software de facturación de productos proporciona una solución integral para las necesidades de tu negocio, garantizando un manejo eficiente y organizado de la información financiera y de inventario.


# Despliegue de la Aplicación Spring Boot con Docker Compose

Este documento proporciona una guía paso a paso para el despliegue de la aplicación de facturación de productos, desarrollada en Spring Boot con Java 17, utilizando Docker Compose. Asegúrate de tener Docker y Docker Compose instalados en tu sistema antes de comenzar.

## Pasos de Despliegue

### Paso 1: Clonar el Repositorio

```bash
git clone https://github.com/jacastanomejia/billing-test.git
cd billing-test
```

### Paso 2: Configurar la Base de Datos

Edita el archivo `docker-compose.yml` para configurar los parámetros de la base de datos, como nombre de usuario, contraseña y nombre de la base de datos.

```yaml
services:
  db:
    environment:
      MYSQL_DATABASE: facturacion
      MYSQL_ROOT_PASSWORD: root
```

### Paso 3: Construir y Levantar los Contenedores con Docker Compose

```bash
docker-compose up --build -d
```

Este comando construirá la imagen Docker de la aplicación y levantará los contenedores definidos en el archivo `docker-compose.yml`. Puedes agregar la bandera `-d` para ejecutar en segundo plano.

### Paso 4: Verificar el Estado de la Aplicación

```bash
docker-compose ps
```

Asegúrate de que todos los contenedores estén en estado "Up".

### Paso 5: Acceder a la Aplicación

Abre tu navegador y accede a http://localhost:8080/docs.html, donde "puerto" es el puerto especificado en el archivo `docker-compose.yml`.

### Paso 6: Detener y Limpiar los Contenedores

```bash
docker-compose down
```

Este comando detendrá y eliminará los contenedores de la aplicación.

## Configuración Adicional

- Ajusta otros parámetros en el archivo `docker-compose.yml`, como los puertos o volúmenes, según tus necesidades específicas.

- Asegúrate de tener una versión de Docker compatible con la aplicación Spring Boot.

- Puedes personalizar la configuración de la aplicación Spring Boot mediante archivos `application.properties`, `docker-compose.yml` o Dockerfile según sea necesario.

## Notas Adicionales

- Para propósito de la prueba la base de datos es inicializada con datos falsos.

- El archivo de setup con la creación de la BD y las consultas SQL se encuentran en la carpeta billing/sql (https://github.com/jacastanomejia/billing-test/tree/master/billing/sql) del proyecto

- La cobertura de los test unitarios puede ser consultada en http://localhost:8080/coverage/index.html

¡Listo! Tu aplicación de facturación de productos desarrollada en Spring Boot debería estar ahora desplegada y accesible a través de Docker Compose.


