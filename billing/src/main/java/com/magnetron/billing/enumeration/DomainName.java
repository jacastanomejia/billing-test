package com.magnetron.billing.enumeration;

public enum DomainName {
	Product("Producto"),
	Person("Persona"),
	BillHeader("Cabecera de Factura"),
	BillDetail("Detalle de Factura");

	private final String message;

	private DomainName(String message) {
		this.message = message;
	}	
	
	public String getMessage() {
		return this.message;
	}
}
