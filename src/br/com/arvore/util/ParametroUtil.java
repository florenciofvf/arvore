package br.com.arvore.util;

public class ParametroUtil {
	private ParametroUtil() {
	}

	public static int[] getIndiceParametros(String s) {
		if (s == null) {
			return new int[0];
		}

		char c = '?';
		int indice = s.indexOf(c);

		if (indice == -1) {
			return new int[0];
		}

		Array a = new Array();
		a.add(indice);

		indice = s.indexOf(c, indice + 1);

		while (indice != -1) {
			a.add(indice);
			indice = s.indexOf(c, indice + 1);
		}

		return a.array;
	}

	private static class Array {
		int[] array = new int[1];
		int indice;

		void add(int i) {
			if (indice >= array.length) {
				int[] bkp = array;
				array = new int[bkp.length + 1];
				System.arraycopy(bkp, 0, array, 0, bkp.length);
			}

			array[indice] = i;
			indice++;
		}
	}
}