CREATE DATABASE IF NOT EXISTS facturacion;
USE facturacion;

-- Tabla persona
CREATE TABLE persona (
    per_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Llave primaria',
    per_nombre VARCHAR(50) COMMENT 'Nombre de la persona',
    per_apellido VARCHAR(50) COMMENT 'Apellido de la persona',
    per_tipo_documento VARCHAR(4) COMMENT 'Tipo de identificación',
    per_documento VARCHAR(20) COMMENT 'Número o código de identificación',
    UNIQUE INDEX Idx_Documento USING BTREE (per_documento)
) ENGINE=InnoDB;
-- La tabla "persona" almacena información sobre individuos.
-- Cada persona tiene un identificador único (per_id), un nombre, un apellido,
--	un tipo de documento y un documento único.
-- Se ha creado un índice único utilizando B-tree en la columna per_documento
--	para garantizar la unicidad.

-- Tabla producto
CREATE TABLE producto (
    prod_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Llave principal',
    prod_description VARCHAR(500) COMMENT 'Descripción del producto',
    prod_precio DOUBLE COMMENT 'Precio de venta del producto',
    prod_costo DOUBLE COMMENT 'Costo de producción del producto',
    prod_um VARCHAR(50) COMMENT 'Unidad monetaria del producto'
) ENGINE=InnoDB;
-- La tabla "producto" almacena información sobre productos.
-- Cada producto tiene un identificador único (prod_id), una descripción, precio de venta,
--	costo de producción y unidad de medida.

-- Tabla fact_encabezado
CREATE TABLE fact_encabezado (
    fenc_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Llave principal',
    fenc_numero INT COMMENT 'Código o número de factura',
    fenc_fecha DATETIME COMMENT 'Fecha y hora de expedición de factura',
    z_per_id BIGINT COMMENT 'Llave foránea al comprador (referencia a persona.per_id)',
    FOREIGN KEY (z_per_id) REFERENCES persona(per_id)
) ENGINE=InnoDB;
-- La tabla "fact_encabezado" almacena información sobre encabezados de facturas.
-- Cada factura tiene un identificador único (fenc_id), un número de factura, una fecha y
--	hora de expedición y una clave foránea (z_per_id) que hace referencia al comprador en
--	la tabla "persona" mediante el campo "per_id".

-- Tabla fact_detalle
CREATE TABLE fact_detalle (
    fdet_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Llave principal',
    fdet_linea VARCHAR(250) COMMENT 'Descripción del detalle',
    fdet_cantidad INT COMMENT 'Cantidad de producto vendido',
    z_prod_id BIGINT COMMENT 'Llave foránea al producto vendido (referencia a producto.prod_id)',
    z_fenc_id BIGINT COMMENT 'Llave foránea al encabezado de la factura (referencia a fact_encabezado.fenc_id)',
    FOREIGN KEY (z_prod_id) REFERENCES producto(prod_id),
    FOREIGN KEY (z_fenc_id) REFERENCES fact_encabezado(fenc_id)
) ENGINE=InnoDB;
-- La tabla "fact_detalle" almacena información detallada sobre productos vendidos en facturas.
-- Cada detalle tiene un identificador único (fdet_id), una descripción, cantidad de producto
--	vendido y claves foráneas (z_prod_id y z_fenc_id) que hacen referencia al producto vendido
--	en la tabla "producto" y al encabezado de la factura en la tabla "fact_encabezado",
--	respectivamente.


-- VIEWS
-- Esta vista calcula el total de ventas por persona, sumando las cantidades
--	multiplicadas por los precios de los productos vendidos.
CREATE VIEW vista_total_ventas AS
SELECT
	person.per_id,
	person.per_nombre,
	person.per_apellido,
	person.per_tipo_documento,
	person.per_documento,
    COALESCE(SUM(detal.fdet_cantidad * product.prod_precio),0) AS per_total

