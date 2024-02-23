package com.magnetron.billing.util;

import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class DataMapper<S, T>{

	/**
	 * Utilidad generica encargada de mapear listas de objetos entre tipos
	 *
	 * @param source lista fuent
	 * @param targetClass tipo objetivo a convertir
	 * @return lista con los elementos convertidos a targetClass
	 * @param <S> tipo fuente
	 * @param <T> tipo objetivo
	 */
	public static <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
		ModelMapper mapper = new ModelMapper();
		return source
			.stream()
			.map(element -> mapper.map(element, targetClass))
			.collect(Collectors.toList());
	}

}
