package com.hvadoda1.dht.chord.starter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.hvadoda1.dht.chord.rpc.connect.IRpcServer;
import com.hvadoda1.dht.chord.rpc.connect.IRpcServerController;

public class ChordStarterFactory {

	private static boolean constructorMatches(Constructor<?> constr, Object... args) {
		if (constr.getParameterCount() != args.length)
			return false;
		Parameter[] params = constr.getParameters();
		for (int i = 0; i < args.length; i++) {
			if (!params[i].getType().isAssignableFrom(args[i].getClass())
					&& !isWrapper(params[i].getType().getSimpleName(), args[i].getClass().getSimpleName()))
				return false;
		}
		return true;
	}

	private static boolean isWrapper(String type, String wrapper) {
		switch (type) {
		case "int":
			return wrapper.equals("Integer");
		case "char":
			return wrapper.equals("CHaracter");
		case "double":
			return wrapper.equals("Double");
		case "long":
			return wrapper.equals("Long");
		}
		return false;
	}

	public static <C extends IRpcServerController<S, E>, S extends IRpcServer<?, ?, ?, E>, E extends Exception> ChordServiceStarter<C, S, E> factory(
			ChordImpl chord, Object... args) {
		Class<? extends ChordServiceStarter<C, S, E>> clazz = (Class<? extends ChordServiceStarter<C, S, E>>) chord
				.getChordImplClass();
		try {
			Constructor<? extends ChordServiceStarter<C, S, E>> constr = Stream.of(clazz.getConstructors())
					.map(con -> (Constructor<? extends ChordServiceStarter<C, S, E>>) con)
					.filter(con -> con.getParameterCount() == args.length && constructorMatches(con, args)).findFirst()
					.orElse(null);

			// Initialize the constructor with parameters 'args'
			if (constr == null)
				throw new RuntimeException(
						"Constructor with provided parameters not found, could not instantiate " + chord);
			return constr.newInstance(args);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("Failed to instantiate Chord RPC Impl of Type [" + chord.name()
					+ "] : Invalid declaration of State constructor or Incorrect number/types of arguments provided (Got "
					+ args.length + " arguments of types ["
					+ Stream.of(args).map(arg -> arg.getClass().getName()).collect(Collectors.joining(", "))
					+ "] as constructor parameters)", e);
		} catch (SecurityException | IllegalAccessException e) {
			throw new RuntimeException("Failed to instantiate Chord RPC Impl of Type [" + chord.name()
					+ "] : Constructor is not accessible, make sure the constructor is public)", e);
		} catch (InstantiationException e) {
			throw new RuntimeException("Failed to instantiate Chord RPC Impl of Type [" + chord.name() + "]", e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(
					"Failed to instantiate Chord RPC Impl of Type [" + chord.name() + "] : Invalid arguments", e);
		}
	}

}
