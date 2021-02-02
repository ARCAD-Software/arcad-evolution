package com.arcadsoftware.mmk.lists.test;

import java.util.Hashtable;

import com.arcadsoftware.mmk.lists.AbstractXmlList;
import com.arcadsoftware.mmk.lists.IListBrowseListener;
//import com.arcadsoftware.mmk.lists.impl.fillers.DirectoryScanFiller;
import com.arcadsoftware.mmk.lists.impl.fillers.DirectoryScanFiller;
import com.arcadsoftware.mmk.lists.impl.lists.FileList;
import com.arcadsoftware.mmk.lists.metadata.StoreItem;

public class Test implements IListBrowseListener {

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		final Test t = new Test();
		t.exec();
	}

	private final boolean show = false;

	public void addItems(final FileList l) {
		final long start = System.currentTimeMillis();
		// StoreItem i = l.toStoreItem(new File("c:\\devtmp\\test001\\addedfile-1.txt"));
		// XmlFileList l2 = new XmlFileList();
		// l2.setXmlFileName("c:\\devtmp\\dom4j\\test-base_toAdd.xml");
		l.addItems(new DirectoryScanFiller(l, "C:\\devtmp\\dom4j\\mergeDir"), true, true);
		final long end = System.currentTimeMillis();
		System.out.println("Temps de d'ajout : " + String.valueOf(end - start));
	}

	public void addItems(final FileList l, final FileList fromList) {
		final long start = System.currentTimeMillis();
		// StoreItem i = l.toStoreItem(new File("c:\\devtmp\\test001\\addedfile-1.txt"));
		// XmlFileList l2 = new XmlFileList();
		// l2.setXmlFileName("c:\\devtmp\\dom4j\\test-base_toAdd.xml");
		l.addItems(fromList, false, true);
		final long end = System.currentTimeMillis();
		System.out.println("Temps de d'ajout : " + String.valueOf(end - start));
	}

	public void browse(final AbstractXmlList l) {
		l.addBrowseListener(this);
		final long start = System.currentTimeMillis();
		l.browse();
		final long end = System.currentTimeMillis();
		System.out.println("Temps de parcours : " + String.valueOf(end - start));
	}

	public void duplicateList(final AbstractXmlList l) {
		final long start = System.currentTimeMillis();
		final FileList l2 = new FileList();
		l2.setXmlFileName("c:\\devtmp\\dom4j\\test-extract-dup.xml");
		l.duplicate(l2);
		final long end = System.currentTimeMillis();
		System.out.println("Temps de remise à jour : " + String.valueOf(end - start));
	}

	@Override
	public void elementBrowsed(final StoreItem item) {
		if (show) {
			System.out.println(item.getUserValue(3));
		}
	}

	public void exec() {
		final FileList l1 = new FileList();
		l1.setXmlFileName("c:\\devtmp\\dom4j\\nw-test-02.xml");
		final FileList l2 = new FileList();
		l2.setXmlFileName("c:\\devtmp\\dom4j\\test100k.xml");
		addItems(l1, l2);
		// duplicateList(l1);
		// l1.load(false);
		//
		// System.out.println(l1.getElementCount());
		// intersectList(l1);
	}

	public void extractItems(final AbstractXmlList l) {
		final long start = System.currentTimeMillis();
		// StoreItem i = l.toStoreItem(new File("C:\\devtmp\\dom4j\\baseDir\\file001.txt"));
		final FileList l2 = new FileList();
		l2.setXmlFileName("c:\\devtmp\\dom4j\\test-extract-result.xml");
		l.extractItems("ext='bat'", l2, false, true, true);
		final long end = System.currentTimeMillis();
		System.out.println("Temps de suppression : " + String.valueOf(end - start));
	}

	public void fill(final FileList l) {
		final long start = System.currentTimeMillis();
		l.setFiller(new DirectoryScanFiller(l, "C:\\devtmp\\dom4j\\mergeDir"));
		l.populate();
		final long end = System.currentTimeMillis();
		System.out.println("Temps de remplissage : " + String.valueOf(end - start));
	}

	public void intersectList(final AbstractXmlList l) {
		final long start = System.currentTimeMillis();
		// cas n1 = Liste resultat <> de L1 et L2 | ctrl existence = false | replacement = false : OK
		// cas n1 = Liste resultat = L1 | ctrl existence = false | replacement = false :
		// cas n1 = Liste resultat = L2 | ctrl existence = false | replacement = false : OK
		// cas n1 = Liste resultat <> de L1 et L2 | ctrl existence = true | replacement = false : OK
		// cas n1 = Liste resultat = L1 | ctrl existence = true | replacement = false :
		// cas n1 = Liste resultat = L2 | ctrl existence = true | replacement = false : OK
		// cas n1 = Liste resultat <> de L1 et L2 | ctrl existence = true | replacement = true : OK
		// cas n1 = Liste resultat = L1 | ctrl existence = true | replacement = true :
		// cas n1 = Liste resultat = L2 | ctrl existence = true | replacement = true : OK

		final AbstractXmlList l2 = new FileList();
		l2.setXmlFileName("c:\\devtmp\\dom4j\\test-cmp.xml");
		final AbstractXmlList l3 = new FileList();
		l3.setXmlFileName("c:\\devtmp\\dom4j\\test-result.xml");
		final Hashtable<String, String> map = new Hashtable<>();
		map.put("size", "<>");

		l.intersect(l2, l3, false, false, map);
		final long end = System.currentTimeMillis();
		System.out.println("Temps de fusion : " + String.valueOf(end - start));
	}

	public void mergeList(final AbstractXmlList l) {
		final long start = System.currentTimeMillis();
		// cas n1 = Liste resultat <> de L1 et L2 | ctrl existence = false | replacement = false : OK
		// cas n1 = Liste resultat = L1 | ctrl existence = false | replacement = false : OK
		// cas n1 = Liste resultat = L2 | ctrl existence = false | replacement = false : OK
		// cas n1 = Liste resultat <> de L1 et L2 | ctrl existence = true | replacement = false : OK
		// cas n1 = Liste resultat = L1 | ctrl existence = true | replacement = false : OK
		// cas n1 = Liste resultat = L2 | ctrl existence = true | replacement = false : OK
		// cas n1 = Liste resultat <> de L1 et L2 | ctrl existence = true | replacement = true : OK
		// cas n1 = Liste resultat = L1 | ctrl existence = true | replacement = true : OK
		// cas n1 = Liste resultat = L2 | ctrl existence = true | replacement = true :

		final FileList l2 = new FileList();
		l2.setXmlFileName("c:\\devtmp\\dom4j\\test-l2.xml");
		final FileList l3 = new FileList();
		l3.setXmlFileName("c:\\devtmp\\dom4j\\test-result.xml");
		l.merge(l2, l2, true, true);
		final long end = System.currentTimeMillis();
		System.out.println("Temps de fusion : " + String.valueOf(end - start));
	}

	public void reinitializeValue(final AbstractXmlList l) {
		final long start = System.currentTimeMillis();
		l.reinitializeValue("size", "03081969");
		final long end = System.currentTimeMillis();
		System.out.println("Temps de remise à jour : " + String.valueOf(end - start));
	}

	public void removeDuplicate(final AbstractXmlList l) {
		final long start = System.currentTimeMillis();
		l.removeDuplicate("");
		final long end = System.currentTimeMillis();
		System.out.println("Temps de dédoublonnage : " + String.valueOf(end - start));
	}

	public void removeItems(final FileList l) {
		final long start = System.currentTimeMillis();
		// StoreItem i = l.toStoreItem(new File("C:\\devtmp\\dom4j\\baseDir\\file001.txt"));
		final AbstractXmlList l2 = new FileList();
		l2.setXmlFileName("c:\\devtmp\\dom4j\\test-remove_toremove.xml");
		l.removeItems(l2);
		final long end = System.currentTimeMillis();
		System.out.println("Temps de suppression : " + String.valueOf(end - start));
	}

	public void substractList(final AbstractXmlList l) {
		final long start = System.currentTimeMillis();
		// cas n1 = Liste resultat <> de L1 et L2 | ctrl existence = false | replacement = false : OK
		// cas n1 = Liste resultat = L1 | ctrl existence = false | replacement = false : OK
		// cas n1 = Liste resultat = L2 | ctrl existence = false | replacement = false : OK
		// cas n1 = Liste resultat <> de L1 et L2 | ctrl existence = true | replacement = false : OK
		// cas n1 = Liste resultat = L1 | ctrl existence = true | replacement = false :
		// cas n1 = Liste resultat = L2 | ctrl existence = true | replacement = false :
		// cas n1 = Liste resultat <> de L1 et L2 | ctrl existence = true | replacement = true : OK
		// cas n1 = Liste resultat = L1 | ctrl existence = true | replacement = true : OK
		// cas n1 = Liste resultat = L2 | ctrl existence = true | replacement = true : OK

		final AbstractXmlList l2 = new FileList();
		l2.setXmlFileName("c:\\devtmp\\dom4j\\test-l2.xml");
		final AbstractXmlList l3 = new FileList();
		l3.setXmlFileName("c:\\devtmp\\dom4j\\test-result.xml");
		l.substract(l2, l2, true, true);
		final long end = System.currentTimeMillis();
		System.out.println("Temps de fusion : " + String.valueOf(end - start));
	}

	public void updateItems(final AbstractXmlList l) {
		final long start = System.currentTimeMillis();
		// StoreItem i = l.toStoreItem(new File("C:\\devtmp\\dom4j\\baseDir\\file001.txt"));
		final AbstractXmlList l2 = new FileList();
		l2.setXmlFileName("c:\\devtmp\\dom4j\\test-update_toupdate.xml");
		l.updateItems(l2);
		final long end = System.currentTimeMillis();
		System.out.println("Temps de suppression : " + String.valueOf(end - start));
	}

}
