package com.github.barteks2x.worldgenloop.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.world.gen.NoiseGeneratorImproved;

public class ReflectionUtil {

	private ReflectionUtil() {
		throw new Error();
	}

	/**
	 * Returns field of a given type from the specified class.
	 * 
	 * @param fieldType
	 *            field type
	 * @param from
	 *            class that contains the field
	 * @param staticMod
	 *            true - only static, false - only non static, null - both
	 * @return field of given type from given class
	 */
	public static Field getFieldByType(Class<?> fieldType, Class<?> from, Boolean staticMod) {
		Field[] fields = from.getDeclaredFields();
		boolean noStatic = staticMod == null ? false : !staticMod;
		boolean noNonStatic = staticMod == null ? false : staticMod;
		for (Field field : fields) {
			boolean isStatic = Modifier.isStatic(field.getModifiers());
			if ((noStatic && isStatic) || (noNonStatic && !isStatic)) {
				continue;
			}
			Class<?> type = field.getType();
			if (type == fieldType) {
				return field;
			}
		}
		return null;
	}

	/**
	 * Returns value of field of a given type from the specified object.
	 * 
	 * @param fieldType
	 *            field type
	 * @param from
	 *            class that contains the field
	 * @param staticMod
	 *            true - only static, false - only non static, null - both
	 * @return value of the field.
	 */
	public static <F, O> F getValueByType(Class<F> fieldType, Class<O> from, O obj, Boolean staticMod) {
		try {
			Field field = getFieldByType(fieldType, from, staticMod);
			field.setAccessible(true);
			return (F) field.get(obj);
		} catch (IllegalArgumentException e) {
			throw new ReflectionException(e);
		} catch (IllegalAccessException e) {
			throw new ReflectionException(e);
		}
	}

	/**
	 * Returns all fields of a given type from the specified class.
	 * 
	 * @param fieldType
	 *            field type
	 * @param from
	 *            class that contains the field(s)
	 * @param staticMod
	 *            true - only static, false - only non static, null - both
	 * @return List of fields. Empty if no fields found.
	 */
	public static List<Field> getFieldsByType(Class<?> fieldType, Class<?> from, Boolean staticMod) {
		Field[] fields = from.getDeclaredFields();
		List<Field> newFields = new ArrayList<Field>(fields.length);
		boolean noStatic = staticMod == null ? false : !staticMod;
		boolean noNonStatic = staticMod == null ? false : staticMod;
		for (Field field : fields) {
			boolean isStatic = Modifier.isStatic(field.getModifiers());
			if ((noStatic && isStatic) || (noNonStatic && !isStatic)) {
				continue;
			}
			Class<?> type = field.getType();
			if (type == fieldType) {
				newFields.add(field);
			}
		}
		return newFields;
	}

	/**
	 * Returns values of all fields of a given type from the specified class.
	 * 
	 * @param fieldType
	 *            field type
	 * @param fromClass
	 *            class that contains the field(s)
	 * @param staticMod
	 *            true - only static, false - only non static, null - both
	 * @return List of oojects (F). Empty if no fields found.
	 */
	public static <F, O> List<F> getValuesByType(Class<F> fieldType, Class<O> fromClass, O fromObj, Boolean staticMod) {
		try {
			List<Field> fields = getFieldsByType(fieldType, fromClass, staticMod);
			List<F> values = new ArrayList<F>(fields.size());
			for (Field field : fields) {
				field.setAccessible(true);
				values.add((F) field.get(fromObj));
			}
			return values;
		} catch (IllegalArgumentException e) {
			throw new ReflectionException(e);
		} catch (IllegalAccessException e) {
			throw new ReflectionException(e);
		}
	}

	public static int getArrayLength(Field field, Object inObject) {
		try {
			field.setAccessible(true);
			Object array = field.get(inObject);
			if (!array.getClass().isArray()) {
				throw new IllegalArgumentException("The field is not an array!");
			}
			return ((Object[]) array).length;
		} catch (IllegalArgumentException e) {
			throw new ReflectionException(e);
		} catch (IllegalAccessException e) {
			throw new ReflectionException(e);
		}
	}

	public static <T extends Object> void copyNonStaticFieldsByType(Class<T> inClass, Class<?> ofType, T from, T to) {
		Field[] fields = inClass.getDeclaredFields();
		for (Field field : fields) {
			if (field.getType() != ofType) {
				continue;
			}
			try {
				field.setAccessible(true);
				Object value = field.get(from);
				field.set(to, value);
			} catch (IllegalArgumentException e) {
				throw new ReflectionException(e);
			} catch (IllegalAccessException e) {
				throw new ReflectionException(e);
			}
		}
	}

	public static <T> T getValue(Field field, Object from, Class<T> fieldType) {
		try {
			field.setAccessible(true);
			return (T) field.get(from);
		} catch (IllegalArgumentException e) {
			throw new ReflectionException(e);
		} catch (IllegalAccessException e) {
			throw new ReflectionException(e);
		}
	}

	public static void setValue(Field field, Object object, Object value) {
		try {
			field.setAccessible(true);
			field.set(object, value);
		} catch (IllegalArgumentException e) {
			throw new ReflectionException(e);
		} catch (IllegalAccessException e) {
			throw new ReflectionException(e);
		}
	}

	public static Method findMethod(Class<?> inClass, Class<?> retType, boolean isStatic, Class... paramTypes) {
		LinkedList<Method> methods = new LinkedList<Method>();
		Class<?> cl_temp = inClass;
		methods.addAll(Arrays.asList(inClass.getDeclaredMethods()));
		// find in superclasses
		while (!cl_temp.getSuperclass().equals(Object.class)) {
			cl_temp = cl_temp.getSuperclass();
			methods.addAll(Arrays.asList(cl_temp.getDeclaredMethods()));
		}
		search: //
		for (Method m : methods) {
			m.setAccessible(true);
			if (m.getParameterTypes().length != paramTypes.length) {
				continue;
			}
			if (Modifier.isStatic(m.getModifiers()) != isStatic) {
				continue;
			}
			if (m.getReturnType() != retType) {
				continue;
			}
			Class<?> types[] = m.getParameterTypes();

			for (int i = 0; i < paramTypes.length; ++i) {
				if (!types[i].getName().equals(paramTypes[i].getName())) {
					continue search;
				}
			}
			return m;
		}
		return null;
	}
	
	public static <T> T invokeMethod(Class<T> returnType, Object obj, Method meth, Object...args){
		try {
			return (T)meth.invoke(obj, args);
		} catch (IllegalAccessException e) {
			throw new ReflectionException(e);
		} catch (IllegalArgumentException e) {
			throw new ReflectionException(e);
		} catch (InvocationTargetException e) {
			throw new ReflectionException(e);
		}
	}
}
