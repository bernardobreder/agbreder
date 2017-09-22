package com.agbreder.test;

import java.util.Arrays;

import org.junit.Test;

import com.agbreder.compiler.util.LightArrayList;

/**
 * Testador da classe LightArrayList
 * 
 * @author bernardobreder
 */
public class LightArrayListTest {
	
	/**
	 * Teste
	 */
	@Test
	public void test1() {
		new LightArrayList<Object>().isEmpty();
		new LightArrayList<Object>(1, 2).isEmpty();
		new LightArrayList<Object>().clear();
		new LightArrayList<Object>(1, 2).clear();
		new LightArrayList<Object>().toArray();
		new LightArrayList<Object>(1, 2).toArray();
		new LightArrayList<Object>().toArray(new Integer[0]);
		new LightArrayList<Object>(1, 2).toArray(new Integer[2]);
		new LightArrayList<Object>().remove(0);
		new LightArrayList<Object>(1, 2).remove(0);
		new LightArrayList<Object>().remove((Integer) 1);
		new LightArrayList<Object>(1, 2).remove((Integer) 1);
		new LightArrayList<Object>().addAll(Arrays.asList(1, 2));
		new LightArrayList<Object>(1, 2).addAll(Arrays.asList(1, 2));
		new LightArrayList<Object>().addAll(0, Arrays.asList(1, 2));
		new LightArrayList<Object>(1, 2).addAll(0, Arrays.asList(1, 2));
		new LightArrayList<Integer>().containsAll(Arrays.asList(1, 2));
		new LightArrayList<Integer>(1, 2).containsAll(Arrays.asList(1, 2));
		new LightArrayList<Integer>().removeAll(Arrays.asList(1, 2));
		new LightArrayList<Integer>(1, 2).removeAll(Arrays.asList(1, 2));
		new LightArrayList<Integer>().retainAll(Arrays.asList(1, 2));
		new LightArrayList<Integer>(1, 2).retainAll(Arrays.asList(1, 2));
		new LightArrayList<Integer>().indexOf(1);
		new LightArrayList<Integer>(1, 2).indexOf(1);
		new LightArrayList<Integer>().lastIndexOf(1);
		new LightArrayList<Integer>(1, 2).lastIndexOf(1);
		new LightArrayList<Integer>().listIterator();
		new LightArrayList<Integer>(1, 2).listIterator();
		new LightArrayList<Integer>().listIterator(1);
		new LightArrayList<Integer>(1, 2).listIterator(1);
		new LightArrayList<Integer>().subList(0, 0);
		new LightArrayList<Integer>(1, 2).subList(0, 0);
		new LightArrayList<Integer>().hashCode();
		new LightArrayList<Integer>(1, 2).hashCode();
		new LightArrayList<Integer>().equals(new LightArrayList<Integer>());
		new LightArrayList<Integer>(1, 2).equals(new LightArrayList<Integer>());
		new LightArrayList<Integer>(1, 2).set(0, 1);
		new LightArrayList<Integer>(1, 2).set(0, 2);
		try {
			new LightArrayList<Integer>().set(0, 2);
		} catch (Exception e) {
		}
		try {
			new LightArrayList<Integer>().get(1);
		} catch (Exception e) {
		}
		try {
			new LightArrayList<Integer>().add(2, 3);
		} catch (Exception e) {
		}
		new LightArrayList<Integer>(1, 2).add(3);
		new LightArrayList<Integer>().add(3);
		new LightArrayList<Integer>(1, 2).add(2, 3);
		for (Integer i : new LightArrayList<Integer>(1, 2)) {
			System.out.println(i);
		}
	}
	
}