FROM persona AS person
	LEFT JOIN fact_encabezado AS enc ON enc.z_per_id = person.per_id
    LEFT JOIN fact_detalle AS detal ON detal.z_fenc_id = enc.fenc_id
	LEFT JOIN producto AS product ON detal.z_prod_id = product.prod_id

GROUP BY person.per_id;


-- Esta vista extrae la lista de personas que han comprado el producto más caro
CREATE VIEW vista_producto_mas_caro AS
SELECT
	person.per_id,
	person.per_nombre,
	person.per_apellido,
	person.per_tipo_documento,
	person.per_documento,
    MAX(product.prod_precio) AS prod_precio

FROM persona AS person
	LEFT JOIN fact_encabezado AS enc ON enc.z_per_id = person.per_id
    LEFT JOIN fact_detalle AS detal ON detal.z_fenc_id = enc.fenc_id
	LEFT JOIN producto AS product ON detal.z_prod_id = product.prod_id

GROUP BY person.per_id
HAVING prod_precio = (SELECT MAX(prod_precio) FROM producto);

-- Esta vista muestra la cantidad total de cada producto facturado,
--	ordenada por la cantidad vendida de mayor a menor.
CREATE VIEW vista_cantidad_facturada AS
SELECT
	product.prod_id,
	product.prod_description,
	product.prod_precio,
	product.prod_costo,
	product.prod_um,
    SUM(detal.fdet_cantidad) as prod_cantidad

FROM producto AS product
	LEFT JOIN fact_detalle AS detal ON detal.z_prod_id = product.prod_id

GROUP BY product.prod_id
ORDER BY Prod_Cantidad DESC;

-- Esta vista calcula la utilidad total por producto,
--	considerando la diferencia entre el precio de venta y el costo de producción.
CREATE VIEW vista_utilidad_bruta AS
SELECT
	product.prod_id,
	product.prod_description,
	product.prod_precio,
    product.prod_costo,
    product.prod_um,
    COALESCE(
        SUM(detail.fdet_cantidad*(product.prod_precio-product.prod_costo))
        , 0) as prod_utilidad

FROM producto AS product
	LEFT JOIN fact_detalle AS detail ON detail.z_prod_id = product.prod_id

GROUP BY product.prod_id
ORDER BY prod_id ASC;

-- Esta vista calcula el margen de ganancia por producto como un porcentaje,
--	teniendo en cuenta la diferencia entre el precio de venta y el costo de
--	producción en relación con el precio de venta.
CREATE VIEW vista_margen_ganancia AS
SELECT
	product.prod_id,
	product.prod_description,
	product.prod_precio,
    product.prod_costo,
    product.prod_um,
    COALESCE(
    	(SUM(detail.fdet_cantidad*(product.prod_precio-product.prod_costo))/SUM(detail.fdet_cantidad*product.prod_precio)) * 100
        , 0) AS prod_margen_ganancia

FROM producto AS product
	LEFT JOIN fact_detalle AS detail ON detail.z_prod_id = product.prod_id

GROUP BY product.prod_id
ORDER BY prod_id ASC;


-- DATA
-- Inserción de datos en la tabla persona con acrónimos para tipos de documentos (solo dígitos en CC y TI)
INSERT INTO persona (per_nombre, per_apellido, per_tipo_documento, per_documento) VALUES
('Juan', 'Gómez', 'CC', '12345678'),
('María', 'López', 'TI', '9876543'),
('Carlos', 'Martínez', 'PP', 'AB123456'),
('Laura', 'Fernández', 'CC', '87654321'),
('Sergio', 'García', 'TI', '8765432'),
('Ana', 'Rodríguez', 'CC', '34567890'),
('Pedro', 'Díaz', 'PP', 'CD987654'),
('Carmen', 'Sánchez', 'TI', '1234567'),
('Luis', 'Pérez', 'CC', '56789012'),
('Isabel', 'González', 'PP', 'EF345678');


INSERT INTO producto (prod_description, prod_precio, prod_costo, prod_um) VALUES
('Transformador de Potencia 500 kVA', 1500.00, 1200.00, 'Unidad'),
('Transformador de Distribución 100 kVA', 800.00, 600.00, 'Unidad'),
('Transformador Monofásico 10 kVA', 200.00, 150.00, 'Unidad'),
('Transformador Trifásico 250 kVA', 1000.00, 800.00, 'Unidad'),
('Transformador de Aislamiento 50 kVA', 500.00, 400.00, 'Unidad'),
('Transformador de Corriente 500 A', 120.00, 90.00, 'Unidad'),
('Transformador de Voltaje 110V/220V', 80.00, 60.00, 'Unidad'),
('Transformador de Instrumentación 1 kVA', 150.00, 120.00, 'Unidad'),
('Transformador de Potencial 1000 V', 60.00, 45.00, 'Unidad'),
('Transformador de Resina 20 kVA', 300.00, 240.00, 'Unidad');

INSERT INTO fact_encabezado (fenc_numero, fenc_fecha, z_per_id) VALUES
(1001, '2023-02-21 08:30:00', 1),
(1002, '2023-02-21 09:15:00', 2),
(1003, '2023-02-21 10:00:00', 3),
(1004, '2023-02-21 10:45:00', 4),
(1005, '2023-02-21 11:30:00', 5),
(1006, '2023-02-21 12:15:00', 6),
(1007, '2023-02-21 13:00:00', 7),
(1008, '2023-02-21 13:45:00', 8),
(1009, '2023-02-21 14:30:00', 9),
(1010, '2023-02-21 15:15:00', 1),
(1011, '2023-02-21 16:00:00', 2),
(1012, '2023-02-21 16:45:00', 3),
(1013, '2023-02-21 17:30:00', 4),
(1014, '2023-02-21 18:15:00', 5),
(1015, '2023-02-21 19:00:00', 6),
(1016, '2023-02-21 19:45:00', 7),
(1017, '2023-02-21 20:30:00', 8),
(1018, '2023-02-21 21:15:00', 9),
(1019, '2023-02-21 22:00:00', 1),
(1020, '2023-02-21 22:45:00', 2);

INSERT INTO fact_detalle (fdet_linea, fdet_cantidad, z_prod_id, z_fenc_id) VALUES
('Transformador 500 kVA', 5, 1, 1),
('Transformador 100 kVA', 10, 2, 2),
('Transformador Monofásico', 8, 3, 3),
('Transformador Trifásico', 15, 4, 4),
('Transformador de Aislamiento', 3, 5, 5),
('Transformador de Corriente 500 A', 12, 6, 6),
('Transformador de Voltaje 110V/220V', 7, 7, 7),
('Transformador de Instrumentación 1 kVA', 20, 8, 8),
('Transformador de Potencial 1000 V', 4, 9, 9),
('Transformador de Resina 20 kVA', 6, 1, 10),
('Transformador 500 kVA', 18, 2, 11),
('Transformador 100 kVA', 9, 3, 12),
('Transformador Monofásico', 14, 4, 13),
('Transformador Trifásico', 2, 5, 14),
('Transformador de Aislamiento', 11, 6, 15),
('Transformador de Corriente 500 A', 5, 7, 16),
('Transformador de Voltaje 110V/220V', 19, 8, 17),
('Transformador de Instrumentación 1 kVA', 7, 9, 18),
('Transformador de Potencial 1000 V', 10, 1, 19),
('Transformador de Resina 20 kVA', 13, 2, 1),
('Transformador Monofásico', 8, 3, 17),
('Transformador Trifásico', 15, 4, 18),
('Transformador de Aislamiento', 3, 5, 19),
('Transformador de Corriente 500 A', 12, 6, 1),
('Transformador de Voltaje 110V/220V', 7, 7, 2),
('Transformador de Instrumentación 1 kVA', 20, 8, 3),
('Transformador de Potencial 1000 V', 4, 9, 4),
('Transformador de Resina 20 kVA', 6, 1, 5);